package it.algos.springwam.entity.servizio;

import com.vaadin.data.Binder;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Layout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.AComboField;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibReflection;
import it.algos.springvaadin.service.FieldService;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springvaadin.service.AlgosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.internal.Function;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_SER)
public class ServizioForm extends AlgosFormImpl {


    @Autowired
    private FieldService fieldService;

    private final static String FUNZIONI = "funzioni";

    /**
     * Funzione specificata in AlgosConfiguration
     */
    @Autowired
    private Function<Class<? extends AField>, AField> fieldFactory;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public ServizioForm(@Qualifier(AppCost.TAG_FUN) AlgosService service,
                        @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                        @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


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
        Field javaField = LibReflection.getField(entityBean.getClass(), FUNZIONI);
        AField fieldFunzioni = null;

        //--crea un AField e regola le varie properties grafiche (caption, visible, editable, width, ecc)
        fieldFunzioni = fieldFactory.apply(ServizioFieldFunzioni.class);
        fieldFunzioni.inizializza(FUNZIONI, source);
        fieldFunzioni.setCaption(LibAnnotation.getFormFieldName(javaField));
        fieldFunzioni.setEntityBean(entityBean);
        fieldFunzioni.setWidth("34em");

        if (fieldFunzioni != null) {
            super.addFieldBinder(javaField, fieldFunzioni);
        }// end of if cycle

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
        AField fieldNote = getField("note");
        AField fieldFunz = getField("funzioni");
        int posVis = fieldList.indexOf(fieldNote);
        int posFunz = fieldList.indexOf(fieldFunz);
        fieldList.remove(posFunz);
        fieldList.add(posVis, fieldFunz);

        super.layoutFields(layout);
    }// end of method


}// end of class

