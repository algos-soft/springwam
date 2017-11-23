package it.algos.springvaadin.menu;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import it.algos.springvaadin.lib.LibText;

import java.util.List;

/**
 * Created by gac on 27/05/17.
 * <p>
 * Contenitore grafico per la barra superiore dei menu
 * A sinistra il menu principale e a destra il menu/bottone del Login
 * <p>
 * A sinistra i menu specifici dell'applicazione (uno o più) <br>
 * A destra il bottone (od i menu) della Security (login). <br>
 * Se l'applicazione non usa la security, il bottone di destra non appare. <br>
 * Può essere sovrascritta dall'applicazione per utilizzi specifici. <br>
 * L'implementazione con GridLayout può essere modificata all'interno di questa classe. <br>
 * Se non usa la security, il GridLayout viene sostituito con HorizontalLayout. <br
 * Le due barre di menu (destra e sinistra) hanno due stili diversi. <br>
 * Per adesso sono implementati uguali nel scss, ma potrebbero essere differenziati. <br>
 * Il costruttore seleziona già se l'applicazione usa la security e regola di conseguenza il menu di destra. <br>
 * I menu specifici dell'applicazione possono essere passati nel costruttore od aggiunti dopo. <br>
 */
public class MenuPlaceholder extends CssLayout {

    /**
     * Primo menu da sinistra. Obbligatorio. Standard. Per utenti normali.
     */
    private MenuBar firstMenuBar;

    /**
     * Lista di altri menu. Opzionali. Possono essere uno o due o tre. Per buttonAdmin e developper o altro.
     * Tipicamente hanno un colore diverso per evidenziarli.
     */
    private List<MenuBar> altriMenuBar;

    /**
     * Utilizzo di itemHome (il primo da sinistra di firstMenuBar).
     */
    private boolean usaItemHome = true;

    /**
     * Utilizzo di itemHelp (l'ultimo a destra di firstMenuBar).
     */
    private boolean usaItemHelp = true;


    /**
     * Constructor senza security
     */
    public MenuPlaceholder() {
        this(false);
    }// end of constructor


    /**
     * Constructor
     */
    public MenuPlaceholder(boolean usaSecurity) {
        init(usaSecurity);
    }// end of constructor


    /**
     * Initialization
     * <p>
     * Niente margine che c'è già nel contenitore parente <br>
     * ˙
     */
    private void init(boolean usaSecurity) {
        //       this.setMargin(false);
        //       this.setSpacing(true);
        this.setHeight("40px");
        this.setWidth("100%");

        createMenuBar();
        this.addComponent(firstMenuBar);

        //@todo da sviluppare
//        if (usaSecurity) {
//            loginBar = createLoginMenuBar();
//            this.addComponent(loginBar);
//        }// fine del blocco if



//        this.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
//        this.addComponent(createNavigationButton("View Scoped View", "alfa"));
//        this.addComponent(createNavigationButton("Pippoz", "beta"));
    }// end of method
//    private Button createNavigationButton(String caption, final String viewName) {
//        Button button = new Button(caption);
//        button.addStyleName(ValoTheme.BUTTON_SMALL);
//        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
//        return button;
//    }

    /**
     * Menu bar specifico dell'applicazione
     * <p>
     * I singoli menu vengono aggiunti dall'applicazione specifica col metodo addMenu. <br>
     */
    private void createMenuBar() {
        firstMenuBar = new MenuBar();
        firstMenuBar.setAutoOpen(true);

        //@todo da sviluppare
        //       if (LibSession.isDeveloper() && AlgosUIParams.DEBUG_GUI) {
        //           menubar.addStyleName("mymenubar");
        //       }// fine del blocco if
        regolaMenuBar();
    }// end of method

    /**
     * Regolazioni aggiuntive del primo menu
     * Eventuale aggiunta del primo item
     */
    private void regolaMenuBar() {
        if (usaItemHome) {
            firstMenuBar.addItem("Home", VaadinIcons.HOME, null);
        }// end of if cycle
    }// end of method

    /**
     * Aggiunge l'ultimo item al menu
     */
    public void creaMenuAfter() {
        if (usaItemHelp) {
            firstMenuBar.addItem("Help", VaadinIcons.QUESTION, null);
        }// end of if cycle
    }// end of method

    /**
     * Adds a menu to the firstMenuBar
     * <p>
     *
     * @param viewClass the view class to instantiate
     * @param label     the text for the menu item
     * @param icon      the icon for the menu item
     */
    public void addMenu(Class<? extends View> viewClass, String label, Resource icon) {
        MenuBar.MenuItem menuItem;
        MenuCommand command = new MenuCommand(this, viewClass);
        AIMenu menuAnnotation = viewClass.getDeclaredAnnotation(AIMenu.class);

        //--primo check di sicurezza
        if (menuAnnotation != null) {
            if (LibText.isEmpty(label)) {
                label = menuAnnotation.label();
            }// end of if cycle

            if (icon == null) {
                icon = menuAnnotation.icon();
            }// end of if cycle
        }// end of if cycle

        //--secondo check di sicurezza
        if (LibText.isEmpty(label)) {
            label = viewClass.getSimpleName();
        }// end of if cycle

        menuItem = firstMenuBar.addItem(label, icon, command);
//        menuItem.setStyleName(MENU_DISABILITATO);@todo da controllare
    }// end of method

    /**
     * Adds a menu to the firstMenuBar
     * <p>
     *
     * @param componentClass the view class to instantiate
     * @param label          the text for the menu item
     * @param icon           the icon for the menu item
     */
    public void addMenu2(Class<? extends CustomComponent> componentClass, String label, Resource icon) {
        MenuBar.MenuItem menuItem;
        MenuCommand command = new MenuCommand(this, componentClass);
        menuItem = firstMenuBar.addItem(label, icon, command);
//        menuItem.setStyleName(MENU_DISABILITATO);@todo da controllare
    }// end of method

    /**
     * de-selects all the items in the menubar
     */
    public void deSelectItemsBut(MenuBar.MenuItem selectedItem) {

        List<MenuBar.MenuItem> items = firstMenuBar.getItems();
        for (MenuBar.MenuItem item : items) {
            deselectItem(item);
        } // fine del ciclo for

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


    public MenuBar getMenuBar() {
        return firstMenuBar;
    }// end of getter method


    public void setMenuBar(MenuBar menuBar) {
        this.firstMenuBar = menuBar;
    }//end of setter method

}// end of class
