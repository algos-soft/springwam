package it.algos.springwam.entity.servizio;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.entity.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-16_08:50:45
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Slf4j
@Service
@Qualifier(AppCost.TAG_SER)
@AIScript(sovrascrivibile = false)
public class ServizioService extends AService {


    @Autowired
    public ATextService text;


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
    private ServizioRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public ServizioService(@Qualifier(AppCost.TAG_SER) MongoRepository repository) {
        super(repository);
        this.repository = (ServizioRepository) repository;
        super.entityClass = Servizio.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione (obbligatoria, non unica)
     * @param oraInizio   prevista di inizio turno (obbligatoria, se orario è true)
     * @param oraFine     prevista di fine turno (obbligatoria, se orario è true)
     *
     * @return la entity trovata o appena creata
     */
    public Servizio findOrCrea(String code, String descrizione, int oraInizio, int oraFine) {
        return findOrCrea(0, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, (List<Funzione>) null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione  (obbligatoria, non unica)
     * @param colore       del servizio (facoltativo)
     * @param orario       predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param oraInizio    prevista di inizio turno (obbligatoria, se orario è true)
     * @param minutiInizio previsti di inizio turno (facoltativo, standard è zero)
     * @param oraFine      prevista di fine turno (obbligatoria, se orario è true)
     * @param minutiFine   previsti di fine turno (facoltativo, standard è zero)
     * @param visibile     nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     * @param funzioni     del servizio (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Servizio findOrCrea(
            int ordine,
            String code,
            String descrizione,
            int colore,
            boolean orario,
            int oraInizio,
            int minutiInizio,
            int oraFine,
            int minutiFine,
            boolean visibile,
            List<Funzione> funzioni) {
        Servizio entity = findByKeyUnica(code);

        if (entity == null) {
            entity = newEntity(ordine, code, descrizione, colore, orario, oraInizio, minutiInizio, oraFine, minutiFine, visibile, funzioni);
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
    public Servizio newEntity() {
        return newEntity("", "", 0, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione (obbligatoria, non unica)
     * @param oraInizio   prevista di inizio turno (obbligatoria, se orario è true)
     * @param oraFine     prevista di fine turno (obbligatoria, se orario è true)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity(String code, String descrizione, int oraInizio, int oraFine) {
        return newEntity(0, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, (List<Funzione>) null);
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
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione  (obbligatoria, non unica)
     * @param colore       del servizio (facoltativo)
     * @param orario       predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param oraInizio    prevista di inizio turno (obbligatoria, se orario è true)
     * @param minutiInizio previsti di inizio turno (facoltativo, standard è zero)
     * @param oraFine      prevista di fine turno (obbligatoria, se orario è true)
     * @param minutiFine   previsti di fine turno (facoltativo, standard è zero)
     * @param visibile     nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     * @param funzioni     del servizio (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity(
            int ordine,
            String code,
            String descrizione,
            int colore,
            boolean orario,
            int oraInizio,
            int minutiInizio,
            int oraFine,
            int minutiFine,
            boolean visibile,
            List<Funzione> funzioni) {
        Servizio entity = findByKeyUnica(code);

        if (entity == null) {
            entity = Servizio.builder()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .code(code)
                    .descrizione(descrizione)
                    .colore(colore)
                    .orario(orario)
                    .oraInizio(oraInizio)
                    .minutiInizio(minutiInizio)
                    .oraFine(oraFine)
                    .minutiFine(minutiFine)
                    .visibile(visibile)
                    .funzioni(funzioni)
                    .build();
        }// end of if cycle

        return (Servizio) addCompany(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByKeyUnica(String code) {
        return findByKeyUnica(login.getCompany(), code);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param code    di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByKeyUnica(Company company, String code) {
        return repository.findByCompanyAndCode(company != null ? company : login.getCompany(), code);
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma il developer può vedere anche tutto
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll() {
        if (login.isDeveloper()) {
            return repository.findByOrderByCodeAsc();
        } else {
            return repository.findByCompanyOrderByOrdineAsc(login.getCompany());
        }// end of if/else cycle
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List findAllByCompanyVisibili() {
        Company company = login.getCompany();

        if (company != null) {
            return repository.findByCompanyAndVisibileOrderByOrdineAsc(company, true);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company),
     * viene calcolato in automatico prima del persist sul database
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    public int getNewOrdine() {
        int ordine = 0;

        List<Servizio> lista = repository.findTop1ByCompanyOrderByOrdineDesc(login.getCompany());
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
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
            entityBean.id = ((Servizio) entityBean).getCompany().getCode() + ((Servizio) entityBean).getCode();
        }// end of if cycle

        return super.save(entityBean);
    }// end of method

}// end of class
