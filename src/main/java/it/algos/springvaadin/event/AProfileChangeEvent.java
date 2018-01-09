package it.algos.springvaadin.event;

import it.algos.springvaadin.login.IAUser;

import java.util.EventObject;

/**
 * User profile change event object
 */
public class AProfileChangeEvent extends EventObject {


    private IAUser user;

    /**
     * @param user Obbligatorio
     */
    public AProfileChangeEvent(Object source, IAUser user) {
        super(source);
        this.user = user;
    }// end of constructor


    public IAUser getUser() {
        return user;
    }// end of method

}// end of class
