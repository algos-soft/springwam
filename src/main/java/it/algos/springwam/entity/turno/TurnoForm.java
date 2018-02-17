package it.algos.springwam.entity.turno;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.form.AForm;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springwam.entity.servizio.ServizioFieldFunzioni;
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
 * Date: 2018-02-04_17:19:25
 * Estende la Entity astratta AForm di tipo AView per visualizzare i fields
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_TUR)
@SpringView(name = AppCost.VIEW_TUR_FORM)
@AIScript(sovrascrivibile = true)
public class TurnoForm extends AForm {


    private final static String ISCRIZIONI = "iscrizioni";

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
     public TurnoForm(@Lazy @Qualifier(AppCost.TAG_TUR) IAPresenter presenter, @Qualifier(ACost.BAR_FORM) IAToolbar toolbar) {
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
        Field javaField = reflection.getField(entityBean.getClass(), ISCRIZIONI);
        AField fieldIscrizioni = null;

        //--crea un AField e regola le varie properties grafiche (caption, visible, editable, width, ecc)
        fieldIscrizioni = fieldFactory.apply(TurnoFieldIscrizioni.class);
        fieldIscrizioni.inizializza(ISCRIZIONI, presenter);
        fieldIscrizioni.setCaption(annotation.getFormFieldName(javaField));
        fieldIscrizioni.setEntityBean(entityBean);
        fieldIscrizioni.setWidth("34em");

        if (fieldIscrizioni != null) {
//            super.addFieldBinder(javaField, fieldIscrizioni);
        }// end of if cycle


//        //--aggiunge AField alla lista interna, necessaria per ''recuperare'' un singolo algosField dal nome
//        fieldList.add(fieldIscrizioni);
//
//        //--Inizializza il field
//        fieldIscrizioni.initContent();

    }// end of method

}// end of class

