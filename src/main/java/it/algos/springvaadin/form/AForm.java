package it.algos.springvaadin.form;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.ATextAreaField;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AFieldService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.toolbar.AFormToolbar;
import it.algos.springvaadin.toolbar.AListToolbar;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 23:23
 */
@Slf4j
public abstract class AForm extends AView implements IAForm {


    @Autowired
    protected AFieldService fieldService;

    //    @Autowired
//    protected AFormToolbar toolbar;
    public AEntity entityBean;

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AReflectionService reflection;

    /**
     * Caption informativa posizionata prima del body con lo scorrevole dei fileds
     * Valori standard. Modificabili nelle classi specifiche
     */
    protected final static String CAPTION_CREATE = "Nuova scheda";
    protected final static String CAPTION_EDIT = "Modifica scheda";

    protected List<AField> fieldList;

    //--collegamento tra i fields e la entityBean
    protected Binder binder;

    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param presenter iniettato da Spring
     */
    public AForm(IAPresenter presenter, @Qualifier(ACost.BAR_FORM) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


    /**
     * Metodo invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Aggiunge il riferimento al menuLayout nella view 'entrante' (this)
     * Passa il controllo al Presenter
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //--Regolazioni comuni a tutte le views
        super.enter(event);

        //--Passa il controllo al Presenter
        //--Il punto di ingresso invocato da SpringNavigator è unico e gestito dalla supeclasse AView
        //--Questa classe diversifica la chiamata al Presenter a seconda del tipo di view (List, Form, ... altro)
        //--Il Presenter prepara/elabora i dati e poi ripassa il controllo al metodo AList.start() di questa view
        if (presenter != null) {
            presenter.setForm();
        }// end of if cycle
    }// end of method


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    @Override
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
        String className = entityClazz != null ? entityClazz.getSimpleName() : null;

        caption = className != null ? className + " - " : "";

//        if (entityBean != null && entityBean.getId() != null) {
        if (false) {
            caption += CAPTION_EDIT;
        } else {
            caption += CAPTION_CREATE;
        }// end of if/else cycle

    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     *
     * @param source              presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param entityBean          di riferimento
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     */
    @Override
    protected void creaBody(IAPresenter source, AEntity entityBean, List<Field> reflectedJavaFields) {
        /**
         * Crea un nuovo binder per questo Form e questa Entity
         * Costruisce i componenti grafici AFields (di tipo CustomField), in base ai reflectedFields ricevuti dal service
         * --e regola le varie properties grafiche (caption, visible, editable, width, ecc)
         * Aggiunge i componenti grafici AField al binder
         * Legge la entityBean, ed inserisce nel binder i valori nei fields grafici AFields
         * Aggiunge i componenti grafici AField al layout
         * Aggiunge i componenti grafici AField ad una fieldList interna,
         * --necessaria per ''recuperare'' un singolo algosField dal nome
         */
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setHeightUndefined();

        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixFields(source, layout, reflectedJavaFields, entityBean);

        //--inserisce il layout con i fields in un pannello scorrevole
        bodyLayout.setContent(layout);
    }// end of method


    /**
     * Prepara i fields
     * Ricostruisce una fieldList interna
     * Crea un nuovo binder per questo Form e questa Entity
     * Costruisce i componenti grafici AFields (di tipo CustomField), in base ai reflectedFields ricevuti dal service
     * --e regola le varie properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i componenti grafici AField al binder
     * Aggiunge i componenti grafici AField ad una fieldList interna,
     * --necessaria per ''recuperare'' un singolo algosField dal nome
     * Inizializza i componenti grafici AField
     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel bind
     * Legge la entityBean, ed inserisce nel binder i valori nei fields grafici AFields
     * Aggiunge i componenti grafici AField al layout
     * Eventuali regolazioni specifiche per i fields, dopo la trascrizione della entityBean nel binder
     *
     * @param source              presenter di riferimento da cui vengono generati gli eventi
     * @param layout              in cui inserire i campi (window o panel)
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param entityBean          nuova istanza da creare, oppure istanza esistente da modificare
     */
    protected void fixFields(IAPresenter source, Layout layout, List<Field> reflectedJavaFields, AEntity entityBean) {
        AField algosField;

        //--Ricostruisce una fieldList interna
        this.fieldList = new ArrayList<>();

        //--Crea un nuovo binder per questo Form e questa Entity
        this.binder = new Binder(entityBean.getClass());

        //--spazzola la lista di javaField
        for (Field reflectedField : reflectedJavaFields) {
            //--crea un AField e regola le varie properties grafiche (caption, visible, editable, width, ecc)
            algosField = fieldService.create(source, reflectedField, entityBean);

            //--Aggiunge AField al binder
            //--Aggiunge AField ad una fieldList interna
            //--Inizializza AField
            addFieldBinder(reflectedField, algosField);
        }// end of for cycle

        //--Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        addSpecificAlgosFields();

        //--Legge la entityBean, ed inserisce i valori nel binder (e quindi nei fields grafici AFields)
        binder.readBean(entityBean);

        //--Aggiunge i componenti grafici AField al layout
        layoutFields(layout);

        //--Eventuali regolazioni specifiche per i fields, dopo la trascrizione della entityBean nel binder
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixFieldsAllways();
        if (entityBean != null && entityBean.getId() != null) {
            //--rimanda ad un metodo separato per poterlo sovrascrivere
            fixFieldsEditOnly();
        } else {
            //--rimanda ad un metodo separato per poterlo sovrascrivere
            fixFieldsNewOnly();
        }// end of if/else cycle
    }// end of method


    /**
     * Aggiunge il field al binder
     * Aggiunge eventuali validatori
     * Aggiunge eventuali convertitori
     * Aggiunge eventuali validatori (successivamente ai convertitori)
     * Aggiunge i componenti grafici AField ad una fieldList interna,
     * --necessaria per ''recuperare'' un singolo algosField dal nome
     * Inizializza il field
     *
     * @param reflectedField previsto nel modello dati della Entity
     * @param algosField     del form da visualizzare
     */
    protected void addFieldBinder(Field reflectedField, AField algosField) {
        if (algosField == null) {
            return;
        }// end of if cycle
        Binder.BindingBuilder builder = binder.forField(algosField);

        for (AbstractValidator validator : fieldService.creaValidatorsPre(reflectedField)) {
            builder = builder.withValidator(validator);
        }// end of for cycle

        //@todo RIMETTERE
//        for (Converter converter : fieldService.creaConverters(reflectedField)) {
//            builder = builder.withConverter(converter);
//        }// end of for cycle

        for (AbstractValidator validator : fieldService.creaValidatorsPost(reflectedField)) {
            builder = builder.withValidator(validator);
        }// end of for cycle

        builder.bind(algosField.getName());

        //--aggiunge AField alla lista interna, necessaria per ''recuperare'' un singolo algosField dal nome
        fieldList.add(algosField);

        //--Inizializza il field
        algosField.initContent();
    }// end of method


    /**
     * Aggiunge al binder eventuali fields specifici, prima di trascrivere la entityBean nel binder
     * Sovrascritto
     * Dopo aver creato un AField specifico, usare il metodo super.addFieldBinder() per:
     * Aggiungere AField al binder
     * Aggiungere AField ad una fieldList interna
     * Inizializzare AField
     */
    protected void addSpecificAlgosFields() {
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
    protected void layoutFields(Layout layout) {
        fieldList = sortLista(fieldList);

        if (fieldList != null && fieldList.size() > 0) {
            for (AField algosField : fieldList) {
                //--aggiunge il componente grafico (AField) al layout selezionato
                layout.addComponent(algosField);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Modifica l'ordine in cui vengono presentati i componenti grafici nel layout
     *
     * @param listaOld di fields
     */
    private List<AField> sortLista(List<AField> listaOld) {
        List<AField> listaNew = new ArrayList<>();
        LinkedHashMap<String, AField> mappa = new LinkedHashMap();
        List<String> listaKeys;

        for (AField field : listaOld) {
            mappa.put(field.getName(), field);
        }// end of for cycle

        listaKeys = new ArrayList<>(mappa.keySet());
        listaKeys = sortNameList(listaKeys);

        for (String nome : listaKeys) {
            listaNew.add(mappa.get(nome));
        }// end of for cycle

        return listaNew;
    }// end of method


    /**
     * Modifica l'ordine in cui vengono presentati i componenti grafici nel layout
     *
     * @param nameList di nomi di fields
     */
    protected List<String> sortNameList(List<String> nameList) {
        return nameList;
    }// end of method


    /**
     * Regolazioni specifiche per i fields di una entity, dopo aver trascritto la entityBean nel binder
     */
    protected void fixFieldsAllways() {
    }// end of method


    /**
     * Regolazioni specifiche per i fields di una nuova entity, dopo aver trascritto la entityBean nel binder
     */
    protected void fixFieldsNewOnly() {
    }// end of method


    /**
     * Regolazioni specifiche per i fields di una entity in modifica, dopo aver trascritto la entityBean nel binder
     */
    protected void fixFieldsEditOnly() {
    }// end of method


    /**
     * Crea la barra inferiore dei bottoni di comando
     * Chiamato ogni volta che la finestra diventa attiva
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     */
    @Override
    protected VerticalLayout creaBottom(IAPresenter source, List<EAButtonType> typeButtons) {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(false);
        bottomLayout.setHeightUndefined();
        toolbar.inizializza(source, typeButtons);

//        fixToolbar();

        bottomLayout.addComponent((AFormToolbar) toolbar);
        return bottomLayout;
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
        Object value = null;
        ATextAreaField textArea;

        try { // prova ad eseguire il codice
            binder.writeBean(entityBean);

            //--@todo PATCH perché il binder non prende (ancora) i fields della superclasse
            for (Object obj : binder.getFields().toArray()) {
                if (obj instanceof ATextAreaField) {
                    textArea = (ATextAreaField) obj;
                    value = textArea.getValue();
                }// end of if cycle
            }// end of for cycle

            entityBean.note = (String) value;
        } catch (Exception unErrore) { // intercetta l'errore
            int errore = 87;
        }// fine del blocco try-catch

        return entityBean;
    }// end of method


    /**
     * Esegue il 'rollback' del Form
     * Revert (ripristina) button pressed in form
     * Usa la entityBean già presente nel form, ripristinando i valori iniziali
     */
    @Override
    public void revert() {
        binder.readBean(entityBean);
    }// end of method


    /**
     * Componente concreto di questa interfaccia
     */
    @Override
    public AForm getForm() {
        return this;
    }// end of method

    /**
     * Entity bean
     */
    public AEntity getBean() {
        return entityBean;
    }// end of method

}// end of class
