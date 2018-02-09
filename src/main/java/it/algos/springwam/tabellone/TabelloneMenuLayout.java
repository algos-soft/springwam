package it.algos.springwam.tabellone;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.home.AHomeView;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALoginButton;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mer, 22-nov-2017
 * Time: 20:13
 */
@Slf4j
@SpringComponent
@Scope("session")
public class TabelloneMenuLayout extends VerticalLayout {


    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ALoginButton loginButton;

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;

    private MenuBar tabMenuBar;

    public TabelloneMenuLayout() {
        super();
    }// end of Spring constructor


    @PostConstruct
    void init() {
        this.setMargin(false);

        tabMenuBar = new MenuBar();
        tabMenuBar.addStyleName("blue");
        tabMenuBar.setAutoOpen(true);
        this.start();

    }// end of method

    /**
     * pulisce la menubar
     */
    public void reset() {
        this.removeAllComponents();

        if (tabMenuBar != null) {
            tabMenuBar.removeItems();
        }// end of if cycle
    }// end of method


    /**
     * avvia la menubar
     */
    public void start() {
        this.reset();

        this.addMenuHome();
        this.addMenuPeriodo();
        this.addMenuNuoviTurni();

        this.addComponent(tabMenuBar);
        this.addComponent(new HorizontalLayout(tabMenuBar, loginButton));
    }// end of method

    private void addMenuHome() {
        MenuBar.Command commandHome = null;

        commandHome = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(ACost.TAG_HOME);
            }// end of inner method
        };// end of anonymous inner class

        tabMenuBar.addItem(text.primaMaiuscola(ACost.TAG_HOME), VaadinIcons.HOME, commandHome);
    }// end of method


    private void addMenuPeriodo() {
        MenuBar.MenuItem item = tabMenuBar.addItem("Periodo", VaadinIcons.LINES, null);
        MenuBar.MenuItem itemAltro;

        item.addItem("Indietro", VaadinIcons.ARROW_LEFT, null);
        item.addItem("Oggi", VaadinIcons.CALENDAR_O, null);
        item.addItem("Lunedì", VaadinIcons.CALENDAR_O, null);
        item.addItem("Avanti", VaadinIcons.ARROW_RIGHT, null);

        itemAltro = item.addItem("Altro...", VaadinIcons.LINES, null);
        itemAltro.addItem("Giorno precedente", VaadinIcons.ARROW_LEFT, null);
        itemAltro.addItem("Giorno successivo", VaadinIcons.ARROW_RIGHT, null);
        itemAltro.addItem("Seleziona periodo", VaadinIcons.SEARCH, null);
    }// end of method


    private void addMenuNuoviTurni() {
        MenuBar.Command commandGenerator = null;

        commandGenerator = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(AppCost.TAG_GEN);
            }// end of inner method
        };// end of anonymous inner class

        tabMenuBar.addItem("Genera", VaadinIcons.CALENDAR, commandGenerator);
    }// end of method

}// end of class
