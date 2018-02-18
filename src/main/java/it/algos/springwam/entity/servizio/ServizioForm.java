package it.algos.springwam.entity.servizio;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Layout;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.form.AForm;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AAnnotationService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.toolbar.IAToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

import java.lang.reflect.Field;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-16_08:50:45
 * Estende la Entity astratta AForm di tipo AView per visualizzare i fields
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_SER)
@SpringView(name = AppCost.VIEW_SER_FORM)
@AIScript(sovrascrivibile = true)
public class ServizioForm extends AForm {


    private final static String FUNZIONI = "funzioni";

    /**
     * Funzione specificata in AlgosConfiguration
     */
    @Autowired
    private Function<Class<? extends AField>, AField> fieldFactory;

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
     public ServizioForm(@Lazy @Qualifier(AppCost.TAG_SER) IAPresenter presenter, @Qualifier(ACost.BAR_FORM) IAToolbar toolbar) {
         super(presenter, toolbar);
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
        Field javaField = reflection.getField(entityBean.getClass(), FUNZIONI);
        AField fieldFunzioni = null;

        //--crea un AField e regola le varie properties grafiche (caption, visible, editable, width, ecc)
        fieldFunzioni = fieldFactory.apply(ServizioFieldFunzioni.class);
        fieldFunzioni.inizializza(FUNZIONI, gestore);
        fieldFunzioni.setCaption(annotation.getFormFieldName(javaField));
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

