package it.algos.springvaadin.event;

import it.algos.springvaadin.enumeration.EALoginTypes;
import it.algos.springvaadin.login.IAUser;
import org.springframework.context.ApplicationEvent;

/**
 * Login event object
 */
public class ALoginEvent extends ApplicationEvent {


    /**
     * Specifica del tipo di evento (obbligatorio)
     */
    private EALoginTypes type;

    private boolean success;
    private IAUser user;
    boolean rememberOption;


    /**
     * @param source         Obbligatorio (presenter, form, field, window, dialog,... ) che che ha generato l'evento
     * @param success        Obbligatorio
     * @param user           Obbligatorio
     * @param type           Obbligatorio specifica del tipo di evento
     * @param rememberOption Opzionale
     */
    public ALoginEvent(Object source, boolean success, IAUser user, EALoginTypes type, boolean rememberOption) {
        super(source);
        this.success = success;
        this.user = user;
        this.type = type;
        this.rememberOption = rememberOption;
    }// end of constructor


    public boolean isSuccess() {
        return success;
    }// end of method

    public IAUser getUser() {
        return user;
    }// end of method

    public EALoginTypes getType() {
        return type;
    }// end of method

    public boolean isRememberOption() {
        return rememberOption;
    }// end of method

}// end of class
