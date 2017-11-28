package it.algos.springwam.tabellone;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;

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
public class TabelloneMenuLayout extends VerticalLayout {


    private MenuBar firstMenuBar;

    public TabelloneMenuLayout() {
        super();
    }// end of Spring constructor


    @PostConstruct
    void init() {
        this.setMargin(false);

        firstMenuBar = new MenuBar();
        firstMenuBar.addStyleName("blue");
        firstMenuBar.setAutoOpen(true);
        this.addComponent(firstMenuBar);

        this.addMenuHome();
        this.addMenuPeriodo();
        this.addMenuNuoviTurni();
    }// end of method


    private void addMenuHome() {
        MenuBar.Command commandHome = null;

        commandHome = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(Cost.TAG_HOME);
            }// end of inner method
        };// end of anonymous inner class

        firstMenuBar.addItem(LibText.primaMaiuscola(Cost.TAG_HOME), VaadinIcons.HOME, commandHome);
    }// end of method


    private void addMenuPeriodo() {
        MenuBar.MenuItem item = firstMenuBar.addItem("Periodo", VaadinIcons.LINES, null);
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

        firstMenuBar.addItem("Genera", VaadinIcons.CALENDAR, commandGenerator);
    }// end of method

}// end of class
