package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 27-ago-2017
 * Time: 11:14
 * <p>
 * Field di un Form
 * Sequenza alla nuovo:
 * AlgosPresenterImpl.edit() -> AlgosPresenterImpl.edit()
 * AlgosViewImpl.setForm() ->
 * AlgosFormImpl.restart() -> AlgosFormImpl.creaAddBindFields() -> AlgosFormImpl.creaFields() ->
 * ViewField.create() ->
 * AFieldFactoryImpl.create() ->
 * AlgosConfiguration.Function<Class<? extends AField>, AField> FieldFactory() -> AlgosConfiguration.getField() ->
 * AField.<init> -> AField.inizia() ->
 * fixCaption() -> creaContent() -> setName() -> setSource() -> addListener() -> regolaParametri() -> setWidth() -> setFocus() ->
 * AlgosFormImpl.bindFields() -> AField.initContent() -> AField.getValue() -> AField.doSetValue
 */
@SpringComponent
public abstract class AField<T> extends CustomField<Object> {


    @Autowired
    public ATextService text;

    //--Obbligatorio publicFieldName
    protected String name;


    //--Obbligatorio presenter che gestisce l'evento
    protected IAListener source;


    //--Opzionale (window, dialog, presenter) a cui indirizzare l'evento
    protected IAListener target;


    //--Opzionale (entityBean) in elaborazione
    protected AEntity entityBean;


    /**
     * Alcuni fields usano un bottone come componente interno
     * Property iniettata nel costruttore usato da Spring PRIMA della chiamata del browser
     * Property iniettata nel costruttore della sottoclasse concreta
     */
    protected AButton button;


    /**
     * Property iniettata da Spring
     */
    @Autowired
    protected ApplicationEventPublisher publisher;


    //--default che può essere sovrascritto nella sottoclasse specifica ed ulteriormente modificato da una @Annotation
    //--si applica al field
    private boolean STANDARD_ENABLED = true;

    //--default che può essere sovrascritto nella sottoclasse specifica ed ulteriormente modificato da una @Annotation
    //--si applica al field
    private boolean STANDARD_REQUIRED_VISIBLE = false;

    //--default che può essere sovrascritto nella sottoclasse specifica ed ulteriormente modificato da una @Annotation
    //--si applica al field
    private boolean STANDARD_VISIBLE = true;

    //--default utilizzato nella sottoclasse specifica ed ulteriormente modificabile da una @Annotation
    //--si applica al Component
    protected String STANDARD_SHORT_TEXT_WITH = "6em";
    protected String STANDARD_MEDIUM_TEXT_WITH = "15em";
    protected String STANDARD_LONG_TEXT_WITH = "24em";
    protected String STANDARD_INT_WITH = "6em";
    protected String STANDARD_DATE_WITH = "8em";


    /**
     * Componente principale
     */
    public TextField textField;


    /**
     * Costruttore base senza parametri
     * Viene utilizzato dalla Funzione -> FieldFactory in AlgosConfiguration
     */
    public AField() {
    }// end of constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
    }// end of method


    /**
     * Metodo invocato da parte di AFieldFactory subito dopo la creazione del field
     * Non parte dal costruttore, perché AFieldFactory usa un costruttore SENZA parametri
     *
     * @param publicFieldName nome visibile del field
     * @param source          del presenter che gestisce questo field
     */
    public void inizializza(String publicFieldName, IAListener source) {
        this.creaContent();
        this.setName(publicFieldName);
        this.setSource(source);
        this.setTarget(target != null ? target : source);
        this.addListener();
        this.regolaParametri();
    }// end of method


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        textField = new TextField();
    }// end of method


    @Override
    public Component initContent() {
        return textField;
    }// end of method


    /**
     * Regola i parametri base per la visualizzazione del field nel form
     * Possono essere sovrascritti nella sottoclasse specifica
     * Possono essere successivamente modificati da una @Annotation
     */
    protected void regolaParametri() {
        this.setEnabled(STANDARD_ENABLED);
        this.setRequiredIndicatorVisible(STANDARD_REQUIRED_VISIBLE);
        this.setVisible(STANDARD_VISIBLE);
        this.setWidth(STANDARD_MEDIUM_TEXT_WITH);
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (textField != null && text.isValid((String) value)) {
            textField.setValue((String) value);
        }// end of if cycle
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        return null;
    }// end of method

    public void doValue(AEntity entityBean) {
    }// end of method

    public String getName() {
        return name;
    }// end of method

    public void setName(String name) {
        this.name = name;
    }// end of method

    //@todo RIMETTERE
//    public AlgosPresenterImpl getFormPresenter() {
//        return null;
//    }// end of method


    public void setWidth(String width) {
        if (textField != null) {
            textField.setWidth(width);
        }// end of if cycle
    }// end of method


    public void setRows(int rows) {
    }// end of method


    public void setFocus(boolean focus) {
        if (textField != null && focus) {
            textField.focus();
        }// end of if cycle
    }// end of method


    public void setSource(IAListener source) {
        this.source = source;
        if (button != null) {
            if (source != null) {
                button.setSource(source);
            }// end of if cycle

            if (entityBean != null) {
                button.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
    }// end of method

    public void setTarget(IAListener target) {
        this.target = target;
        if (button != null) {
            if (target != null) {
                button.setTarget(target);
            }// end of if cycle

            if (entityBean != null) {
                button.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (textField != null) {
            textField.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method


    public void setEntityBean(AEntity entityBean) {
        this.entityBean = entityBean;
    }// end of method


    public void setButton(AButton button) {
        this.button = button;
    }// end of method

    public AButton getButton() {
        return button;
    }

    public void setIconButton(Resource res) {
        if (button != null) {
            button.setIcon(res);
        }// end of if cycle
    }// end of method


    /**
     * Fire event
     * source     Obbligatorio questo field
     * target     Obbligatorio (window, dialog, presenter) a cui indirizzare l'evento
     * entityBean Opzionale (entityBean) in elaborazione
     */
    public void publish() {
        if (source != null) {
            publisher.publishEvent(new AFieldEvent(EATypeField.valueChanged, source, target, entityBean,this));
        }// end of if cycle
    }// end of method


}// end of class

