package it.algos.springvaadin.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by gac on 12/06/17
 * <p>
 * Superclasse astratta della UI di partenza dell'applicazione, selezionata da SpringBoot
 * Questa classe NON prevede l'Annotation @SpringViewDisplay()
 * Mantiene e regola i parametri che regolano la gestione della view
 * Lascia che la sottoclasse AlgosUI si occupi SOLO della parte grafica
 */
@Slf4j
public abstract class AUIParams extends UI {


//    @Autowired
//    protected AlgosStartService algosStartService;


//    /**
//     * Flag per un main layout verticale piuttosto che orizzontale.
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaRootLayoutVerticale;
//
//    /**
//     * Flag di utilizzo di itemMenuHome (il primo da sinistra di firstMenuBar)
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaItemMenuHome;
//
//    /**
//     * Flag di utilizzo di itemMenuHelp (l'ultimo a destra di firstMenuBar)
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaItemMenuHelp;
//
//
//    /**
//     * Flag.
//     * Display only the new record in the table, after successful editing (persisted).<br>
//     * Not final flag<br>
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean displayNewRecordOnly;
//
//
//    /**
//     * Flag.
//     * Display tooltips on rollover the field
//     * Not final flag
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean displayToolTips;
//
//
//    /**
//     * Flag.
//     * Null selection allowed in combobox.
//     * Not final
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean comboBoxNullSelectionAllowed;
//
//
//    /**
//     * Flag.
//     * Multi selection or Single selection or None in Grid.
//     * Not final
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public Grid.SelectionMode gridSelectionMode;
//
//
//    /**
//     * Flag.
//     * Form in un Dialog modale.
//     * Se è falso, visualizza i dati del Form nel viewPlaceholder, senza aprire un Dialog
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaSeparateFormDialog;
//
//
//    /**
//     * Flag.
//     * Dialogo di conferma prima della delete
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean chiedeConfermaPrimaDiCancellare;
//
//
//    /**
//     * Flag.
//     * Utilizzo di bottoni con colore indicativo della 'priorità'
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaBottoniColorati;
//
//
//    /**
//     * Flag.
//     * Utilizzo di (alcuni) avvisi col testo in rosso
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaAvvisiColorati;
//
//
//    /**
//     * Flag.
//     * Utilizzo di bottoni sempre con la prima lettera maiuscola
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaBottoniPrimaMaiuscola;
//
//
//    /**
//     * Flag.
//     * Utilizzo di dialoghi 'verbosi' (con maggiori informazioni)
//     * Regolato nel metodo genericFixAndPrint() di questa classe
//     * Può essere modificato in @PostConstruct.inizia() della sottoclasse concreta
//     */
//    public boolean usaDialoghiVerbosi;


    /**
     * Metodo invocato DOPO il costruttore e PRIMA del metodo init(VaadinRequest request)
     * Serve per regolare i parametri utilizzati nel metodo init(VaadinRequest request)
     * Stampa a video (productionMode) i valori per controllo
     */
    @PostConstruct
    protected void inizia() {
        log.info("Algos - Inizio sessione. Primo punto di ingresso dopo la chiamata del browser @PostConstruct AUIParams.inizia()");
        this.printBefore(AUIParams.InterfacciaUtente.generica);
        this.genericFixAndPrint();
        this.printAfter(AUIParams.InterfacciaUtente.generica);
    }// end of method

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
        //--Legge eventuali parametri passati nella request
        checkParams(request);

        //--Legge i cookies dalla request
        checkCookies(request);

        //--Controlla il login della security
        checkSecurity(request);

        //--Controlla la company selezionata
        checkCompany(request);
    }// end of method


    /**
     * Legge eventuali parametri passati nella request
     * Scorporato per permettere di sovrascriverlo nelle sottoclassi
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    protected void checkParams(VaadinRequest request) {
        //@todo RIMETTERE
//        if (AlgosApp.USE_CHECK_PARAMS) {
//            algosStartService.checkParams(request);
//        }// end of if cycle
    }// end of method


    /**
     * Legge i cookies dalla request
     * Scorporato per permettere di sovrascriverlo nelle sottoclassi
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    protected void checkCookies(VaadinRequest request) {
        //@todo RIMETTERE
//        if (AlgosApp.USE_CHECK_COOKIES) {
//            algosStartService.checkCookies(request);
//        }// end of if cycle
    }// end of method


    /**
     * Controlla il login della security
     * Scorporato per permettere di sovrascriverlo nelle sottoclassi
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    protected void checkSecurity(VaadinRequest request) {
    //@todo RIMETTERE
//        if (AlgosApp.USE_SECURITY) {
//            algosStartService.checkSecurity(request);
//        } else {
////            LibSession.setDeveloper(true);
//        }// end of if/else cycle
    }// end of method


    /**
     * Controlla la company selezionata
     * Scorporato per permettere di sovrascriverlo nelle sottoclassi
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    protected void checkCompany(VaadinRequest request) {
        //@todo RIMETTERE
//        if (AlgosApp.USE_MULTI_COMPANY) {
//            algosStartService.checkCompany(request);
//        }// end of if cycle
    }// end of method

    /**
     * Stampa a video (productionMode) una info PRIMA dei valori
     */
    protected void printBefore(AUIParams.InterfacciaUtente ui) {
        Class claz = this.getClass();
        String className = claz.getSimpleName();

        switch (ui) {
            case generica:
                log.info("Application received the server requests - Dopo la chiamata del browser - start generic initializing code nella classe AUIParams.printBefore()");
                break;
            case specifica:
                log.info("Start specific initializing code nella classe " + className);
                break;
        } // fine del blocco switch
    }// end of method

    /**
     * Regola alcuni flag generici dell'applicazione che riguardano l'annotation video
     * Valori di default
     * Can be overwritten on local xxxUI.specificFixAndPrint() method of subclass
     * Stampa a video (productionMode) i valori per controllo
     */
    protected void genericFixAndPrint() {
//        this.usaRootLayoutVerticale = true;
//        log.info("AUIParams.usaRootLayoutVerticale: " + this.usaRootLayoutVerticale);
//
//        this.usaItemMenuHome = true;
//        log.info("AUIParams.usaItemMenuHome: " + this.usaItemMenuHome);
//
//        this.usaItemMenuHelp = false;
//        log.info("AUIParams.usaItemMenuHelp: " + this.usaItemMenuHelp);
//
//        this.displayNewRecordOnly = false;
//        log.info("AUIParams.displayNewRecordOnly: " + this.displayNewRecordOnly);
//
//        this.displayToolTips = false;
//        log.info("AUIParams.displayToolTips: " + this.displayToolTips);
//
//        this.comboBoxNullSelectionAllowed = false;
//        log.info("AUIParams.comboBoxNullSelectionAllowed: " + this.comboBoxNullSelectionAllowed);
//
//        this.gridSelectionMode = Grid.SelectionMode.SINGLE;
//        log.info("AUIParams.gridSelectionMode: " + this.gridSelectionMode);
//
//        this.usaSeparateFormDialog = false;
//        log.info("AUIParams.usaSeparateFormDialog: " + this.usaSeparateFormDialog);
//
//        this.chiedeConfermaPrimaDiCancellare = true;
//        log.info("AUIParams.chiedeConfermaPrimaDiCancellare: " + this.chiedeConfermaPrimaDiCancellare);
//
//        this.usaBottoniColorati = false;
//        log.info("AUIParams.usaBottoniColorati: " + this.usaBottoniColorati);
//
//        this.usaAvvisiColorati = true;
//        log.info("AUIParams.usaAvvisiColorati: " + this.usaAvvisiColorati);
//
//        this.usaBottoniPrimaMaiuscola = true;
//        log.info("AUIParams.usaBottoniPrimaMaiuscola: " + this.usaBottoniPrimaMaiuscola);
//
//        this.usaDialoghiVerbosi = false;
//        log.info("AUIParams.usaDialoghiVerbosi: " + this.usaDialoghiVerbosi);
//
//        log.info("All this params can be found in LibParams");

    }// end of method

    /**
     * Stampa a video (productionMode) una info DOPO i valori
     */
    protected void printAfter(AUIParams.InterfacciaUtente ui) {
        switch (ui) {
            case generica:
                log.info("End generic initializing code");
                break;
            case specifica:
                log.info("End specific initializing code");
                break;
        } // fine del blocco switch
    }// end of method


    protected enum InterfacciaUtente {
        generica, specifica
    }// end of internal enumeration

}// end of class
