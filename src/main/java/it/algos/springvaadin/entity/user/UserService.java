package it.algos.springvaadin.entity.user;

import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Qualifier(Cost.TAG_USE)
@Slf4j
public class UserService extends AlgosServiceImpl {


    private UserRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public UserService(@Qualifier(Cost.TAG_USE) MongoRepository repository) {
        super(repository);
        this.repository = (UserRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password password (obbligatoria o facoltativa, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(String nickname, String password) {
        return findOrCrea((Company) null, nickname, password, true);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password password (obbligatoria o facoltativa, non unica)
     * @param enabled  buttonUser abilitato (facoltativo, di default true)
     *
     * @return la entity trovata o appena creata
     */
    public User findOrCrea(Company company, String nickname, String password, boolean enabled) {

        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(company, nickname)) {
                try { // prova ad eseguire il codice
                    return (User) save(newEntity(company, nickname, password, enabled));
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return null;
                }// fine del blocco try-catch
            } else {
                return repository.findByCompanyAndNickname(company, nickname);
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
    public User newEntity() {
        return newEntity((Company) null, "", "", true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password password (obbligatoria o facoltativa, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(String nickname, String password) {
        return newEntity((Company) null, nickname, password, true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nickname di riferimento (obbligatorio, unico per company)
     * @param password password (obbligatoria o facoltativa, non unica)
     * @param enabled  buttonUser abilitato (facoltativo, di default true)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public User newEntity(Company company, String nickname, String password, boolean enabled) {
        User entity = null;

        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        if (company != null) {
            if (nonEsiste(company, nickname)) {
                entity = new User(nickname, password, enabled);
                entity.setCompany(company);
            } else {
                return repository.findByCompanyAndNickname(company, nickname);
            }// end of if/else cycle
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company  (obbligatoria, se manca usa quella presente in LibSession)
     * @param nickname di riferimento (obbligatorio, unico per company)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Company company, String nickname) {
        return repository.findByCompanyAndNickname(company, nickname) == null;
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
            return repository.findByOrderByNicknameAsc();
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
            return repository.findByCompanyOrderByNicknameAsc(company);
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
        Company company = ((ACompanyEntity) entityBean).getCompany();
        String nickname = ((User) entityBean).getNickname();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, nickname)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente per questa company");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method


}// end of class
