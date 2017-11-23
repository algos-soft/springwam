package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Company demo
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 */
@SpringComponent
@Slf4j
public class CompanyDataOld {


    //--il service (contenente la repository) viene iniettato qui
    @Autowired
    protected CompanyService serviceCompany;

    //--il service (contenente la repository) viene iniettato qui
    @Autowired
    protected IndirizzoService serviceIndirizzo;


    /**
     * Creazione di una collezione di company
     */
    public void creaAllCompany() {
        creaAndLog("crf", "Croce Rossa Fidenza", "Via Soderini, 55", "Milano", "20146");
        creaAndLog("crpt", "Croce Rossa Ponte Taro", "Largo Boccioni, 2","Bergamo","81256");
        creaAndLog("pap", "Pubblica Assistenza Pianoro", "Piazza Risorgimento, 3","Trapani","35090");
        creaAndLog("gaps", "Gruppo Accoglienza Pronto Soccorso", "Non pervenuto","Capitale","10000");
    }// end of method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param sigla sigla di riferimento interna (interna, obbligatoria ed unica)
     * @param desc  ragione sociale o descrizione2 della company (visibile - obbligatoria)
     * @param ind   indirizzo stradale
     * @param loc   citta, paese
     * @param cap   postale
     */
    private void creaAndLog(String sigla, String desc, String ind, String loc, String cap) {
//        serviceCompany.crea(sigla, desc, serviceIndirizzo.newEntity(ind, loc, cap));
//        log.warn("Company: " + sigla);
    }// end of method



}// end of class
