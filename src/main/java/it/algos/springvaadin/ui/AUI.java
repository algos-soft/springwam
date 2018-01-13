package it.algos.springvaadin.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.footer.AFooter;
import it.algos.springvaadin.lib.ACost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 30/05/17.
 * <p>
 * Superclasse astratta della UI di partenza dell'applicazione, selezionata da SpringBoot
 * Questa classe DEVE prevedere l'Annotation @SpringViewDisplay()
 * <p>
 * La @SpringViewDisplay() crea un SpringNavigator con SpringViewProvider con tutte le view indirizzabili
 * <p>
 * Delega alla superclasse astratta la regolazione dei parametri di gestione della parte grafica
 * Delega ad un service la business logic iniziale (request parameter, cookie, security, developper)
 * Costruisce un layout standard (modificabile nello specifico per la gioia di Alex)
 * Delega ad altre classi l'implementazione effettiva degli specifici layout
 */
//@SpringViewDisplay()
@Slf4j
public abstract class AUI extends AUIViews implements ViewDisplay {

    //--versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--crea la UI di base, un VerticalLayout
    protected VerticalLayout root;

    //--A placeholder component which a single module can put is own menubar
    public VerticalLayout menuPlaceholder;

//    //--A placeholder component which a SpringNavigator can populate with different views
//    @Autowired
//    protected ViewPlaceholder viewPlaceholder;

    //--A placeholder for a footer component
    @Autowired
    public AFooter footer;

//    @Autowired
//    public PreferenzaService pref;


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


//        //--Controlla (se MultiUtenza) che sia stata selezionata una company valida
//        //--Crea la User Interface
//        if (AlgosApp.USE_MULTI_COMPANY) {
//            if (LibSession.isCompanyValida()) {
//                this.creazioneInizialeUI();
//            } else {
//                if (LibSession.isDeveloper()) {
//                    if (algosStartService.getSiglaCompany(request).equals("")) {
//                        LibAvviso.warn("Manca una company di riferimento");
//                    } else {
//                        LibAvviso.warn("La company indicata non esiste");
//                    }// end of if/else cycle
//                    this.creazioneInizialeUI();
//                } else {
//                    if (algosStartService.getSiglaCompany(request).equals("")) {
//                        LibAvviso.error("Manca una company di riferimento");
//                    } else {
//                        LibAvviso.error("La company indicata non esiste");
//                    }// end of if/else cycle
//                }// end of if/else cycle
//            }// end of if/else cycle
//        } else {
//            this.creazioneInizialeUI();
//        }// end of if/else cycle

        this.creazioneInizialeUI();
    }// end of method

    /**
     * Crea la UI di base (User Interface) iniziale dell'applicazione, un VerticalLayout
     * Layout standard composto da:
     * Top      - una barra composita di menu e login
     * Body     - un placeholder per il portale della tavola/modulo
     * Footer   - un striscia per eventuali informazioni (Algo, copyright, ecc)
     * Tutti i 3 componenti vengono inseriti a livello di root nel layout verticale
     * <p>
     * Può essere sovrascritto per gestire la UI in maniera completamente diversa
     */
    protected void creazioneInizialeUI() {
        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(new MarginInfo(true, false, false, true));

//        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
//            root.addStyleName("pinkBg");
//        } else {
//            root.addStyleName("colorebase");
//        }// end of if/else cycle

        if (ACost.DEBUG) {// @TODO costante provvisoria da sostituire con preferenzeService
            root.addStyleName("pinkBg");
        }// end of if cycle

        this.setContent(root);
        startVistaIniziale();
    }// end of method


    /**
     * Costruisce il placeholder e visualizza i menu
     * Visualizza il placeholder (già esistente) che contiene la view
     * Visualizza il footer (già esistente)
     */
    protected void restart() {
        root.removeAllComponents();

        //--Costruisce il placeholder e visualizza i menu
        menuPlaceholder = creaTop();
        root.addComponent(menuPlaceholder);

//        //--Visualizza il placeholder (già esistente) che contiene la view
//        root.addComponentsAndExpand(viewPlaceholder);
//
//        //--Visualizza il footer (già esistente)
//        root.addComponent(footer);
//
//        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
//            menuLayout.addStyleName("yellowBg");
//            viewPlaceholder.addStyleName("yellowBg");
//            footer.addStyleName("yellowBg");
//        } else {
//            menuLayout.addStyleName("colorebase");
//            viewPlaceholder.addStyleName("colorebase");
//            footer.addStyleName("colorebase");
//        }// end of if/else cycle

    }// end of method

    /**
     * Top  - una barra composita di menu e login
     *
     * @return layout - normalmente un VerticalLayout
     */
    protected VerticalLayout creaTop() {
        VerticalLayout menuPlaceholder = new VerticalLayout();

        menuPlaceholder.setMargin(false);
        menuPlaceholder.addComponent(menuLayout);
        menuLayout.start();

        return menuPlaceholder;
    }// end of method


    /**
     * Punto di ritorno da getNavigator().navigateTo(viewName)
     * Visualizza la view nel placeholder
     */
    public void showView(View view) {
        View navView = view;

//        if (view instanceof AlgosNavView) {
//            navView = ((AlgosNavView) view).getLinkedView();
//        }// end of if cycle
//
//        if (navView != null) {
//            if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
//                if (navView instanceof AlgosViewImpl) {
//                    ((AlgosViewImpl) navView).setMargin(false);
//                    ((AlgosViewImpl) navView).addStyleName("greenBg");
//                }// end of if cycle
//            }// end of if cycle
//
//            restart();
//
//            viewPlaceholder.removeAllComponents();
//            viewPlaceholder.addComponent((Component) navView);
//        } else {
//            root.removeAllComponents();
//            if (view instanceof AlgosNavView) {
//                root.addComponent((AlgosNavView) view);
//            }// end of if cycle
//        }// end of if/else cycle

        if (root!=null) {
            root.removeAllComponents();
            root.addComponentsAndExpand((Component) view);
            root.addComponent(footer);
        }// end of if cycle

    }// end of method


}// end of UI class