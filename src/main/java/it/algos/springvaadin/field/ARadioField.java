package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.entity.preferenza.PrefEffect;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 27-ott-2017
 * Time: 20:40
 */
@SpringComponent
@Scope("prototype")
@Slf4j
@Qualifier(Cost.FIELD_RADIO)
public class ARadioField extends AField {

    private RadioButtonGroup radio;

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        radio = new RadioButtonGroup();
    }// end of method


    @Override
    public void setWidth(String width) {
        radio.setWidth(width);
    }// end of method


    public void fixRadio(Object[] items) {
        try { // prova ad eseguire il codice
            fixRadio(Arrays.asList(items));
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString() + " items");
        }// fine del blocco try-catch
    }// end of method


    public void fixRadio(List items) {
        if (radio != null) {
            radio.setItems(items);
            try { // prova ad eseguire il codice
                radio.setValue((String) items.get(0));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        return radio;
    }// end of method

    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        return radio.getValue();
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        radio.setSelectedItem(value);
    }// end of method

    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (radio != null) {
            radio.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method

}// end of class
