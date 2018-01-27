package it.algos.springvaadin.entity.stato;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AData;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AArrayService;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springvaadin.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 14-ott-2017
 * Time: 09:40
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class StatoData extends AData {


    /**
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private StatoService service;

    @Autowired
    public AResourceService resource;

    @Autowired
    public AArrayService array;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public StatoData(@Qualifier(ACost.TAG_STA) IAService service) {
        super(service);
        this.service = (StatoService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione
     * Solo se non ci sono records
     */
    public void findOrCrea() {
        int numRec = 0;

        if (nessunRecordEsistente()) {
            creaStati();
            numRec = service.count();
            log.warn("Algos - Creazione dati iniziali @EventListener ABoot.onApplicationEvent() -> iniziaDataStandard() -> StatoData.findOrCrea(): " + numRec + " schede");
        } else {
            numRec = service.count();
            log.info("Algos - Data. La collezion Stato è presente: " + numRec + " schede");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione di una collezione di stati
     */
    private void creaStati() {
        String fileName = "stati.txt";
        List<String> righe = resource.readText(fileName);

        if (array.isValid(righe)) {
            service.deleteAll();
            for (String riga : righe) {
                creaStato(riga);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Creazione di un singolo stato
     */
    private void creaStato(String riga) {
        String[] parti = riga.split(",");
        Stato stato;
        int ordine = 0;
        String nome = "";
        String alfaDue = "";
        String alfaTre = "";
        String numerico = "";
        byte[] bandiera = null;
        String suffix = ".png";

        if (parti.length > 0) {
            nome = parti[0];
        }// end of if cycle
        if (parti.length > 1) {
            alfaDue = parti[1];
        }// end of if cycle
        if (parti.length > 2) {
            alfaTre = parti[2];
            bandiera = resource.getImgBytes(alfaTre.toUpperCase() + suffix);
        }// end of if cycle
        if (parti.length > 3) {
            numerico = parti[3];
        }// end of if cycle

        service.findOrCrea(ordine, nome, alfaDue, alfaTre, numerico, bandiera);

        if (bandiera == null || bandiera.length == 0) {
            log.warn("Stato: " + riga + " - Manca la bandiera");
        } else {
            log.info("Stato: " + riga + " - Tutto OK");
        }// end of if/else cycle
    }// end of method

}// end of class
