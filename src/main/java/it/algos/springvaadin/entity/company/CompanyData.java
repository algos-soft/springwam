package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaService;
import it.algos.springvaadin.entity.preferenza.PrefEffect;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.login.ARoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Company demo
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat) <br>
 * Eseguita quindi ad ogni avvio/attivazione del server e NON ad ogni sessione <br>
 */
@SpringComponent
@Slf4j
public class CompanyData {


    @Autowired
    @Qualifier(Cost.TAG_COMP)
    private CompanyService service;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private PreferenzaService preferenzaService;

    /**
     * Creazione di una collezione
     */
    public void creaAll() {
        if (nessunRecordEsistente()) {
            creaCompanyDemo();
            creaCompanyTest();
        } else {
            log.info("La collezione di company è presente");
        }// end of if/else cycle
    }// end of method


    /**
     * Controlla se la collezione esiste già
     */
    private boolean nessunRecordEsistente() {
        return service.count() == 0;
    }// end of method


    /**
     * Crea una company
     */
    public Company creaCompanyDemo() {
        Company company = creaAndLog(
                "demo",
                "Algos s.r.l.",
                personaService.newEntity("Marco", "Beretta"),
                "",
                "mariorossi@win.com",
                indirizzoService.newEntity("via Procaccini, 37", "Milano", "20131", (Stato) null));
        return company;
    }// end of method


    /**
     * Crea una company
     */
    public Company creaCompanyTest() {
        Company company = creaAndLog(
                "test",
                "Associazione Volontaria di Misericordia",
                personaService.newEntity("Marcello", "Tamburini"),
                "",
                "presidente@associazioneroverasco.it",
                indirizzoService.newEntity("piazza Libertà", "Roverasco", "35117", (Stato) null));

        return company;
    }// end of method


    /**
     * Controlla che le company abbiano tutte le preferenze specifiche
     * Se non le hanno, le crea
     * Se in un nuovo update del programma, si aggiungono delle preferenze,
     * queste vengono create anche per tutte le company (se hanno il flag 'replica' attivo)
     */
    public void updatePreferenze() {
        updatePreferenze(service.findAllAll());
    }// end of method


    /**
     * Controlla che le company abbiano tutte le preferenze specifiche
     * Se non le hanno, le crea
     * Se in un nuovo update del programma, si aggiungono delle preferenze,
     * queste vengono create anche per tutte le company (se hanno il flag 'replica' attivo)
     */
    public void updatePreferenze(List<? extends Company> listaComp) {
        if (listaComp != null && listaComp.size() > 0) {
            for (Company company : listaComp) {
                updatePreferenze(company);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Controlla che la company indicata abbia tutte le preferenze specifiche col flag 'replica' attivo
     * Se non le ha, le crea
     * Se in un nuovo update del programma, si aggiungono delle preferenze, queste vengono create anche per la company
     */
    public void updatePreferenze(Company company) {
        List<Preferenza> listaPref = preferenzaService.findAllSenzaCompany();
        String note = "Preferenza valida solo per questa company\nAltre company possono avere valori diversi";
        Preferenza preferenza;
        String code = "";
        PrefType type;
        ARoleType livello;
        String descrizione;
        Object value;
        PrefEffect attivazione;
        boolean replica;

        if (listaPref != null && listaPref.size() > 0) {
            for (Preferenza pref : listaPref) {
                replica = pref.isReplica();
                if (replica) {
                    code = pref.getCode();
                    type = pref.getType();
                    livello = pref.getLivello();
                    descrizione = pref.getDescrizione();
                    value = pref.getValore();
                    attivazione = pref.getAttivazione();
                    preferenza = creaAndSavePreferenza(company, code, type, livello, descrizione, value, attivazione, true);

                    if (LibText.isEmpty(preferenza.note)) {
                        preferenza.note = note;
                        try { // prova ad eseguire il codice
                            preferenzaService.save(preferenza);
                        } catch (Exception unErrore) { // intercetta l'errore
                            log.error(unErrore.toString());
                        }// fine del blocco try-catch
                    }// end of if cycle
                }// end of if cycle

            }// end of for cycle
        }// end of if cycle

    }// end of method

    /**
     * Creazione di una entity
     * Log a video
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contact     persona di riferimento (facoltativo)
     * @param telefono    della company (facoltativo)
     * @param email       delal company (facoltativo)
     * @param indirizzo   della company (facoltativo)
     */
    protected Company creaAndLog(String code, String descrizione, Persona contact, String telefono, String email, Indirizzo indirizzo) {
        Company company = service.findOrCrea(code, descrizione, contact, telefono, email, indirizzo);
        log.warn("Company: " + code);

        return company;
    }// end of method


    protected Preferenza creaAndSavePreferenza(Company company, String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect attivazione, boolean replica) {
        return preferenzaService.findOrCrea(company, code, type, level, descrizione, value, attivazione, replica);
    }// end of method


}// end of class
