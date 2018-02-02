package it.algos.springvaadin.footer;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springvaadin.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * Created by gac on 12/06/17
 * VerticalLayout has 100% width and undefined height by default.
 * Barra inferiore di messaggi all'utente.
 * Può essere visibile o nascosta a seconda del flag booleano KEY_DISPLAY_FOOTER_INFO
 * La visibilità viene gestita da AlgosUI
 * Tipicamente dovrebbe mostrare:
 * Copyright di Algos
 * Nome dell'applicazione
 * Versione dell'applicazione
 * Livello di accesso dell'utente loggato (developer, admin, utente) eventualmente oscurato per l'utente semplice
 * Company selezionata (nel caso di applicazione multiCompany)
 */
@SpringComponent
@Scope("session")
public class AFooter extends VerticalLayout {

    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    @Lazy
    public ALogin login;

    @Autowired
    public AHtmlService html;

    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ATextService text;


    private final static String DEVELOPER_COMPANY = "Algos® ";
    public  static String PROJECT = "";
    public  static String VERSION = "";
    private final static LocalDate DATA = LocalDate.now();
    private final static String DEV_TAG = "Dev: ";
    private final static String ADMIN_TAG = "Admin: ";
    private final static String USER_TAG = "User: ";
    private String message = "";
    private Label label;

    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    protected void inizia() {
        this.setMargin(false);
        this.setSpacing(true);

        if (ACost.DEBUG) {// @TODO costante provvisoria da sostituire con preferenzeService
            this.addStyleName("greenBg");
        }// end of if cycle

        this.start();
    }// end of method

    public void setAppMessage(String message) {
        this.message = message;
        this.start();
    }// end of method


    public void start() {
        String message;
        String sep = " - ";
        String spazio = " ";
        String tag = "all companies";
        String companyName = login.getCompany() != null ? login.getCompany().getDescrizione() : "";
        String userName = login.getUser() != null ? login.getUser().getNickname() : "";
        this.removeAllComponents();

        //@todo RIMETTERE
//        companyCode = LibSession.getCompany() != null ? LibSession.getCompany().getCode() : "";
//        if (AlgosApp.USE_MULTI_COMPANY) {
//            if (LibText.isValid(companyCode)) {
//                message += " - " + companyCode;
//            } else {
//                message += " - " + tag;

//            }// end of if/else cycle
//        }// end of if cycle

        //@todo RIMETTERE
//        if (LibSession.isDeveloper()) {
//            message += " (dev)";
//        } else {
//            if (LibSession.isAdmin()) {
//                message += " (admin)";
//            } else {
//                message += " (buttonUser)";
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//        label = new LabelRosso(DEVELOPER_NAME + message);

        String data;
        message = html.setVerdeBold(DEVELOPER_COMPANY + sep + PROJECT);
        message += spazio;
        message += html.setBold(VERSION);
        message += " del ";
        message += DATA.toString();
        if (text.isValid(companyName)) {
            message += sep;
            message += html.setBluBold(companyName);
        }// end of if cycle
        if (text.isValid(userName)) {
            switch (login.getTypeLogged()) {
                case user:
                    message += sep;
                    message += html.setBluBold(USER_TAG + userName);
                    break;
                case admin:
                    message += sep;
                    message += html.setVerdeBold(ADMIN_TAG + userName);
                    break;
                case developer:
                    message += sep;
                    message += html.setRossoBold(DEV_TAG + userName);
                    break;
                default:
                    break;
            } // end of switch statement
        }// end of if cycle
        this.addComponent(new Label(message, ContentMode.HTML));
    }// end of method

}// end of class
