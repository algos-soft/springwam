package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AlgosData;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mer, 04-ott-2017
 * Time: 07:40
 */
@SpringComponent
@Slf4j
public class FunzioneData extends AlgosData {


    public final static String AUTMSA = "autmsa";
    public final static String MSA1 = "1msa";
    public final static String MSA2 = "2msa";
    public final static String AUTAMB = "autamb";
    public final static String AMB1 = "1amb";
    public final static String AMB2 = "2amb";
    public final static String AUTORD = "autord";
    public final static String DAE = "dae";
    public final static String BAR = "bar";
    public final static String BAR2 = "bar2";
    public final static String AVIS = "avis";
    public final static String CEN = "cen";


    //--il service (contenente la repository) viene iniettato qui
    @Autowired
    protected FunzioneService service;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service iniettata da Spring
     */
    public FunzioneData(@Qualifier(AppCost.TAG_FUN) AlgosService service) {
        super(service);
        this.service = (FunzioneService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione di funzioni per la company demo
     * Cancella le funzioni esistenti e le ricrea
     *
     * @param croceDemo a cui agganciare le funzioni
     */
    public void creaDemoAllways(Croce croceDemo) {
        service.deleteAll();
        creaFunzioni(croceDemo);
        log.info("La precedente collezione di funzioni demo è stata cancellata");
    }// end of method


    /**
     * Creazione di una collezione di funzioni per la company demo
     * Le crea solo se la collezione è vuota
     *
     * @param croceDemo a cui agganciare le funzioni
     */
    public void creaDemoEmpty(Croce croceDemo) {
        if (nessunRecordEsistente()) {
            creaFunzioni(croceDemo);
        } else {
            log.info("La collezione di funzioni è presente");
        }// end of if/else cycle
    }// end of method


    /**
     * Crea una collezione di funzioni
     * Le crea solo se non esistono già
     *
     * @param demo a cui agganciare le funzioni
     */
    private void creaFunzioni(Croce demo) {
        creaAndLog(demo, AUTMSA, "Aut-msa", "Autista automedica abilitato 118", VaadinIcons.DIPLOMA, true);
        creaAndLog(demo, MSA1, "1° Soc", "Primo soccorritore automedica", VaadinIcons.HEART);
        creaAndLog(demo, MSA2, "2° Soc", "Secondo soccorritore automedica", VaadinIcons.STETHOSCOPE);

        creaAndLog(demo, AUTAMB, "Aut-amb", "Autista ambulanza abilitato 118", VaadinIcons.AMBULANCE, true);
        creaAndLog(demo, AMB1, "1° Soc", "Primo soccorritore ambulanza", VaadinIcons.HEART,true);
        creaAndLog(demo, AMB2, "2° Soc", "Secondo soccorritore ambulanza", VaadinIcons.STETHOSCOPE);

        creaAndLog(demo, AUTORD, "Aut-ord", "Autista ordinario", VaadinIcons.AMBULANCE, true);
        creaAndLog(demo, DAE, "DAE", "Soccorritore abilitato DAE", VaadinIcons.HEART,true);
        creaAndLog(demo, BAR, "Bar", "Barelliere", VaadinIcons.STETHOSCOPE);
        creaAndLog(demo, BAR2, "Bar-aff", "Barelliere in affiancamento", VaadinIcons.USER);

        creaAndLog(demo, AVIS, "Avis", "Operatore trasporto AVIS", VaadinIcons.MONEY_EXCHANGE);
        creaAndLog(demo, CEN, "Cen", "Centralinista", VaadinIcons.PHONE);
    }// end of method


    /**
     * Creazione di una entity
     * Log a video
     *
     * @param sigla sigla di riferimento interna (interna, obbligatoria ed unica)
     */
    private void creaAndLog(Croce demo, String code, String sigla, String descrizione, VaadinIcons icon) {
        Funzione funzione = service.findOrCrea(demo, code, sigla, descrizione, icon, false);
        log.warn("Funzione: " + funzione);
    }// end of static method

    /**
     * Creazione di una entity
     * Log a video
     *
     * @param sigla sigla di riferimento interna (interna, obbligatoria ed unica)
     */
    private void creaAndLog(Croce demo, String code, String sigla, String descrizione, VaadinIcons icon, boolean obbligatoria) {
        Funzione funzione = service.findOrCrea(demo, code, sigla, descrizione, icon, obbligatoria);
        log.warn("Funzione: " + funzione);
    }// end of static method


}// end of class
