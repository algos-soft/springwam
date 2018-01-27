package it.algos.springwam.entity.croce;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.address.Address;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_22:57:46
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
@Qualifier(AppCost.TAG_CRO)
@AIScript(sovrascrivibile = false)
public class CroceService extends AService {


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private CroceRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public CroceService(@Qualifier(AppCost.TAG_CRO) MongoRepository repository) {
        super(repository);
        super.entityClass = Croce.class;
        this.repository = (CroceRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     *
     * @return la entity trovata o appena creata
     */
    public Croce findOrCrea(String code, String descrizione) {
        return findOrCrea((EAOrganizzazione) null, (Persona) null, code, descrizione, (Persona) null, "", "", (Address) null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param organizzazione (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contatto       persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param email          della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Croce findOrCrea(EAOrganizzazione organizzazione, Persona presidente, String code, String descrizione, Persona contatto, String telefono, String email, Address indirizzo) {
        Croce entity = findByKeyUnica(code);

        if (entity == null) {
            entity = newEntity(organizzazione, presidente, code, descrizione, contatto, telefono, email, indirizzo);
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
    public Croce newEntity() {
        return newEntity((EAOrganizzazione) null, (Persona) null, "", "", (Persona) null, "", "", (Address) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Croce newEntity(String code, String descrizione) {
        return newEntity((EAOrganizzazione) null, (Persona) null, code, descrizione, (Persona) null, "", "", (Address) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param organizzazione (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contatto       persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param email          della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Croce newEntity(EAOrganizzazione organizzazione, Persona presidente, String code, String descrizione, Persona contatto, String telefono, String email, Address indirizzo) {
        Croce entity = findByKeyUnica(code);

        if (entity == null) {
            entity = new Croce(organizzazione, presidente);
            entity.setCode(code);
            entity.setDescrizione(descrizione);
            entity.setContatto(contatto);
            entity.setTelefono(telefono);
            entity.setEmail(email);
            entity.setIndirizzo(indirizzo);
        } else {
            return findByKeyUnica(code);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Croce findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean nuova da creare
     */
    @Override
    protected boolean isEsisteEntityKeyUnica(AEntity entityBean) {
        return findByKeyUnica(((Croce) entityBean).getCode()) != null;
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
        return repository.findByOrderByCodeAsc();
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
            entityBean.id = ((Company) entityBean).getCode();
        }// end of if cycle

        return super.save(entityBean);
    }// end of method

}// end of class
