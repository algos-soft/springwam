package it.algos.springvaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.company.CompanyList;
import it.algos.springvaadin.entity.role.RoleForm;
import it.algos.springvaadin.entity.role.RoleList;
import it.algos.springvaadin.entity.user.UserList;
import it.algos.springvaadin.home.AHomeView;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.view.IAView;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by gac on 12/06/17
 * <p>
 * Superclasse astratta della UI di partenza dell'applicazione, selezionata da SpringBoot
 * Questa classe NON prevede l'Annotation @SpringViewDisplay()
 * Mantiene i metodi di nuovo ed accesso delle views
 * Lascia che la sottoclasse AlgosUI si occupi SOLO della parte grafica
 */
public abstract class AUIViews extends AUIParams {


    /**
     * Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * A seconda del layout può essere posizionato in alto, oppure a sinistra
     */
    @Autowired
    protected MenuLayout menuLayout;

    /**
     * Initializes this UI.
     * This method is intended to build the view and configure non-component functionality.
     * Performing the initialization in a constructor is not suggested as the state of the UI
     * is not properly set up when the constructor is invoked.
     * <p>
     * The {@link VaadinRequest} can be used to get information about the request that caused this UI to be created.
     * </p>
     * Se viene sovrascritto dalla sottoclasse, deve (DEVE) richiamare anche il metodo della superclasse
     * di norma DOPO aver effettuato alcune regolazioni <br>
     * Nella sottoclasse specifica viene eventualmente regolato il nome del modulo di partenza <br>
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void init(VaadinRequest request) {
        super.init(request);

        //--pulisce la menubar
        menuLayout.reset();

        //--Crea i menu per la gestione delle SpringView (standard e specifiche)
        this.addAllViste();

        //--avvia la menubar, dopo aver aggiunto tutte le viste
        menuLayout.start();
    }// end of method

    /**
     * Aggiunge tutte le viste (SpringView) standard e specifiche
     */
    protected void addAllViste() {
//        //--l'eventuale menu Home è sempre il primo
//        if (usaItemMenuHome) {
        menuLayout.addView(AHomeView.class);
//        }// end of if cycle

        this.addVisteStandard();
        this.addVisteSpecifiche();

//        //--l'eventuale menu Help è sempre l'ultimo
//        if (usaItemMenuHelp) {
//            menuLayout.addView(HelpNavView.class);
//        }// end of if cycle
    }// end of method


    /**
     * Aggiunge le viste (moduli) standard
     * Alcuni moduli sono specifici di un collegamento come programmatore
     * Alcuni moduli sono già definiti per tutte le applicazioni (LogMod, VersMod, PrefMod)
     * Vengono usati come da relativo flag: AlgosApp.USE_LOG, AlgosApp.USE_VERS, AlgosApp.USE_PREF
     */
    protected void addVisteStandard() {
        if (AlgosApp.USE_SECURITY) {
            menuLayout.addView(RoleList.class);
            menuLayout.addView(UserList.class);
        }// end of if cycle

//        if (LibParams.useVers()) {
//            menuLayout.addView(Versione.class, VersioneNavView.class);
//        }// end of if cycle
//        menuLayout.addView(Preferenza.class, PreferenzaNavView.class);
//        menuLayout.addView(Stato.class, StatoNavView.class);
//        menuLayout.addView(Indirizzo.class, IndirizzoNavView.class);
//        menuLayout.addView(Persona.class, PersonaNavView.class);
//        if (LibParams.useLog()) {
//            menuLayout.addView(Log.class, LogNavView.class);
//        }// end of if cycle
//        if (LibParams.useMultiCompany()) {
//            menuLayout.addView(Company.class, CompanyNavView.class);
//        }// end of if cycle
    }// end of method

    /**
     * Creazione delle viste (moduli) specifiche dell'applicazione.
     * La superclasse AlgosUIParams crea (flag true/false) le viste (moduli) usate da tutte le applicazioni
     * I flag si regolano in @PostConstruct:init()
     * <p>
     * Aggiunge al menu generale, le viste (moduli) disponibili alla partenza dell'applicazione
     * Ogni modulo può eventualmente modificare il proprio menu
     * <p>
     * Deve (DEVE) essere sovrascritto dalla sottoclasse
     * Chiama il metodo  addView(...) della superclasse per ogni vista (modulo)
     * La vista viene aggiunta alla barra di menu principale (di partenza)
     * La vista viene aggiunta allo SpringViewProvider usato da SpringNavigator
     */
    protected void addVisteSpecifiche() {
    }// end of method


    /**
     * Lancio della vista iniziale
     * Chiamato DOPO aver finito di costruire il MenuLayout e la AlgosUI
     * Deve (DEVE) essere sovrascritto dalla sottoclasse
     */
    protected void startVistaIniziale() {
    }// end of method


    /**
     * Adds a lazy view to the MenuBar
     *
     * @param viewClass the view class to instantiate
     */
    protected void addView(Class<? extends IAView> viewClass) {
        menuLayout.addView(viewClass);
    }// end of method


    /**
     * Mostra una view
     *
     * @param viewName the view to showAll
     */
    public void navigateTo(String viewName) {
        getNavigator().navigateTo(viewName);
    }// end of method

}// end of class
