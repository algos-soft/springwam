package it.algos.springwam.entity.croce;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaService;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.lib.Cost;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 05-nov-2017
 * Time: 08:02
 */
@SpringComponent
@Slf4j
public class CroceData extends CompanyData {

    @Autowired
    @Qualifier(AppCost.TAG_CRO)
    private CroceService service;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private PreferenzaService preferenzaService;


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
     * Crea una croce
     */
    public Croce creaCroceTest() {
        Croce croce = creaAndLog(
                (Organizzazione) null,
                (Persona) null,
                "crf",
                "provvisoria",
                (Persona) null,
                "",
                "",
                (Indirizzo) null);
        return croce;
    }// end of method


    /**
     * Crea una croce
     */
    public Croce creaCroceDemo() {
        Croce croce = creaAndLog(
                Organizzazione.csv,
                personaService.newEntity("Aldo", "Rovaschi"),
                Cost.SIGLA_COMPANY_DEMO,
                "Algos s.r.l.",
                personaService.newEntity("Marco", "Beretta"),
                "",
                "mariorossi@win.com",
                indirizzoService.newEntity("via Procaccini, 37", "Milano", "20131", (Stato) null));
        return croce;
    }// end of method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param organizzazione (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contact        persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param email          delal company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     */
    private Croce creaAndLog(Organizzazione organizzazione, Persona presidente, String code, String descrizione, Persona contact, String telefono, String email, Indirizzo indirizzo) {
        Croce croce = service.findOrCrea(organizzazione, presidente, code, descrizione, contact, telefono, email, indirizzo);
        log.warn("Croce: " + code);

        return croce;
    }// end of method

}// end of class
