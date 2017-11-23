package it.algos.springwam.entity.utente;

import com.vaadin.icons.VaadinIcons;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_UTE)
@Slf4j
public class UtenteService extends AlgosServiceImpl {

    private UtenteRepository repository;

    @Autowired
    private UserService serviceUser;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public UtenteService(@Qualifier(AppCost.TAG_UTE) MongoRepository repository) {
        super(repository);
        this.repository = (UtenteRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Utente findOrCrea(String nome, String cognome, String telefono) {
        return findOrCrea((Company) null, (User) null, nome, cognome, telefono, "", false,false,false,null);
    }// end of method


    /**
     * Ricerca una entity
     * Se la trova, la cancella
     * Crea una nuova entity
     * All properties
     *
     * @param company   (obbligatoria, se manca usa quella presente in LibSession)
     * @param user      di riferimento (obbligatorio, One to One)
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     * @param email:    posta elettronica (facoltativo)
     * @param funzioni  abilitate per questo milite/volontario/utente
     *
     * @return la entity trovata o appena creata
     */
    public Utente deleteAndCrea(Company company, User user, String nome, String cognome, String telefono, String email, List<Funzione> funzioni) {
        Utente utente = null;

        if (esiste(company, nome, cognome)) {
            utente = findByCompanyAndNomeAndCognome(company, nome, cognome);
            delete(utente);
        }// end of if cycle

        return findOrCrea(company, user, nome, cognome, telefono, email, false, false, false, funzioni);
    }// end of method

    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param company   (obbligatoria, se manca usa quella presente in LibSession)
     * @param user      di riferimento (obbligatorio, One to One)
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     * @param email:    posta elettronica (facoltativo)
     * @param funzioni  abilitate per questo milite/volontario/utente
     *
     * @return la entity trovata o appena creata
     */
    public Utente findOrCrea(
            Company company,
            User user,
            String nome,
            String cognome,
            String telefono,
            String email,
            boolean admin,
            boolean dipendente,
            boolean infermiere,
            List<Funzione> funzioni) {
        Utente entity = null;

        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (user == null) {
            user = serviceUser.findOrCrea(company, nome + cognome, nome + "123", true);
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(company, nome, cognome)) {
                try { // prova ad eseguire il codice
                    return (Utente) save(newEntity(company, user, nome, cognome, telefono, email, admin, dipendente, infermiere, funzioni));
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return null;
                }// fine del blocco try-catch
            } else {
                return findByCompanyAndNomeAndCognome(company, nome, cognome);
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
    public Utente newEntity() {
        return newEntity((Company) null, (User) null, "", "", "", "", false, false, false, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome:     (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:  (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono: (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(String nome, String cognome, String telefono) {
        return newEntity((Company) null, (User) null, nome, cognome, telefono, "", false, false, false, (List<Funzione>) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param company     (obbligatoria, se manca usa quella presente in LibSession)
     * @param user        di riferimento (obbligatorio, One to One)
     * @param nome:       (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome:    (obbligatorio, non unico singolarmente nella company ma unico con nome)
     * @param telefono:   (facoltativo)
     * @param email:      posta elettronica (facoltativo)
     * @param admin:      abilitato come admin (facoltativo)
     * @param dipendente: della croce (facoltativo)
     * @param infermiere: professionale (facoltativo)
     * @param funzioni    abilitate per questo milite/volontario/utente
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Utente newEntity(
            Company company,
            User user,
            String nome,
            String cognome,
            String telefono,
            String email,
            boolean admin,
            boolean dipendente,
            boolean infermiere,
            List<Funzione> funzioni) {
        Utente entity = null;

        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (user == null) {
            user = serviceUser.findOrCrea(company, nome + cognome, nome + "123", true);
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(company, nome, cognome)) {
                entity = new Utente(user, admin, dipendente, infermiere, funzioni);
                entity.setCompany(company);
                entity.setNome(nome);
                entity.setCognome(cognome);
                entity.setTelefono(telefono);
                entity.setEmail(email);
            } else {
                return findByCompanyAndNomeAndCognome(company, nome, cognome);
            }// end of if/else cycle
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che  esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(Company company, String nome, String cognome) {
        return findByCompanyAndNomeAndCognome(company, nome, cognome) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Company company, String nome, String cognome) {
        return findByCompanyAndNomeAndCognome(company, nome, cognome) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nome:    (obbligatorio, non unico singolarmente nella company ma unico con cognome)
     * @param cognome: (obbligatorio, non unico singolarmente nella company ma unico con nome)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Utente findByCompanyAndNomeAndCognome(Company company, String nome, String cognome) {
        return repository.findByCompanyAndNomeAndCognome(company, nome, cognome);
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
            return repository.findByOrderByCognomeAsc();
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
            return repository.findByCompanyOrderByCognomeAsc(company);
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
        Croce company = (Croce) ((Utente) entityBean).getCompany();
        String nome = ((Utente) entityBean).getNome();
        String cognome = ((Utente) entityBean).getCognome();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, nome, cognome)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente per questa company");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method


}// end of class
