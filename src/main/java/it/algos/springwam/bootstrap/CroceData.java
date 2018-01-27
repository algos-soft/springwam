package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.IAService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.croce.CroceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 22-dic-2017
 * Time: 10:41
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class CroceData extends AData {


    public final static String ALGOS = "algos";
    public final static String DEMO = "demo";
    public final static String TEST = "test";


    /**
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private CroceService service;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public CroceData(@Qualifier(AppCost.TAG_CRO) IAService service) {
        super(service);
        this.service = (CroceService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione
     */
    public void findOrCrea() {
        int numRec = 0;

        creaCroci();
        numRec = service.count();
        log.warn("Algos - Creazione dati iniziali @EventListener ABoot.onApplicationEvent() -> iniziaData.inizia() -> CompanyData.findOrCrea(): " + numRec + " schede");
    }// end of method


    /**
     * Creazione delle croci
     * Solo se non esistono
     */
    public void creaCroci() {
        service.findOrCrea(ALGOS, "Algos s.r.l.");
        service.findOrCrea(DEMO, "Company di prova");
        service.findOrCrea(TEST, "Altra company");
//        service.findOrCrea(RoleService.GUEST);
    }// end of method

}// end of class
