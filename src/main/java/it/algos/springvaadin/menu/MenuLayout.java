package it.algos.springvaadin.menu;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.entity.preferenza.PreferenzaNavView;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibReflection;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.login.ARoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by gac on 01/06/17.
 * .
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class MenuLayout extends VerticalLayout {

    public static String MENU_ABILITATO = "highlight";
    public static String MENU_DISABILITATO = "disabilitato";

    private MenuBar firstMenuBar;
    private MenuBar secondMenuBar;
    private MenuBar thirdMenuBar;

    public MenuLayout() {
        super();
    }

    @PostConstruct
    void init() {
        this.setMargin(false);

        firstMenuBar = new MenuBar();
        firstMenuBar.addStyleName("blue");
        firstMenuBar.setAutoOpen(true);

        secondMenuBar = new MenuBar();
        secondMenuBar.addStyleName("verde");
        secondMenuBar.setAutoOpen(true);

        thirdMenuBar = new MenuBar();
        thirdMenuBar.addStyleName("rosso");
        thirdMenuBar.setAutoOpen(true);
    }// end of method


    /**
     * pulisce la menubar
     */
    public void reset() {
        this.removeAllComponents();

        if (firstMenuBar != null) {
            firstMenuBar.removeItems();
        }// end of if cycle

        if (secondMenuBar != null) {
            secondMenuBar.removeItems();
        }// end of if cycle

        if (thirdMenuBar != null) {
            thirdMenuBar.removeItems();
        }// end of if cycle
    }// end of method


    /**
     * avvia la menubar, dopo aver aggiunto tutte le viste
     */
    public void start() {
        if (firstMenuBar.getItems().size() > 0) {
            this.addComponent(firstMenuBar);
        }// end of if cycle

        if (secondMenuBar.getItems().size() > 0) {
            this.addComponent(secondMenuBar);
        }// end of if cycle

        if (thirdMenuBar.getItems().size() > 0) {
            this.addComponent(thirdMenuBar);
        }// end of if cycle
    }// end of method

    /**
     * Adds a view to the firstMenuBar
     * Chechk if the logged buttonUser is enabled to views this view
     *
     * @param entityClazz the model class to check the visibility
     * @param viewClazz   the view class to adds
     */
    public void addView(Class<? extends AEntity> entityClazz, Class<? extends View> viewClazz) {
        if (LibAnnotation.isEntityClassVisibile(entityClazz)) {
            addView(viewClazz);
        }// end of if cycle
    }// end of method


    /**
     * Adds a view to the firstMenuBar
     *
     * @param viewClass the view class to adds
     */
    public void addView(Class<? extends View> viewClass) {
        MenuBar.Command viewCommand = null;
        Object obj = LibReflection.getMethodButton(viewClass, "getButtoneMenu");
        String viewName = LibAnnotation.getNameView(viewClass);
        Resource viewIcon = LibReflection.getPropertyRes(viewClass, "VIEW_ICON");
        ARoleType roleTypeVisibility = LibAnnotation.getViewRoleType(viewClass);

        if (obj != null && obj instanceof MenuBar.Command) {
            viewCommand = (MenuBar.Command) obj;
        } else {
            viewCommand = new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem menuItem) {
                    deselezionaAllItemButOne(menuItem);
                    getUI().getNavigator().navigateTo(viewName);
                }// end of inner method
            };// end of anonymous inner class
        }// end of if/else cycle

        if (roleTypeVisibility == ARoleType.admin) {
            if (!LibSession.isAdmin()) {
                return;
            }// end of if cycle
        }// end of if cycle
        if (roleTypeVisibility == ARoleType.developer) {
            if (!LibSession.isDeveloper()) {
                return;
            }// end of if cycle
        }// end of if cycle

        switch (roleTypeVisibility) {
            case user:
                firstMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
                break;
            case admin:
                secondMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
                break;
            case developer:
                thirdMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

    }// end of method

    /**
     * Regola l'aspetto di tutti i menu <br>
     */
    public void deselezionaAllItemButOne(MenuBar.MenuItem selectedItem) {
        deselezionaAllItemButOneBase(firstMenuBar, selectedItem);
        deselezionaAllItemButOneBase(secondMenuBar, selectedItem);
        deselezionaAllItemButOneBase(thirdMenuBar, selectedItem);
    }// end of method

    /**
     * Regola l'aspetto di tutti i menu <br>
     */
    private void deselezionaAllItemButOneBase(MenuBar menuBar, MenuBar.MenuItem selectedItem) {
        List<MenuBar.MenuItem> lista = menuBar.getItems();

        for (MenuBar.MenuItem menuItem : lista) {
            if (menuItem != selectedItem) {
                menuItem.setStyleName(MENU_DISABILITATO);
            }// end of if cycle
        }// fine del ciclo for

        if (selectedItem != null) {
            selectedItem.setStyleName(MENU_ABILITATO);
        }// end of if cycle

    }// end of method

    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    public void menuSelezionato(MenuBar.MenuItem selectedItem) {

        // de-selects all the items in the menubar
        if (firstMenuBar != null) {
            List<MenuBar.MenuItem> items = firstMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                deselectItem(item);
            } // fine del ciclo for
        }// fine del blocco if

        // highlights the selected item
        // the style name will be prepended automatically with "v-menubar-menuitem-"
        selectedItem.setStyleName("highlight");

        // it this item is inside another item, highlight also the parents in the chain
        MenuBar.MenuItem item = selectedItem;
        while (item.getParent() != null) {
            item = item.getParent();
            item.setStyleName("highlight");
        } // fine del ciclo while

    }// end of method

    /**
     * Recursively de-selects one item and all its children
     */
    private void deselectItem(MenuBar.MenuItem item) {
        item.setStyleName(null);
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem child : items) {
                deselectItem(child);
            } // fine del ciclo for
        }// fine del blocco if
    }// end of method


    /**
     * Recupera un menu
     */
    public MenuBar.MenuItem getMenu(String viewName) {
        MenuBar.MenuItem itemRequested = null;

        if (firstMenuBar != null) {
            List<MenuBar.MenuItem> items = firstMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(viewName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        if (secondMenuBar != null) {
            List<MenuBar.MenuItem> items = secondMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(viewName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        if (thirdMenuBar != null) {
            List<MenuBar.MenuItem> items = thirdMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(viewName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        return itemRequested;
    }// end of method


    /**
     * Elimina un menu
     */
    public void removeView(String viewName) {
        MenuBar.MenuItem item = getMenu(viewName);

        if (firstMenuBar != null && item != null) {
            firstMenuBar.removeItem(item);
        }// fine del blocco if

        if (secondMenuBar != null && item != null) {
            secondMenuBar.removeItem(item);
        }// fine del blocco if

        if (thirdMenuBar != null && item != null) {
            thirdMenuBar.removeItem(item);
        }// fine del blocco if

    }// end of method

}// end of class

