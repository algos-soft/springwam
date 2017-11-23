package it.algos.springvaadin.menu;

import com.vaadin.ui.MenuBar;

/**
 * Created by gac on 25/05/17
 */
public class AlgosMenuBar extends MenuBar {

    /**
     * Regolazioni scss.
     */
    public static String MENU_ABILITATO = "highlight";
    public static String MENU_DISABILITATO = "disabilitato";

    private MenuBar algosBar = new MenuBar();
//    private LoginButton loginBar;

    /**
     * Constructor senza security
     */
    public AlgosMenuBar() {
        this(false);
    }// end of constructor


    /**
     * Constructor
     */
    public AlgosMenuBar(boolean usaSecurity) {
        inizializzazione(usaSecurity);
    }// end of constructor

    /**
     * Initialization
     * <p>
     * Niente margine che c'è già nel contenitore parente <br>
     * ˙
     */
    private void inizializzazione(boolean usaSecurity) {
  //      this.setMargin(false);
  //      this.setSpacing(true);
        this.setHeight("40px");
        this.setWidth("100%");

        algosBar = createMenuBar();
    //    this.addComponent(algosBar);

//        if (usaSecurity) {
//            loginBar = createLoginMenuBar();
//            this.addComponent(loginBar);
//        }// fine del blocco if

    }// end of method

    /**
     * Menu bar specifico dell'applicazione
     * <p>
     * I singoli menu vengono aggiunti dall'applicazione specifica col metodo addMenu. <br>
     */
    private MenuBar createMenuBar() {
        MenuBar menubar = new MenuBar();
        menubar.setAutoOpen(true);

        //       if (LibSession.isDeveloper() && AlgosUI.DEBUG_GUI) {
        menubar.addStyleName("mymenubar");
        //       }// fine del blocco if


        return menubar;
    }// end of method


}// end of class
