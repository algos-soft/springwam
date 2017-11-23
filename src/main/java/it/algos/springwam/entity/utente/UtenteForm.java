package it.algos.springwam.entity.utente;

import com.vaadin.ui.Layout;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibArray;
import it.algos.springvaadin.lib.LibReflection;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.entity.servizio.ServizioFieldFunzioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.internal.Function;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_UTE)
public class UtenteForm extends AlgosFormImpl {

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
    public UtenteForm(@Qualifier(AppCost.TAG_UTE) AlgosService service,
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
        fieldFunzioni = fieldFactory.apply(UtenteFieldFunzioni.class);
        fieldFunzioni.inizializza(FUNZIONI, source);
        fieldFunzioni.setCaption(LibAnnotation.getFormFieldName(javaField));
        fieldFunzioni.setEntityBean(entityBean);
        fieldFunzioni.setWidth(LibAnnotation.getWidthEM(javaField));

        if (fieldFunzioni != null) {
            super.addFieldBinder(javaField, fieldFunzioni);
        }// end of if cycle

    }// end of method


    /**
     * Modifica l'ordine in cui vengono presentati i componenti grafici nel layout
     *
     * @param nameList di nomi di fields
     */
    protected List<String> sortNameList(List<String> nameList) {
        String userFieldName = "user";
        String cognomeFieldName = "cognome";
        String funzioniFieldName = "funzioni";
        String adminFieldName = "admin";
        String dipendenteFieldName = "dipendente";
        String infermiereFieldName = "infermiere";

        nameList = LibArray.inserisceDopo(nameList, userFieldName, cognomeFieldName);
        nameList = LibArray.inserisceDopo(nameList, funzioniFieldName, userFieldName);
        nameList = LibArray.inserisceDopo(nameList, adminFieldName, funzioniFieldName);
        nameList = LibArray.inserisceDopo(nameList, dipendenteFieldName, adminFieldName);
        nameList = LibArray.inserisceDopo(nameList, infermiereFieldName, dipendenteFieldName);

        return nameList;
    }// end of method


}// end of class

