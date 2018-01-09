package it.algos.springvaadin.entity.address;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.stato.Stato;
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
@Qualifier(ACost.TAG_ADD)
public class AddressService extends AService {


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private AddressRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public AddressService(@Qualifier(ACost.TAG_ADD) MongoRepository repository) {
        super(repository);
        super.entityClass = Address.class;
        this.repository = (AddressRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @return la entity trovata o appena creata
     */
    public Address findOrCrea() {
        return findOrCrea("", "", "", (Stato) null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     * @param stato:     stato (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Address findOrCrea(String indirizzo, String localita, String cap, Stato stato) {
        try { // prova ad eseguire il codice
            return (Address) save(newEntity(indirizzo, localita, cap, stato));
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
            return null;
        }// fine del blocco try-catch
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Address newEntity() {
        return newEntity("", "", "", (Stato) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     * @param stato:     stato (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Address newEntity(String indirizzo, String localita, String cap, Stato stato) {
        Address entity = null;

        entity = Address.builder().indirizzo(indirizzo).localita(localita).cap(cap).stato(stato).build();

        return entity;
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
        return repository.findAll();
    }// end of method


}// end of class
