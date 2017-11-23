package it.algos.springvaadin.presenter;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.event.*;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

/**
 * Created by gac on 18/06/17.
 * Riceve gli eventi, lanciati da Bottoni ed Azioni, e li gestisce in questa classe
 */
public abstract class AlgosPresenterEvents implements AlgosPresenter {

    @Autowired
    protected ApplicationEventPublisher publisher;


    /**
     * Evento
     * Create button pressed in grid
     * Create a new item and edit it in a form
     */
    @Override
    public void create() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Nuovo", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    /**
     * Evento
     * Apre un dialodo standard di selezioni di files
     * Create a file chooser
     */
    @Override
    public void chooser(AEntity entityBean, Window parentDialog) {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Chooser", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void edit(AEntity entityBean) {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Modifica", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void editLink(ApplicationListener source, AEntity entityBean, AField sourceField, AButtonType type) {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Modifica Linkata", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void editImage(AEntity entityBean, AField sourceField) {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Modifica Immagine", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void delete() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Cancella", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void deleteLink(ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField, AButtonType type) {
    }// end of method

    @Override
    public void search() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Cerca", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void showAll() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Tutto (annulla la selezione)", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void importa() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Import di dati", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void attach() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Azione", "Aggiunta  una riga nella Grid", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void click() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Azione", "Click nella Grid", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void doppioClick(AEntity entityBean) {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Azione", "Doppio click nella Grid", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void selectionChanged() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Azione", "Modificata la selezione della Grid", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void listener() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Azione", "AzioneOld generica della Grid", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void annulla() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Annulla", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void back() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Back", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    @Override
    public void revert() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Revert", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    /**
     * Evento 'save' (registra) button pressed in form
     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
     * Registra le modifiche nel DB, tramite il service
     * Ritorna alla lista
     */
    @Override
    public void registra() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Registra", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method


    protected void registraLink(ApplicationListener source, ApplicationListener target, AField sourceField) {
    }// end of method

    protected void registraLinkBack(ApplicationListener target, AEntity entityBean, AField sourceField) {
    }// end of method

    /**
     * Evento 'accetta' (conferma) button pressed in form
     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
     * NON registra le modifiche nel DB
     * Ritorna alla lista
     */
    @Override
    public void accetta() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("TipoBottone", "Premuto Accetta", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void valoreCambiato() {
        if (AlgosApp.USE_DEBUG) {
            Notification.show("Field", "Modificato valore del campo", Notification.Type.HUMANIZED_MESSAGE);
        }// end of if cycle
    }// end of method

    @Override
    public void fieldModificato(ApplicationListener source, AEntity entityBean, AField sourceField) {
    }// end of method

    /**
     * Metodo invocato dalla view ogni volta che questa diventa attiva
     */
    @Override
    public void enter() {
    }// end of method

    /**
     * Metodo invocato da un daemon per spedire mail
     */
    @Override
    public void daemonMail() {
    }// end of method

    /**
     * Usato da AzioneOld
     * Usato da TipoBottone
     */
    public ApplicationEventPublisher getApplicationEventPublisher() {
        return publisher;
    }// end of method


    /**
     * Handle an application event.
     *
     * @param event to respond to
     */
    @Override
    public void onApplicationEvent(AEvent event) {
        Class thisClazz = this.getClass();
        Class sourceClazz = event.getSource() != null ? event.getSource().getClass() : null;
        Class targetClazz = event.getTarget() != null ? event.getTarget().getClass() : null;
        ApplicationListener source = event.getSource();
        ApplicationListener target = event.getTarget();
        AEntity entityBean = event.getEntityBean();
        AField sourceField = event.getSourceField();

        if (event instanceof AFieldEvent && targetClazz == thisClazz) {
            onFieldEvent((AFieldEvent) event, source, target, entityBean, sourceField);
        }// end of if cycle

        if (event instanceof AButtonEvent && targetClazz == thisClazz) {
            onListEvent((AButtonEvent) event);
        }// end of if cycle

        if (event instanceof AActionEvent && sourceClazz == thisClazz) {
            onGridAction((AActionEvent) event);
        }// end of if cycle

    }// end of method


    /**
     * Handle a field event
     *
     * @param event the event to respond to
     */
    private void onFieldEvent(AFieldEvent event, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
        TypeField type = event.getFieldType();

        switch (type) {
            case valueChanged:
                valoreCambiato();
                break;
            case fieldModificato:
                fieldModificato(source, entityBean, sourceField);
                break;
            default: // caso non definito
                break;
        } // fine del blocco switch
    }// end of method


    /**
     * Handle a button event
     * Vedi enum TipoBottone
     *
     * @param event the event to respond to
     */
    private void onListEvent(AButtonEvent event) {
        AButtonType type = event.getButtonType();
        Class thisClazz = this.getClass();
        Class targetClazz = event.getTarget() != null ? event.getTarget().getClass() : null;
        ApplicationListener source = event.getSource();
        ApplicationListener target = event.getTarget();
        AEntity entityBean = event.getEntityBean();
        AField sourceField = event.getSourceField();

        switch (type) {
            case create:
                create();
                break;
            case chooser:
//                chooser(entityBean, parentDialog);
                break;
            case edit:
                edit(entityBean);
                break;
            case editLinkDBRef:
                editLink(source, entityBean, sourceField, type);
                break;
            case editLinkNoDBRef:
                editLink(source, entityBean, sourceField, type);
                break;
            case linkRegistra:
                registraLink(source, target, sourceField);
                break;
            case linkAccetta:
                registraLinkBack(source, entityBean, sourceField);
                break;
//            case image:
//                editImage(entityBean, parentField);
//                break;
            case delete:
                delete();
                break;
            case deleteLink:
                deleteLink(source, target, entityBean, sourceField, type);
//                publisher.publishEvent(new AFieldEvent(TypeField.fieldModificato, target, source, (AEntity) null, sourceField));
                break;
            case search:
                search();
                break;
            case showAll:
                showAll();
                break;
            case importa:
                importa();
                break;
            case annulla:
                annulla();
                break;
            case back:
                back();
                break;
            case revert:
                revert();
                break;
            case registra:
                registra();
                break;
            case accetta:
                accetta();
                break;
            default: // caso non definito
                break;
        } // fine del blocco switch
    }// end of method


    /**
     * Handle an action event
     * Vedi enum AzioneOld
     *
     * @param event the event to respond to
     */
    private void onGridAction(AActionEvent event) {
        TypeAction tipo = event.getActionType();
        AEntity entityBean = event.getEntityBean();

        switch (tipo) {
            case attach:
                click();
                break;
            case click:
                click();
                break;
            case doppioClick:
                doppioClick(entityBean);
                break;
            case singleSelectionChanged:
            case multiSelectionChanged:
                selectionChanged();
                break;
            case listener:
                listener();
                break;
            case daemonMail:
                daemonMail();
                break;
            default: // caso non definito
                break;
        } // fine del blocco switch
    }// end of method

}// end of class
