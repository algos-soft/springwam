package it.algos.springvaadin.menu;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.login.ALoginButton;
import it.algos.springvaadin.panel.APanel;
import it.algos.springvaadin.service.AAnnotationService;
import it.algos.springvaadin.service.AArrayService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springvaadin.view.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 01/06/17.
 * VerticalLayout has 100% width and undefined height by default.
 */
@Slf4j
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_MENU_LAYOUT)
public class MenuLayout extends AMenu {

//    @Autowired
//    public PreferenzaService pref;
//@todo RIMETTERE

    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ALoginButton loginButton;


    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ALogin login;


    @Autowired
    public ATextService text;

    @Autowired
    AAnnotationService annotation;

    @Autowired
    AReflectionService reflection;

    public static String MENU_ABILITATO = "highlight";
    public static String MENU_DISABILITATO = "disabilitato";

    private final static String MENU_NAME = "MENU_NAME";
    private MenuBar firstMenuBar;
    private MenuBar secondMenuBar;
    private MenuBar thirdMenuBar;

    private List<Class<? extends IAView>> listaViews;

    public MenuLayout() {
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

        secondMenuBar = new MenuBar();
        secondMenuBar.addStyleName("verde");
        secondMenuBar.setAutoOpen(true);

        thirdMenuBar = new MenuBar();
        thirdMenuBar.addStyleName("rosso");
        thirdMenuBar.setAutoOpen(true);

        listaViews = new ArrayList<>();
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
     * Avvia la menubar, dopo aver aggiunto tutte le viste
     */
    public void start() {
        this.removeAllComponents();

        if (AlgosApp.USE_SECURITY) {
            if (login.isUser() && firstMenuBar.getItems().size() > 0) {
                this.addComponent(new HorizontalLayout(firstMenuBar, loginButton));
            }// end of if/else cycle

            if (login.isAdmin() && secondMenuBar.getItems().size() > 0) {
                this.addComponent(secondMenuBar);
            }// end of if cycle

            if (login.isDeveloper() && thirdMenuBar.getItems().size() > 0) {
                this.addComponent(thirdMenuBar);
            }// end of if cycle
        } else {
            if (firstMenuBar.getItems().size() > 0) {
                this.addComponent(firstMenuBar);
            }// end of if/else cycle

//            if (secondMenuBar.getItems().size() > 0) {
//                this.addComponent(secondMenuBar);
//            }// end of if cycle
//
//            if (thirdMenuBar.getItems().size() > 0) {
//                this.addComponent(thirdMenuBar);
//            }// end of if cycle
        }// end of if/else cycle

    }// end of method

    /**
     * Adds a view to the firstMenuBar
     * Chechk if the logged buttonUser is enabled to views this view
     *
     * @param viewClazz the view class to adds
     */
    public void addView(Class<? extends IAView> viewClazz) {
        EARoleType roleTypeVisibility = annotation.getViewRoleType(viewClazz);
        if (roleTypeVisibility != null) {
            addView(roleTypeVisibility, viewClazz);
        }// end of if cycle
    }// end of method


    /**
     * Adds a view to the firstMenuBar
     * Chechk if the logged buttonUser is enabled to views this view
     *
     * @param entityClazz the model class to check the visibility
     * @param viewClazz   the view class to adds
     */
    public void addView(Class<? extends AEntity> entityClazz, Class<? extends IAView> viewClazz) {
        //@todo RIMETTERE

        EARoleType roleTypeVisibility = annotation.getEntityRoleType(entityClazz);
        if (roleTypeVisibility != null) {
            addView(roleTypeVisibility, viewClazz);
        }// end of if cycle
    }// end of method


    /**
     * Adds a view to the firstMenuBar
     * Regola il nome che appare nel menu:
     * 1) usa la specifica property statica 'MENU_NAME' eventualmente indicata nella classe di tipo view
     * 2) se non la trova, usa il 'name' usato internamente da SpringNavigator e indicato dalla Annotation @SpringView della classe
     * 3) se non trova nulla (errore) usa il 'simpleName' della classe java
     *
     * @param viewClass       the view class to adds
     * @param itemToAddBefore quello che si vuole inserire
     */
    public void addViewBefore(EARoleType roleTypeVisibility, Class<? extends IAView> viewClass, MenuBar.MenuItem itemToAddBefore) {
        String navigatorInternalName = annotation.getViewName(viewClass);
        String captionMenuName = reflection.getPropertyStr(viewClass, MENU_NAME);
        Resource viewIcon = reflection.getPropertyRes(viewClass, "VIEW_ICON");

        if (text.isEmpty(captionMenuName)) {
            captionMenuName = navigatorInternalName;
        }// end of if cycle
        captionMenuName = text.primaMaiuscola(captionMenuName);

        MenuBar.Command viewCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                deselezionaAllItemButOne(menuItem);
                getUI().getNavigator().navigateTo(navigatorInternalName);
            }// end of inner method
        };// end of anonymous inner class

        if (AlgosApp.USE_SECURITY) {
            switch (roleTypeVisibility) {
                case user:
                    if (itemToAddBefore == null) {
                        firstMenuBar.addItem(captionMenuName, viewIcon, viewCommand);
                    } else {
                        firstMenuBar.addItemBefore(captionMenuName, viewIcon, viewCommand, itemToAddBefore);
                    }// end of if/else cycle
                    break;
                case admin:
//                if (login.isAdmin()) {
                    if (itemToAddBefore == null) {
                        secondMenuBar.addItem(captionMenuName, viewIcon, viewCommand);
                    } else {
                        secondMenuBar.addItemBefore(captionMenuName, viewIcon, viewCommand, itemToAddBefore);
                    }// end of if/else cycle
                    break;
                case developer:
//                if (login.isDeveloper()) {
                    if (itemToAddBefore == null) {
                        thirdMenuBar.addItem(captionMenuName, viewIcon, viewCommand);
                    } else {
                        thirdMenuBar.addItemBefore(captionMenuName, viewIcon, viewCommand, itemToAddBefore);
                    }// end of if/else cycle
                    break;
                default:
                    log.error("Switch - caso '" + roleTypeVisibility.name() + "' non definito in MenuLayout.addViewBefore()");
                    break;
            } // end of switch statement
        } else {
            if (itemToAddBefore == null) {
                firstMenuBar.addItem(captionMenuName, viewIcon, viewCommand);
            } else {
                firstMenuBar.addItemBefore(captionMenuName, viewIcon, viewCommand, itemToAddBefore);
            }// end of if/else cycle
        }// end of if/else cycle


        //@todo CONTROLLARE SE SERVE
//        Object obj = LibReflection.getMethodButton(viewClass, "getButtoneMenu");
//        ARoleType roleTypeVisibility = LibAnnotation.getViewRoleType(viewClass);
//
//        if (obj != null && obj instanceof MenuBar.Command) {
//            viewCommand = (MenuBar.Command) obj;
//        } else {
//        }// end of if/else cycle

        //@todo RIMETTERE
//        if (roleTypeVisibility == ARoleType.admin) {
//            if (!LibSession.isAdmin()) {
//                return;
//            }// end of if cycle
//        }// end of if cycle
//        if (roleTypeVisibility == ARoleType.developer) {
//            if (!LibSession.isDeveloper()) {
//                return;
//            }// end of if cycle
//        }// end of if cycle

//        switch (roleTypeVisibility) {
//            case user:
//                firstMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
//                break;
//            case admin:
//                secondMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
//                break;
//            case developer:
//                thirdMenuBar.addItem(LibText.primaMaiuscola(viewName), viewIcon, viewCommand);
//                break;
//            default:
//                log.warn("Switch - caso non definito");
//                break;
//        } // end of switch statement

        listaViews.add(viewClass);
    }// end of method


    /**
     * Adds a view to the firstMenuBar
     * Regola il nome che appare nel menu:
     * 1) usa la specifica property statica 'MENU_NAME' eventualmente indicata nella classe di tipo view
     * 2) se non la trova, usa il 'name' usato internamente da SpringNavigator e indicato dalla Annotation @SpringView della classe
     * 3) se non trova nulla (errore) usa il 'simpleName' della classe java
     *
     * @param viewClass       the view class to adds
     * @param viewClassBefore before quella che si vuole inserire
     */
    public void addViewBefore(EARoleType roleTypeVisibility, Class<? extends IAView> viewClass, Class<? extends IAView> viewClassBefore) {
        this.addViewBefore(roleTypeVisibility, viewClass, reflection.getPropertyStr(viewClassBefore, MENU_NAME));
    }// end of method


    /**
     * Adds a view to the firstMenuBar
     * Regola il nome che appare nel menu:
     * 1) usa la specifica property statica 'MENU_NAME' eventualmente indicata nella classe di tipo view
     * 2) se non la trova, usa il 'name' usato internamente da SpringNavigator e indicato dalla Annotation @SpringView della classe
     * 3) se non trova nulla (errore) usa il 'simpleName' della classe java
     *
     * @param viewClass       the view class to adds
     * @param captionMenuName visibile nella barra di menu
     */
    public void addViewBefore(EARoleType roleTypeVisibility, Class<? extends IAView> viewClass, String captionMenuName) {
        this.addViewBefore(roleTypeVisibility, viewClass, getMenu(captionMenuName));
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
    public void addView(EARoleType roleTypeVisibility, Class<? extends IAView> viewClass) {
        this.addViewBefore(roleTypeVisibility, viewClass, (MenuBar.MenuItem) null);
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
     *
     * @param captionMenuName visibile nella barra di menu
     */
    public MenuBar.MenuItem getMenu(String captionMenuName) {
        MenuBar.MenuItem itemRequested = null;

        if (firstMenuBar != null) {
            List<MenuBar.MenuItem> items = firstMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(captionMenuName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        if (secondMenuBar != null) {
            List<MenuBar.MenuItem> items = secondMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(captionMenuName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        if (thirdMenuBar != null) {
            List<MenuBar.MenuItem> items = thirdMenuBar.getItems();
            for (MenuBar.MenuItem item : items) {
                if (item.getText().equalsIgnoreCase(captionMenuName)) {
                    itemRequested = item;
                }// end of if cycle
            } // fine del ciclo for
        }// fine del blocco if

        return itemRequested;
    }// end of method


    /**
     * Elimina un menu
     *
     * @param viewClass the view class to remove
     */
    public void removeView(Class<? extends IAView> viewClass) {
        removeView(annotation.getViewName(viewClass));
    }// end of method


    /**
     * Elimina un menu
     *
     * @param captionMenuName visibile nella barra di menu
     */
    public void removeView(String captionMenuName) {
        MenuBar.MenuItem item = getMenu(captionMenuName);

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

