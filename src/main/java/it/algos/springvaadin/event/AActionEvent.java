package it.algos.springvaadin.event;


import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.presenter.IAPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 04/06/17.
 * Eventi generati da una azione (nella Grid, ad esempio)
 * Link: http://www.baeldung.com/spring-events
 */
@Slf4j
@SpringComponent
@Scope("session")
public class AActionEvent extends AEvent {


    //--Obbligatorio specifica del tipo di evento
    private EATypeAction type;


    /**
     */
    public AActionEvent() {
        this(null);
    }// end of constructor


    /**
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AActionEvent(IAPresenter source) {
        this(EATypeAction.click, source);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AActionEvent(EATypeAction type, IAPresenter source) {
        this(type, source, (IAPresenter) null, (AEntity) null);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     */
    public AActionEvent(EATypeAction type, IAPresenter source, IAListener target) {
        this(type, source, source, null);
    }// end of constructor


    /**
     * @param type       Obbligatorio specifica del tipo di evento
     * @param source     Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target     Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AActionEvent(EATypeAction type, IAPresenter source, IAPresenter target, AEntity entityBean) {
        super(type, source, target, entityBean, null);
    }// end of constructor


    public EATypeAction getType() {
        return type;
    }// end of method


}// end of class