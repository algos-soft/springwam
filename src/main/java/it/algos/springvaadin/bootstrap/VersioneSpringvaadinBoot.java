package it.algos.springvaadin.bootstrap;

import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.log.Log;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.entity.log.LogType;
import it.algos.springvaadin.entity.preferenza.PrefEffect;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.entity.role.RoleData;
import it.algos.springvaadin.entity.stato.StatoData;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springvaadin.entity.versione.VersioneService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.service.AlgosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 12-nov-2017
 * Time: 11:34
 * Per essere sicuri che questa classe venga eseguita sempre PRIMA della sottoclasse,
 * viene invocata direttamente dalla sottoclasse stessa e NON usa l'Annotation @SpringComponent
 */
@Slf4j
public class VersioneSpringvaadinBoot extends VersioneBoot {


    private final static String PROGETTO = "springvaadin";


    @Autowired
    private RoleData roleData;


    @Autowired
    private StatoData statoData;


    @Autowired
    protected CompanyData companyData;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public VersioneSpringvaadinBoot(
            @Qualifier(Cost.TAG_VERS) AlgosService service,
            @Qualifier(Cost.TAG_COMP) AlgosService companyService,
            @Qualifier(Cost.TAG_PRE) AlgosService preferenzaService,
            @Qualifier(Cost.TAG_LOG) AlgosService logger) {
        super(service, companyService, preferenzaService, logger);
    }// end of Spring constructor


    /**
     * Esegue su chiamata della sottoclasse che implementa l'Annotation @EventListener
     * Per essere sicuri di eseguire queste regolazioni PRIMA della sottoclasse
     */
    protected void inizializza() {
        this.cronistoriaVersioniSpringvaadin();
        this.cronistoriaVersioniProgetto();

        if (AlgosApp.USE_MULTI_COMPANY) {
            companyData.updatePreferenze();
        }// end of if cycle
    }// end of method


    /**
     * Cronistoria delle versioni istallate nell'applicazione base
     */
    protected void cronistoriaVersioniSpringvaadin() {
        int k = 0;

        //--prima installazione del programma
        //--non fa nulla, solo informativo
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaVersione(
                    PROGETTO,
                    "Setup",
                    "Creazione ed installazione iniziale dell'applicazione",
                    "Senza company specifica, perché è un'operazione valida per tutte le companies");
        }// fine del blocco if


        //--creazione dei ruoli per la security
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            roleData.creaAll();
            creaVersione(
                    PROGETTO,
                    "Role",
                    "Creati i ruoli per la security");
        }// fine del blocco if


        //--creazione degli stati
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            statoData.creaAll();
            creaVersione(
                    PROGETTO,
                    "Stato",
                    "Creati alcuni stati base, usati nel popup degli indirizzi");
        }// fine del blocco if


        //--crea una nuova preferenza, globale per tutte le company
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaPreferenzaAndVersione(
                    PROGETTO,
                    Cost.KEY_USE_DEBUG,
                    PrefType.bool,
                    "Flag di debug",
                    false,
                    "Flag generale di debug (ce ne possono essere di specifici, validi solo se questo è vero)");
        }// fine del blocco if


        //--crea una nuova preferenza, globale per tutte le company
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaPreferenzaAndVersione(
                    PROGETTO,
                    Cost.KEY_DISPLAY_NEW_RECORD_ONLY,
                    PrefType.bool,
                    "Display only the new record in the grid, after successful editing (persisted).",
                    false);
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaPreferenzaAndVersione(
                    PROGETTO,
                    Cost.KEY_DISPLAY_FOOTER_INFO,
                    PrefType.bool,
                    "Visualizza nel footer copyright ed informazioni sul programma.",
                    true);
        }// fine del blocco if


        //--crea una nuova preferenza, globale per tutte le company
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaPreferenzaAndVersione(
                    PROGETTO,
                    Cost.KEY_DISPLAY_TOOLTIPS,
                    PrefType.bool,
                    "Visualizza i toolTips di aiuto nel rollover sui campi del Form (occorre riavviare).",
                    false);
        }// fine del blocco if

        //--crea una nuova preferenza, globale per tutte le company
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            creaPreferenzaAndVersione(
                    PROGETTO,
                    Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID,
                    PrefType.bool,
                    ARoleType.admin,
                    "Selezione multipla nella grid.",
                    true,
                    PrefEffect.subito);
        }// fine del blocco if

    }// end of method

    /**
     * Cronistoria delle versioni istallate nel progetto specifico
     */
    protected void cronistoriaVersioniProgetto() {
    }// end of method

}// end of class
