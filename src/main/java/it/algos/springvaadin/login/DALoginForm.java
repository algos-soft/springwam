package it.algos.springvaadin.login;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.field.ATextField;
import it.algos.springvaadin.field.IAFieldFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by alex on 26/05/16.
 */
@SpringComponent
@Scope("session")
public class DALoginForm extends ALoginForm {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public UserService userService;

    private IAFieldFactory fieldFactory;

    public ATextField nameField;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param fieldFactory
     */
    public DALoginForm(IAFieldFactory fieldFactory) {
        this.fieldFactory = fieldFactory;
    }// end of Spring constructor

    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     * Aggiunge i listener al Login
     */
    @PostConstruct
    private void inizia() {
        getPassField().setWidth("15em");
    }// end of method


    /**
     * Recupera dal dialogo UI, il valore dell'utente selezionato
     * Nel dialogo può esserci un field di tipo testo in cui scrivere il nickName dell'utente
     * Oppure un popup con l'elenco degli utenti abilitati (per la company selezionata, se esiste)
     *
     * @return the selected user
     */
    public IAUser getSelectedUser() {
        String nickname = nameField.getValue();
        IAUser user = userService.findByNickname(nickname);

        return user;
    }// end of method


    /**
     * Create the component to input the username.
     *
     * @return the username component
     */
    public Component createUsernameComponent() {
        nameField = (ATextField) fieldFactory.crea(null, EAFieldType.text, null, null);
        nameField.inizializza("Username", null);
        nameField.setWidth("15em");
        nameField.setCaption("Username");
        nameField.setFocus(true);

        return nameField;
    }// end of method

    public void setUsername(String name) {
        nameField.setValue(name);
    }// end of method


    public String getNickname() {
        String value = "";

        if (nameField != null) {
            value = nameField.getValue();
        }// end of if cycle

        return value;
    }// end of method


    public String getPassword() {
        String value = "";

        if (passField != null) {
            value = passField.getValue();
        }// end of if cycle

        return value;
    }// end of method


}// end of class
