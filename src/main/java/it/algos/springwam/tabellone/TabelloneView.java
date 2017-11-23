package it.algos.springwam.tabellone;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.MenuBar;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.ui.AlgosUI;
import it.algos.springvaadin.view.AlgosViewImpl;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.ui.SpringwamUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 08:55
 */
@SpringComponent
@Qualifier(AppCost.TAG_TAB)
public class TabelloneView extends AlgosViewImpl {

    @Autowired
    private TabelloneMenuLayout tabMenuLayout;


    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public TabelloneView(@Qualifier(AppCost.TAG_TAB) AlgosList list, @Qualifier(AppCost.TAG_TAB) AlgosForm form) {
        super(list, form);
    }// end of Spring constructor


    /**
     * Opzione per personalizzare il menu
     * Sovrascritto
     */
    @Override
    protected void fixMenu() {
        AlgosUI algosUI = getUI();

        if (algosUI != null) {
            algosUI.menuPlaceholder.removeAllComponents();
            algosUI.menuPlaceholder.addComponent(tabMenuLayout);
        }// end of if cycle
    }// end of method


}// end of class
