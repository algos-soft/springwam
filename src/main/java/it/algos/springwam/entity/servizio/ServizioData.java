package it.algos.springwam.entity.servizio;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AlgosData;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneData;
import it.algos.springwam.entity.funzione.FunzioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 05-nov-2017
 * Time: 11:04
 */
@SpringComponent
@Slf4j
public class ServizioData extends AlgosData {


    protected ServizioService service;

    @Autowired
    protected FunzioneService serviceFunzione;

    @Autowired
    protected FunzioneData funz;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service iniettata da Spring
     */
    public ServizioData(@Qualifier(AppCost.TAG_SER) AlgosService service) {
        super(service);
        this.service = (ServizioService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione di servizi per la company demo
     * Cancella i servizi esistenti e li ricrea
     *
     * @param croceDemo a cui agganciare i servizi
     */
    public void creaDemoAllways(Croce croceDemo) {
        service.deleteAll();
        creaServizi(croceDemo);
        log.info("La precedente collezione di servizi demo è stata cancellata");
    }// end of method


    /**
     * Creazione di una collezione di servizi per la company demo
     * Li crea solo se la collezione è vuota
     *
     * @param croceDemo a cui agganciare i servizi
     */
    public void creaDemoEmpty(Croce croceDemo) {
        if (nessunRecordEsistente()) {
            creaServizi(croceDemo);
        } else {
            log.info("La collezione di servizi è presente");
        }// end of if/else cycle
    }// end of method


    /**
     * Crea una collezione di servizi
     * Li crea solo se non esistono già
     *
     * @param demo a cui agganciare i servizi
     */
    private void creaServizi(Croce demo) {
        creaAndLog(demo, getFunzA(demo), "med-mat", "Automedica mattino", 7, 12);
        creaAndLog(demo, getFunzA(demo), "med-pom", "Automedica pomeriggio", 12, 18);
        creaAndLog(demo, getFunzA(demo), "med-ser", "Automedica sera", 18, 22);
        creaAndLog(demo, getFunzB(demo), "med-ser2", "Automedica sera (estiva)", 18, 24, false);
        creaAndLog(demo, getFunzC(demo), "amb-mat", "Ambulanza mattino", 8, 14);
        creaAndLog(demo, getFunzC(demo), "amb-pom", "Ambulanza pomeriggio", 14, 20);
        creaAndLog(demo, getFunzC(demo), "amb-sera", "Ambulanza notte", 20, 8);
        creaAndLog(demo, getFunzB(demo), "extra", "Servizio extra non programmato");
    }// end of method


    /**
     * Recupera una collezione di funzioni
     *
     * @param demo da cui recuperare le funzioni
     */
    private List<Funzione> getFunzA(Croce demo) {
        List<Funzione> lista = new ArrayList<>();

        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.AUTMSA));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.MSA1));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.MSA2));

        return lista;
    }// end of method


    /**
     * Recupera una collezione di funzioni
     *
     * @param demo da cui recuperare le funzioni
     */
    private List<Funzione> getFunzB(Croce demo) {
        List<Funzione> lista = new ArrayList<>();

        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.AUTAMB));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.AMB1));

        return lista;
    }// end of method


    /**
     * Recupera una collezione di funzioni
     *
     * @param demo da cui recuperare le funzioni
     */
    private List<Funzione> getFunzC(Croce demo) {
        List<Funzione> lista = new ArrayList<>();

        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.AUTORD));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.DAE));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.BAR));
        lista.add(serviceFunzione.findByCompanyAndCode(demo,funz.BAR2));

        return lista;
    }// end of method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param code codice di riferimento (obbligatorio)
     */
    private void creaAndLog(Croce demo, List<Funzione> funzioni, String code, String descrizione) {
        Servizio servizio = service.findOrCrea(0, demo, code, descrizione, 0, false, 0, 0, 0, 0, true,funzioni);
        log.warn("Servizio: " + servizio);
    }// end of static method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param code codice di riferimento (obbligatorio)
     */
    private void creaAndLog(Croce demo, List<Funzione> funzioni, String code, String descrizione, int oraInizio, int oraFine) {
        Servizio servizio = service.findOrCrea(0, demo, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true,funzioni);
        log.warn("Servizio: " + servizio);
    }// end of static method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param code codice di riferimento (obbligatorio)
     */
    private void creaAndLog(Croce demo, List<Funzione> funzioni, String code, String descrizione, int oraInizio, int oraFine, boolean visibile) {
        Servizio servizio = service.findOrCrea(0, demo, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, visibile,funzioni);
        log.warn("Servizio: " + servizio);
    }// end of static method


}// end of class
