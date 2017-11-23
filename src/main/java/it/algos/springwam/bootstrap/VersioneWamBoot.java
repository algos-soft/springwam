package it.algos.springwam.bootstrap;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.bootstrap.VersioneBoot;
import it.algos.springvaadin.bootstrap.VersioneSpringvaadinBoot;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyData;
import it.algos.springvaadin.entity.stato.StatoData;
import it.algos.springvaadin.entity.versione.VersioneService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceData;
import it.algos.webbase.web.lib.LibPref;
import it.algos.webbase.web.lib.LibVers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 24-set-2017
 * Time: 22:08
 * <p>
 * Log delle versioni, modifiche e patch installate
 * Setup non-UI logic here
 * This class will be executed on container startup when the application is ready to server requests.
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat)
 * Eseguita quindi ad ogni avvio/attivazione del server e NON ad ogni sessione
 * <p>
 * In order to create a class that acts like a bootstrap for the application,
 * that class needs to implements the @EventListener annotation
 * Any class that implements the @EventListener annotation will be executed before the application is up
 * and its onApplicationEvent method will be called
 * <p>
 * ATTENZIONE: in questa fase NON sono disponibili le Librerie e le classi che dipendono dalla UI e dalla Session
 */
@SpringComponent
@Slf4j
public class VersioneWamBoot extends VersioneSpringvaadinBoot {

    private final static String PROGETTO = "springwam";

    @Autowired
    private DemoData demoData;


    @Autowired
    private CroceData croceData;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     */
    public VersioneWamBoot(
            @Qualifier(Cost.TAG_VERS) AlgosService service,
            @Qualifier(Cost.TAG_COMP) AlgosService companyService,
            @Qualifier(Cost.TAG_PRE) AlgosService preferenzaService,
            @Qualifier(Cost.TAG_LOG) AlgosService logger) {
        super(service, companyService, preferenzaService, logger);
    }// end of Spring constructor


    /**
     * Running logic after the Spring context has been initialized
     * Any class that use this @EventListener annotation,
     * will be executed before the application is up and its onApplicationEvent method will be called
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        super.inizializza();
        croceData.updatePreferenze();
    }// end of method


    /**
     * Cronistoria delle versioni istallate nel progetto specifico
     */
    @Override
    protected void cronistoriaVersioniProgetto() {
        int k = 0;

        //--Creata una croce demo
        if (service.versioneNonAncoraUsata(PROGETTO, ++k)) {
            Croce croce = demoData.creaCroceDemo();
            if (croce != null) {
                creaVersione(
                        PROGETTO,
                        croce,
                        "Croce",
                        "Creata una croce demo",
                        "Creazione iniziale con alcuni dati fittizi\nCreate anche alcune preferenze specifiche per questa croce");
            }// end of if cycle
        }// fine del blocco if


//        //--crea una nuova preferenza, globale per tutte le company
//        if (LibVers.installa(++k)) {
//            LibPref.newVersBool(WAMApp.USA_REFRESH_DEMO, true, "Ricostruisce periodicamente la company demo");
//        }// fine del blocco if
//
//        //--crea una nuova preferenza, globale per tutte le company
//        if (LibVers.installa(++k)) {
//            LibPref.newVersBool(WAMApp.ATTIVA_MIGRATION, true, "Importa periodicamente le croci da webambulanze");
//        }// fine del blocco if
//
//        //--crea una nuova preferenza, globale per tutte le company
//        if (LibVers.installa(++k)) {
//            LibPref.newVersBool(WAMApp.USA_MIGRATION_COMPLETA, false, "Importa tutti i turni da webambulanze (invece che solo l'ultima settimana)");
//        }// fine del blocco if

    }// end of method

}// end of class

