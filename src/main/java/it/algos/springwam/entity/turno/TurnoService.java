package it.algos.springwam.entity.turno;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.ADateService;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.servizio.Servizio;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
 * Estende la Entity astratta AService. Layer di collegamento tra il Presenter e la Repository.
 * Annotated with @@Slf4j (facoltativo) per i logs automatici
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Service (ridondante)
 * Annotated with @Scope (obbligatorio = 'singleton')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@SpringComponent
@Service
@Qualifier(AppCost.TAG_TUR)
@AIScript(sovrascrivibile = false)
public class TurnoService extends AService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ADateService date;

    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
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
        super.entityClass = Turno.class;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Turno newEntity() {
        return newEntity((LocalDate) null, (Servizio) null, (LocalDateTime) null, (LocalDateTime) null, (List<Iscrizione>) null, "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     * La company viene controllata nel metodo AService.addCompany()
     *
     * @param giorno        di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     * @param servizio      di riferimento (obbligatorio)
     * @param inizio        giorno, ora e minuto di inizio turno (obbligatorio)
     * @param fine          giorno, ora e minuto di fine turno (obbligatorio, suggerito da servizio)
     * @param iscrizioni    dei volontari a questo turno (obbligatorio per un turno valido)
     * @param titoloExtra   motivazione del turno extra (facoltativo)
     * @param localitaExtra nome evidenziato della località per turni extra (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Turno newEntity(
            LocalDate giorno,
            Servizio servizio,
            LocalDateTime inizio,
            LocalDateTime fine,
            List<Iscrizione> iscrizioni,
            String titoloExtra,
            String localitaExtra) {
        Turno entity = Turno.builder()
                .giorno(giorno)
                .servizio(servizio)
                .inizio(inizio)
                .fine(fine)
                .iscrizioni(iscrizioni)
                .titoloExtra(titoloExtra)
                .localitaExtra(localitaExtra)
                .build();

        return (Turno) addCompany(entity);
    }// end of method


    /**
     * Returns all instances of the type
     * La Entity è EACompanyRequired.nonUsata. Non usa Company.
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll() {
        if (login.isDeveloper()) {
            return repository.findByCompanyAndGiornoIsGreaterThanEqual(login.getCompany(), LocalDate.now());
//            return repository.findByOrderByNicknameAsc();
        } else {
            LocalDate inizio = LocalDate.now();
//            inizio = inizio.minus(1, ChronoUnit.DAYS); //@todo opzionale - mettere in pref
            return repository.findTop42ByCompanyAndGiornoIsGreaterThanEqual(login.getCompany(), inizio);
        }// end of if/else cycle
    }// end of method


    /**
     * Returns all instances of the type
     * La Entity è EACompanyRequired.nonUsata. Non usa Company.
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll(LocalDate giorno) {
        if (login.isDeveloper()) {
            return null;
//            return repository.findByOrderByNicknameAsc();
        } else {
            return repository.findByCompanyAndGiorno(login.getCompany(), giorno);
        }// end of if/else cycle
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param giorno   di inizio turno (obbligatorio)
     * @param servizio di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Turno findByGiornoAndServizio(LocalDate giorno, Servizio servizio) {
        Company company = login.getCompany();

        if (company != null) {
            return repository.findByCompanyAndGiornoAndServizio(company, giorno,servizio);
        } else {
            return null;
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
    public AEntity save(AEntity entityBean) {

        if (text.isEmpty(entityBean.id)) {
            entityBean.id = ((Turno) entityBean).getCompany().getCode() + ((Turno) entityBean).getServizio().getCode() + ((Turno) entityBean).getInizio();
        }// end of if cycle

        return super.save(entityBean);
    }// end of method

}// end of class
