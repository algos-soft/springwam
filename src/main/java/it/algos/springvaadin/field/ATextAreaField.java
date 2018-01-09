package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 18-ott-2017
 * Time: 09:00
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Configurable("ATextField")
@Scope("prototype")
@Qualifier(ACost.FIELD_TEXT_AREA)
public class ATextAreaField extends ATextField {


    @Autowired
    public ATextService text;

    private final static int STANDARD_ROWS = 3;

    /**
     * Componente principale
     */
    public TextArea textField;

    /**
     * Regola i parametri base per la visualizzazione del field nel form
     * Possono essere sovrascritti nella sottoclasse specifica
     * Possono essere successivamente modificati da una @Annotation
     */
    protected void regolaParametri() {
        super.regolaParametri();
        textField.setRows(STANDARD_ROWS);
        this.setWidth(STANDARD_LONG_TEXT_WITH);
    }// end of method

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        textField = new TextArea();
    }// end of method


    @Override
    public Component initContent() {
        return textField;
    }// end of method

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


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (textField != null && text.isValid(value)) {
            textField.setValue((String) value);
        }// end of if cycle
    }// end of method


    public void setWidth(String width) {
        if (textField != null) {
            textField.setWidth(width);
        }// end of if cycle
    }// end of method


    public void setRows(int rows) {
        if (textField != null) {
            textField.setRows(rows);
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (textField != null) {
            textField.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method

}// end of class

