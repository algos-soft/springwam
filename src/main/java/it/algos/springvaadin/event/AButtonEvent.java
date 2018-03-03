package it.algos.springvaadin.event;


import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.presenter.IAPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 03/06/17.
 * Eventi generati dai bottoni
 * Link: http://www.baeldung.com/spring-events
 */
@Slf4j
@SpringComponent
@Scope("session")
public class AButtonEvent extends AEvent {


    public AButtonEvent() {
        this(null);
    }// end of constructor


    /**
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
     */
    public AButtonEvent(IAPresenter source) {
        this(EATypeButton.annulla, source, source);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
     * @param target Obbligatorio (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
     */
    public AButtonEvent(EATypeButton type, IAPresenter source, IAPresenter target) {
        this(type, source, target, (AEntity) null);
    }// end of constructor


    /**
     * @param type       Obbligatorio specifica del tipo di evento
     * @param source     Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
     * @param target     Obbligatorio (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AButtonEvent(EATypeButton type, IAPresenter source, IAPresenter target, AEntity entityBean) {
        this(type, source, target, entityBean, (AField) null);
    }// end of constructor


    /**
     * @param type        Obbligatorio specifica del tipo di evento
     * @param source      Obbligatorio (presenter, form, field, window, dialog,... ) che ha generato l'evento
     * @param target      Obbligatorio (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
     * @param entityBean  Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     * @param sourceField Opzionale (field) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AButtonEvent(EATypeButton type, IAPresenter source, IAPresenter target, AEntity entityBean, AField sourceField) {
        super(type, source, target, entityBean, sourceField);
    }// end of constructor


    public EATypeButton getType() {
        return (EATypeButton) type;
    }// end of method


}// end of class