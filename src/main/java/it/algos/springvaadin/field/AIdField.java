package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import it.algos.springvaadin.lib.Cost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 30-ago-2017
 * Time: 19:23
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_ID)
public class AIdField extends AField {


    public TextField field = null;


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        field = new TextField();
    }// end of method


    /**
     * Regola i parametri base per la visualizzazione del field nel form
     * Possono essere sovrascritti nella sottoclasse specifica
     * Possono essere successivamente modificati da una @Annotation
     */
    protected void regolaParametri() {
        super.regolaParametri();
        this.setCaption(Cost.FIELD_ID);
        this.setEnabled(false);
        this.setRequiredIndicatorVisible(false);
        this.setVisible(true);
        this.setWidth(STANDARD_MEDIUM_TEXT_WITH);
    }// end of method


    public void setWidth(String width) {
        if (field != null) {
            field.setWidth(width);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        return field;
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public String getValue() {
        if (field != null) {
            return field.getValue();
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (field != null && value != null) {
            field.setValue((String) value);
        }// end of if cycle
    }// end of method


}// end of class


