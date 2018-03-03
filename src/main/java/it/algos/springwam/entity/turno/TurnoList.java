package it.algos.springwam.entity.turno;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.annotation.AIView;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.ADateService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.servizio.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
 * Estende la Entity astratta AList di tipo AView per visualizzare la Grid
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIView (facoltativo) per selezionarne la 'visibilità' secondo il ruolo dell'User collegato
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_TUR)
@SpringView(name = AppCost.VIEW_TUR_LIST)
@AIScript(sovrascrivibile = true)
public class TurnoList extends AList {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ADateService date;

    private static int LAR_COL = 85;

    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = AppCost.TAG_TUR;


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Use @Lazy to avoid the Circular Dependency
     * A simple way to break the cycle is saying Spring to initialize one of the beans lazily.
     * That is: instead of fully initializing the bean, it will create a proxy to inject it into the other bean.
     * The injected bean will only be fully created when it’s first needed.
     *
     * @param presenter iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     * @param toolbar   iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public TurnoList(
            @Lazy @Qualifier(AppCost.TAG_TUR) IAPresenter presenter,
            @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    @Override
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
        super.fixCaption(entityClazz, items);
        if (login.isDeveloper()) {
            caption += "</br>Lista visibile a tutti";
            caption += "</br>Solo il developer vede queste note";
        } else {
            char uniCharCircaTilde = '\u007E';
            caption += " - Da oggi per i prossimi 7 giorni "+uniCharCircaTilde;
        }// end of if/else cycle
    }// end of method

    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     *
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    protected void creaBody( Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        super.creaBody(entityClazz, columns, items);
        addColumnInizio();
        addColumnFine();
        addColumnIscrizioni();
        addColumnTitoloExtra();
    }// end of method


    /**
     * Crea la colonna orario (di tipo int)
     */
    private void addColumnInizio() {
        String idKey = "inizioInt";

        Grid.Column colonna = grid.getGrid().addColumn(turno -> {
            LocalDateTime inizio = ((Turno) turno).getInizio();
            int oraInizio = date.getOre(date.localDateTimeToDate(inizio));
            return oraInizio;
        });//end of lambda expressions

        colonna.setId(idKey);
        colonna.setCaption("Inizio");
        colonna.setWidth(LAR_COL);
        float larghezza = grid.getGrid().getWidth();
        grid.getGrid().setWidth(larghezza + LAR_COL, Unit.PIXELS);
    }// end of method

    /**
     * Crea la colonna orario (di tipo int)
     */
    private void addColumnFine() {
        String idKey = "fineInt";

        Grid.Column colonna = grid.getGrid().addColumn(turno -> {
            LocalDateTime fine = ((Turno) turno).getFine();
            int oraFine = date.getOre(date.localDateTimeToDate(fine));
            return oraFine;
        });//end of lambda expressions

        colonna.setId(idKey);
        colonna.setCaption("Fine");
        colonna.setWidth(LAR_COL);
        float larghezza = grid.getGrid().getWidth();
        grid.getGrid().setWidth(larghezza + LAR_COL, Unit.PIXELS);
    }// end of method


    /**
     * Crea la colonna iscrizioni
     */
    private void addColumnIscrizioni() {
        String idKey = "iscrizioni";
        int larColIsc = 800;

        Grid.Column colonna = grid.getGrid().addComponentColumn(turno -> {
            String valueLabel = "";
            String valueIsc = "";
            String code = "";
            String tag = " - ";
            List<Funzione> funzioni = ((Turno) turno).getServizio().getFunzioni();
            List<Iscrizione> iscrizioni = ((Turno) turno).getIscrizioni();
            HashMap<String, Iscrizione> mappa = new HashMap<>();

            if (iscrizioni != null) {
                for (Iscrizione iscrizione : iscrizioni) {
                    mappa.put(iscrizione.getFunzione().getCode(), iscrizione);
                }// end of for cycle
            }// end of if cycle

            for (Funzione funz : funzioni) {
                valueIsc = "";
                if (funz != null) {
                    code = funz.getCode();
                    if (mappa.containsKey(code)) {
                        Iscrizione iscrizione = mappa.get(code);
                        Milite milite = iscrizione.getMilite();
                        valueIsc = html.setVerdeBold(milite.getCognome() + " (" + code + ")");
                    } else {
                        if (funz.isObbligatoria()) {
                            valueIsc = html.setRossoBold(funz.getCode());
                        } else {
                            valueIsc = html.setBluBold(funz.getCode());
                        }// end of if/else cycle
                    }// end of if/else cycle
                    valueLabel += valueIsc + tag;
                }// end of if cycle
            }// end of for cycle

            valueLabel = text.levaCoda(valueLabel, tag);
            return new Label(valueLabel, ContentMode.HTML);
        });//end of lambda expressions

        colonna.setId(idKey);
        colonna.setCaption("Iscrizioni");
        colonna.setWidth(larColIsc);
        float larghezza = grid.getGrid().getWidth();
        grid.getGrid().setWidth(larghezza + larColIsc, Unit.PIXELS);
    }// end of method

    /**
     * Crea la colonna titolo extra (di tipo string)
     */
    private void addColumnTitoloExtra() {
        String idKey = "titoloExtra";
        int larColExtra = 200;

        Grid.Column colonna = grid.getGrid().addColumn(turno -> {
            String titolo = ((Turno) turno).getTitoloExtra();
            return titolo;
        });//end of lambda expressions

        colonna.setId(idKey);
        colonna.setCaption("Extra");
        colonna.setWidth(larColExtra);
        float larghezza = grid.getGrid().getWidth();
        grid.getGrid().setWidth(larghezza + larColExtra, Unit.PIXELS);
    }// end of method

}// end of class
