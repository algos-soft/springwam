package it.algos.springvaadin.lib;

import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;

import static it.algos.springvaadin.lib.LibVaadin.getUI;

/**
 * Created by gac on 18/06/17
 * .
 */
public class LibSpring {


    public static AlgosPresenterImpl getPresenter() {
        return getView().getPresenter();
    }// end of static method


    public static AlgosNavView getView() {
        return (AlgosNavView) getUI().getNavigator().getCurrentView();
    }// end of static method


}// end of static class
