package it.algos.springvaadin.dialog;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.bottone.*;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.event.*;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 20-ago-2017
 * Time: 12:02
 */
@SpringComponent
@Qualifier(Cost.VIEW_IMAGE)
public class ImageDialog extends Window implements ApplicationListener {

    private final static String WIDTH = "20em";
    private final static String HEIGHT = "10em";

    /**
     * Property iniettata nel costruttore usato da Spring PRIMA della chiamata del browser
     */
    protected ApplicationEventPublisher applicationEventPublisher;

    private ApplicationListener presenter;
    private AButtonFactory buttonFactory;

    private Label labelLayout = new LabelRosso("Gestione delle immagini");
    private VerticalLayout imageLayout = new VerticalLayout();
    private VerticalLayout buttonsLayout = new VerticalLayout();

    private AEntity entityBean;
    private AField sourceField;
    private byte[] imgBytes;

    private String space7 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

    private AButton buttonBack;
    private AButton buttonCreate;
    private AButton buttonDelete;
    private AButton buttonAccetta;
    private boolean accettaEnabled;

    private Edit2Dialog editDialog;
    private Image image;

    public ImageDialog(
            ApplicationEventPublisher applicationEventPublisher,
            Edit2Dialog editDialog,
            AButtonFactory buttonFactory) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.editDialog = editDialog;
        this.buttonFactory = buttonFactory;
    }// end of constructor

    /**
     * Metodo invocato (automaticamente dalla annotation Spring) DOPO il costruttore
     */
    @PostConstruct
    public void inizia() {
        setModal(true);
        setClosable(true);
        setResizable(false);
        center();

        Layout mainLayout = new VerticalLayout(labelLayout, imageLayout, getSpace(), buttonsLayout);
        setContent(mainLayout);
    }// end of method


    public void show(AEntity entityBean, ApplicationListener presenter, AField sourceField) {
        this.entityBean = entityBean;
        this.sourceField = sourceField;
        this.presenter = presenter;
        this.accettaEnabled = false;
        getImageFromField();
        restart();
        resetDialog();
        UI.getCurrent().addWindow(this);
    }// end of method


    private void restart() {

        buttonBack = buttonFactory.crea(AButtonType.back, this, presenter, sourceField);
        buttonCreate = buttonFactory.crea(AButtonType.create, this, editDialog, sourceField);
        buttonDelete = buttonFactory.crea(AButtonType.delete, this, this, sourceField);
        buttonAccetta = buttonFactory.crea(AButtonType.linkAccetta, this, presenter, sourceField);

        buttonsLayout.removeAllComponents();
        buttonsLayout.addComponent(new HorizontalLayout(buttonBack, getSpace(), buttonDelete));
        buttonsLayout.addComponent(new HorizontalLayout(buttonCreate, getSpace(), buttonAccetta));

        imageLayout.setWidth(WIDTH);
        imageLayout.setHeight(HEIGHT);
    }// end of method


    private Component getSpace() {
        return new LabelRosso(space7);
    }// end of method

    private void resetDialog() {
        imageLayout.removeAllComponents();
        if (image != null) {
            imageLayout.addComponent(image);
        }// end of if cycle

        buttonDelete.setEnabled(image != null);
        buttonAccetta.setEnabled(accettaEnabled);
    }// end of method


    private void getImageFromField() {
        imgBytes = ((AImageField) sourceField).getImgByte();
        image = LibResource.getImage(imgBytes);

        if (image != null) {
            image.setWidth(WIDTH);
            image.setHeight(HEIGHT);
        } else {
            LibAvviso.warn("Non esiste una immagine col nome selezionato");
        }// end of if/else cycle
    }// end of method




    private void create() {
        String message = "Bandiera dello stato</br>Sigla di 3 caratteri";
        this.editDialog.inizia(message, null, new Pippo());
    }// end of method


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AEvent) {
            onEvent((AEvent) event);
        }// end of if cycle
    }// end of method


    /**
     * Handle an application event.
     *
     * @param algosEvent the event to respond to
     */
    private void onEvent(AEvent algosEvent) {
        Class thisClazz = this.getClass();
        Class sourceClazz = algosEvent.getSource() != null ? algosEvent.getSource().getClass() : null;
        Class targetClazz = algosEvent.getTarget() != null ? algosEvent.getTarget().getClass() : null;
        AField sourceField = algosEvent.getSourceField();
        AButtonEvent eventButton = null;
        AFieldEvent eventField = null;
        AButtonType type = null;

        if (algosEvent instanceof AFieldEvent) {
            eventField = (AFieldEvent) algosEvent;
            if (eventField.getType() == TypeField.valueChanged) {
            }// end of if cycle
            if (eventField.getType() == TypeField.linkTarget && targetClazz == thisClazz) {
                this.show(eventField.getEntityBean(), eventField.getSource(), sourceField);
            }// end of if cycle
        }// end of if cycle


        if (algosEvent instanceof AButtonEvent) {
            eventButton = (AButtonEvent) algosEvent;
            type = eventButton.getType();

            if (targetClazz != null && targetClazz == thisClazz && type == AButtonType.image) {
                this.show(eventButton.getEntityBean(), eventButton.getSource(), sourceField);
            }// end of if cycle


            if (sourceClazz != null && sourceClazz == thisClazz) {
                switch (type) {
                    case back:
                        this.close();
                        break;
                    case create:
                        this.create();
                        accettaEnabled = true;
                        break;
                    case delete:
                        imgBytes = new byte[0];
                        image = null;
                        accettaEnabled = true;
                        resetDialog();
                        break;
                    case linkAccetta:
                        ((AImageField) sourceField).setImgByte(imgBytes);
                        sourceField.doSetValue(image);
                        this.close();
                        fireBack();
                        LibAvviso.info("Occorre registrare la scheda per rendere definitive le modifiche effettuate");
                        break;
                    default: // caso non definito
                        break;
                } // fine del blocco switch
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     * L'evento viene intercettato nella classe AlgosPresenterEvents->onApplicationEvent(AEvent event)
     */
    private void fireBack() {
        applicationEventPublisher.publishEvent(new AFieldEvent(TypeField.valueChanged, this, presenter, entityBean, sourceField));
    }// end of method


    public class Pippo implements Edit2Dialog.Recipient {
        @Override
        public void gotInput(String input, Window win) {
            imgBytes = LibResource.getImgBytes(input.toUpperCase() + ".png");
            image = LibResource.getImage(imgBytes);
            if (image != null) {
                image.setWidth(WIDTH);
                image.setHeight(HEIGHT);
            } else {
                LibAvviso.warn("Non esiste una immagine col nome selezionato");
            }// end of if/else cycle
            resetDialog();
            win.close();
        }// end of method
    }// end of inner class

}// end of class

