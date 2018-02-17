package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.IAUser;
import it.algos.springvaadin.service.ALoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Estende la Entity astratta AService. Layer di collegamento tra il Presenter e la Repository.
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Service (ridondante)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @@Slf4j (facoltativo) per i logs automatici
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@SpringComponent
@Service
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Qualifier(ACost.TAG_USE)
@AIScript(sovrascrivibile = false)
public class UserService extends ALoginService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private RoleService roleService;


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
        this.repository = (UserRepository) repository;
        super.entityClass = User.class;
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
        return findOrCrea(nickname, password, (Role) null);
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
        return findOrCrea(nickname, nickname, role);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(String nickname, String password, Role role) {
        User entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = newEntity(nickname, password, role);
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
        return newEntity(nickname, password, (Role) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     * La company viene controllata nel metodo AService.addCompany()
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(String nickname, String password, Role role) {
        User entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = User.builder()
                    .nickname(nickname)
                    .password(password)
                    .role(role != null ? role : roleService.getUser())
                    .enabled(true)
                    .build();
        }// end of if cycle

        return (User) addCompany(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public IAUser findByNickname(String nickname) {
        return repository.findByNickname(nickname);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public User findByKeyUnica(String nickname) {
        return findByKeyUnica(login.getCompany(), nickname);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company  di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public User findByKeyUnica(Company company, String nickname) {
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
     * Controlla che esiste un utente con questo nickname e questa password
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public boolean passwordValida(String nickname, String password) {
        boolean valida = false;
        IAUser entity = findByNickname(nickname);

        if (entity != null) {
            valida = entity.getPassword().equals(password);
        }// end of if cycle

        return valida;
    }// end of method

}// end of class
