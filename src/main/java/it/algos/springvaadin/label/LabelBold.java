package it.algos.springvaadin.label;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import it.algos.springvaadin.lib.LibText;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 08:31
 */
public class LabelBold extends Label {

    public LabelBold() {
        super("", ContentMode.HTML);
    }// end of constructor

    public LabelBold(String text) {
        super(LibText.setBold(text), ContentMode.HTML);
    }// end of constructor

    @Override
    public void setValue(String value) {
        super.setValue(LibText.setBold(value));
    }// end of method

}// end of class

