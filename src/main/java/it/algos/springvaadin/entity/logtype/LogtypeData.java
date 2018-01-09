package it.algos.springvaadin.entity.logtype;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AData;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 06-gen-2018
 * Time: 15:29
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class LogtypeData extends AData {


    /**
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private LogtypeService service;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public LogtypeData(@Qualifier(ACost.TAG_LOGTYPE) IAService service) {
        super(service);
        this.service = (LogtypeService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione
     * Solo se non ci sono records
     */
    public void findOrCrea() {
        int numRec = 0;

        if (nessunRecordEsistente()) {
            creaRuoli();
            numRec = service.count();
            log.warn("Algos - Creazione dati iniziali @EventListener ABoot.onApplicationEvent() -> iniziaDataStandard() -> LogtypeData.findOrCrea(): " + numRec + " schede");
        } else {
            numRec = service.count();
            log.info("Algos - Data. La collezion Logtype è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione dei ruoli
     */
    public void creaRuoli() {
        service.findOrCrea(LogtypeService.SETUP);
        service.findOrCrea(LogtypeService.NEW);
        service.findOrCrea(LogtypeService.EDIT);
        service.findOrCrea(LogtypeService.DELETE);
    }// end of method


}// end of class
