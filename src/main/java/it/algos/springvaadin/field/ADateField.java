package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateTimeField;
import it.algos.springvaadin.lib.ACost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 23-feb-2018
 * Time: 20:12
 */
@SpringComponent
@Scope("prototype")
@Qualifier(ACost.FIELD_DATE)
public class ADateField extends AField {


    /**
     * Componente principale
     */
    private DateField dateField;


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        dateField = new DateField();
        dateField.setDateFormat("EEE, d-MMM-yyyy");
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
        dateField.setValue((LocalDate)value);
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
            dateField.addValueChangeListener(new ValueChangeListener<LocalDate>() {
                @Override
                public void valueChange(ValueChangeEvent<LocalDate> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method

}// end of class
