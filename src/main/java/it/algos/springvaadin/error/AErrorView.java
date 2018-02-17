package it.algos.springvaadin.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 20:35
 */
@Slf4j
@SpringView(name = "error")
public class AErrorView extends VerticalLayout implements View {


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

        Notification.show("Image non trovata", "This is the description", Notification.Type.HUMANIZED_MESSAGE);
    }// end of method


    /**
     * Metodo invocato (da SpringBoot) ogni volta che si richiama la view dallo SpringNavigator
     * Passa il controllo alla classe xxxPresenter che gestisce la business logic
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.addComponent(new Label("Errore"));
    }// end of method


}// end of class
