package it.algos.springwam.entity.milite;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.PersonaService;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.IAUser;
import it.algos.springvaadin.service.ALoginService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-31_15:21:40
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
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Qualifier(AppCost.TAG_MIL)
@AIScript(sovrascrivibile = false)
public class MiliteService extends ALoginService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    private RoleService roleService;


    @Autowired
    private PersonaService personaService;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private MiliteRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public MiliteService(@Qualifier(AppCost.TAG_MIL) MongoRepository repository) {
        super(repository);
        this.repository = (MiliteRepository) repository;
        super.entityClass = Milite.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return la entity trovata o appena creata
     */
    public Milite findOrCrea(String nickname, String password, Role role, String nome, String cognome) {
        return findOrCrea(nickname, password, role, nome, cognome, "", "");
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param nickname  di riferimento (obbligatorio, unico per company)
     * @param password  (obbligatoria o facoltativa, non unica)
     * @param role      (obbligatoria, non unica)
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     * @param email:    posta elettronica (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Milite findOrCrea(
            String nickname,
            String password,
            Role role,
            String nome,
            String cognome,
            String telefono,
            String email) {
        Milite entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = newEntity(nickname, password, role, nome, cognome, telefono, email);
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
    public Milite newEntity() {
        return newEntity("", "", (Role) null, "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password (obbligatoria o facoltativa, non unica)
     * @param role     (obbligatoria, non unica)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Milite newEntity(String nickname, String password, Role role, String nome, String cognome) {
        return newEntity(nickname, password, role, nome, cognome, "", "");
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
     * @param nickname  di riferimento (obbligatorio, unico per company)
     * @param password  (obbligatoria o facoltativa, non unica)
     * @param role      (obbligatoria, non unica)
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     * @param email:    posta elettronica (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Milite newEntity(
            String nickname,
            String password,
            Role role,
            String nome,
            String cognome,
            String telefono,
            String email) {
        Milite entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = new Milite();
            entity.setNickname(nickname);
            entity.setPassword(password);
            entity.setRole(role);
            entity.setEnabled(true);
            entity.setNome(nome);
            entity.setCognome(cognome);
            entity.setTelefono(telefono);
            entity.setEmail(email);
            entity.setDipendente(false);
            entity.setInfermiere(false);
        }// end of if cycle

        return (Milite) addCompany(entity);
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
    public Milite findByKeyUnica(String nickname) {
        return findByKeyUnica((Company) login.getCompany(), nickname);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param croce    di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Milite findByKeyUnica(Company croce, String nickname) {
        return repository.findByCompanyAndNickname(croce != null ? croce : (Company) login.getCompany(), nickname);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param croce    di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Milite findByNomeCognome(Company croce, String nome, String cognome) {
        return repository.findByCompanyAndNomeAndCognome(croce != null ? croce : (Company) login.getCompany(), nome, cognome);
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
            return repository.findByOrderByCognomeAsc();
        } else {
            return repository.findByCompanyOrderByCognomeAsc((Company) login.getCompany());
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
            entityBean.id = ((Milite) entityBean).getCompany().getCode() + ((Milite) entityBean).getNickname();
        }// end of if cycle

        return super.save(entityBean);
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
