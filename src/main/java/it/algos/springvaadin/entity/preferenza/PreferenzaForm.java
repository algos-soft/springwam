package it.algos.springvaadin.entity.preferenza;

import com.vaadin.data.Binder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.entity.log.LogType;
import it.algos.springvaadin.field.AComboField;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.field.AJSonField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by gac on 16-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PRE)
@Slf4j
public class PreferenzaForm extends AlgosFormImpl {

    @Autowired
    private LogService logger;

    private final static String TYPE = "type";
    private final static String VALUE = "value";

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public PreferenzaForm(@Qualifier(Cost.TAG_PRE) AlgosService service,
                          @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                          @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


    /**
     * Eventuali regolazioni specifiche per i fields di una entity, dopo il binder
     */
    @Override
    protected void fixFieldsNewOnly() {
        super.setFieldValue("type", PrefType.bool);
        super.setFieldValue("livello", ARoleType.developer);
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
        AField fieldType = null;
        AField fieldValue = null;
        PrefType type = null;
        byte[] bytes;
        Object objValue;
        String stringValue;
        int intValue;

        try { // prova ad eseguire il codice

            fieldType = getField(TYPE);
            if (fieldType != null && fieldType instanceof AComboField) {
                type = (PrefType) ((AComboField) fieldType).getValue();
            }// end of if cycle

            fieldValue = getField(VALUE);
            if (fieldValue != null && fieldValue instanceof AJSonField) {
                bytes = ((AJSonField) fieldValue).getValue();
                objValue = type.bytesToObject(bytes);
                switch (type) {
                    case string:
                        stringValue = (String) objValue;
                        if (LibText.isEmpty(stringValue)) {
                            LibAvviso.error("Occorre inserire un valore");
                            return false;
                        }// end of if cycle
                        break;
                    case integer:
                        intValue = (Integer) objValue;
                        if (intValue < 1) {
                            LibAvviso.error("Il valore deve essere maggiore di zero");
                            return false;
                        }// end of if cycle
                        break;
                    case email:
                        stringValue = (String) objValue;
                        if (LibText.isEmpty(stringValue)) {
                            LibAvviso.error("Occorre inserire un indirizzo email");
                            return false;
                        }// end of if cycle
                        if (LibText.isWrongEmail(stringValue)) {
                            LibAvviso.error("Non sembra un indirizzo email valido");
                            return false;
                        }// end of if cycle
                        break;
                    default:
                        log.warn("Switch - caso non definito");
                        break;
                } // end of switch statement
            }// end of if cycle

            entityValida = binder != null && binder.validate().isOk();
        } catch (Exception unErrore) { // intercetta l'errore
            Notification.show("Accetta", "Scheda non valida", Notification.Type.WARNING_MESSAGE);
        }// fine del blocco try-catch

        return entityValida;
    }// end of method


}// end of class

