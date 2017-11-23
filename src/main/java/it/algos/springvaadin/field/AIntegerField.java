package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 29-ago-2017
 * Time: 07:46
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_INTEGER)
public class AIntegerField extends AField {


    /**
     * Regola i parametri base per la visualizzazione del field nel form
     * Possono essere sovrascritti nella sottoclasse specifica
     * Possono essere successivamente modificati da una @Annotation
     */
    protected void regolaParametri() {
        super.regolaParametri();
        this.setWidth(STANDARD_INT_WITH);
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (textField != null && value instanceof Integer) {
            textField.setValue(Integer.toString((int) value));
        }// end of if cycle
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Integer getValue() {
        String textValue = "";

        if (textField != null) {
            textValue = textField.getValue();
            if (textValue != null && textValue.length() > 0) {
                if (LibText.isNumber(textValue)) {
                    return Integer.decode(textValue);
                } else {
                    return null;
                }// end of if/else cycle
            } else {
                return null;
            }// end of if/else cycle
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


}// end of class


