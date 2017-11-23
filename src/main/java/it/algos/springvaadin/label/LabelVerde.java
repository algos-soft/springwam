package it.algos.springvaadin.label;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import it.algos.springvaadin.lib.LibText;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 18-nov-2017
 * Time: 17:56
 */
@Slf4j
public class LabelVerde extends Label {


    public LabelVerde(VaadinIcons icona) {
        this(icona.getHtml());
    }// end of constructor

    public LabelVerde(String text) {
        super(LibText.setVerdeBold(text), ContentMode.HTML);
        this.addStyleName("verde");
    }// end of constructor

}// end of class
