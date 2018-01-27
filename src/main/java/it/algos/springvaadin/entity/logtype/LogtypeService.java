package it.algos.springvaadin.entity.logtype;

import it.algos.springvaadin.annotation.AIScript;
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
 * Annotated with @Scope (obbligatorio = 'singleton')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@SpringComponent
@Service
@Scope("singleton")
@Qualifier(ACost.TAG_LOGTYPE)
@AIScript(sovrascrivibile = false)
public class LogtypeService extends AService {


    public final static String SETUP = "Setup";
    public final static String NEW = "New";
    public final static String EDIT = "Edit";
    public final static String DELETE = "Delete";



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
        this.repository = (LogtypeRepository) repository;
        super.entityClass = Logtype.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Logtype findOrCrea(String code) {
        Logtype entity = findByKeyUnica(code);

        if (entity == null) {
            entity = newEntity(0, code);
            save(entity);
        }// end of if cycle

        return entity;
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
        Logtype entity = findByKeyUnica(code);

        if (entity == null) {
            entity = Logtype.builder()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .code(code)
                    .build();
        }// end of if cycle

        return entity;
    }// end of method



    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Logtype findByKeyUnica(String code) {
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
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean nuova da creare
     */
    @Override
    protected boolean isEsisteEntityKeyUnica(AEntity entityBean) {
        return findByKeyUnica(((Logtype) entityBean).getCode()) != null;
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
            entityBean.id = ((Logtype)entityBean).getCode();
        }// end of if cycle

        return super.save(entityBean);
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
        return findByKeyUnica(SETUP);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (nuova entity)
     *
     * @return la entity appena trovata
     */
    public Logtype getNew() {
        return findByKeyUnica(NEW);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity modificata)
     *
     * @return la entity appena trovata
     */
    public Logtype getEdit() {
        return findByKeyUnica(EDIT);
    }// end of method


    /**
     * Raggruppamento logico dei log per type di eventi (entity cancellata)
     *
     * @return la entity appena trovata
     */
    public Logtype getDelete() {
        return findByKeyUnica(DELETE);
    }// end of method

}// end of class
