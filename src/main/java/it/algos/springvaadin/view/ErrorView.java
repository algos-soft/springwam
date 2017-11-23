package it.algos.springvaadin.view;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.lib.LibResource;

import javax.annotation.PostConstruct;

/**
 * Created by gac on 07/06/17
 * A class encapsulating an ErrorScreen in a View
 */
@SpringView(name = ErrorView.VIEW_NAME)
public class ErrorView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "error";


    public ErrorView() {
        super();
    }// end of constructor

    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        setSizeFull();

        Image img = LibResource.getImage("test.png");
        if (img != null) {
            addComponent(img);
        } else {
            Notification.show("Image non trovata", "This is the description", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if/else cycle

    }// end of method


    /**
     * Metodo invocato ogni volta che la view diventa attiva
     * <p>
     * Le classi sono lazy.
     * Il metodo enter(viewChangeEvent) viene chiamato ogni volta che si richiama la view
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String msg = "Questa pagina non esiste: " + event.getViewName();
//        if (errScreen != null) {
//            errScreen.setMessage(msg);
//        }// fine del blocco if
    }// end of method

}// end of inner class
