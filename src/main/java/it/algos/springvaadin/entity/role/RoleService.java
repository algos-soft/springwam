package it.algos.springvaadin.entity.role;

import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gac on 11-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(Cost.TAG_ROL)
@Slf4j
public class RoleService extends AlgosServiceImpl {

    private RoleRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public RoleService(@Qualifier(Cost.TAG_ROL) MongoRepository repository) {
        super(repository);
        this.repository = (RoleRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine di rilevanza (obbligatorio)
     * @param codice di riferimento (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Role findOrCrea(int ordine, String codice) {
        if (nonEsiste(codice)) {
            try { // prova ad eseguire il codice
                return (Role) save(newEntity(ordine, codice));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return repository.findByCode(codice);
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
     * @param ordine di rilevanza (obbligatorio)
     * @param codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Role newEntity(int ordine, String codice) {
        Role entity = null;

        if (nonEsiste(codice)) {
            entity = new Role(ordine, codice);
        } else {
            return repository.findByCode(codice);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param codice di riferimento (obbligatorio)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String codice) {
        return repository.findByCode(codice) == null;
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
            return repository.findByOrderByOrdineAsc();
        }// end of if cycle

        return null;
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

        if (LibText.isValid(entityBean.id)) {
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


}// end of class
