package it.algos.springvaadin.entity.logtype;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.role.Role;
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
@Qualifier(ACost.TAG_LOGTYPE)
public class LogtypeService extends AService {


    public final static String SETUP = "Setup";
    public final static String NEW = "New";
    public final static String EDIT = "Edit";
    public final static String DELETE = "Delete";


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private LogtypeRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public LogtypeService(@Qualifier(ACost.TAG_LOGTYPE) MongoRepository repository) {
        super(repository);
        super.entityClass = Logtype.class;
        this.repository = (LogtypeRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Logtype findOrCrea(String code) {
        return this.findOrCrea(0, code);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine di rilevanza (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param code   di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Logtype findOrCrea(int ordine, String code) {
        if (nonEsiste(code)) {
            try { // prova ad eseguire il codice
                return (Logtype) save(newEntity(ordine, code));
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
    public Logtype newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param ordine di rilevanza (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param code   di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Logtype newEntity(int ordine, String code) {
        Logtype entity = null;

        if (nonEsiste(code)) {
            entity = Logtype.builder().ordine(ordine != 0 ? ordine : this.getNewOrdine()).code(code).build();
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
    public Logtype findByCode(String code) {
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
        return repository.findByOrderByOrdineAsc();
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
        String code = ((Logtype) entityBean).getCode();

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


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company),
     * viene calcolato in automatico prima del persist sul database
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    private int getNewOrdine() {
        int ordine = 0;

        List<Logtype> lista = repository.findTop1ByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getSetup() {
        return findByCode(SETUP);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getNew() {
        return findByCode(NEW);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity modificata)
     *
     * @return la entity appena trovata
     */
    public Logtype getEdit() {
        return findByCode(EDIT);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity cancellata)
     *
     * @return la entity appena trovata
     */
    public Logtype getDelete() {
        return findByCode(DELETE);
    }// end of method

}// end of class
