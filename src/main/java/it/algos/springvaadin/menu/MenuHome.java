package it.algos.springvaadin.menu;

import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import it.algos.springvaadin.home.AHomeView;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALoginButton;
import it.algos.springvaadin.service.AAnnotationService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springvaadin.view.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 20-dic-2017
 * Time: 17:47
 */
@Slf4j
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_MENU_LAYOUT)
public class MenuHome extends AMenu {

    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ALoginButton loginButton;

    @Autowired
    public ATextService text;

    @Autowired
    AAnnotationService annotation;

    @Autowired
    AReflectionService reflection;


    public static String MENU_ABILITATO = "highlight";
    public static String MENU_DISABILITATO = "disabilitato";

    private final static String MENU_NAME = "Home";
    private MenuBar firstMenuBar;


    public MenuHome() {
        super();
    }

    @PostConstruct
    void init() {
        this.setMargin(false);
        if (ACost.DEBUG) {// @TODO costante provvisoria da sostituire con preferenzeService
            this.addStyleName("yellowBg");
        }// end of if cycle

        firstMenuBar = new MenuBar();
        firstMenuBar.addStyleName("blue");
        firstMenuBar.setAutoOpen(true);

        start();
    }// end of method


    /**
     * pulisce la menubar
     */
    public void reset() {
        this.removeAllComponents();

        if (firstMenuBar != null) {
            firstMenuBar.removeItems();
        }// end of if cycle

    }// end of method


    /**
     * avvia la menubar
     */
    public void start() {
        this.reset();

        addView(AHomeView.class);

        if (firstMenuBar.getItems().size() > 0) {
            this.addComponent(new HorizontalLayout(firstMenuBar, loginButton));
        }// end of if cycle

    }// end of method


    /**
     * Adds a view to the firstMenuBar
     * Regola il nome che appare nel menu:
     * 1) usa la specifica property statica 'MENU_NAME' eventualmente indicata nella classe di tipo view
     * 2) se non la trova, usa il 'name' usato internamente da SpringNavigator e indicato dalla Annotation @SpringView della classe
     * 3) se non trova nulla (errore) usa il 'simpleName' della classe java
     *
     * @param viewClass the view class to adds
     */
    public void addView(Class<? extends IAView> viewClass) {
        Resource viewIcon = reflection.getPropertyRes(viewClass, "VIEW_ICON");

        MenuBar.Command viewCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                String message = "Per usare il programma, devi prima effettuare il login\nCliccando sul secondo bottone in alto, grazie";
                Notification.show("Login", message, Notification.Type.WARNING_MESSAGE);
            }// end of inner method
        };// end of anonymous inner class

        firstMenuBar.addItem(MENU_NAME, viewIcon, viewCommand);

    }// end of method


}// end of class

