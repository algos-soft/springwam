package it.algos.springvaadin.login;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import it.algos.springvaadin.dialog.AConfirmDialog;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.field.ACheckBoxField;
import it.algos.springvaadin.field.ATextField;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.listener.ALoginListener;
import it.algos.springvaadin.service.ACookieService;
import it.algos.springvaadin.service.ALoginService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Abstract Login form.
 */
@SpringComponent
public abstract class ALoginForm extends AConfirmDialog {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    public ALoginService userService;

    @Autowired
    public ACookieService cookieService;

    //--componente specifico per il nickname
    //--la sottoclasse può implementare un textField oppure un comboBox
    protected Component usernameField;

    //--componente fisso per la password
    //    private PasswordField passField; @todo ricambiare
    protected ATextField passField;

    //--componente fisso per il flag booleano
    protected ACheckBoxField rememberField;

    /**
     * Login gestisce il form ed alla chiusura controlla la validità del nuovo utente
     * Lancia il fire di questo evento, se l'utente è valido.
     * Si registra qui il solo listener di Login perché BaseLoginForm e Login sono 1=1
     * Login a sua volta rilancia l'evento per i propri listeners
     * (che si registrano a Login che è singleton nella sessione, mentre BaseLoginForm può essere instanziata diverse volte)
     */
    private ALoginListener loginListener;


    /**
     * Constructor
     */
    public ALoginForm(ALoginService service) {
        super(null);
        this.userService = service;
    }// end of constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
        FormLayout layout = new FormLayout();//@todo controllare la larghezza con AFormLayout
        layout.setWidthUndefined();
        layout.setSpacing(true);

        // crea i campi
        usernameField = createUsernameComponent();

        //        passField = new PasswordField("Password");@todo ricambiare
        passField = new ATextField("Password");
        passField.inizializza("alfa", null);
        passField.setCaption("Password");
        passField.setWidthUndefined();

//        passField = new TextField("Password");
        rememberField = new ACheckBoxField("Ricordami su questo computer");
        rememberField.inizializza("beta", null);
        rememberField.setCaption("Ricordami su questo computer");
        rememberField.setValue(true);

        // aggiunge i campi al layout
        layout.addComponent(usernameField);
        layout.addComponent(passField);
        layout.addComponent(rememberField);

        addComponent(layout);
    }// end of method


    /**
     * Create the component to input the username.
     *
     * @return the username component
     */
    protected Component createUsernameComponent() {
        return null;
    }// end of method


    @Override
    protected void onConfirm() {
        IAUser user = getSelectedUser();

        if (user != null) {
            String password = passField.getValue();

            if (user.validatePassword(password)) {
                super.onConfirm();
                Notification.show("Login valido", Notification.Type.HUMANIZED_MESSAGE);
                utenteLoggato();
                LibVaadin.getUI().getNavigator().navigateTo(ACost.VIEW_LOGTYPE_LIST);
                LibVaadin.getUI().getNavigator().navigateTo(ACost.VIEW_HOME);
            } else {
                passField.textField.setValue("");
                Notification.show("Login fallito", Notification.Type.WARNING_MESSAGE);
            }// end of if/else cycle

            if (rememberField.getValue()) {
                writeCookies(user.getNickname(), password, user.getCompany());
            }// end of if cycle

        }// end of if cycle

    }// end of method


    /**
     * Reads data from the fields and writes the cookies
     */
    public void writeCookies(String nickName, String password, Company company) {
        cookieService.setCookie(getLoginKey(), nickName, 86400);
        cookieService.setCookie(getPasswordKey(), password, 86400);
        cookieService.setCookie(getRememberKey(), "true", 86400);
        cookieService.setCookie(getCompanyKey(), company.getCode(), 86400);

//        LibCookie.setCookie(getLoginKey(), user.getNickname(), getLoginPath(), expiryTime);
//        LibCookie.setCookie(getPasswordKey(), user.getEncryptedPassword(), getLoginPath(), expiryTime);

//        if (rememberField.getValue()) {
//            LibCookie.setCookie(getRememberKey(), "true", getLoginPath(), expiryTime);
//        } else {
//            LibCookie.deleteCookie(getRememberKey());
//        }
        int a = 87;
    }// end of method

    /**
     * Recupera dal dialogo UI, il valore dell'utente selezionato
     * Nel dialogo può esserci un field di tipo testo in cui scrivere il nickName dell'utente
     * Oppure un popup con l'elenco degli utenti abilitati (per la company selezionata, se esiste)
     *
     * @return the selected user
     */
    protected IAUser getSelectedUser() {
        return null;
    }// end of method


    /**
     * Evento generato quando si modifica l'utente loggato <br>
     * <p>
     * Informa (tramite listener) chi è interessato (solo la classe Login, che poi rilancia) <br>
     */
    protected void utenteLoggato() {
        if (loginListener != null) {
            loginListener.onUserLogin(null);
        }// end of if cycle
    }// end of method

    public void setLoginListener(ALoginListener listener) {
        this.loginListener = listener;
    }// end of method

    public Window getWindow() {
        return this;
    }// end of method

    protected void setNickname(String name) {
    }// end of method

    protected void setPassword(String password) {
    }// end of method

    protected String getNickname() {
        return null;
    }// end of method

    protected String getPassword() {
        return null;
    }// end of method

//    public void setPassword(String password) {
//        passField.setValue(password);
//    }

    public void setRemember(boolean remember) {
        rememberField.setValue(remember);
    }


    public Component getUsernameField() {
        return usernameField;
    }

    public ATextField getPassField() {
        return passField;
    }

    public ACheckBoxField getRememberField() {
        return rememberField;
    }


    protected String getLoginKey() {
        String name = "";

//        if (!cookiePrefix.equals("")) {
//            name += cookiePrefix + ".";
//        }

        return name += ACost.COOKIE_NAME_LOGIN;
    }// end of method

    protected String getPasswordKey() {
        String name = "";

//        if (!cookiePrefix.equals("")) {
//            name += cookiePrefix + ".";
//        }

        return name += ACost.COOKIE_NAME_PASSWORD;
    }// end of method

    protected String getRememberKey() {
        String name = "";

//        if (!cookiePrefix.equals("")) {
//            name += cookiePrefix + ".";
//        }

        return name += ACost.COOKIE_NAME_REMEMBER;
    }// end of method

    protected String getCompanyKey() {
        String name = "";

//        if (!cookiePrefix.equals("")) {
//            name += cookiePrefix + ".";
//        }

        return name += ACost.COOKIE_NAME_COMPANY;
    }// end of method

}// end of class

