package it.algos.springvaadin.login;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.event.ALoginEvent;
import it.algos.springvaadin.footer.AFooter;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.listener.ALoginListener;
import it.algos.springvaadin.listener.ALogoutListener;
import it.algos.springvaadin.listener.AProfileChangeListener;
import it.algos.springvaadin.service.ACookieService;
import it.algos.springvaadin.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

/**
 * Main Login object (Login business logic).
 * An instance of this object is created and stored in the current session when getLogin() in invoked.
 * Subsequent calls to getLogin() return the same object from the session.
 */
@SpringComponent
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ALogin {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public UserService userService;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ACookieService cookieService;


    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    @Lazy
    public AFooter footer;


    // defaults
    private static final int DEFAULT_EXPIRY_TIME_SEC = 604800;    // 1 week
    private static final boolean DEFAULT_RENEW_COOKIES_ON_LOGIN = true;    // renews the cookies on login

    // default cookie names
    public static final String COOKIENAME_LOGIN = "login_username";
    public static final String COOKIENAME_PASSWORD = "login_password";
    public static final String COOKIENAME_REMEMBER = "login_remember";

    /**
     * Evento lanciato quando viene tentato un login.
     * I listeners ricevono un LoginEvent con i dettagli sull'evento.
     */
    private ArrayList<ALoginListener> loginListeners = new ArrayList<>();

    // listeners notificati al logout
    private ArrayList<ALogoutListener> logoutListeners = new ArrayList<>();

    // listeners notificati quando si modifica il profilo utente
    private ArrayList<AProfileChangeListener> profileListeners = new ArrayList<>();

    private IAUser user;

    private String cookiePrefix = "";
    private int expiryTime = DEFAULT_EXPIRY_TIME_SEC;
    private boolean renewCookiesOnLogin = DEFAULT_RENEW_COOKIES_ON_LOGIN;

    /**
     * Il Form di dialogo viene iniettato come 'session', dal costruttore @Autowired
     */
    private DALoginForm loginForm;

//    private AbsUserProfileForm profileForm;

    private EARoleType typeLogged;

    private Company company;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param loginForm
     */
    public ALogin(DALoginForm loginForm) {
        this.loginForm = loginForm;
        //        profileForm = new DefaultUserProfileForm(); @todo rimettere
    }// end of Spring constructor


//    /**
//     * Retrieve the User Profile form
//     *
//     * @return the User Profile form to show
//     */
//    public AbsUserProfileForm getUserProfileForm() {
//        return profileForm;
//    }


    /**
     * Displays the Login form
     */
    public void showLoginForm() {
        // set the login listener in the form
        loginForm.setLoginListener(new ALoginListener() {
            @Override
            public void onUserLogin(ALoginEvent e) {
                attemptLogin();
            }// end of inner method
        });// end of anonymous inner class

        // retrieve login data from the cookies
//        readCookies(); @todo rimettere

        loginForm.nameField.textField.setValue("");
        loginForm.passField.textField.setValue("");

        // Open it in the UI
        UI.getCurrent().addWindow(loginForm);
    }// end of method


//    /**
//     * Displays the user profile
//     */
//    public void showUserProfile() {
//
//        AbsUserProfileForm profileForm = getUserProfileForm();
//        profileForm.setUser(Login.getLogin().getUser());
//
//        // sets the confirm listener in the form
//        profileForm.setConfirmListener(new ConfirmDialog.ConfirmListener() {
//            @Override
//            public void confirmed(ConfirmDialog dialog) {
//                ProfileChangeEvent e = new ProfileChangeEvent(Login.this, user);
//                for (ProfileChangeListener listener : profileListeners) {
//                    listener.profileChanged(e);
//                }
//            }
//        });
//
//        Window window = profileForm.getWindow();
//        window.center();
//        UI.getCurrent().addWindow(window);
//
//    }


    /**
     * Tenta il login con i dati presenti nella UI.
     * Se riesce, notifica i LoginListener(s)
     *
     * @return true se riuscito
     */
    public boolean attemptLogin() {
        boolean riuscito = false;
        boolean valido = false;
        boolean remember = false;

        String nickname = loginForm.getNickname();
        String password = loginForm.getPassword();
        valido = userService.passwordValida(nickname, password);

        if (valido) {
            Notification.show("Login", "Valido", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show("Login", "Non valido", com.vaadin.ui.Notification.Type.WARNING_MESSAGE);
        }// end of if/else cycle


        this.user = loginForm.getSelectedUser();
        this.typeLogged = EARoleType.getType(((User) user).role.getCode());
        if (this.user != null) {
            this.setCompany(((User) this.user).getCompany());
        }// end of if cycle
        footer.start();

        //        if (user != null) {
//            String pass = loginForm.getPassField().getValue();
//            if (pass != null && !pass.isEmpty()) {
//                if (user.validatePassword(pass)) {
//
//                    setUser(user);
//                    remember = loginForm.getRememberField().getValue();
//
//                    // create/update the cookies
//                    if (remember) {
////                        writeCookies();@todo rimettere
//                    } else {
////                        deleteCookies();@todo rimettere
//                    }// end of if/else cycle
//
//                    riuscito = true;
//                }// end of if cycle
//            }// end of if cycle
//        }// end of if cycle
//

        ALoginEvent evento = new ALoginEvent(this, riuscito, user, null, remember);
        fireLoginListeners(evento);

        return riuscito;
    }// end of method


    /**
     * Esegue il login con i dati presenti nella UI.
     *
     * @return true se riuscito
     */
    public boolean esegueLogin(String nickname, String password) {
        boolean valido = userService.passwordValida(nickname, password);
        IAUser user = userService.findByNick(nickname);
        this.user=user;
        this.typeLogged = EARoleType.getType(((User) user).role.getCode());

        if (this.user != null) {
            this.setCompany(((User) this.user).getCompany());
        }// end of if cycle

        footer.start();

        ALoginEvent evento = new ALoginEvent(this, valido, user, null, false);
        fireLoginListeners(evento);

        return valido;
    }// end of method


    /**
     * Logout the current user
     */
    public void logout() {
//        UserIF oldUser = this.user;
        this.user = null;
//        LogoutEvent e = new LogoutEvent(this, oldUser);
//        for (LogoutListener l : logoutListeners) {
//            l.onUserLogout(e);
//        }
        deleteCookies();//@todo aggiunta di gac 12.2.17
//        LibVaadin.getUI().getNavigator().navigateTo(ACost.VIEW_USE_LIST);
        LibVaadin.getUI().getNavigator().navigateTo(ACost.VIEW_HOME);
    }// end of method


//    /**
//     * Reads the cookies and puts the data in the fields
//     */
//    public void readCookies() {
//        String username = cookieService.getCookieValue(ACost.COOKIE_NAME_LOGIN);
//        String password = cookieService.getCookieValue(ACost.COOKIE_NAME_PASSWORD);
//        String rememberStr = cookieService.getCookieValue(ACost.COOKIE_NAME_REMEMBER);
//        boolean remember = (rememberStr.equalsIgnoreCase("true"));
//
//        AbsLoginForm loginForm = getLoginForm();
//        loginForm.setUsername(username);
//        loginForm.setPassword(password);
//        loginForm.setRemember(remember);
//
//    }// end of method


//    /**
//     * Reads data from the fields and writes the cookies
//     */
//    public void writeCookies() {
//        AbsLoginForm loginForm = getLoginForm();
//        boolean remember = loginForm.getRememberField().getValue();
//        UserIF user = loginForm.getSelectedUser();
//
//        LibCookie.setCookie(getLoginKey(), user.getNickname(), getLoginPath(), expiryTime);
//        LibCookie.setCookie(getPasswordKey(), user.getEncryptedPassword(), getLoginPath(), expiryTime);
//        if (remember) {
//            LibCookie.setCookie(getRememberKey(), "true", getLoginPath(), expiryTime);
//        } else {
//            LibCookie.deleteCookie(getRememberKey());
//        }
//
//    }


//    /**
//     * Renew the expiry time for all the cookies
//     */
//    private void renewCookies() {
//        Cookie cookie;
//        cookie = LibCookie.getCookie(getLoginKey());
//        LibCookie.setCookie(getLoginKey(), cookie.getValue(), getLoginPath(), expiryTime);
//        cookie = LibCookie.getCookie(getPasswordKey());
//        LibCookie.setCookie(getPasswordKey(), cookie.getValue(), getLoginPath(), expiryTime);
//        cookie = LibCookie.getCookie(getRememberKey());
//        LibCookie.setCookie(getRememberKey(), cookie.getValue(), getLoginPath(), expiryTime);
//    }

    /**
     * Delete all the cookies
     */
    private void deleteCookies() {
        cookieService.deleteCookie(ACost.COOKIE_NAME_LOGIN, "/");
        cookieService.deleteCookie(ACost.COOKIE_NAME_PASSWORD, "/");
        cookieService.deleteCookie(ACost.COOKIE_NAME_REMEMBER, "/");
    }// end of method


//    /**
//     * Attempts a login directly from the cookies without showing the login form.
//     *
//     * @return true if success
//     */
//    public boolean loginFromCookies() {
//        boolean success = false;
//        String username = LibCookie.getCookieValue(getLoginKey());
//        String encPassword = LibCookie.getCookieValue(getPasswordKey());
//        String clearPass = LibCrypto.decrypt(encPassword);
//
//        IAUser user = userFromNick(username);
//
//        if (user != null) {
//            if (user.validatePassword(clearPass)) {
//                this.user = user;
//                success = true;
//            }
//        }
//
//        // if success, renew the cookies (if the option is on)
//        // if failed, delete the cookies
//        if (success) {
//            if (renewCookiesOnLogin) {
//                renewCookies();
//            }
//            LoginEvent e = new LoginEvent(this, true, user, LoginTypes.TYPE_COOKIES, false);
//            fireLoginListeners(e);
//
//        } else {
//            deleteCookies();
//        }
//
//
//        return success;
//    }
//
//
//    /**
//     * Retrieves the user from a given username
//     *
//     * @return the user corresponding to a given username
//     */
//    protected IAUser userFromNick(String username) {
//        Utente aUser = Utente.read(username);
//        return aUser;
//    }
//
//
//    /**
//     * @return the expiry time of the cookies in seconds
//     */
//    public int getExpiryTime() {
//        return expiryTime;
//    }
//
//    /**
//     * Sets the expiry time for the cookies
//     *
//     * @param expiryTime the expiry time of the cookies in seconds
//     */
//    public void setExpiryTime(int expiryTime) {
//        this.expiryTime = expiryTime;
//    }
//
//    /**
//     * Whether the cookies are renewed after a successful login.
//     *
//     * @return true if the cookies are renewed
//     */
//    public boolean isRenewCookiesOnLogin() {
//        return renewCookiesOnLogin;
//    }// end of method
//
//    /**
//     * Whether the cookies are renewed after a successful login.
//     *
//     * @param renewCookiesOnLogin true to renew the expiry time on each login
//     */
//    public void setRenewCookiesOnLogin(boolean renewCookiesOnLogin) {
//        this.renewCookiesOnLogin = renewCookiesOnLogin;
//    }// end of method
//
//    public void setCookiePrefix(String cookiePrefix) {
//        this.cookiePrefix = cookiePrefix;
//    }
//
//    protected String getLoginPath() {
//        return AlgosApp.COOKIES_PATH;
//    }// end of method
//
//    protected String getLoginKey() {
//        String name = "";
//        if (!cookiePrefix.equals("")) {
//            name += cookiePrefix + ".";
//        }
//        return name += COOKIENAME_LOGIN;
//    }// end of method

    protected String getPasswordKey() {
        String name = "";
        if (!cookiePrefix.equals("")) {
            name += cookiePrefix + ".";
        }
        return name += COOKIENAME_PASSWORD;
    }

    protected String getRememberKey() {
        String name = "";
        if (!cookiePrefix.equals("")) {
            name += cookiePrefix + ".";
        }
        return name += COOKIENAME_REMEMBER;
    }

    /**
     * Removes all the login listeners
     */
    public void removeAllLoginListeners() {
        loginListeners.clear();
    }// end of method

    /**
     * Adds a LoginListener
     */
    public void addLoginListener(ALoginListener l) {
        loginListeners.add(l);
    }// end of method

    /**
     * Registers a unique LoginListener.
     * All the previous LoginListeners are deleted
     */
    public void setLoginListener(ALoginListener l) {
        removeAllLoginListeners();
        addLoginListener(l);
    }// end of method

    /**
     * Adds a LogoutListener
     */
    public void addLogoutListener(ALogoutListener l) {
        logoutListeners.add(l);
    }// end of method

    /**
     * Adds a ProfileChangeListener
     * Warning! Login is static - avoid leaving listeners attached!
     */
    public void addProfileListener(AProfileChangeListener l) {
        profileListeners.add(l);
    }// end of method

    /**
     * Sets the ProfileChangeListener
     */
    public void setProfileListener(AProfileChangeListener l) {
        profileListeners.clear();
        addProfileListener(l);
    }// end of method

    /**
     * @return true if a user is logged
     */
    public boolean isLogged() {
        return (getUser() != null);
    }// end of method

    public IAUser getUser() {
        return user;
    }// end of method

    public void setUser(IAUser user) {
        this.user = user;
        Object a = ((User) user).role;
    }// end of method

    public void setTypeLogged(EARoleType typeLogged) {
        this.typeLogged = typeLogged;
    }

    public EARoleType getTypeLogged() {
        return typeLogged;
    }// end of method

    public boolean isUser() {
        return getTypeLogged() == EARoleType.user || isAdmin();
    }// end of method

    public boolean isAdmin() {
        return getTypeLogged() == EARoleType.admin || isDeveloper();
    }// end of method

    public boolean isDeveloper() {
        if (AlgosApp.USE_SECURITY) {
            return getTypeLogged() == EARoleType.developer;
        } else {
            return true;
        }// end of if/else cycle
    }// end of method

    public Company getCompany() {
        if (AlgosApp.USE_MULTI_COMPANY) {
            return company;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    public void setCompany(Company company) {
        if (AlgosApp.USE_MULTI_COMPANY) {
            this.company = company;
        } else {
            this.company = null;
        }// end of if/else cycle
    }// end of method

    /**
     * Notifica i LoginListeners.
     * Esegue l'iterazione su una copia della ArrayList.
     * Inquesto modo chi viene notificato può aggiungere dei LoginListeners
     * senza causare una ConcurrentModificationException
     *
     * @param e il LoginEvent
     */
    private void fireLoginListeners(ALoginEvent e) {
        if (loginListeners != null) {
            ArrayList<ALoginListener> listenersCopy = (ArrayList<ALoginListener>) loginListeners.clone();
            for (ALoginListener listener : listenersCopy) {
                listener.onUserLogin(e);
            }// end of for cycle
        }// end of if cycle
    }// end of method


}// end of class
