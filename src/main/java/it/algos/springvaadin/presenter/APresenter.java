package it.algos.springvaadin.presenter;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.entity.logtype.Logtype;
import it.algos.springvaadin.entity.role.RoleForm;
import it.algos.springvaadin.entity.role.RoleList;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.enumeration.EALogLevel;
import it.algos.springvaadin.enumeration.EAPrefType;
import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.exception.NullCompanyException;
import it.algos.springvaadin.form.IAForm;
import it.algos.springvaadin.list.IAList;
import it.algos.springvaadin.service.*;
import it.algos.springvaadin.ui.AUIParams;
import it.algos.springvaadin.view.AView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:32
 */
@Slf4j
@SpringComponent
@Scope("session")
public abstract class APresenter extends APresenterEvents {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AArrayService array;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AReflectionService reflection;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AAnnotationService annotation;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;

    @Autowired
    AUIParams params;

    /**
     * Il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
     */
    protected Class<? extends AEntity> entityClass;


    /**
     * Il service (contenente la repository) viene iniettato dal costruttore della sottoclasse concreta
     */
    public IAService service;


    /**
     * La view viene iniettata dal costruttore della sottoclasse concreta
     */
    protected IAList list;


    /**
     * La view viene iniettata dal costruttore della sottoclasse concreta
     */
    protected IAForm form;


    @Autowired
    private LogService logger;

    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     * La sottoclasse usa un @Qualifier(), per avere la sottoclasse specifica
     * La sottoclasse usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     * @param list    iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     * @param form    iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public APresenter(IAService service, IAList list, IAForm form) {
        this.service = service;
        this.list = list;
        this.form = form;
    }// end of Spring constructor


    /**
     * Gestione di una Lista visualizzata con una Grid
     * Metodo invocato da:
     * 1) una view quando diventa attiva
     * 2) un Evento (azione) che necessita di aggiornare e ripresentare la Lista;
     * tipo dopo un delete, dopo un nuovo record, dopo la edit di un record
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera dal service le colonne da mostrare nella grid
     * Recupera dal service gli items (records) della collection, da mostrare nella grid
     * Recupera dal service i bottoni comando da mostrare nella toolbar del footer (sotto la Grid)
     * Passa il controllo alla view con i dati necessari
     */
    public void setList() {
        List items = null;
        List<Field> columns = null;
        List<EAButtonType> typeButtons = null;

        columns = service.getListFields();
        if (array.isEmpty(columns)) {
            log.warn("Algos - Columns. La grid: " + entityClass + " non ha colonne visibili");
        }// end of if cycle

        items = service.findAll();
        if (array.isEmpty(items)) {
            log.info("Algos - Items. La grid: " + entityClass + " non ha nessuna scheda");
        }// end of if cycle

        typeButtons = service.getListTypeButtons();

        list.start(this, entityClass, columns, items, typeButtons);
    }// end of method


    /**
     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
     */
    public void fireList() {
        Class clazz = list.getViewComponent().getClass();
        params.getNavigator().navigateTo(annotation.getViewName(clazz));
    }// end of method


    /**
     * Usa lo SpringNavigator per cambiare view ed andare ad AForm
     *
     * @param entityBean istanza da creare/elaborare
     */
    public void fireForm(AEntity entityBean) {
        form.getForm().entityBean = entityBean != null ? entityBean : service.newEntity();
        Class clazz = form.getViewComponent().getClass();
        params.getNavigator().navigateTo(annotation.getViewName(clazz));
    }// end of method


    /**
     * Gestione di un Form per presentare i fields
     * Metodo invocato da:
     * 1) una view quando diventa attiva
     * 2) un Evento (azione) che necessita di aggiornare e ripresentare il Form;
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera l'entityBean da mostrare/modificare. Nullo per una nuova scheda
     * Recupera dal service i fields (property) della collection, da mostrare nella view
     * Recupera dal service i bottoni comando da mostrare nella toolbar del footer (sotto i fields)
     * Passa il controllo alla view con i dati necessari
     */
    public void setForm() {
        AEntity entityBean = form.getForm().entityBean != null ? form.getForm().entityBean : service.newEntity();
        List<Field> fields = null;
        List<EAButtonType> typeButtons = null;

        fields = service.getFormFields();
        typeButtons = service.getFormTypeButtons();

        form.start(this, entityClass, entityBean, fields, typeButtons);
    }// end of method


    /**
     * Modificata la selezione della Grid
     * Controlla nella Grid quanti sono i records selezionati
     * Abilita e disabilita i bottoni Modifica ed Elimina della List
     * Se abilitato, inietta nel bottone Edit l'entityBean selezionato
     */
    @Override
    public void selectionChanged() {
        boolean unaSolaRigaSelezionata = false;
        int numRigheSelezionate = 0;
        AButton buttonEdit = null;
        AButton buttonDelete = null;
        AEntity entityBean = null;

        if (list == null) {
            log.warn("List null in APresenter.selectionChanged()");
            return;
        }// end of if cycle
        log.warn("List null in APresenter.selectionChanged()");

        //--recupera le informazioni dalla Grid
        unaSolaRigaSelezionata = list.isUnaSolaRigaSelezionata();
        entityBean = list.getGrid().getEntityBean();

        //--il bottone Edit viene abilitato se c'è UNA SOLA riga selezionata
        if (list.getButton(EAButtonType.edit) != null) {
            buttonEdit = list.getButton(EAButtonType.edit);
            buttonEdit.setEnabled(unaSolaRigaSelezionata);
            buttonEdit.setEntityBean(unaSolaRigaSelezionata ? entityBean : null);
        }// end of if cycle

        //--il bottone Delete viene abilitato in funzione della modalità di selezione adottata
        if (list.getButton(EAButtonType.delete) != null) {
            buttonDelete = list.getButton(EAButtonType.delete);
            buttonDelete.setEnabled(unaSolaRigaSelezionata);
            buttonDelete.setEntityBean(unaSolaRigaSelezionata ? entityBean : null);
        }// end of if cycle

        //@todo RIMETTERE
//        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
//            numRigheSelezionate = view.numRigheSelezionate();
//            if (numRigheSelezionate >= 1) {
//                view.enableButtonList(AButtonType.delete, true);
//            } else {
//                view.enableButtonList(AButtonType.delete, false);
//            }// end of if/else cycle
//        } else {
//            view.enableButtonList(AButtonType.delete, unaSolaRigaSelezionata);
//        }// end of if/else cycle

    }// end of method


    /**
     * Creazione o modifica di un singolo record (entityBean)
     * If entityBean=null, create a new item and edit it in a form
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Passa il controllo alla view con i dati necessari
     *
     * @param entityBean istanza da elaborare
     */
    public void editBean(AEntity entityBean) {
        int numRigheSelezionate = 0;
    }// end of method


    /**
     * Revert (ripristina) button pressed in form
     * Rimane nella view (Form) SENZA registrare e ripristinando i valori iniziali del record (entityBean)
     */
    public void revert() {
        form.revert();
        this.abilitaBottoniForm(false);
    }// end of method


    /**
     * Cancellazione di un singolo record (entityBean)
     * If entityBean=null, recupera dal Form il record selezionato (se riesce)
     * Usa il service per cancellare il record selezionato
     * Rimane nella view (List) e la ricostruisce, ricaricando i dati aggiornati dal service
     *
     * @param entityBean istanza da cancellare
     */
    public void delete(AEntity entityBean) {
        service.delete(entityBean);
        setList();
    }// end of method


    /**
     * Modificato il contenuto di un Field
     * Abilita e disabilita i bottoni Revert e Registra/Accetta del Form
     */
    @Override
    public void valueChanged() {
        this.abilitaBottoniForm(true);
    }// end of method


    /**
     * Evento 'save' (registra) button pressed in form
     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
     * Registra le modifiche nel DB, tramite il service
     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
     */
    public void registra() {
        AEntity oldBean = getBean();
        AEntity modifiedBean = form.commit();

        try { // prova ad eseguire il codice
            service.save(oldBean,modifiedBean);
            fireList();
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            Notification.show("Nuova scheda", NullCompanyException.MESSAGE, Notification.Type.ERROR_MESSAGE);
        }// fine del blocco try-catch
    }// end of method


//    public void logDifferences(Map<String, String> mappa, AEntity modifiedBean, boolean nuovaEntity) {
//        String note;
//        String aCapo = "\n";
//        String clazz = text.primaMaiuscola(modifiedBean.getClass().getSimpleName());
//
//        for (String publicFieldName : mappa.keySet()) {
//            note = "";
//            note += clazz;
//            note += ": ";
//            note += modifiedBean;
//            note += aCapo;
//            note += "Property: ";
//            note += text.primaMaiuscola(publicFieldName);
//            note += aCapo;
//            note += mappa.get(publicFieldName);
//            logger.logEdit(modifiedBean, note);
//        }// end of for cycle
//
//    }// end of method


    public AEntity getBean() {
        AEntity oldBean = null;
        String oldId = form.getBean().getId();
        if (text.isValid(oldId)) {
            oldBean = (AEntity) service.find(oldId);
        }// end of if cycle
        return oldBean;
    }// end of method


//    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean) {
//        return chekDifferences(oldBean, modifiedBean, (EAPrefType) null);
//    }// end of method
//
//    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean, EAPrefType type) {
//        Map<String, String> mappa = new LinkedHashMap();
//        List<String> listaNomi = reflection.getAllFieldsNameNoCrono(oldBean.getClass());
//        Object oldValue;
//        Object newValue;
//        String descrizione = "";
//
//        for (String publicFieldName : listaNomi) {
//            oldValue = reflection.getPropertyValue(oldBean, publicFieldName);
//            newValue = reflection.getPropertyValue(modifiedBean, publicFieldName);
//            descrizione = text.getModifiche(oldValue, newValue, type);
//            if (text.isValid(descrizione)) {
//                mappa.put(publicFieldName, descrizione);
//            }// end of if cycle
//        }// end of for cycle
//
//        return mappa;
//    }// end of method

    /**
     * Regola lo stato dei bottoni del Form
     *
     * @param enabled abilitati o disabilitati
     */
    protected void abilitaBottoniForm(boolean enabled) {
        if (form.getButton(EAButtonType.revert) != null) {
            form.getButton(EAButtonType.revert).setEnabled(enabled);
        }// end of if cycle

        if (form.getButton(EAButtonType.registra) != null) {
            form.getButton(EAButtonType.registra).setEnabled(enabled);
        }// end of if cycle

        if (form.getButton(EAButtonType.accetta) != null) {
            form.getButton(EAButtonType.accetta).setEnabled(enabled);
        }// end of if cycle
    }// end of method

}// end of class
