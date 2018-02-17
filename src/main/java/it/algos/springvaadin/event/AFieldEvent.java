package it.algos.springvaadin.event;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.field.AField;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 18:00
 * Eventi generati da un Field (campo) di un Form
 */
public class AFieldEvent extends AEvent {


    /**
     * Specifica del tipo di evento (obbligatorio)
     * Valore modificato oppure link verso un target diverso dal field
     */
    private EATypeField type;


    /**
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AFieldEvent(IAListener source) {
        this(EATypeField.valueChanged, source);
    }// end of constructor


    /**
     * @param type   Obbligatorio specifica del tipo di evento
     * @param source Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     */
    public AFieldEvent(EATypeField type, IAListener source) {
        this(type, source, (IAListener) null, (AEntity) null);
    }// end of constructor


    /**
     * @param type       Obbligatorio specifica del tipo di evento
     * @param source     Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target     Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AFieldEvent(EATypeField type, IAListener source, IAListener target, AEntity entityBean) {
        this(type, source, target, entityBean, (AField) null);
    }// end of constructor


    /**
     * @param type        Obbligatorio specifica del tipo di evento
     * @param source      Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param target      Opzionale (window, dialog, presenter) a cui indirizzare l'evento
     * @param entityBean  Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     * @param sourceField Opzionale (field) in elaborazione. Ha senso solo per alcuni eventi
     */
    public AFieldEvent(EATypeField type, IAListener source, IAListener target, AEntity entityBean, AField sourceField) {
        super(type, source, target, entityBean, sourceField);
    }// end of constructor


    public EATypeField getType() {
        return type;
    }// end of method


}// end of class
