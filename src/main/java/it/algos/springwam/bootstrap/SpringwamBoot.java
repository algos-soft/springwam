package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.bootstrap.ABoot;
import it.algos.springwam.migration.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Created by gac on 12/06/17.
 * Regolazione di flag specifici dell'applicazione
 * <p>
 * Setup non-UI logic here
 * This class will be executed on container startup when the application is ready to server requests.
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat)
 * Eseguita quindi ad ogni avvio/attivazione del server e NON ad ogni sessione
 * <p>
 * In order to create a class that acts like a bootstrap for the application,
 * that class needs to implements the @EventListener annotation
 * Any class that implements the @EventListener annotation will be executed before the application is up
 * and its onApplicationEvent method will be called
 * <p>
 * ATTENZIONE: in questa fase NON sono disponibili le Librerie e le classi che dipendono dalla UI e dalla Session
 */
@Slf4j
@SpringComponent
public class SpringwamBoot extends ABoot {


    @Autowired
    protected CroceData croce;


    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public UtenteData utente;


    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public MigrationService migration;


    /**
     * Running logic after the Spring context has been initialized
     * Any class that use this @EventListener annotation,
     * will be executed before the application is up and its onApplicationEvent method will be called
     * <p>
     * Viene normalmente creata una sottoclasse per l'applicazione specifica:
     * - per regolare eventualmente alcuni flag in maniera non standard
     * - per lanciare eventualmente gli schedulers in background
     * - per costruire e regolare eventualmente una versione demo
     * - per controllare eventualmente l'esistenza di utenti abilitati all'accesso
     * <p>
     * Stampa a video (productionMode) i valori per controllo
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        super.inizializzaValoriDefault();
        this.iniziaData();
        this.inizializzaValori();
    }// end of method


    /**
     * Inizializzazione dei dati standard di alcune collections sul DB
     */
    protected void iniziaData() {
        super.iniziaDataStandard();
        migration.importAllCrociSetup();
//        migration.importAllFunzioniSetup();
//        migration.importAllServiziSetup();
//        this.company.findOrCrea();
//        this.user.findOrCrea();
        croce.findOrCrea();
        utente.findOrCrea();
    }// end of method


    /**
     * Prima vengono regolati i valori standard di default
     * Una eventuale sottoclasse, dopo aver controllato che il metodo inizializzaValoriDefault()
     * sia stato eseguito una ed una sola volta, può modificare le impostazioni/regolazioni di base
     */
//    @Override
    protected void inizializzaValori() {
//        if (super.classeAlgosBootAncoraDaEseguire) {
//            super.inizializzaValoriDefault();
//        }// end of if cycle

        this.printBefore(Boot.specifico);
        this.specificFixAndPrint();
        this.printAfter(Boot.specifico);
    }// end of method


    /**
     * Regola alcuni flag dell'applicazione
     * Valori specifici
     * Stampa a video (productionMode) i valori per controllo
     */
    protected void specificFixAndPrint() {
        AlgosApp.SETUP_TIME = false;
        log.info("AlgosApp.SETUP_TIME: " + AlgosApp.SETUP_TIME);

        AlgosApp.USE_DEBUG = false;
        log.debug("AlgosApp.USE_DEBUG: " + AlgosApp.USE_DEBUG);

        AlgosApp.USE_SECURITY = true;//@todo RIMETTERE a TRUE
        log.debug("AlgosApp.USE_SECURITY: " + AlgosApp.USE_SECURITY);

        AlgosApp.USE_MULTI_COMPANY = true;
        log.debug("AlgosApp.USE_MULTI_COMPANY: " + AlgosApp.USE_MULTI_COMPANY);

        AlgosApp.USE_VERS = false;//@todo RIMETTERE a TRUE;
        log.debug("AlgosApp.USE_VERS: " + AlgosApp.USE_VERS);

        AlgosApp.USE_LOG = false;//@todo RIMETTERE a TRUE;
        log.debug("AlgosApp.USE_LOG: " + AlgosApp.USE_LOG);
    }// end of method

}// end of class
