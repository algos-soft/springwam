package it.algos.springwam.entity.turno;

import com.vaadin.navigator.ViewChangeListener;
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
    private TurnoService service;

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


    /**
     * Metodo di ingresso nella view (nella sottoclasse concreta)
     * Viene invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Passa il controllo al gestore che gestisce questa view (individuato nel costruttore della sottoclasse concreta)
     * Questa classe diversifica la chiamata al presenter a seconda del tipo di view (List, Form, ... altro)
     * Il gestore prepara/elabora i dati e poi ripassa il controllo al metodo AForm.start() di questa view
     *
     * @param event
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    /**
     * Creazione di una view (AForm) contenente i fields
     * Metodo invocato dal Presenter (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * La view comprende:
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param entityClazz         di riferimento, sottoclasse concreta di AEntity
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param typeButtons         lista di (tipi di) bottoni visibili nella toolbar della view AForm
     */
    @Override
    public void start(Class<? extends AEntity> entityClazz, List<Field> reflectedJavaFields, List<EATypeButton> typeButtons) {
        super.start(entityClazz, reflectedJavaFields, typeButtons);
    }

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
     * Regolazioni specifiche per i fields di una entity, dopo aver trascritto la entityBean nel binder
     */
    @Override
    protected void fixFieldsAllways() {
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
        List<Iscrizione> items = service.getIscrizioni((Turno) entityBean);
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
        durata = servizio.getDurata();
        funzioni = servizio.getFunzioni();

        fieldIscrizioni.setItems(items);

        if (fieldIscrizioni != null) {
//            super.addFieldBinder(javaField, fieldIscrizioni);
        }// end of if cycle

        //--aggiunge AField alla lista interna, necessaria per ''recuperare'' un singolo algosField dal nome
        fieldList.add(fieldIscrizioni);

        //--Inizializza il field
        fieldIscrizioni.initContent();

//        //--aggiunge AField alla lista interna, necessaria per ''recuperare'' un singolo algosField dal nome
//        fieldList.add(fieldIscrizioni);
//
        //--Inizializza il field
        fieldIscrizioni.initContent();
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
        if (((Turno) entityBean).getServizio().isOrario()) {
        } else {
            AField fieldTitolo = getField("titoloExtra");
            AField fieldLocalita = getField("localitaExtra");

            int posTitolo = fieldList.indexOf(fieldTitolo);
            if (posTitolo > 0) {
                fieldList.remove(posTitolo);
            }// end of if cycle

            int posLocalita = fieldList.indexOf(fieldLocalita);
            if (posLocalita > 0) {
                fieldList.remove(posLocalita);
            }// end of if cycle

            fieldList.add(fieldTitolo);
            fieldList.add(fieldLocalita);
        }// end of if/else cycle

        super.layoutFields(layout);
    }// end of method

    /**
     * eventuali ultime regolazioni facoiltative nella sottoclasse
     */
    @Override
    protected void fixFormBeforeShow() {
        super.fixFormBeforeShow();
        toolbar.getButton(EATypeButton.conferma).setEnabled(true);
    }// end of method

    /**
     * Trasferisce i valori dai fields del Form alla entityBean
     * Esegue la (eventuale) validazione dei dati
     * Esegue la (eventuale) trasformazione dei dati
     *
     * @return la entityBean del Form
     */
    @Override
    public AEntity commit() {
        AField fieldIscrizioni = getField(ISCRIZIONI);
        List<Iscrizione> listaModificata = null;

        if (fieldIscrizioni != null) {
            listaModificata = (List<Iscrizione>) fieldIscrizioni.getValue();
        }// end of if cycle
        ((Turno)entityBean).setIscrizioni(listaModificata);

        return super.commit();
    }// end of method

}// end of class

