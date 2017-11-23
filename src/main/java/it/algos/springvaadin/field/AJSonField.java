package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.event.AButtonEvent;
import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 18-ott-2017
 * Time: 07:36
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Scope("prototype")
@Slf4j
@Qualifier(Cost.FIELD_IMAGE)
public class AJSonField extends AField {

    private HorizontalLayout placeholder = new HorizontalLayout();
    private AField jSonField = null;
    private PrefType type;

    @Autowired
    private AFieldFactory fieldFactory;

    /**
     * Regolazioni varie DOPO aver creato l'istanza
     * L'istanza può essere creata da Spring o con clone(), ma necessita comunque di questi due parametri
     */
    @Override
    public void inizializza(String publicFieldName, ApplicationListener source) {
        super.inizializza(publicFieldName, source);
        this.entityBean = entityBean;
        chooseComponent();
    }// end of method


    @Override
    public void setWidth(String width) {
        if (jSonField != null) {
            jSonField.setWidth(width);
            jSonField.setHeight("3em");
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        if (placeholder != null) {
            if (placeholder.getComponentCount() > 0) {
                try { // prova ad eseguire il codice
                    placeholder.removeAllComponents();
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                }// fine del blocco try-catch
            }// end of if/else cycle
            if (jSonField != null) {
                placeholder.addComponent(jSonField);
                return placeholder;
            } else {
                LibAvviso.error("Manca il jsonfield");
                return new LabelRosso("Errore");
            }// end of if/else cycle
        } else {
            LibAvviso.error("Manca il placeholder");
            return new LabelRosso("Errore");
        }// end of if/else cycle
    }// end of method


    private void chooseComponent() {
        AFieldType fieldType = type.getFieldType();
        jSonField = fieldFactory.crea(null, fieldType, source, "value", null);

        placeholder.removeAllComponents();
        placeholder.addComponent(jSonField);
        this.addListener();
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public byte[] getValue() {
        Object value;

        if (jSonField != null) {
            value = jSonField.getValue();
            return type.objectToBytes(value);
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
        Object valore = type.bytesToObject((byte[]) value);
        jSonField.setValue(valore);
    }// end of method


    public void setType(PrefType type) {
        this.type = type;
    }// end of method


    public void changeType(AField sourceField) {
        AComboField comboField;
        String typeName;
        if (sourceField instanceof AComboField) {
            comboField = (AComboField) sourceField;
        } else {
            return;
        }// end of if/else cycle

        if (comboField != null && comboField.name.equals("type")) {
            typeName = (String) comboField.getValue();
            type = PrefType.valueOf(typeName);
            chooseComponent();
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
        if (jSonField != null) {
            jSonField.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class

            if (jSonField instanceof ACheckBoxField) {
                ACheckBoxField checkBoxField=(ACheckBoxField)jSonField;
                CheckBox checkBox=checkBoxField.getCheckBox();
                checkBox.addValueChangeListener(new HasValue.ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent valueChangeEvent) {
                        publish();
                    }// end of inner method
                });// end of anonymous inner class
            }// end of if cycle

        }// end of if cycle
    }// end of method

    public void refreshFromComboField(PrefType type) {
        setType(type);
        chooseComponent();
    }// end of method

}// end of class
