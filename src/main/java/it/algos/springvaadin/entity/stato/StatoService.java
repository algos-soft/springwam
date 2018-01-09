package it.algos.springvaadin.entity.stato;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.role.Role;
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
@Qualifier(ACost.TAG_STA)
public class StatoService extends AService {


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private StatoRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public StatoService(@Qualifier(ACost.TAG_STA) MongoRepository repository) {
        super(repository);
        super.entityClass = Stato.class;
        this.repository = (StatoRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Stato findOrCrea(String nome) {
        return findOrCrea(0, nome, "", "", "", (byte[]) null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine   di presentazione (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param nome     corrente completo, non ufficiale (obbligatorio ed unico)
     * @param alfaDue  codice alfabetico di 2 cifre (obbligatorio, unico)
     * @param alfaTre  codice alfabetico di 3 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-3
     * @param numerico codice numerico di 3 cifre numeriche (facoltativo, vuoto oppure unico). Codifica ISO 3166-1 numerico
     * @param bandiera immagine (facoltativo, unico).
     *
     * @return la entity trovata o appena creata
     */
    public Stato findOrCrea(int ordine, String nome, String alfaDue, String alfaTre, String numerico, byte[] bandiera) {
        if (nonEsiste(nome)) {
            try { // prova ad eseguire il codice
                return (Stato) save(newEntity(ordine, nome, alfaDue, alfaTre, numerico, bandiera));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return findByNome(nome);
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
    public Stato newEntity() {
        return newEntity("");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Stato newEntity(String nome) {
        return newEntity(0, nome, "", "", "", (byte[]) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param ordine   di presentazione (obbligatorio, unico, con inserimento automatico se è zero, non modificabile)
     * @param nome     corrente completo, non ufficiale (obbligatorio ed unico)
     * @param alfaDue  codice alfabetico di 2 cifre (obbligatorio, unico)
     * @param alfaTre  codice alfabetico di 3 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-3
     * @param numerico codice numerico di 3 cifre numeriche (facoltativo, vuoto oppure unico). Codifica ISO 3166-1 numerico
     * @param bandiera immagine (facoltativo, unico).
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Stato newEntity(int ordine, String nome, String alfaDue, String alfaTre, String numerico, byte[] bandiera) {
        Stato entity = null;

        if (nonEsiste(nome)) {
            entity = Stato.builder().ordine(ordine != 0 ? ordine : this.getNewOrdine()).nome(nome).alfaDue(alfaDue).alfaTre(alfaTre).numerico(numerico).bandiera(bandiera).build();
        } else {
            return findByNome(nome);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(String nome) {
        return findByNome(nome) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String nome) {
        return findByNome(nome) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Stato findByNome(String nome) {
        return repository.findByNome(nome);
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
        return repository.findByOrderByOrdineAsc();
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
        String code = ((Stato) entityBean).getNome();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (text.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(code)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente, ma unica");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company),
     * viene calcolato in automatico prima del persist sul database
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    private int getNewOrdine() {
        int ordine = 0;

        List<Stato> lista = repository.findTop1ByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method

}// end of class
