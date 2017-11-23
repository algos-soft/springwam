package it.algos.springvaadin.dialog;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonFactory;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 22-ago-2017
 * Time: 08:48
 */
@SpringComponent
@SuppressWarnings("serial")
public class Edit2Dialog extends Window implements ApplicationListener {

    private static String CAPTION = "Inserimento di un valore";

    Recipient recipient;

    TextField field = new TextField();

    private AButtonFactory buttonFactory;
    private AButton buttonBack;
    private AButton buttonAccetta;

//    public Edit2Dialog() {
//    }// end of constructor


    public Edit2Dialog(AButtonFactory buttonFactory) {
        super();
        this.buttonFactory = buttonFactory;
    }// end of constructor


    public void inizia(String message, AlgosPresenterImpl source, Recipient recipient) {
        this.recipient = recipient;
        final Window winDialog = this;

        buttonBack = buttonFactory.crea(AButtonType.back, this, this, null);
        buttonAccetta = buttonFactory.crea(AButtonType.accetta, this, this, null);


        field.setValue("");
//        buttonBack.inizializza(null,source);
//        buttonAccetta.inizializza(null,source);
        this.setModal(true);
        this.setResizable(false);
        this.setClosable(false);
        this.setWidth("18em");
        this.setHeight("11em");
        VerticalLayout layout = new VerticalLayout();

        if (message.equals("")) {
            layout.addComponent(new LabelRosso(CAPTION));
        } else {
            layout.addComponent(new LabelRosso(message));
        }// end of if/else cycle

        layout.addComponent(field);
        layout.addComponent(new HorizontalLayout(buttonBack, buttonAccetta));
        field.focus();
        this.center();


        buttonBack.setSource(null);
        buttonBack.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                winDialog.close();
            }// end of inner method
        });// end of anonymous inner class

        buttonAccetta.setEnabled(true);
        buttonAccetta.setSource(null);

//        Collection collezione = buttonAccetta.getListeners(Button.ClickListener.class);
//        if (collezione != null) {
//            for (Object obj : collezione) {
//                if (obj instanceof Button.ClickListener) {
//                    buttonAccetta.removeListener((Listener) obj);
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//        Collection collezione2 = buttonAccetta.getListeners(Button.ClickListener.class);
        buttonAccetta.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                recipient.gotInput(field.getValue(), winDialog);
            }// end of inner method
        });// end of anonymous inner class

        this.setContent(layout);
        LibVaadin.getUI().addWindow(this);
        this.setVisible(true);
    }// end of method


    public interface Recipient {
        public void gotInput(String input, Window win);
    }// end of method

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
    }// end of method

}// end of class

