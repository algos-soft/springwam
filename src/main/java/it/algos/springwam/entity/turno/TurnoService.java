package it.algos.springwam.entity.turno;

import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.servizio.Servizio;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_TUR)
@Slf4j
public class TurnoService extends AlgosServiceImpl {

    private TurnoRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public TurnoService(@Qualifier(AppCost.TAG_TUR) MongoRepository repository) {
        super(repository);
        this.repository = (TurnoRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Turno findOrCrea(Servizio servizio, LocalDateTime inizio) {
        return findOrCrea((Company) null, servizio, inizio, (LocalDateTime) null, (List<Iscrizione>) null, "", "");
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param company       (obbligatoria, se manca usa quella presente in LibSession, se manca anche quella non esegue)
     * @param servizio      di riferimento (obbligatorio)
     * @param inizio        giorno, ora e minuto di inizio turno (obbligatorio)
     * @param fine          giorno, ora e minuto di fine turno (obbligatorio, suggerito da servizio)
     * @param iscrizioni    dei volontari a questo turno (obbligatorio per un turno valido)
     * @param titoloExtra   motivazione del turno extra (facoltativo)
     * @param localitaExtra nome evidenziato della località per turni extra (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Turno findOrCrea(Company company, Servizio servizio, LocalDateTime inizio, LocalDateTime fine, List<Iscrizione> iscrizioni, String titoloExtra, String localitaExtra) {
        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(servizio, inizio)) {
                try { // prova ad eseguire il codice
                    return (Turno) save(newEntity(company, servizio, inizio, fine, iscrizioni, titoloExtra, localitaExtra));
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return null;
                }// fine del blocco try-catch
            } else {
                return findByServizioAndInizio(servizio, inizio);
            }// end of if/else cycle
        } else {
            log.error("findOrCrea senza Company");
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Turno newEntity() {
        return newEntity((Company) null, (Servizio) null, (LocalDateTime) null, (LocalDateTime) null, (List<Iscrizione>) null, "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity(Servizio servizio, LocalDateTime inizio) {
        return newEntity((Company) null, servizio, inizio, (LocalDateTime) null, (List<Iscrizione>) null, "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param company       (obbligatoria, se manca usa quella presente in LibSession, se manca anche quella non esegue)
     * @param servizio      di riferimento (obbligatorio)
     * @param inizio        giorno, ora e minuto di inizio turno (obbligatorio)
     * @param fine          giorno, ora e minuto di fine turno (obbligatorio, suggerito da servizio)
     * @param iscrizioni    dei volontari a questo turno (obbligatorio per un turno valido)
     * @param titoloExtra   motivazione del turno extra (facoltativo)
     * @param localitaExtra nome evidenziato della località per turni extra (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity(Company company, Servizio servizio, LocalDateTime inizio, LocalDateTime fine, List<Iscrizione> iscrizioni, String titoloExtra, String localitaExtra) {
        Turno entity = null;
        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(servizio, inizio)) {
                entity = new Turno(
                        servizio,
                        inizio,
                        fine,
                        iscrizioni,
                        titoloExtra,
                        localitaExtra);
                entity.setCompany(company);
            } else {
                return findByServizioAndInizio(servizio, inizio);
            }// end of if/else cycle
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(Servizio servizio, LocalDateTime inizio) {
        return findByServizioAndInizio(servizio, inizio) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Servizio servizio, LocalDateTime inizio) {
        return findByServizioAndInizio(servizio, inizio) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Turno findByServizioAndInizio(Servizio servizio, LocalDateTime inizio) {
        return findByCompanyAndServizioAndInizio(LibSession.getCompany(), servizio, inizio);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession, se manca anche quella non esegue)
     * @param servizio di riferimento (obbligatorio)
     * @param inizio   giorno, ora e minuto di inizio turno (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Turno findByCompanyAndServizioAndInizio(Company company, Servizio servizio, LocalDateTime inizio) {
        return repository.findByCompanyAndServizioAndInizio(company, servizio, inizio);
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma il developer può vedere anche tutto
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll() {
        if (LibSession.isDeveloper()) {
            return repository.findByOrderByInizioAsc();
        }// end of if cycle

        return null;
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List findAllByCompany() {
        return findAllByCompany(LibSession.getCompany());
    }// end of method


    /**
     * Returns all instances of the type.
     * Usa MultiCompany obbligatoria -> ACompanyRequired.obbligatoria
     * Filtrata sulla company indicata
     * Se la company è nulla, rimanda a findAll
     * Lista ordinata
     *
     * @param company ACompanyRequired.obbligatoria
     *
     * @return entities filtrate
     */
    public List findAllByCompany(Company company) {
        if (company == null) {
            return findAll();
        } else {
            return repository.findByCompanyOrderByServizioAsc(company);
        }// end of if/else cycle
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    @Override
    public AEntity save(AEntity entityBean) throws Exception {
        Company company = ((ACompanyEntity) entityBean).getCompany();
        Servizio servizio = ((Turno) entityBean).getServizio();
        LocalDateTime inizio = ((Turno) entityBean).getInizio();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (company == null) {
            log.error("Ha cercato di salvare una entity senza company");
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(servizio, inizio)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente per questa company");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method


}// end of class
