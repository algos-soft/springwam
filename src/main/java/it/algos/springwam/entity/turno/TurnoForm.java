package it.algos.springwam.entity.turno;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.button.AButtonFactory;
import it.algos.springvaadin.component.AHorizontalLayout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.form.AForm;
import it.algos.springvaadin.menu.IAMenu;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AArrayService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springvaadin.toolbar.AFormToolbar;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.iscrizione.IscrizioneService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioFieldFunzioni;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
 * Estende la Entity astratta AForm di tipo AView per visualizzare i fields
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@Slf4j
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_TUR)
@SpringView(name = AppCost.VIEW_TUR_FORM)
@AIScript(sovrascrivibile = true)
public class TurnoForm extends AForm {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AArrayService array;

    /**
     * Factory per la creazione dei bottoni
     */
    @Autowired
    private AButtonFactory buttonFactory;

    /**
     * Funzione specificata in AlgosConfiguration
     */
    @Autowired
    private Function<Class<? extends AField>, AField> fieldFactory;

    @Autowired
    private IscrizioneService iscrizioneService;

    private final static String TORNA = "Torna al tabellone";

    private final static String ISCRIZIONI = "iscrizioni";


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
     */
    public TurnoForm(@Lazy @Qualifier(AppCost.TAG_TUR) IAPresenter presenter, @Qualifier(ACost.BAR_FORM) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


//    /**
//     * Crea due bottoni in basso:
//     * Annulla
//     * Conferma
//     */
//    private AFormToolbar creaBottomTurno(IAPresenter gestore, IAPresenter source) {
//        ((AFormToolbar) toolbar).deleteAllButtons();
//
//        AButton buttonAnnulla = ((AFormToolbar) toolbar).creaAddButton(EATypeButton.annulla, source);
//        buttonAnnulla.setWidth("12em");
//        buttonAnnulla.setCaption(TORNA);
//
//        ((AFormToolbar) toolbar).creaAddButton(EATypeButton.registra, gestore, source, null, null);
//
//        return (AFormToolbar) toolbar;
//    }// end of method


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    @Override
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
        String servizio = "";
        String data = "";

        if (entityBean != null && entityBean.getId() != null) {
            if (entityBean instanceof Turno) {
                servizio = ((Turno) entityBean).getServizio().getDescrizione();
            }// end of if cycle
            caption = "Turno di " + servizio + "  - Modifica delle iscrizioni";
        } else {
            caption = "Creazione di un nuovo turno";
        }// end of if/else cycle
    }// end of method


    /**
     * Regolazioni specifiche per i fields di una entity in modifica, dopo aver trascritto la entityBean nel binder
     */
    protected void fixFieldsEditOnly() {
        disabilitaField("giorno");
        disabilitaField("servizio");
        disabilitaField("inizio");
        disabilitaField("fine");
    }// end of method


    /**
     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
     * Sovrascritto
     * Dopo aver creato un AField specifico, usare il metodo super.addFieldBinder() per:
     * Aggiungere AField al binder
     * Aggiungere AField ad una fieldList interna
     * Inizializzare AField
     */
    @Override
    protected void addSpecificAlgosFields() {
        Field javaField = reflection.getField(entityBean.getClass(), ISCRIZIONI);
        TurnoFieldIscrizioni fieldIscrizioni = null;
        Servizio servizio = null;
        List<Funzione> funzioni = null;
        List<Iscrizione> items = new ArrayList<>();
        String funzCode = "";
        boolean trovata = false;
        Iscrizione iscrizione;
        int durata = 0;

        //--crea un AField e regola le varie properties grafiche (caption, visible, editable, width, ecc)
        fieldIscrizioni = (TurnoFieldIscrizioni) fieldFactory.apply(TurnoFieldIscrizioni.class);
        fieldIscrizioni.inizializza(ISCRIZIONI, source);
        fieldIscrizioni.setCaption(annotation.getFormFieldName(javaField));
        fieldIscrizioni.setEntityBean(entityBean);
        fieldIscrizioni.setWidth("28em");

        servizio = ((Turno) entityBean).getServizio();
        durata= servizio.getDurata();
        funzioni = servizio.getFunzioni();
        List<Iscrizione> iscrizioni = ((Turno) entityBean).getIscrizioni();

        for (Funzione funz : funzioni) {
            funzCode = funz.getCode();
            trovata = false;

            if (array.isValid(iscrizioni)) {
                for (Iscrizione iscr : iscrizioni) {
                    if (iscr.getFunzione().id.equals(funz.id)) {
                        items.add(iscr);
                        trovata = true;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            if (!trovata) {
                items.add(iscrizioneService.newEntity(funz,0));
            }// end of if cycle
        }// end of for cycle

        fieldIscrizioni.setItems(items);

        if (fieldIscrizioni != null) {
            super.addFieldBinder(javaField, fieldIscrizioni);
        }// end of if cycle


//        //--aggiunge AField alla lista interna, necessaria per ''recuperare'' un singolo algosField dal nome
//        fieldList.add(fieldIscrizioni);
//
//        //--Inizializza il field
//        fieldIscrizioni.initContent();

    }// end of method


    /**
     * Aggiunge i componenti grafici AField al layout
     * Inserimento automatico nel layout ''verticale''
     * La sottoclasse può sovrascrivere integralmente questo metodo per realizzare un layout personalizzato
     * La sottoclasse può sovrascrivere questo metodo; richiamarlo e poi aggiungere altri AField al layout verticale
     * Nel layout sono già presenti una Label (sopra) ed una Toolbar (sotto)
     *
     * @param layout in cui inserire i campi (window o panel)
     */
    @Override
    protected void layoutFields(Layout layout) {
        AField fieldTitolo = getField("titoloExtra");
        AField fieldLocalita = getField("localitaExtra");

        int posTitolo = fieldList.indexOf(fieldTitolo);
        fieldList.remove(posTitolo);

        int posLocalita = fieldList.indexOf(fieldLocalita);
        fieldList.remove(posLocalita);

        fieldList.add(fieldTitolo);
        fieldList.add(fieldLocalita);

        super.layoutFields(layout);
    }// end of method

}// end of class

