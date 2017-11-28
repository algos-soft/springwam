package it.algos.springwam.ui;

import it.algos.springwam.entity.iscrizione.IscrizioneNavView;
import it.algos.springwam.entity.utente.UtenteNavView;
import it.algos.springwam.entity.turno.TurnoNavView;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.service.AlgosStartService;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceNavView;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.servizio.ServizioNavView;
import com.vaadin.server.VaadinRequest;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.FunzioneNavView;
import com.vaadin.annotations.Theme;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Grid;
import it.algos.springvaadin.ui.AlgosUI;
import it.algos.springwam.tabellone.TabelloneNavView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by gac on @TODAY@
 * <p>
 * UI di partenza dell'applicazione, selezionata da SpringBoot con @SpringUI()
 * Questa classe DEVE estendere AlgosUIParams
 * Questa classe DEVE prevedere l'Annotation @SpringUI()
 * All'interno dell'applicazione, @SpringUI() deve essere utilizzata in una sola classe che estende UI
 */
@Theme("wam")
@SpringUI()
@SpringViewDisplay()
@Slf4j
public class SpringwamUI extends AlgosUI {

    @Autowired
    private AlgosStartService startService;


    @Autowired
    private CroceService croceService;

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
//		footer.setAppMessage("Springwam 1.0");
//
//        String message = "Algos® ";
//        String companyCode = LibSession.getCompany() != null ? LibSession.getCompany().getSigla() : "";
//        if (AlgosApp.USE_MULTI_COMPANY && LibText.isValid(companyCode)) {
//            message += " - " + companyCode;
//        }// end of if cycle
        log.info("Versione dell'applicazione: SpringVaadin 1.0");

        super.printBefore(InterfacciaUtente.specifica);
        this.specificFixAndPrint();
        super.printAfter(InterfacciaUtente.specifica);

        super.init(request);
        footer.setAppMessage("SpringWam 1.0");
    }// end of method


    /**
     * Regola alcuni flag specifici dell'applicazione che riguardano l'interfaccia video
     * Can be overwritten on local xxxUI.specificFixAndPrint() method of subclass
     * Stampa a video (productionMode) i valori per controllo
     */
    @SuppressWarnings("all")
    private void specificFixAndPrint() {
        super.gridSelectionMode = Grid.SelectionMode.MULTI;
        System.out.println("AlgosUIParams.gridSelectionMode: " + super.gridSelectionMode);

        super.displayToolTips = true;
        System.out.println("AlgosUIParams.displayToolTips: " + super.displayToolTips);

        super.usaSeparateFormDialog = false;
        System.out.println("AlgosUIParams.usaSeparateFormDialog: " + super.usaSeparateFormDialog);

        super.usaBottoniColorati = true;
        System.out.println("AlgosUIParams.usaBottoniColorati: " + super.usaBottoniColorati);

        this.usaDialoghiVerbosi = true;
        log.info("AlgosUIParams.usaDialoghiVerbosi: " + this.usaDialoghiVerbosi);
    }// end of method


    /**
     * Controlla la company selezionata
     * Scorporato per permettere di sovrascriverlo nelle sottoclassi
     * Nello specifico deve convertire Company in Croce
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    @Override
    protected void checkCompany(VaadinRequest request) {
        Croce croce = null;
        String siglaCompany = startService.getSiglaCompany(request);

        if (LibText.isValid(siglaCompany)) {
            croce = croceService.findByCode(siglaCompany);
        }// end of if cycle

        LibSession.setCompany(croce);
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
		menuLayout.addView(IscrizioneNavView.class);
        menuLayout.removeView(Cost.TAG_PER);
        menuLayout.addView(UtenteNavView.class);
        menuLayout.removeView(Cost.TAG_COMP);
        menuLayout.addView(Croce.class, CroceNavView.class);
        menuLayout.addView(FunzioneNavView.class);
        menuLayout.addView(ServizioNavView.class);
        menuLayout.addView(TurnoNavView.class);
        menuLayout.addView(TabelloneNavView.class);
    }// end of method

    /**
     * Lancio della vista iniziale
     * Chiamato DOPO aver finito di costruire il MenuLayout e la AlgosUI
     * Deve (DEVE) essere sovrascritto dalla sottoclasse
     */
    @Override
    protected void startVistaIniziale() {
        navigateTo(AppCost.TAG_FUN);
    }// end of method

}// end of class
