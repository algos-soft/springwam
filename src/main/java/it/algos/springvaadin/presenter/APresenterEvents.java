package it.algos.springvaadin.presenter;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.event.AButtonEvent;
import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.field.AField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

/**
 * Created by gac on 18/06/17.
 * Riceve gli eventi, lanciati da Bottoni ed Azioni, e li gestisce in questa classe
 */
@Slf4j
public abstract class APresenterEvents implements IAPresenter {

    @Autowired
    protected ApplicationEventPublisher publisher;


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

        if (event instanceof AActionEvent && targetClazz == thisClazz) {
            onGridAction(event.getActionType(), entityBean);
        }// end of if cycle

    }// end of method


    /**
     * Handle a field event
     *
     * @param event the event to respond to
     */
    private void onFieldEvent(AFieldEvent event, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
        EATypeField type = event.getFieldType();

        switch (type) {
            case valueChanged:
                valueChanged();
                break;
            case fieldModificato:
//                fieldModificato(source, entityBean, sourceField);
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
        EAButtonType type = event.getButtonType();
        Class thisClazz = this.getClass();
        Class targetClazz = event.getTarget() != null ? event.getTarget().getClass() : null;
        ApplicationListener source = event.getSource();
        ApplicationListener target = event.getTarget();
        AEntity entityBean = event.getEntityBean();
        AField sourceField = event.getSourceField();

        switch (type) {
            case create:
                this.fireForm(null);
                break;
//            case chooser:
////                chooser(entityBean, parentDialog);
//                break;
            case edit:
                this.fireForm(entityBean);
//                this.editBean(entityBean);
                break;
//            case editLinkDBRef:
//                editLink(source, entityBean, sourceField, type);
//                break;
//            case editLinkNoDBRef:
//                editLink(source, entityBean, sourceField, type);
//                break;
//            case linkRegistra:
//                registraLink(source, target, sourceField);
//                break;
//            case linkAccetta:
//                registraLinkBack(source, entityBean, sourceField);
//                break;
////            case image:
////                editImage(entityBean, parentField);
////                break;
            case delete:
                delete(entityBean);
                break;
//            case deleteLink:
//                deleteLink(source, target, entityBean, sourceField, type);
//                break;
//            case search:
//                search();
//                break;
//            case showAll:
//                showAll();
//                break;
//            case importa:
//                importa();
//                break;
            case annulla:
                this.fireList();
                break;
//            case back:
//                back();
//                break;
            case revert:
                revert();
                break;
            case registra:
                registra();
                break;
//            case accetta:
//                accetta();
//                break;
            default: // caso non definito
                log.warn("Switch - caso '" + type.name() + "' non definito in APresenterEvents.onListEvent()");
                break;
        } // fine del blocco switch
    }// end of method


    /**
     * Handle an action event
     * Vedi enum EATypeAction
     *
     * @param type       Obbligatorio specifica del tipo di evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    protected void onGridAction(EATypeAction type, AEntity entityBean) {
        //@todo RIMETTERE
        switch (type) {
            case attach:
//                click();
                break;
//            case click:
//                click();
//                break;
            case doppioClick:
                this.fireForm(entityBean);
                break;
            case singleSelectionChanged:
            case multiSelectionChanged:
                selectionChanged();
                break;
//            case listener:
//                listener();
//                break;
//            case daemonMail:
//                daemonMail();
//                break;
            default: // caso non definito
                log.warn("Switch - caso '" + type.name() + "' non definito in APresenterEvents.onGridAction()");
                break;
        } // fine del blocco switch
    }// end of method

}// end of class
