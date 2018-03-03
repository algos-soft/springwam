package it.algos.springvaadin.event;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.ATextService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 18:00
 * Eventi specifici del Framework
 * Link: http://www.baeldung.com/spring-events
 */
public abstract class AEvent extends ApplicationEvent {


    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected ATextService text;


    //--Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
    //--ApplicationListener source -> gestito dalla superclasse


    //--Obbligatorio specifica del tipo di evento
    protected Object type;


    //--Opzionale (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento successivo
    // (dopo l'azione prevista in questo evento)
    protected IAPresenter target;


    //--Opzionale nome della classe (di tipo IAListener) a cui indirizzare l'evento
    //--Serve per bypasasare i proxy
    protected String targetSimpleClassName;


    //--Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
    protected AEntity entityBean;


    //--Opzionale (field) in elaborazione. Ha senso solo per alcuni eventi
    protected AField sourceField;


    /**
     * @param type        Obbligatorio specifica del tipo di evento
     * @param source      Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
     * @param target      Obbligatorio (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
     * @param entityBean  Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     * @param sourceField Opzionale (field) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AEvent(Object type, IAPresenter source, IAPresenter target, AEntity entityBean, AField sourceField) {
        super(source);
        this.type = type;
        this.target = target;
        this.entityBean = entityBean;
        this.sourceField = sourceField;
    }// end of constructor


    public Object getType() {
        return type;
    }// end of method


    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @Override
    public IAPresenter getSource() {
        return (IAPresenter) super.getSource();
    }// end of method


    public String getSourceClassName() {
        String sourceClassName = source.getClass().getCanonicalName();

        if (AopUtils.isAopProxy(source)) {
            sourceClassName = source.toString();
//            sourceClassName = text.levaCodaDa(sourceClassName, "@");
            sourceClassName = sourceClassName.substring(0, sourceClassName.indexOf("@"));
        }// end of if cycle

        return sourceClassName;
    }// end of method


    public IAPresenter getTarget() {
        return target;
    }// end of method


    public EATypeButton getButtonType() {
        if (type != null && type instanceof EATypeButton) {
            return (EATypeButton) type;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public EATypeAction getActionType() {
        if (type != null && type instanceof EATypeAction) {
            return (EATypeAction) type;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public EATypeField getFieldType() {
        if (type != null && type instanceof EATypeField) {
            return (EATypeField) type;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public AEntity getEntityBean() {
        return entityBean;
    }// end of method

    public AField getSourceField() {
        return sourceField;
    }// end of method

    public String getTargetSimpleClassName() {
        return targetSimpleClassName;
    }

    public void setTargetSimpleClassName(String targetSimpleClassName) {
        this.targetSimpleClassName = targetSimpleClassName;
    }
}// end of abstract class