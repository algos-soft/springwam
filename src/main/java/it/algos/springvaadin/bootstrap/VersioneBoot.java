package it.algos.springvaadin.bootstrap;

import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.log.Log;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.entity.log.LogType;
import it.algos.springvaadin.entity.preferenza.PrefEffect;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
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
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 21-ott-2017
 * Time: 22:06
 * <p>
 * Log delle versioni, modifiche e patch installate
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
 * <p>
 * Per essere sicuri che questa classe venga eseguita sempre PRIMA della sottoclasse,
 * viene invocata direttamente dalla sottoclasse stessa e NON usa l'Annotation @SpringComponent
 */
@Slf4j
public abstract class VersioneBoot {


    //--il service (contenente la repository) viene iniettato nel costruttore
    protected VersioneService service;


    //--il service (contenente la repository) viene iniettato nel costruttore
    protected CompanyService companyService;


    //--il service (contenente la repository) viene iniettato nel costruttore
    protected PreferenzaService preferenzaService;


    //--il service (contenente la repository) viene iniettato nel costruttore
    protected LogService logger;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public VersioneBoot(
            @Qualifier(Cost.TAG_VERS) AlgosService service,
            @Qualifier(Cost.TAG_COMP) AlgosService companyService,
            @Qualifier(Cost.TAG_PRE) AlgosService preferenzaService,
            @Qualifier(Cost.TAG_LOG) AlgosService logger) {
        this.service = (VersioneService) service;
        this.companyService = (CompanyService) companyService;
        this.preferenzaService = (PreferenzaService) preferenzaService;
        this.logger = (LogService) logger;
    }// end of Spring constructor


    /**
     * Creazione di una entity (se non trovata)
     * Log a video
     *
     * @param progetto    (obbligatorio, non unica)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     */
    protected void creaVersione(String progetto, String gruppo, String descrizione) {
        creaVersione(progetto, (Company) null, gruppo, descrizione, "");
    }// end of method


    /**
     * Creazione di una entity (se non trovata)
     * Log a video
     *
     * @param progetto    (obbligatorio, non unica)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     * @param note        descrittive (facoltative)
     */
    protected void creaVersione(String progetto, String gruppo, String descrizione, String note) {
        creaVersione(progetto, (Company) null, gruppo, descrizione, note);
    }// end of method


    /**
     * Creazione di una entity (se non trovata)
     * Log a video
     *
     * @param progetto    (obbligatorio, non unica)
     * @param company     (facoltativa)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     * @param note        descrittive (facoltative)
     */
    protected void creaVersione(String progetto, Company company, String gruppo, String descrizione, String note) {
        Versione vers = service.crea(progetto, company, gruppo, descrizione);

        if (LibText.isValid(note)) {
            vers.note = note;
            try { // prova ad eseguire il codice
                service.save(vers);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle


        //--siamo in fase di boot e NON esiste ancora la session,
        //  perciò non può prenderla in automatico durante il save standard
        //  comunque non sono sicuro che serva questo log
        if (true) {
            log.warn(gruppo, descrizione);
            Log logg = logger.newEntity(null, LibText.primaMaiuscola(LogType.versione.toString()), descrizione, null);
            logg.setCompany(company);
            try { // prova ad eseguire il codice
                logger.save(logg);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if/else cycle

    }// end of method


    /**
     * Creazione di una versione (se non trovata)
     * Creazione di una preferenza (se non trovata)
     * Log a video
     *
     * @param progetto              (obbligatorio, non unica)
     * @param codePreferenza        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param typePreferenza        di dato memorizzato (obbligatorio)
     * @param descrizionePreferenza visibile (obbligatoria)
     * @param valuePreferenza       valore della preferenza (obbligatorio)
     */
    protected Preferenza creaPreferenzaAndVersione(
            String progetto,
            String codePreferenza,
            PrefType typePreferenza,
            String descrizionePreferenza,
            Object valuePreferenza) {
        return creaPreferenzaAndVersione(
                progetto,
                (Company) null,
                codePreferenza,
                typePreferenza,
                ARoleType.developer,
                descrizionePreferenza,
                valuePreferenza,
                PrefEffect.subito,
                true,
                "");
    }// end of method


    /**
     * Creazione di una versione (se non trovata)
     * Creazione di una preferenza (se non trovata)
     * Log a video
     *
     * @param progetto              (obbligatorio, non unica)
     * @param codePreferenza        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param typePreferenza        di dato memorizzato (obbligatorio)
     * @param descrizionePreferenza visibile (obbligatoria)
     * @param valuePreferenza       valore della preferenza (obbligatorio)
     * @param notePreferenza        (facoltativo, di default vuote)
     */
    protected Preferenza creaPreferenzaAndVersione(
            String progetto,
            String codePreferenza,
            PrefType typePreferenza,
            String descrizionePreferenza,
            Object valuePreferenza,
            String notePreferenza) {
        return creaPreferenzaAndVersione(
                progetto,
                (Company) null,
                codePreferenza,
                typePreferenza,
                ARoleType.developer,
                descrizionePreferenza,
                valuePreferenza,
                PrefEffect.subito,
                true,
                notePreferenza);
    }// end of method


    /**
     * Creazione di una versione (se non trovata)
     * Creazione di una preferenza (se non trovata)
     * Log a video
     *
     * @param progetto              (obbligatorio, non unica)
     * @param codePreferenza        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param typePreferenza        di dato memorizzato (obbligatorio)
     * @param levelPreferenza       di accesso alla preferenza (obbligatorio)
     * @param descrizionePreferenza visibile (obbligatoria)
     * @param valuePreferenza       valore della preferenza (obbligatorio)
     * @param riavvioPreferenza     attivazione del programma per avere effetto (obbligatorio, di default false)
     */
    protected Preferenza creaPreferenzaAndVersione(
            String progetto,
            String codePreferenza,
            PrefType typePreferenza,
            ARoleType levelPreferenza,
            String descrizionePreferenza,
            Object valuePreferenza,
            PrefEffect riavvioPreferenza) {
        return creaPreferenzaAndVersione(
                progetto,
                (Company) null,
                codePreferenza,
                typePreferenza,
                levelPreferenza,
                descrizionePreferenza,
                valuePreferenza,
                riavvioPreferenza,
                true,
                "");
    }// end of method


        /**
          * Creazione di una versione (se non trovata)
          * Creazione di una preferenza (se non trovata)
          * Log a video
          *
          * @param progetto              (obbligatorio, non unica)
          * @param company               (facoltativa)
          * @param codePreferenza        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
          * @param typePreferenza        di dato memorizzato (obbligatorio)
          * @param levelPreferenza       di accesso alla preferenza (obbligatorio)
          * @param descrizionePreferenza visibile (obbligatoria)
          * @param valuePreferenza       valore della preferenza (obbligatorio)
          * @param riavvioPreferenza     attivazione del programma per avere effetto (obbligatorio, di default false)
          * @param replica               per ogni company (facoltativo, di default falso)
          * @param notePreferenza        (facoltativo, di default vuote)
          */
    protected Preferenza creaPreferenzaAndVersione(
            String progetto,
            Company company,
            String codePreferenza,
            PrefType typePreferenza,
            ARoleType levelPreferenza,
            String descrizionePreferenza,
            Object valuePreferenza,
            PrefEffect riavvioPreferenza,
            boolean replica,
            String notePreferenza) {
        Preferenza preferenza = null;
        Versione versione = null;
        String gruppo = "Pref";
        String descrizioneVersione = "Creazione della preferenza " + codePreferenza + " di tipo " + typePreferenza;
        String note = "Senza company specifica, perché è un valore di default \nPuò essere modificato nella singola company";

        preferenza = preferenzaService.findOrCrea(
                company,
                codePreferenza,
                typePreferenza,
                levelPreferenza,
                descrizionePreferenza,
                valuePreferenza,
                riavvioPreferenza,
                replica);
        try { // prova ad eseguire il codice
            preferenza.note = notePreferenza;
            preferenzaService.save(preferenza);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        versione = service.crea(progetto, gruppo, descrizioneVersione);
        try { // prova ad eseguire il codice
            versione.setCompany(company);
            versione.note = note;
            service.save(versione);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        logger.warn(LogType.versione.toString(), descrizioneVersione);

        return preferenza;
    }// end of method


}// end of class
