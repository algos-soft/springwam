package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.event.TypeField;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 31-ago-2017
 * Time: 22:42
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Slf4j
@Scope("prototype")
@Qualifier(Cost.FIELD_COMBO)
public class AComboField extends AField {

    protected ComboBox combo = null;
    protected List items;

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        combo = new ComboBox();
    }// end of method


    public void fixCombo(List items, boolean nullSelectionAllowed, boolean newItemsAllowed) {
        if (combo != null) {
            combo.setItems(items);
            this.items=items;
            combo.setEmptySelectionAllowed(nullSelectionAllowed);

            // Allow adding new items and add
            // handling for new items
            if (newItemsAllowed) {
                creaHandler();
            }// end of if cycle

            try { // prova ad eseguire il codice
                combo.setValue(items.get(0));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle
    }// end of method


    /**
     * Crea un (eventuale) handler per l'inserimento di nuovi items
     * Sovrascritto dalla sottoclasse
     */
    protected void creaHandler() {
    }// end of method


    public void setWidth(String width) {
        if (combo != null) {
            combo.setWidth(width);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        return combo;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (combo != null) {
            combo.setValue(value);
        }// end of if cycle
    }// end of method

    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        if (combo != null) {
            return combo.getValue();
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (combo != null) {
            combo.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method


    /**
     * Fire event
     * source     Obbligatorio questo field
     * target     Obbligatorio (window, dialog, presenter) a cui indirizzare l'evento
     * entityBean Opzionale (entityBean) in elaborazione
     */
    public void publish() {
        if (source != null) {
            publisher.publishEvent(new AFieldEvent(TypeField.valueChanged, source, target, entityBean, this));
            publisher.publishEvent(new AFieldEvent(TypeField.fieldModificato, source, target, entityBean, this));
        }// end of if cycle
    }// end of method

}// end of class

