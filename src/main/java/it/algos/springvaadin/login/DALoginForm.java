package it.algos.springvaadin.login;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.field.ATextField;
import it.algos.springvaadin.field.IAFieldFactory;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.ALoginService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by alex on 26/05/16.
 */
@SpringComponent
@Scope("prototype")
public class DALoginForm extends ALoginForm {


    private IAFieldFactory fieldFactory;

    public ATextField nameField;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Use @Lazy to avoid the Circular Dependency
     * A simple way to break the cycle is saying Spring to initialize one of the beans lazily.
     * That is: instead of fully initializing the bean, it will create a proxy to inject it into the other bean.
     * The injected bean will only be fully created when it’s first needed.
     *
     * @param service      iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     * @param fieldFactory
     */
    public DALoginForm(@Qualifier(ACost.TAG_USE) ALoginService service, IAFieldFactory fieldFactory) {
        super(service);
        this.fieldFactory = fieldFactory;
    }// end of Spring constructor

//    /**
//     * Costruttore @Autowired
//     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
//     *
//     * @param fieldFactory
//     */
//    public DALoginForm(IAFieldFactory fieldFactory) {
//        this.fieldFactory = fieldFactory;
//    }// end of Spring constructor


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

    @Override
    public void setNickname(String name) {
        if (nameField != null) {
            nameField.setValue(name);
        }// end of if cycle
    }// end of method


    @Override
    public String getNickname() {
        String value = "";

        if (nameField != null) {
            value = nameField.getValue();
        }// end of if cycle

        return value;
    }// end of method


    @Override
    public void setPassword(String name) {
        if (passField != null) {
            passField.setValue(name);
        }// end of if cycle
    }// end of method


    @Override
    public String getPassword() {
        String value = "";

        if (passField != null) {
            value = passField.getValue();
        }// end of if cycle

        return value;
    }// end of method


}// end of class
