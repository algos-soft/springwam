package it.algos.springvaadin.event;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.login.IAUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.util.EventObject;

/**
 * Logout event object
 */
@Slf4j
@SpringComponent
@Scope("session")
public class ALogoutEvent extends EventObject {


    private IAUser user;

    /**
     * @param user Obbligatorio
     */
    public ALogoutEvent(Object source, IAUser user) {
        super(source);
        this.user = user;
    }// end of constructor


    public IAUser getUser() {
        return user;
    }// end of method

}// end of class
