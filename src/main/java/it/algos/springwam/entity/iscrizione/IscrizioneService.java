package it.algos.springwam.entity.iscrizione;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:23:11
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
@Qualifier(AppCost.TAG_ISC)
@AIScript(sovrascrivibile = false)
public class IscrizioneService extends AService {


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private IscrizioneRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public IscrizioneService(@Qualifier(AppCost.TAG_ISC) MongoRepository repository) {
        super(repository);
        this.repository = (IscrizioneRepository) repository;
        super.entityClass = Iscrizione.class;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Iscrizione newEntity() {
        return newEntity((Milite) null, (Funzione) null, (LocalDateTime) null, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param milite    di riferimento (obbligatorio)
     * @param funzione  per cui il milite/volontario/utente si iscrive (obbligatorio)
     * @param timestamp di creazione (obbligatorio, inserito in automatico)
     * @param durata    effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity(Milite milite, Funzione funzione, LocalDateTime timestamp, int durata) {
        Iscrizione entity = Iscrizione.builder()
                .milite(milite)
                .funzione(funzione)
                .timestamp(timestamp)
                .durata(durata)
                .build();

        return (Iscrizione) addCompany(entity);
    }// end of method


    /**
     * Returns all instances of the type
     * La Entity è EACompanyRequired.nonUsata. Non usa Company.
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll(Turno turno) {
        if (login.isDeveloper()) {
            return null;
//            return repository.findByOrderByNicknameAsc();
        } else {
            return repository.findAll();
        }// end of if/else cycle
    }// end of method


}// end of class
