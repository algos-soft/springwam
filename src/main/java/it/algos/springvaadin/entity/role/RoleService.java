package it.algos.springvaadin.entity.role;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ASessionService;
import it.algos.springvaadin.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 08:41
 * Estende la Entity astratta AService. Layer di collegamento tra il Presenter e la Repository.
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Service (ridondante)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @@Slf4j (facoltativo) per i logs automatici
 */
@Slf4j
@SpringComponent
@Service
@Scope("singleton")
@Qualifier(ACost.TAG_ROL)
public class RoleService extends AService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ASessionService session;


    public final static String DEV = "developer";
    public final static String ADMIN = "admin";
    public final static String USER = "user";
    public final static String GUEST = "guest";


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    public RoleRepository repository;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param repository iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public RoleService(@Qualifier(ACost.TAG_ROL) MongoRepository repository) {
        super(repository);
        this.repository = (RoleRepository) repository;
        super.entityClass = Role.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Role findOrCrea(String code) {
        return this.findOrCrea(0, code);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine di rilevanza (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param code di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Role findOrCrea(int ordine, String code) {
        if (nonEsiste(code)) {
            try { // prova ad eseguire il codice
                return (Role) save(newEntity(ordine, code));
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
    public Role newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param ordine di rilevanza (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param code di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Role newEntity(int ordine, String code) {
        Role entity = null;

        if (nonEsiste(code)) {
            entity = Role.builder().ordine(ordine != 0 ? ordine : this.getNewOrdine()).code(code).build();
        } else {
            return findByCode(code);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String code) {
        return findByCode(code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Role findByCode(String code) {
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
    public List<Role> findAll() {
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
        String codice = ((Role) entityBean).getCode();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (text.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(codice)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente");
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

        List<Role> lista = repository.findTop1ByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    /**
     * Role developer
     *
     * @return entity richiesta
     */
    public Role getDev() {
        return findByCode(DEV);
    }// end of method


    /**
     * Role developer
     *
     * @return entity richiesta
     */
    public Role getAdmin() {
        return findByCode(ADMIN);
    }// end of method


    /**
     * Role developer
     *
     * @return entity richiesta
     */
    public Role getUser() {
        return findByCode(USER);
    }// end of method


    /**
     * Role developer
     *
     * @return entity richiesta
     */
    public Role getGuest() {
        return findByCode(GUEST);
    }// end of method

}// end of class
