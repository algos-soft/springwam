package it.algos.springvaadin.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.app.AlgosApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Created by gac on 10/06/17
 * Regolazione di flag standard del framework
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
@SpringComponent
@Slf4j
public class AlgosBoot {

    /**
     * Flag per assicurarsi che questa classe di inizializzazione venga eseguita PRIMA di un'eventuale sottoclasse
     * Questo dovuto al fatto che l'ordine di 'chiamata' delle classi che usano l'Annotation @EventListener,
     * non è controllabile
     */
    protected boolean classeAlgosBootAncoraDaEseguire = true;

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
        if (this.classeAlgosBootAncoraDaEseguire) {
            this.inizializzaValoriDefault();
        }// end of if cycle
    }// end of method

    /**
     * Prima vengono regolati i valori standard di default
     * Una eventuale sottoclasse, dopo aver controllato che il metodo inizializzaValoriDefault()
     * sia stato eseguito una ed una sola volta, può modificare le impostazioni/regolazioni di base
     */
    protected void inizializzaValoriDefault() {
        this.printBefore(Boot.generico);
        this.genericFixAndPrint();
        this.printAfter(Boot.generico);
        this.classeAlgosBootAncoraDaEseguire = false;
    }// end of method

    /**
     * Regola alcuni flag dell'applicazione
     * Valori di default
     * Can be overwritten on local xxxBoot.specificFixAndPrint() method
     * Stampa a video (productionMode) i valori per controllo
     */
    private void genericFixAndPrint() {
        AlgosApp.USE_DEBUG = false;
        log.info("AlgosApp.USE_DEBUG: " + AlgosApp.USE_DEBUG);

        AlgosApp.USE_MULTI_COMPANY = false;
        log.info("AlgosApp.USE_MULTI_COMPANY: " + AlgosApp.USE_MULTI_COMPANY);

        AlgosApp.USE_SECURITY = false;
        log.info("AlgosApp.USE_SECURITY: " + AlgosApp.USE_SECURITY);

        AlgosApp.USE_LOGIN_OBBLIGATORIO = false;
        log.info("AlgosApp.USE_LOGIN_OBBLIGATORIO: " + AlgosApp.USE_LOGIN_OBBLIGATORIO);

        AlgosApp.USE_CHECK_PARAMS = true;
        log.info("AlgosApp.USE_CHECK_PARAMS: " + AlgosApp.USE_CHECK_PARAMS);

        AlgosApp.USE_CHECK_COOKIES = true;
        log.info("AlgosApp.USE_CHECK_COOKIES: " + AlgosApp.USE_CHECK_COOKIES);

        AlgosApp.USE_VERS = true;
        log.info("AlgosApp.USE_VERS: " + AlgosApp.USE_VERS);

        AlgosApp.USE_LOG = false;
        log.info("AlgosApp.USE_LOG: " + AlgosApp.USE_LOG);

        AlgosApp.USE_PREF = false;
        log.info("AlgosApp.USE_PREF: " + AlgosApp.USE_PREF);

//        AlgosApp.DISPLAY_NEW_RECORD_ONLY = true;
//        System.out.println("AlgosApp.DISPLAY_NEW_RECORD_ONLY: " + AlgosApp.DISPLAY_NEW_RECORD_ONLY);
//
//        AlgosApp.DISPLAY_TOOLTIPS = false;
//        System.out.println("AlgosApp.DISPLAY_TOOLTIPS: " + AlgosApp.DISPLAY_TOOLTIPS);
//
//        AlgosApp.COMBO_BOX_NULL_SELECTION_ALLOWED = false;
//        System.out.println("AlgosApp.COMBO_BOX_NULL_SELECTION_ALLOWED: " + AlgosApp.COMBO_BOX_NULL_SELECTION_ALLOWED);
    }// end of method


    /**
     * Stampa a video (productionMode) una info PRIMA dei valori
     */
    protected void printBefore(Boot boot) {
        switch (boot) {
            case generico:
                log.info("Application is coming up and is ready to server requests - PRIMA della chiamata del browser - start generic bootstrap code nella classe AlgosBoot");
                break;
            case specifico:
                log.info("Start specific bootstrap code nella classe xxxBoot");
                break;
        } // fine del blocco switch
    }// end of method

    /**
     * Stampa a video (productionMode) una info DOPO i valori
     */
    protected void printAfter(Boot boot) {
        switch (boot) {
            case generico:
                log.info("All this params can be found also in LibParams - End generic bootstrap code");
                break;
            case specifico:
                log.info("End specific bootstrap code");
                break;
        } // fine del blocco switch
    }// end of method

    protected enum Boot {
        generico, specifico
    }// end of internal enumeration

}// end of class