package it.algos.springvaadin.label;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import it.algos.springvaadin.lib.LibText;

public class LabelRosso extends Label {


    public LabelRosso(VaadinIcons icona) {
        this(icona.getHtml());
    }// end of constructor

    public LabelRosso(String text) {
        super(LibText.setRossoBold(text), ContentMode.HTML);
        this.addStyleName("rosso");
    }// end of constructor

}// end of class
