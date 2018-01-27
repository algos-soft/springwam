package it.algos.springvaadin.entity.stato;

import it.algos.springvaadin.annotation.AIScript;
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
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@SpringComponent
@Service
@Scope("singleton")
@Qualifier(ACost.TAG_STA)
@AIScript(sovrascrivibile = false)
public class StatoService extends AService {



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
        this.repository = (StatoRepository) repository;
        super.entityClass = Stato.class;
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
        Stato entity = findByKeyUnica(nome);

        if (entity == null) {
            entity = newEntity(ordine, nome, alfaDue, alfaTre, numerico, bandiera);
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
    public Stato newEntity() {
        return newEntity(0, "", "", "", "", (byte[]) null);
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
        Stato entity = findByKeyUnica(nome);

        if (entity == null) {
            entity = Stato.builder()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .nome(nome)
                    .alfaDue(alfaDue)
                    .alfaTre(alfaTre)
                    .numerico(numerico)
                    .bandiera(bandiera)
                    .build();
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param nome corrente completo, non ufficiale (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Stato findByKeyUnica(String nome) {
        return repository.findByNome(nome);
    }// end of method


//    /**
//     * Returns all instances of the type
//     * La Entity è EACompanyRequired.nonUsata. Non usa Company.
//     * Lista ordinata
//     *
//     * @return lista ordinata di tutte le entities
//     */
//    @Override
//    public List findAll() {
//        return repository.findByOrderByOrdineAsc();
//    }// end of method


//    /**
//     * Saves a given entity.
//     * Use the returned instance for further operations
//     * as the save operation might have changed the entity instance completely.
//     *
//     * @param entityBean da salvare
//     *
//     * @return the saved entity
//     */
//    @Override
//    public AEntity save(AEntity entityBean)  {
//        String code = ((Stato) entityBean).getNome();
//
//        if (entityBean == null) {
//            return null;
//        }// end of if cycle
//
//        if (text.isValid(entityBean.id)) {
//            return super.save(entityBean);
//        } else {
//            if (nonEsiste(code)) {
//                return super.save(entityBean);
//            } else {
//                log.error("Ha cercato di salvare una entity già esistente, ma unica");
//                return null;
//            }// end of if/else cycle
//        }// end of if/else cycle
//    }// end of method


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
