package it.algos.springvaadin.menu;

import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 10-feb-2018
 * Time: 12:59
 */
@Slf4j
public abstract class AMenu extends VerticalLayout implements IAMenu {

    /**
     * Avvia la menubar, dopo aver aggiunto tutte le viste
     */
    @Override
    public void start() {
    }// end of method

    /**
     * Componente concreto di questa interfaccia
     */
    public AMenu getMenu() {
        return this;
    }// end of method


}// end of class
