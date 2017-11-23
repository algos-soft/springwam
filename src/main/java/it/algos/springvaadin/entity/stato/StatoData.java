package it.algos.springvaadin.entity.stato;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.stato.StatoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 14-ott-2017
 * Time: 09:40
 */
@SpringComponent
@Slf4j
public class StatoData {

    @Autowired
    private StatoService service;


    /**
     * Creazione di una collezione
     */
    public void creaAll() {
        if (recordsInsufficienti()) {
            service.creaStati();
        } else {
            log.info("La collezione di stati è presente");
        }// end of if/else cycle
    }// end of method


    /**
     * Controlla se la collezione esiste già
     * Controlla se esiste solo la scheda di default (Italia)
     */
    private boolean recordsInsufficienti() {
        return service.count() < 2;
    }// end of method



}// end of class
