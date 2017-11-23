package it.algos.springvaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import it.algos.springvaadin.lib.LibResource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;

/**
 * Created by gac on 31/05/17.
 * Vista di partenza, se non viene specificato altrimenti
 */
@SpringView(name = DefaultView.VIEW_NAME)
public class DefaultView extends Panel implements View {
    public static final String VIEW_NAME = "";

    /**
     * Metodo invocato DOPO il costruttore e PRIMA del metodo init(VaadinRequest request)
     * Serve per regolare i parametri utilizzati nel metodo init(VaadinRequest request)
     */
    @PostConstruct
    void init() {
        Image img = LibResource.getImage("splash_image.png");
        img.addClickListener(e -> getUI().getNavigator().navigateTo("error"));
        setContent(img);
    }// end of method


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }// end of method

}// end of class
