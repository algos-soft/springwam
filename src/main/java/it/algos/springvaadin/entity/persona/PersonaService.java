package it.algos.springvaadin.entity.persona;

import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gac on 11-ott-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(Cost.TAG_PER)
@Slf4j
public class PersonaService extends AlgosServiceImpl {


    public PersonaRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public PersonaService(@Qualifier(Cost.TAG_PER) MongoRepository repository) {
        super(repository);
        this.repository = (PersonaRepository) repository; //casting per uso locale
    }// end of Spring constructor


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * Properties obbligatorie
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param nome:    obbligatorio
     * @param cognome: obbligatorio
     *
     * @return la entity trovata o appena creata
     */
    public Persona findOrCrea(String nome, String cognome) {
        return findOrCrea(nome, cognome, "", "", (Indirizzo) null);
    }// end of method


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * All properties
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param nome:      obbligatorio
     * @param cognome:   obbligatorio
     * @param telefono:  facoltativo
     * @param email:     facoltativo
     * @param indirizzo: via, nome e numero (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Persona findOrCrea(String nome, String cognome, String telefono, String email, Indirizzo indirizzo) {
        return newEntity(nome, cognome, telefono, email, indirizzo);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    @Override
    public Persona newEntity() {
        return newEntity("", "", "", "", (Indirizzo) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     *
     * @param nome:    obbligatorio
     * @param cognome: obbligatorio
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    public Persona newEntity(String nome, String cognome) {
        return newEntity(nome, cognome, "", "", (Indirizzo) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome:      obbligatorio
     * @param cognome:   obbligatorio
     * @param telefono:  facoltativo
     * @param email:     facoltativo
     * @param indirizzo: via, nome e numero (facoltativo)
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    public Persona newEntity(String nome, String cognome, String telefono, String email, Indirizzo indirizzo) {
        return new Persona(nome, cognome, telefono, email, indirizzo);
    }// end of method

    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma non obbligatoria -> ACompanyRequired.facoltativa
     * Filtrata sulla company indicata
     * Se la company è nulla, prende solo le entities che hanno la property company=null
     * (questo perché la property company NON è obbligatoria; se lo fosse, prenderebbe tutte le entities)
     * Lista ordinata
     *
     * @param company ACompanyRequired.facoltativa
     *
     * @return entities filtrate
     */
    @Override
    public List findAllByCompany(Company company) {
        return repository.findByCompanyOrderByCognomeAsc(company);
    }// end of method

}// end of class
