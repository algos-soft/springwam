package it.algos.springwam.entity.utente;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.label.LabelVerde;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.servizio.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_UTE)
public class UtenteList extends AlgosListImpl {


    /**
     * Property iniettata da Spring
     */
    @Autowired
    protected FunzioneService funzioneService;

    private final static int LAR = 75;

    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public UtenteList(@Qualifier(AppCost.TAG_UTE) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void inizializza(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.inizializza(className, items);
            caption += "</br>Lista visibile a tutti";
            caption += "</br>Usa la company che è ACompanyRequired.obbligatoria";
            caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Utente previste.";
        }// end of if/else cycle
    }// end of method

    /**
     * Creazione della grid
     * Ricrea tutto ogni volta che diventa attivo
     *
     * @param source      di riferimento per gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    public void restart(AlgosPresenterImpl source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        super.restart(source, entityClazz, columns, items);

        columnAdmin();
        columnDipendente();
        columnInfermiere();

        if (LibSession.isCompanyValida()) {
            columnsFunzioni();
        }// end of if cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnAdmin() {
        String tag = LibAnnotation.getColumnName(Utente.class,"admin");
        Grid.Column colonna = grid.addComponentColumn(utente -> {
            if (((Utente) utente).isAdmin()) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, tag, tag, LAR);
        spostaColonnaPrima(tag, "nome");
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnDipendente() {
        String tag = LibAnnotation.getColumnName(Utente.class,"dipendente");
        Grid.Column colonna = grid.addComponentColumn(utente -> {
            if (((Utente) utente).isDipendente()) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, tag, tag, LAR);
        spostaColonnaPrima(tag, "nome");
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnInfermiere() {
        String tag = LibAnnotation.getColumnName(Utente.class,"infermiere");
        Grid.Column colonna = grid.addComponentColumn(utente -> {
            if (((Utente) utente).isInfermiere()) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, tag, tag, LAR);
        spostaColonnaPrima(tag, "nome");
    }// end of method


    /**
     * Crea le colonne (di tipo Component) per visualizzare le funzioni
     */
    private void columnsFunzioni() {
        List<Funzione> funzioniCroce = funzioneService.findAllByCompany();

        for (Funzione funzioneCroce : funzioniCroce) {
            columnFunzione(funzioneCroce);
        }// end of for cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnFunzione(Funzione funzioneCroce) {
        Grid.Column colonna = grid.addComponentColumn(utente -> {
            List<Funzione> funzioniUtenteAbilitate = ((Utente) utente).getFunzioni();
            if (funzioniUtenteAbilitate != null && funzioniUtenteAbilitate.size() > 0 && contiene(funzioniUtenteAbilitate, funzioneCroce)) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, funzioneCroce.getCode(), funzioneCroce.getCode(), 110);
    }// end of method

    /**
     * Controlla l'esistenza della funzione tra quelle abilitate per l'utente
     */
    private boolean contiene(List<Funzione> funzioniUtenteAbilitate, Funzione funzioneCroce) {
        boolean contiene = false;

        for (Funzione funz : funzioniUtenteAbilitate) {
            if (funz.getCode().equals(funzioneCroce.getCode())) {
                contiene = true;
                break;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method

}// end of class
