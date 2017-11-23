package it.algos.springvaadin.event;

import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.entity.AEntity;
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


    //--Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
    //--ApplicationListener source -> gestito dalla superclasse


    //--Obbligatorio specifica del tipo di evento
    protected Object type;


    //--Obbligatorio (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
    protected ApplicationListener target;


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
    public AEvent(Object type, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
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
    public ApplicationListener getSource() {
        return (ApplicationListener) super.getSource();
    }// end of method


    public ApplicationListener getTarget() {
        return target;
    }// end of method


    public AButtonType getButtonType() {
        if (type != null && type instanceof AButtonType) {
            return (AButtonType) type;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public TypeAction getActionType() {
        if (type != null && type instanceof TypeAction) {
            return (TypeAction) type;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    public TypeField getFieldType() {
        if (type != null && type instanceof TypeField) {
            return (TypeField) type;
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

}// end of abstract class