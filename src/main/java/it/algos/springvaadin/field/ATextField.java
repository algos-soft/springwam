package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

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
@Qualifier(Cost.FIELD_TEXT)
public class ATextField extends AField {


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

