package it.algos.springwam.entity.turno;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.menu.AMenu;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mar, 20-feb-2018
 * Time: 17:07
 */
@Slf4j
@SpringComponent
@Scope("session")
public class TurnoMenuLayout extends AMenu {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;


    private MenuBar tabMenuBar;

    public TurnoMenuLayout() {
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

        this.addMenuAnnulla();
        this.addComponent(tabMenuBar);
        this.addComponent(new HorizontalLayout(tabMenuBar));
    }// end of method

    private void addMenuAnnulla() {
        MenuBar.Command commandHome = null;

        commandHome = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(ACost.TAG_HOME);
            }// end of inner method
        };// end of anonymous inner class

        tabMenuBar.addItem(text.primaMaiuscola(ACost.BOT_ANNULLA), VaadinIcons.ARROW_BACKWARD, commandHome);
    }// end of method

}// end of class
