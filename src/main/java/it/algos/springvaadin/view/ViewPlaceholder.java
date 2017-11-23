package it.algos.springvaadin.view;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.lib.LibSession;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * Created by gac on 12/06/17
 * .
 */
@Lazy
@SpringComponent
public class ViewPlaceholder extends Panel {

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
        this.addStyleName(ValoTheme.PANEL_BORDERLESS);

        if (AlgosApp.USE_DEBUG) {
            this.addStyleName("yellowBg");
        }// fine del blocco if

    }// end of method


}// end of class
