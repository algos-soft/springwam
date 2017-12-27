package it.algos.springvaadin.form;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.Converter;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.panel.AlgosPanel;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.service.FieldService;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.AToolbarImpl;
import it.algos.springvaadin.toolbar.LinkToolbar;
import it.algos.springvaadin.view.ViewField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by gac on 10/07/17
 * Implementazione standard dell'annotation AlgosField
 * <p>
 * Creazione del form - Ricrea tutto ogni volta che diventa attivo
 * Riceve dal service tutti i dati da presentare: entityBean e lista di javaFields
 * Presentazione scelta tra pannello a tutto schermo, oppure finestra popup
 * Dai javaFields costruisce i componenti grafici di tipo AField (estensione di Vaadine.CustomField)
 */
@Slf4j
public class AlgosFormImpl extends VerticalLayout implements AlgosForm {


    @Autowired
    private ViewField viewField;

    //--il service (contenente la repository) viene iniettato dal costruttore della sottoclasse concreta
    protected AlgosService service;

    @Autowired
    protected FieldService fieldService;

    //--eventuale finestra (in alternativa alla presentazione a tutto schermo)
    protected Window window;

    //--flag
//    private boolean usaSeparateFormDialog;

    //--L'entityBean viene inserita come parametro nel metodo restart, chiamato dal presenter
    protected AEntity entityBean;

    //--collegamento tra i fields e la entityBean
    protected Binder binder;

    protected List<AField> fieldList;

    protected ApplicationListener source;

    //--intestazioni informative per Form
    //--valori standard
    protected final static String CAPTION_CREATE = "Nuova scheda";
    protected final static String CAPTION_EDIT = "Modifica scheda";


//    //--Top - Eventuali scritte esplicative come collezione usata, records trovati, ecc
//    protected VerticalLayout topLayout;
//
//    //--valore che può essere regolato nella classe specifica
//    //--usando un metodo @PostConstruct
//    protected String caption;
//
//    //--Body - Grid. Scorrevole
//    protected Panel bodyPanel ;
//
//    //--AlgosGrid, iniettata dal costruttore
//    //--un eventuale Grid specifico verrebbe iniettato dal costruttore della sottoclasse concreta
//    protected AlgosGrid grid;
//
//    //--Bottom - Barra dei bottoni
//    protected VerticalLayout bottomLayout;

    @Autowired
    protected AlgosPanel bodyPanel;


    //--toolbar coi bottoni, iniettato dal costruttore
    //--un eventuale Toolbar specifica verrebbe iniettata dal costruttore della sottoclasse concreta
    protected AToolbar toolbar;
    protected AToolbar toolbarNormale;
    private AToolbar toolbarLink;

    @Autowired
    public PreferenzaService pref;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param toolbarNormale iniettata da Spring
     * @param toolbarLink    iniettata da Spring
     */
    public AlgosFormImpl(AlgosService service,
                         @Qualifier(Cost.BAR_FORM) AToolbar toolbarNormale,
                         @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        this.service = service;
        this.toolbarNormale = toolbarNormale;
        this.toolbarLink = toolbarLink;
        this.setMargin(false);
    }// end of Spring constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
    }// end of method


    /**
     * Creazione del form
     * Ricrea tutto ogni volta che diventa attivo
     * Sceglie tra pannello a tutto schermo, oppure finestra popup
     *
     * @param source          presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param reflectedFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param entityBean      nuova istanza da creare, oppure istanza esistente da modificare
     */
    @Override
    public void restart(ApplicationListener source, List<Field> reflectedFields, AEntity entityBean) {
        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
            this.addStyleName("blueBg");
        }// end of if cycle
        this.setMargin(false);
        this.setWidth("100%");
        this.setHeight("100%");

        this.source = source;
        this.entityBean = entityBean;
        toolbar = toolbarNormale;

        usaAllScreen(source, reflectedFields, entityBean);
    }// end of method


    /**
     * Creazione del form di un altro modulo/collezione
     * Ricrea tutto ogni volta che diventa attivo
     * Solo finestra popup
     *
     * @param source          presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param sourceField     di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     * @param entityBean      nuova istanza da creare, oppure istanza esistente da modificare
     * @param reflectedFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param type            per selezionare il ButtonRegistra, che registra subito
     *                        oppure ButtonAccetta, che demanda la registrazione alla scheda chiamante
     */
    public void restartLink(ApplicationListener source, ApplicationListener target, AField sourceField, AEntity entityBean, List<Field> reflectedFields, AButtonType type) {
        this.entityBean = entityBean;
        toolbar = toolbarLink;

        if (type == AButtonType.editLinkDBRef) {
            ((LinkToolbar) toolbar).setUsaBottoneRegistra(true);
        } else {
            ((LinkToolbar) toolbar).setUsaBottoneRegistra(false);
        }// end of if/else cycle

        usaSeparateFormDialog(source, target, entityBean, sourceField, reflectedFields);
    }// end of method


    /**
     * Usa tutto lo schermo
     * Rimuove i componenti grafici preesistenti
     * Prepara la caption e la aggiunge al layout
     * Crea un nuovo binder per questo Form e questa Entity
     * Costruisce i componenti grafici AFields (di tipo CustomField), in base ai reflectedFields ricevuti dal service
     * --e regola le varie properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i componenti grafici AField al binder
     * Legge la entityBean, ed inserisce nel binder i valori nei fields grafici AFields
     * Aggiunge i componenti grafici AField al layout
     * Aggiunge i componenti grafici AField ad una fieldList interna,
     * --necessaria per ''recuperare'' un singolo algosField dal nome
     * Prepara la toolbar e la aggiunge al layout
     *
     * @param source          presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param reflectedFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param entityBean      nuova istanza da creare, oppure istanza esistente da modificare
     */
    protected void usaAllScreen(ApplicationListener source, List<Field> reflectedFields, AEntity entityBean) {
        //--Rimuove i componenti grafici preesistenti
        this.removeAllComponents();

        //--Prepara la caption e la aggiunge al layout
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixTop(this);

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
        VerticalLayout bodyLayout = new VerticalLayout();
        bodyLayout.setMargin(false);
        bodyLayout.setHeightUndefined();

        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixFields(source, bodyLayout, reflectedFields, entityBean);

        //--inserisce il layout con i fields in un pannello scorrevole
        bodyPanel.setContent(bodyLayout);
        this.addComponent(bodyPanel);

        //--Prepara la toolbar e la aggiunge al layout
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixToolbar(this);

        this.setExpandRatio(bodyPanel, 1);
    }// end of method

    /**
     * Prepara la caption e la aggiunge al contenitore grafico
     * Sovrascrivibile
     */
    protected void fixTop(Layout layout) {
        String caption = fixCaption(entityBean);
        Label label = new LabelRosso(caption);
        layout.addComponent(label);
    }// end of method

    /**
     * Prepara la toolbar e la aggiunge al contenitore grafico
     * Sovrascrivibile
     */
    protected void fixToolbar(Layout layout) {
        List<AButtonType> typeButtons = service.getFormTypeButtons();
        toolbar.inizializza(source, typeButtons);
        layout.addComponent(toolbar.get());
    }// end of method


    /**
     * Prepara la toolbar e la aggiunge al contenitore grafico
     * Sovrascrivibile
     */
    protected void fixToolbar(Layout layout, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
        toolbar.inizializza(source, target, entityBean, sourceField);
        layout.addComponent(toolbar.get());
    }// end of method


    /**
     * Crea una finestra a se, che verrà chiusa alla dismissione del Form
     *
     * @param source          presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param entityBean      nuova istanza da creare, oppure istanza esistente da modificare
     * @param sourceField     di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     * @param reflectedFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     */
    protected void usaSeparateFormDialog(ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField, List<Field> reflectedFields) {
//        this.removeAllComponents();

        if (window != null) {
            window.close();
            window = null;
        }// end of if cycle

        // create the window
        window = new Window();
        window.setResizable(true);
        window.setModal(true);
        window.setHeightUndefined();

        VerticalLayout layout = new VerticalLayout();

        //--Prepara la caption e la aggiunge al layout
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixTop(layout);

        fixFields(target, layout, reflectedFields, entityBean);

        //--Prepara la toolbar e la aggiunge al layout
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        fixToolbar(layout, source, target, entityBean, sourceField);

        window.setContent(layout);
        window.center();
        LibVaadin.getUI().addWindow(window);
        window.bringToFront();
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
     * @param source          presenter di riferimento da cui vengono generati gli eventi
     * @param layout          in cui inserire i campi (window o panel)
     * @param reflectedFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param entityBean      nuova istanza da creare, oppure istanza esistente da modificare
     */
    protected void fixFields(ApplicationListener source, Layout layout, List<Field> reflectedFields, AEntity entityBean) {
        AField algosField;

        //--Ricostruisce una fieldList interna
        this.fieldList = new ArrayList<>();

        //--Crea un nuovo binder per questo Form e questa Entity
        this.binder = new Binder(entityBean.getClass());

        //--spazzola la lista di javaField
        for (Field reflectedField : reflectedFields) {
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

        for (Converter converter : fieldService.creaConverters(reflectedField)) {
            builder = builder.withConverter(converter);
        }// end of for cycle

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
     * Valore del binder specifico
     *
     * @param propertyName da cui recuperare il valore
     *
     * @return valore del binding selezionato
     */
    @Deprecated
    protected Object getBinderValue(String propertyName) {
        Object value = null;
        Optional<Binder.Binding> optional = null;
        Binder.Binding binding = null;
        AField field = null;

        if (binder == null) {
            return value;
        }// end of if cycle

        optional = binder.getBinding(propertyName);

        if (optional != null) {
            binding = optional.get();
        }// end of if cycle

        if (binding != null) {
            field = (AField) binding.getField();
        }// end of if cycle

        if (field != null) {
            value = field.getValue();
        }// end of if cycle

        return value;
    }// end of method

    /**
     * Valore di default per una nuova scheda
     * Valore eventualmente modificato da codice nella sottoclasse per cause specifiche non di InterfacciaUtente
     */
    protected void setFieldValue(String publicFieldName, Object value) {
        AField field = getField(publicFieldName);

        if (entityBean != null) {
            try { // prova ad eseguire il codice
                if (field != null) {
                    field.setValue(value);
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
    }// end of method


    /**
     * Valore di un field
     */
    protected Object getFieldValue(String publicFieldName) {
        Object value = null;
        AField field = getField(publicFieldName);

        if (entityBean != null) {
            try { // prova ad eseguire il codice
                if (field != null) {
                    value = field.getValue();
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return value;
    }// end of method

    /**
     * Field enabled
     */
    protected void setFieldEnabled(String publicFieldName, boolean enabled) {
        AField field = getField(publicFieldName);

        if (entityBean != null) {
            try { // prova ad eseguire il codice
                if (field != null) {
                    field.setEnabled(enabled);
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
    }// end of method


    /**
     * Recupera il field dal nome
     */
    protected AField getField(String publicFieldName) {

        for (AField field : fieldList) {
            if (field.getName().equals(publicFieldName)) {
                return field;
            }// end of if cycle
        }// end of for cycle

        return null;
    }// end of method


    /**
     * Esegue il 'rollback' del Form
     * Revert (ripristina) button pressed in form
     * Usa la entityBean già presente nel form, ripristinando i valori iniziali
     * Rimane nel form SENZA registrare
     */
    @Override
    public void revert() {
        binder.readBean(entityBean);
    }// end of method

    /**
     * Checks all current validation errors
     * Se ce ne sono, rimane nel form SENZA registrare
     *
     * @return ci sono errori in almeno una delle property della entity
     */
    @Override
    public boolean entityHasError() {
        return binder.validate().hasErrors();
    }// end of method

    /**
     * Checks if the entity has no current validation errors at all
     * Se la entity è OK, può essere registrata
     *
     * @return tutte le property della entity sono valide
     */
    @Override
    public boolean entityIsOk() {
        boolean entityValida = false;

        try { // prova ad eseguire il codice
            entityValida = binder != null && binder.validate().isOk();
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("Accetta", "Scheda non valida", Notification.Type.WARNING_MESSAGE);
        }// fine del blocco try-catch

        return entityValida;
    }// end of method

    /**
     * All fields errors
     *
     * @return errors
     */
    @Override
    public List<ValidationResult> getEntityErrors() {
        BinderValidationStatus<Preferenza> status = binder.validate();
        return binder != null ? binder.validate().getValidationErrors() : new ArrayList<>();
    }// end of method

    /**
     * Trasferisce i valori dai campi dell'annotation alla entityBean
     * Esegue la (eventuale) validazione dei dati
     * Esegue la (eventuale) trasformazione dei dati
     *
     * @return la entityBean del Form
     */
    @Override
    public AEntity commit() {

        try { // prova ad eseguire il codice
            binder.writeBean(entityBean);

            //--@todo PATCH perché il binder non prende (ancora) i fields della superclasse
            AField field = getField("note");
            entityBean.note = (String) field.getValue();
        } catch (Exception unErrore) { // intercetta l'errore
            int errore = 87;
        }// fine del blocco try-catch

        closeWindow();

        return entityBean;
    }// end of method


    /**
     * Label di informazione
     *
     * @param entityBean istanza da presentare
     *
     * @return la label a video
     */
    protected String fixCaption(AEntity entityBean) {
        String caption = entityBean.getClass().getSimpleName() + " - ";

        if (entityBean != null && entityBean.getId() != null) {
            caption += CAPTION_EDIT;
        } else {
            caption += CAPTION_CREATE;
        }// end of if/else cycle

        return caption;
    }// end of method


    /**
     * Chiude la (eventuale) finestra utilizzata
     */
    @Override
    public void closeWindow() {
        if (window != null) {
            window.close();
            window = null;
        }// end of if cycle
    }// end of method


    /**
     * Restituisce il componente concreto
     *
     * @return il componente (window o panel)
     */
    @Override
    public Component getComponent() {
        return this;
    }// end of method


    /**
     * Restituisce la entity utilizzata
     *
     * @return la entityBean del Form
     */
    @Override
    public AEntity getEntityBean() {
        return entityBean;
    }// end of method

    /**
     * Abilita o disabilita lo specifico bottone della Toolbar
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    @Override
    public void enableButton(AButtonType type, boolean status) {
        toolbar.enableButton(type, status);
    }// end of method


    /**
     * Inserisce nei bottoni Registra o Accetta il Field che va notificato
     *
     * @param parentField che ha richiesto questo form
     */
    @Override
    public void setParentField(AField parentField) {
//        toolbar.setParentField(parentField);@todo rimettere
    }// end of method

    /**
     * Registra eventuali dipendenze di un field del Form
     */
    @Override
    public void saveSons() {
        for (AField field : fieldList) {
//            field.saveSon();
        }// end of for cycle
    }// end of method
//
//    public void setUsaSeparateFormDialog(boolean usaSeparateFormDialog) {
//        this.usaSeparateFormDialog = usaSeparateFormDialog;
//    }// end of method


    public List<AField> getFieldList() {
        return fieldList;
    }// end of method

}// end of class
