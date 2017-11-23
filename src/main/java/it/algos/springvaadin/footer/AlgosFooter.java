package it.algos.springvaadin.footer;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by gac on 12/06/17
 * .
 */
@SpringComponent
public class AlgosFooter extends HorizontalLayout {

    private final static String DEVELOPER_NAME = "Algos® ";
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
        this.setWidth("100%");
        this.setMargin(new MarginInfo(false, false, true, false));
        this.setSpacing(true);
        this.setHeight("60px");
    }// end of method

    public void setAppMessage(String message) {
        this.message = message;
        this.fixMessage();
    }// end of method


    private void fixMessage() {
        String tag="all companies";
        String companyCode = "";
        this.removeAllComponents();

        companyCode = LibSession.getCompany() != null ? LibSession.getCompany().getCode() : "";
        if (AlgosApp.USE_MULTI_COMPANY) {
            if (LibText.isValid(companyCode)) {
                message += " - " + companyCode;
            } else {
                message += " - " + tag;
            }// end of if/else cycle
        }// end of if cycle

        if (LibSession.isDeveloper()) {
            message += " (dev)";
        } else {
            if (LibSession.isAdmin()) {
                message += " (admin)";
            } else {
                message += " (buttonUser)";
            }// end of if/else cycle
        }// end of if/else cycle

//        label.setValue(DEVELOPER_NAME + message);
        label = new LabelRosso(DEVELOPER_NAME + message);
        this.addComponent(label);
    }// end of method

}// end of class
