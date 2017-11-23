package it.algos.springvaadin.field;

import com.vaadin.data.HasValue;
import com.vaadin.event.MouseEvents;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import it.algos.springvaadin.lib.Cost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 17-set-2017
 * Time: 11:36
 */
@Slf4j
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_CHEK_BOX)
public class ACheckBoxField extends AField<Boolean> {

    private CheckBox checkBox = null;

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        checkBox = new CheckBox();
    }// end of method


    @Override
    public Component initContent() {
        return checkBox;
    }// end of method


    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }// end of method

    @Override
    public void doSetValue(Object value) {
        if (value instanceof Boolean) {
            checkBox.setValue((Boolean) value);
        }// end of if cycle
    }// end of method

    /**
     * Aggiunge il listener al field
     */
    @Override
    protected void addListener() {

        if (checkBox != null) {
            checkBox.addValueChangeListener(new HasValue.ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent valueChangeEvent) {
                    publish();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

    }// end of method

    public CheckBox getCheckBox() {
        return checkBox;
    }// end of method

}// end of class

