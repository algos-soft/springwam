package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

/**
 * Created by gac on 24/06/17
 * .
 */
@SpringComponent
@Scope("prototype")
@Qualifier(ACost.FIELD_DATE_TIME)
public class ADateTimeField extends AField {


    /**
     * Componente principale
     */
    private DateTimeField dateField;


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        dateField = new DateTimeField();
        dateField.setDateFormat("EEE, d-MMM-yyyy HH:mm");
    }// end of method


    public void setWidth(String width) {
        if (dateField != null) {
            dateField.setWidth(width);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        return dateField;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        dateField.setValue((LocalDateTime)value);
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        return dateField.getValue();
    }// end of method


    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (dateField != null) {
            dateField.addValueChangeListener(new ValueChangeListener<LocalDateTime>() {
                @Override
                public void valueChange(ValueChangeEvent<LocalDateTime> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method

}// end of class
