package it.algos.springvaadin.entity.persona;

import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.address.Address;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-14_06:46:57
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Le entity SONO 'embedded' dentro un'altra collection
 * Sottoclassi specifiche possono NON essere 'embedded' ed avere una 'KeyUnica' su nome e cognome
 */
@Slf4j
@Service
@Qualifier(ACost.TAG_PER)
@AIScript(sovrascrivibile = false)
public class PersonaService extends AService {


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private PersonaRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public PersonaService(@Qualifier(ACost.TAG_PER) MongoRepository repository) {
        super(repository);
        this.repository = (PersonaRepository) repository;
        super.entityClass = Persona.class;
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Persona newEntity() {
        return newEntity("", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome:    obbligatorio
     * @param cognome: obbligatorio
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Persona newEntity(String nome, String cognome) {
        return newEntity(nome, cognome, "", "", (Address) null);
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
     * @param nome:      obbligatorio
     * @param cognome:   obbligatorio
     * @param telefono:  facoltativo
     * @param email:     facoltativo
     * @param indirizzo: via, nome e numero (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Persona newEntity(String nome, String cognome, String telefono, String email, Address indirizzo) {
        Persona entity = null;

        entity = Persona.builder()
                .nome(nome)
                .cognome(cognome)
                .telefono(telefono)
                .email(email)
                .indirizzo(indirizzo)
                .build();

        return entity;
    }// end of method


}// end of class
