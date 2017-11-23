package it.algos.springvaadin.lib;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;

import java.io.File;

/**
 * Created by gac on 26/05/17.
 * Accessing UI, Page, Session, and Service
 */
public class LibVaadin {

    /**
     * Get the current View
     */
    public static AlgosPresenterImpl getCurrentPresenter() {
        AlgosPresenterImpl presenter = null;
        AlgosNavView view = getCurrentView();

        if (view != null) {
            presenter = view.getPresenter();
        }// end of if cycle

        return presenter;
    }// end of static method

    /**
     * Get the current View
     */
    public static AlgosNavView getCurrentView() {
        AlgosNavView algosView = null;
        View vaadinView = null;
        Navigator navigator = getNavigator();

        if (navigator != null) {
            vaadinView = navigator.getCurrentView();
            if (vaadinView instanceof AlgosNavView) {
                algosView = (AlgosNavView) vaadinView;
            }// end of if cycle
        }// end of if cycle

        return algosView;
    }// end of static method


    /**
     * Get the Navigator
     */
    public static Navigator getNavigator() {
        Navigator navigator = null;
        UI ui = getUI();

        if (ui != null) {
            navigator = ui.getNavigator();
        }// end of if cycle

        return navigator;
    }// end of static method

    /**
     * Get the UI
     */
    public static UI getUI() {
        try { // prova ad eseguire il codice
            return UI.getCurrent();
        } catch (Exception unErrore) { // intercetta l'errore
            return null;
        }// fine del blocco try-catch
    }// end of static method


    /**
     * Get the Page
     */
    public static Page getPage() {
        return Page.getCurrent();
    }// end of static method


    /**
     * Get the Session
     */
    public static VaadinSession getSession() {
        return VaadinSession.getCurrent();
    }// end of static method


    /**
     * Get the baseDir path
     */
    public static File getBaseDir() {
        File file = null;
        VaadinService service = VaadinService.getCurrent();

        if (service != null) {
            file = service.getBaseDirectory();
        }// end of if cycle

        return file;
    }// end of static method


    /**
     * Get the real path
     */
    public static String getPath() {
        String path = "";
        File baseDir = LibVaadin.getBaseDir();

        if (baseDir != null) {
            path = baseDir.getAbsolutePath();
        }// end of if cycle

        return path;
    }// end of static method


}// end of abstract class
