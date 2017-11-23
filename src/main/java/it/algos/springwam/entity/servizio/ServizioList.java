package it.algos.springwam.entity.servizio;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.label.LabelVerde;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.renderer.IconRenderer;
import it.algos.springvaadin.toolbar.AToolbarImpl;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.migration.Migration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_SER)
public class ServizioList extends AlgosListImpl {

    @Autowired
    private Migration migration;

    @Autowired
    private ServizioService service;

    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public ServizioList(@Qualifier(AppCost.TAG_FUN) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


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

        columnOrarioBool();
        columnOrarioText();
        columnVisibile();
        columnFunzioni();
    }// end of method


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
            super.caption = "Servizi previsti. Visibili nel tabellone (secondo il flag)";
        }// end of if/else cycle
    }// end of method


    /**
     * Crea la colonna orario (di tipo CheckBox)
     */
    private void columnOrarioBool() {
        String idKey = "orarioBool";
        Grid.Column colonna = grid.addComponentColumn(servizio -> {
            if (((Servizio) servizio).isOrario()) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, idKey, "Fix", LibColumn.WIDTH_CHECK_BOX);
        spostaColonnaPrima(idKey, "oraInizio");
    }// end of method


    /**
     * Crea la colonna orario (di tipo String)
     */
    private void columnOrarioText() {
        String idKey = "orarioText";
        Grid.Column colonna = grid.addColumn(servizio -> {
            return service.getStrOrario((Servizio) servizio);
        });//end of lambda expressions

        fixColumn(colonna, idKey, "Orario", 160);
        spostaColonnaPrima(idKey, "visibile");
    }// end of method


    /**
     * Crea la colonna tabellone (di tipo CheckBox)
     */
    private void columnVisibile() {
        String idKey = "tab";
        Grid.Column colonna = grid.addComponentColumn(servizio -> {
            if (((Servizio) servizio).isVisibile()) {
                return new LabelVerde(VaadinIcons.CHECK);
            } else {
                return new LabelRosso(VaadinIcons.CLOSE);
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, idKey, "Tab", 68);
        spostaColonnaDopo("tab", "oraFine");
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnFunzioni() {
        Grid.Column colonna = grid.addComponentColumn(servizio -> {
            String valueLabel = "";
            String valueFunz;
            String tag = " - ";

            List<Funzione> funzioni = ((Servizio) servizio).getFunzioni();
            if (funzioni != null && funzioni.size() > 0) {
                for (Funzione funz : funzioni) {
                    valueFunz = "";
                    if (funz != null) {
                        valueFunz = funz.getSigla();
                        if (funz.isObbligatoria()) {
                            valueFunz = LibText.setRossoBold(valueFunz);
                        } else {
                            valueFunz = LibText.setBluBold(valueFunz);
                        }// end of if/else cycle
                        valueLabel += valueFunz + tag;
                    }// end of if cycle
                }// end of for cycle
                valueLabel = LibText.levaCoda(valueLabel, tag);
                return new Label(valueLabel, ContentMode.HTML);
            } else {
                return null;
            }// end of if/else cycle
        });//end of lambda expressions

        fixColumn(colonna, "funzioni", "Funzioni del servizio", 350);
    }// end of method

}// end of class
