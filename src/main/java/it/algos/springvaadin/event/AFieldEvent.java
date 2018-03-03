package it.algos.springvaadin.event;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.presenter.IAPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 18:00
 * Eventi generati da un Field (campo) di un Form
 */
@Slf4j
@SpringComponent
@Scope("session")
public class AFieldEvent extends AEvent {


    /**
     * Specifica del tipo di evento (obbligatorio)
     * Valore modificato oppure link verso un target diverso dal field
     */
    private EATypeField type;


    public AFieldEvent() {
        this(null);
    }// end of constructor


    /**
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AFieldEvent(IAPresenter source) {
        this(EATypeField.valueChanged, source);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AFieldEvent(EATypeField type, IAPresenter source) {
        this(type, source, (IAPresenter) null, (AEntity) null);
    }// end of constructor


    /**
     * @param type       Obbligatorio specifica del tipo di evento
     * @param source     Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target     Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AFieldEvent(EATypeField type, IAPresenter source, IAPresenter target, AEntity entityBean) {
        this(type, source, target, entityBean, (AField) null);
    }// end of constructor


    /**
     * @param type        Obbligatorio specifica del tipo di evento
     * @param source      Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target      Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean  Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     * @param sourceField Opzionale (field) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AFieldEvent(EATypeField type, IAPresenter source, IAPresenter target, AEntity entityBean, AField sourceField) {
        super(type, source, target, entityBean, sourceField);
    }// end of constructor


    public EATypeField getType() {
        return type;
    }// end of method


}// end of class
