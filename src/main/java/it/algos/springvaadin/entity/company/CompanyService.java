package it.algos.springvaadin.entity.company;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Estende la Entity astratta AService. Layer di collegamento tra il Presenter e la Repository.
 * Annotated with @@Slf4j (facoltativo) per i logs automatici
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Service (ridondante)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 */
@Slf4j
@SpringComponent
@Service
@Scope("singleton")
@Qualifier(ACost.TAG_COM)
public class CompanyService extends AService {


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private CompanyRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public CompanyService(@Qualifier(ACost.TAG_COM) MongoRepository repository) {
        super(repository);
        super.entityClass = Company.class;
        this.repository = (CompanyRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Company findOrCrea(String code) {
        return findOrCrea(code, "");
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Company findOrCrea(String code, String descrizione) {
        if (nonEsiste(code)) {
            try { // prova ad eseguire il codice
                return (Company) save(newEntity(code, descrizione));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return findByCode(code);
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
    public Company newEntity() {
        return newEntity("");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Company newEntity(String code) {
        return newEntity(code, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        codice di riferimento (obbligatorio)
     * @param descrizione (facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Company newEntity(String code, String descrizione) {
        Company entity = null;

        if (nonEsiste(code)) {
            entity = Company.builder().code(code).descrizione(descrizione).build();
        } else {
            return findByCode(code);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param code sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(String code) {
        return findByCode(code) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param code sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String code) {
        return findByCode(code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Company findByCode(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Returns all instances of the type
     * La Entity è EACompanyRequired.nonUsata. Non usa Company.
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    @Override
    public List findAll() {
        return repository.findByOrderByCodeAsc();
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
        String code = ((Company) entityBean).getCode();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (text.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(code)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente, ma unica");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

}// end of class
