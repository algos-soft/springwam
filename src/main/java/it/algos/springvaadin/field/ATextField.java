package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 27-ago-2017
 * Time: 17:36
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Configurable("ATextField")
@Scope("prototype")
@Qualifier(ACost.FIELD_TEXT)
public class ATextField extends AField {


    /**
     * Costruttore @Autowired
     */
    public ATextField() {
    }// end of Spring constructor


    /**
     * Constructor
     */
    public ATextField(String value) {
        super();
        if (textField != null) {
            textField.setValue(value);
        }// end of if cycle
    }// end of constructor


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public String getValue() {
        if (textField != null) {
            return textField.getValue();
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


}// end of class

