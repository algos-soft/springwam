package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.data.AData;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.IAService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 20-dic-2017
 * Time: 07:14
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class MiliteData extends AData {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private RoleService roleService;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private CroceService croceService;

    /**
     * Il service iniettato dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia IAService
     * Spring costruisce al volo, quando serve, una implementazione di IAService (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private MiliteService service;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param service iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public MiliteData(@Qualifier(ACost.TAG_MIL) IAService service) {
        super(service);
        this.service = (MiliteService) service;
    }// end of Spring constructor


    /**
     * Creazione di una collezione
     */
    public void findOrCrea() {
        int numRec = 0;

        creaMiliti();
        numRec = service.count();
        log.warn("Algos - Creazione dati iniziali @EventListener ABoot.onApplicationEvent() -> iniziaData.inizia() -> MiliteData.findOrCrea(): " + numRec + " schede");
    }// end of method


    /**
     * Creazione degli users di PROVA
     * Solo se non esistono
     */
    public void creaMiliti() {
        Milite entity;
        Croce croceAlgos = croceService.findByKeyUnica(CroceData.ALGOS);
        Croce croceDemo = croceService.findByKeyUnica(CroceData.DEMO);
        Croce croceTest = croceService.findByKeyUnica(CroceData.TEST);

        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceAlgos, "gac", roleService.getDev());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceAlgos, "alex", roleService.getDev());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceDemo, "developer", roleService.getAdmin());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceDemo, "admin", roleService.getDev());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceTest, "volontario", roleService.getUser());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceTest, "ospite", roleService.getUser());
        creaSingoloMiliteSenzaLaCompanyDellaSessione(croceTest, "guest", roleService.getUser());
    }// end of method


    public void creaSingoloMiliteSenzaLaCompanyDellaSessione(Croce croce, String nickname, Role role) {
        Milite entity;

        if (croce != null) {
            if (service.findByKeyUnica(croce, nickname) == null) {
                entity = new Milite();
                entity.setNickname(nickname);
                entity.setPassword(nickname);
                entity.setRole(role);
                entity.setEnabled(true);
                entity.setNome(nickname);
                entity.setCognome(nickname);
                entity.setEnabled(true);
                entity.setDipendente(false);
                entity.setInfermiere(false);
                entity.company = croce;
                service.save(entity);
            }// end of if cycle
        }// end of if cycle
    }// end of method


}// end of class
