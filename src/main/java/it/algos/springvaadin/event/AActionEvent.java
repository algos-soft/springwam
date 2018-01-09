package it.algos.springvaadin.event;


import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import org.springframework.context.ApplicationListener;

/**
 * Created by gac on 04/06/17.
 * Eventi generati da una azione (nella Grid, ad esempio)
 * Link: http://www.baeldung.com/spring-events
 */
public class AActionEvent extends AEvent {


    //--Obbligatorio specifica del tipo di evento
    private EATypeAction type;


    /**
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AActionEvent(IAListener source) {
        this(EATypeAction.click, source);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AActionEvent(EATypeAction type, IAListener source) {
        this(type, source, (IAListener) null, (AEntity) null);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     */
    public AActionEvent(EATypeAction type, IAListener source, IAListener target) {
        this(type, source, target, null);
    }// end of constructor


    /**
     * @param type       Obbligatorio specifica del tipo di evento
     * @param source     Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target     Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AActionEvent(EATypeAction type, IAListener source, IAListener target, AEntity entityBean) {
        super(type, source, target, entityBean, null);
    }// end of constructor


    public EATypeAction getType() {
        return type;
    }// end of method


}// end of class