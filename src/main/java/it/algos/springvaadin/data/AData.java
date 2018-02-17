package it.algos.springvaadin.data;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 12-nov-2017
 * Time: 14:59
 */
@Slf4j
@SpringComponent
@Scope("singleton")
@Qualifier(ACost.TAG_ROL)
public abstract class AData {


    /**
     * Il service viene iniettato dal costruttore della sottoclasse concreta
     */
    protected IAService service;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param service iniettato da Spring
     */
    public AData(IAService service) {
        this.service = service;
    }// end of Spring constructor


    /**
     * Controlla se la collezione esiste già
     *
     * @return true se la collection è inesistente
     */
    protected boolean nessunRecordEsistente() {
        return service.count() == 0;
    }// end of method


}// end of class
