package it.algos.springvaadin.data;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 12-nov-2017
 * Time: 14:59
 */
@SpringComponent
@Slf4j
public class AlgosData {


    private AlgosService service;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service iniettata da Spring
     */
    public AlgosData(AlgosService service) {
        this.service = service;
    }// end of Spring constructor


    /**
     * Controlla se la collezione esiste già
     */
    protected boolean nessunRecordEsistente() {
        return service.count() == 0;
    }// end of method

}// end of class
