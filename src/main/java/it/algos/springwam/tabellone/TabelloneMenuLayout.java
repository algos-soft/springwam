package it.algos.springwam.tabellone;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
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

        this.addMenu();
    }// end of method


    private void addMenu() {
        MenuBar.Command commandHome = null;

        commandHome = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(Cost.TAG_HOME);
            }// end of inner method
        };// end of anonymous inner class

        firstMenuBar.addItem(LibText.primaMaiuscola(Cost.TAG_HOME), VaadinIcons.HOME, commandHome);
    }// end of method

}// end of class
