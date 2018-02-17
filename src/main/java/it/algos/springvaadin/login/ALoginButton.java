package it.algos.springvaadin.login;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import it.algos.springvaadin.event.ALoginEvent;
import it.algos.springvaadin.event.ALogoutEvent;
import it.algos.springvaadin.event.AProfileChangeEvent;
import it.algos.springvaadin.listener.ALoginListener;
import it.algos.springvaadin.listener.ALogoutListener;
import it.algos.springvaadin.listener.AProfileChangeListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.annotation.PostConstruct;

/**
 * Menubar di gestione del login
 * <p>
 * Se si è loggati, mostra il nome dell'utente ed un popup per modificare il profilo-utente e logout <br>
 * Se non si è loggati, mostra un bottone per eseguire il login <br>
 */
@SpringComponent
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ALoginButton extends MenuBar {


    /**
     * La classe di 'business logic' viene iniettata come 'session', dal costruttore @Autowired
     */
    private ALogin login;


    /**
     * Rappresentazione grafica del bottone/comando
     */
    private MenuItem loginItem;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param login business logic
     */
    public ALoginButton(ALogin login) {
        this.login = login;
    }// end of Spring constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     * Aggiunge i listeners al bottone/comando
     * Updates the UI based on the current Login state
     */
    @PostConstruct
    private void inizia() {
        this.addStyleName("blue");

        //--stato iniziale del bottone/comando
        loginItem = this.addItem("", null, null);

        addListeners();
        updateUI();
    }// end of method


    /**
     * Aggiunge i listeners al bottone/comando
     */
    private void addListeners() {
        login.addLoginListener(new ALoginListener() {
            @Override
            public void onUserLogin(ALoginEvent e) {
                updateUI();
            }// end of inner method
        });// end of anonymous inner class


        login.addLogoutListener(new ALogoutListener() {
            @Override
            public void onUserLogout(ALogoutEvent e) {
                updateUI();
            }// end of inner method
        });// end of anonymous inner class


        login.setProfileListener(new AProfileChangeListener() {
            @Override
            public void profileChanged(AProfileChangeEvent e) {
                updateUI();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Updates the UI based on the current Login state
     */
    public void updateUI() {
        IAUser user = login.getUser();

        if (user == null) {
            loginItem.setText("Login");
            loginItem.removeChildren();
            loginItem.setIcon(VaadinIcons.SIGN_IN);
            loginItem.setCommand(new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    loginCommandSelected();
                }// end of inner method
            });// end of anonymous inner class
        } else {
            String username = user.toString();
            username=username.replaceAll("_"," ");
            loginItem.setCommand(null);
            loginItem.setText(username);
            loginItem.setIcon(VaadinIcons.USER);
            loginItem.removeChildren();

            loginItem.addItem("Il mio profilo...", VaadinIcons.USER, new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    myProfileCommandSelected();
                }// end of inner method
            });// end of anonymous inner class

            loginItem.addItem("Logout", VaadinIcons.SIGN_OUT, new Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    logoutCommandSelected();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if/else cycle

    }// end of method


    /**
     * Login button pressed
     */
    protected void loginCommandSelected() {
        login.showLoginForm();
    }// end of method


    /**
     * Logout button pressed
     */
    private void logoutCommandSelected() {
        login.logout();
        updateUI();
    }// end of method


    /**
     * Profilo utente button pressed
     */
    private void myProfileCommandSelected() {
        Notification.show("Profile", "Caming soon...", Notification.Type.HUMANIZED_MESSAGE);

//        login.showUserProfile();
    }// end of method


}// end of class

