package it.algos.springwam.entity.utente;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserRepository;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.IAUser;
import it.algos.springvaadin.service.ALoginService;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-16_10:27:41
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@Service
@Scope("singleton")
@Qualifier(AppCost.TAG_UTE)
@AIScript(sovrascrivibile = false)
public class UtenteService extends ALoginService {


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
    private UtenteRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public UtenteService(@Qualifier(AppCost.TAG_UTE) MongoRepository repository) {
        super(repository);
        this.repository = (UtenteRepository) repository;
        super.entityClass = Utente.class;
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
    public Utente findOrCrea(String nickname, String password, Role role, String nome, String cognome) {
        return findOrCrea(nickname, password, role, nome, cognome);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param nickname    di riferimento (obbligatorio, unico per company)
     * @param password    (obbligatoria o facoltativa, non unica)
     * @param role        (obbligatoria, non unica)
     * @param nome:       (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:    (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono:   (facoltativo)
     * @param email:      posta elettronica (facoltativo)
     * @param dipendente: della croce (facoltativo)
     * @param infermiere: professionale (facoltativo)
     * @param funzioni    abilitate per questo milite/volontario/utente
     *
     * @return la entity trovata o appena creata
     */
    public Utente findOrCrea(
            String nickname,
            String password,
            Role role,
            String nome,
            String cognome,
            String telefono,
            String email,
            boolean dipendente,
            boolean infermiere,
            List<Funzione> funzioni) {
        Utente entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = newEntity(nickname, password, role, nome, cognome, telefono, email, dipendente, infermiere, funzioni);
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
    public Utente newEntity() {
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
    public Utente newEntity(String nickname, String password, Role role, String nome, String cognome) {
        return newEntity(nickname, password, role, nome, cognome, "", "", false, false, (List<Funzione>) null);
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
     * @param nickname    di riferimento (obbligatorio, unico per company)
     * @param password    (obbligatoria o facoltativa, non unica)
     * @param role        (obbligatoria, non unica)
     * @param nome:       (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:    (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono:   (facoltativo)
     * @param email:      posta elettronica (facoltativo)
     * @param dipendente: della croce (facoltativo)
     * @param infermiere: professionale (facoltativo)
     * @param funzioni    abilitate per questo milite/volontario/utente
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(
            String nickname,
            String password,
            Role role,
            String nome,
            String cognome,
            String telefono,
            String email,
            boolean dipendente,
            boolean infermiere,
            List<Funzione> funzioni) {
        Utente entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = new Utente(nickname, password, role, true, dipendente, infermiere, funzioni);
            entity.setNome(nome);
            entity.setCognome(cognome);
            entity.setTelefono(telefono);
            entity.setEmail(email);
        }// end of if cycle

        return (Utente) addCompany(entity);
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
    public Utente findByKeyUnica(String nickname) {
        return findByKeyUnica((Croce) login.getCompany(), nickname);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param croce    di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Utente findByKeyUnica(Croce croce, String nickname) {
        return repository.findByCompanyAndNickname(croce != null ? croce : (Croce) login.getCompany(), nickname);
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
            return repository.findByCompanyOrderByCognomeAsc((Croce) login.getCompany());
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
    @Override
    public boolean passwordValida(String nickname, String password) {
        return false;
    }// end of method

}// end of class
