package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.bootstrap.AlgosBoot;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceData;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.funzione.FunzioneData;
import it.algos.springwam.entity.servizio.ServizioData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Created by gac on @TODAY@
 * .
 */
@SpringComponent
@Slf4j
public class SpringwamBoot extends AlgosBoot {

    @Autowired
    protected CroceData croceData;

    @Autowired
    protected CroceService croceService;

    @Autowired
    protected FunzioneData funzioneData;

    @Autowired
    protected ServizioData servizioData;

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
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.inizializzaValoriDefault();
    }// end of method


    /**
     * Prima vengono regolati i valori standard di default
     * Una eventuale sottoclasse, dopo aver controllato che il metodo inizializzaValoriDefault()
     * sia stato eseguito una ed una sola volta, può modificare le impostazioni/regolazioni di base
     */
    @Override
    protected void inizializzaValoriDefault() {

        if (super.classeAlgosBootAncoraDaEseguire) {
            super.inizializzaValoriDefault();
        }// end of if cycle

        this.printBefore(Boot.specifico);
        this.specificFixAndPrint();
        this.printAfter(Boot.specifico);
    }// end of method

    /**
     * Regola alcuni flag dell'applicazione
     * Valori specifici che modificano quelli di default della supeclasse
     * Stampa a video (productionMode) i valori per controllo
     */
    private void specificFixAndPrint() {
        AlgosApp.USE_DEBUG = false;
        log.debug("AlgosApp.USE_DEBUG: " + AlgosApp.USE_DEBUG);

        AlgosApp.USE_SECURITY = true;
        log.debug("AlgosApp.USE_SECURITY: " + AlgosApp.USE_SECURITY);

        AlgosApp.USE_MULTI_COMPANY = true;
        log.debug("AlgosApp.USE_MULTI_COMPANY: " + AlgosApp.USE_MULTI_COMPANY);

        AlgosApp.USE_VERS = true;
        log.debug("AlgosApp.USE_VERS: " + AlgosApp.USE_VERS);

        AlgosApp.USE_LOG = true;
        log.debug("AlgosApp.USE_LOG: " + AlgosApp.USE_LOG);
        System.out.println("AlgosApp.USE_LOG: " + AlgosApp.USE_LOG);
    }// end of method

}// end of class
