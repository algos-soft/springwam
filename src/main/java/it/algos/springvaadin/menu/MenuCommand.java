package it.algos.springvaadin.menu;

import com.vaadin.navigator.View;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

/**
 * Created by gac on 27/05/17
 */
public class MenuCommand implements MenuBar.Command {

    private MenuPlaceholder placeholder;
    private View view;
    private Class clazz;
    private boolean cached;

    /**
     * Constructor - lazy
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The viewCached parameter controls if the view will be instantiated only once
     * or each time is requested bu yhe Navigator.
     *
     * @param placeholder the MenuBar
     * @param clazz       the class to instantiate (must implement View)
     */
    public MenuCommand(MenuPlaceholder placeholder, Class clazz) {
        this(placeholder, clazz, true);
    }// end of constructor


    /**
     * Constructor - lazy
     * Will create a lazy (class-based) view provider
     * The view will be instantiated by the view provider from the provided class
     * The viewCached parameter controls if the view will be instantiated only once
     * or each time is requested bu yhe Navigator.
     *
     * @param placeholder the layout for MenuBar
     * @param clazz       the class to instantiate (must implement View)
     * @param cached      true to instantiated only once, false to instantiate each time
     */
    public MenuCommand(MenuPlaceholder placeholder, Class clazz, boolean cached) {
        this.placeholder = placeholder;
        this.clazz = clazz;
        this.view = null;
        this.cached = cached;
    }// end of constructor

    /**
     * Constructor - heavyweight
     * Will create a heavyweight view provider
     * The view provided here will be used
     *
     * @param placeholder the layout for MenuBar
     * @param view        the view to diplay
     */
    public MenuCommand(MenuPlaceholder placeholder, View view) {
        this.placeholder = placeholder;
        this.view = view;
        this.clazz = null;
    }// end of constructor

    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        String address = getNavigatorAddress();

        // Navigate to a specific state
        UI.getCurrent().getNavigator().navigateTo(address);

       // de-selects all the items in the menubar
        placeholder.deSelectItemsBut(selectedItem);

    }// end of method


    /**
     * @return the string used as address by the Navigator
     */
    public String getNavigatorAddress() {
        String addr;

        if (view != null) {
            addr = view.getClass().getName();
        } else {
            addr = clazz.getName();
        }// end of if/else cycle

        return addr;
    }// end of method


    public Class getClazz() {
        return clazz;
    }// end of method

    public boolean isCached() {
        return cached;
    }// end of method

    public View getView() {
        return view;
    }// end of method

}// end of class
