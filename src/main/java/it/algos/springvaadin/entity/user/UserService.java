package it.algos.springvaadin.entity.user;

import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
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
@Qualifier(ACost.TAG_USE)
public class UserService extends AService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private RoleService roleService;

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private ALogin login;


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private UserRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public UserService(@Qualifier(ACost.TAG_USE) MongoRepository repository) {
        super(repository);
        super.entityClass = User.class;
        this.repository = (UserRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(String nickname) {
        return findOrCrea(nickname, nickname);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(String nickname, String password) {
        return findOrCrea((Company) null, nickname, password, (Role) null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param role     (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(String nickname, Role role) {
        return findOrCrea((Company) null, nickname, nickname, role);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param role     (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(Company company, String nickname, Role role) {
        return findOrCrea(company, nickname, nickname, role);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(Company company, String nickname, String password, Role role) {
        if (nonEsiste(company, nickname)) {
            try { // prova ad eseguire il codice
                return (User) save(newEntity(company, nickname, password, role));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return findByCompanyAndNickname(company, nickname);
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
    public User newEntity() {
        return newEntity("");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(String nickname) {
        return newEntity(nickname, nickname);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(String nickname, String password) {
        return newEntity((Company) null, nickname, password, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(Company company, String nickname, String password, Role role) {
        User entity = null;

        if (nonEsiste(company, nickname)) {
            entity = User.builder().nickname(nickname).password(password).role(role != null ? role : roleService.getUser()).enabled(true).build();
            entity.company = company != null ? company : login.getCompany();
        } else {
            return findByCompanyAndNickname(company, nickname);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(Company company, String nickname) {
        return findByCompanyAndNickname(company, nickname) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Company company, String nickname) {
        return findByCompanyAndNickname(company, nickname) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public User findByNickname(String nickname) {
        return repository.findByNickname(nickname);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public User findByCompanyAndNickname(Company company, String nickname) {
        return repository.findByCompanyAndNickname(company != null ? company : login.getCompany(), nickname);
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma il developer può vedere anche tutto
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll() {
        if (login.isDeveloper()) {
            return repository.findByOrderByNicknameAsc();
        } else {
            return repository.findByCompanyOrderByNicknameAsc(login.getCompany());
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
    public AEntity save(AEntity entityBean) throws Exception {
        Company company = ((ACEntity) entityBean).getCompany();
        String nickname = ((User) entityBean).getNickname();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (text.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, nickname)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente, ma unica");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Controlla che esiste un utente con questo nickname e questa password
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public boolean check(String nickname, String password) {
        return repository.findByNicknameAndPassword(nickname, password) != null;
    }// end of method

}// end of class
