package it.algos.springvaadin.entity.role;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AlgosData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.persona.PersonaService;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 12-nov-2017
 * Time: 14:54
 */
@SpringComponent
@Slf4j
public class RoleData extends AlgosData {


    private RoleService service;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service iniettata da Spring
     */
    public RoleData(@Qualifier(Cost.TAG_ROL) AlgosService service) {
        super(service);
        this.service = (RoleService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione
     */
    public void creaAll() {
        if (nessunRecordEsistente()) {
            creaRuoli();
        } else {
            log.info("La collezione di Role è presente");
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione dei ruoli
     */
    public void creaRuoli() {
        service.findOrCrea(1, "ROLE_DEVELOPER");
        service.findOrCrea(2, "ROLE_ADMIN");
        service.findOrCrea(3, "ROLE_USER");
        service.findOrCrea(4, "ROLE_GUEST");
    }// end of method

}// end of class
