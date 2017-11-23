package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceData;
import it.algos.springwam.entity.croce.Organizzazione;
import it.algos.springwam.entity.funzione.FunzioneData;
import it.algos.springwam.entity.servizio.ServizioData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 05-nov-2017
 * Time: 10:24
 */
@SpringComponent
@Slf4j
public class DemoData {


    @Autowired
    private CroceData croceData;

    @Autowired
    FunzioneData funzioneData;

    @Autowired
    ServizioData servizioData;


    /**
     * Crea una croce demo completa
     * <p>
     * Crea una croce
     * Crea le funzioni
     * Crea i servizi
     */
    public Croce creaCroceDemo() {
        Croce croce;
        croce = croceData.creaCroceDemo();

        if (croce != null) {
            creaFunzioniDemo(croce);
            creaServiziDemo(croce);
        }// end of if cycle

        return croce;
    }// end of method


    /**
     * Crea le funzioni demo della croce corrente (se esiste)
     */
    public void creaFunzioniDemo() {
        creaFunzioniDemo((Croce) LibSession.getCompany());
    }// end of method


    /**
     * Crea le funzioni demo di una croce
     */
    public void creaFunzioniDemo(Croce croce) {
        funzioneData.creaDemoAllways(croce);
    }// end of method

    
    /**
     * Crea i serivizi demo della croce corrente (se esiste)
     */
    public void creaServiziDemo() {
        creaServiziDemo((Croce) LibSession.getCompany());
    }// end of method


    /**
     * Crea i serivizi demo di una croce
     */
    public void creaServiziDemo(Croce croce) {
        servizioData.creaDemoAllways(croce);
    }// end of method

}// end of class
