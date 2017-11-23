package it.algos.springwam.entity.riga;

import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 19-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_RIG)
@Slf4j
public class RigaService extends AlgosServiceImpl {


    @Autowired
    private ServizioService servizioService;

    @Autowired
    private TurnoService turnoService;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public RigaService() {
        super(null);
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Riga newEntity() {
        return newEntity(null, null, null, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param giorno   di riferimento (obbligatorio)
     * @param servizio di riferimento (obbligatorio)
     * @param turni    effettuati (facoltativi, di norma 7)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Riga newEntity(LocalDate giorno, Servizio servizio, List<Turno> turni) {
        return newEntity(LibSession.getCompany(), giorno, servizio, turni);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param giorno   di riferimento (obbligatorio)
     * @param servizio di riferimento (obbligatorio)
     * @param turni    effettuati (facoltativi, di norma 7)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Riga newEntity(Company company, LocalDate giorno, Servizio servizio, List<Turno> turni) {
        Riga entity = null;

        if (company != null) {
                entity = new Riga(giorno, servizio, turni);
                entity.setCompany(company);
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List findAllByCompany() {
        return findAllByCompany(LocalDate.now(), 7);
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List<Riga> findAllByCompany(LocalDate giornoInizio, int giorni) {
        return creaRighe(giornoInizio, giorni);
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List<Riga> creaRighe(LocalDate giornoInizio, int giorni) {
        List<Riga> righe = new ArrayList<>();
        List<Turno> listaTurni = null;
        Turno turno = null;
        List<Servizio> listaServiziVisibili = servizioService.findAllByCompany();//@todo SOLO visibili (da fare)
        Riga riga;

        if (listaServiziVisibili!=null&&listaServiziVisibili.size()>0) {
            for (int k = 0; k < listaServiziVisibili.size(); k++) {
                listaTurni = new ArrayList<>();
                for (int y = 0; y < giorni; y++) {
                    turno = turnoService.newEntity(null,null);
                    listaTurni.add(turno);
                }// end of for cycle

                riga = newEntity(giornoInizio, listaServiziVisibili.get(k), listaTurni);
                righe.add(riga);
            }// end of for cycle
        }// end of if cycle

        return righe;
    }// end of method





}// end of class
