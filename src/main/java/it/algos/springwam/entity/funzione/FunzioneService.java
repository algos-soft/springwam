package it.algos.springwam.entity.funzione;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
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
 * Date: 2018-01-13_16:21:12
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Slf4j
@Service
@Qualifier(AppCost.TAG_FUN)
@AIScript(sovrascrivibile = false)
public class FunzioneService extends AService {


    private FunzioneRepository repository;

    @Autowired
    public ATextService text;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public FunzioneService(@Qualifier(AppCost.TAG_FUN) MongoRepository repository) {
        super(repository);
        super.entityClass = Funzione.class;
        this.repository = (FunzioneRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(String code, String sigla, String descrizione) {
        return findOrCrea((Company) null, 0, code, sigla, descrizione, 0, false);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param company      di riferimento (obbligatoria=EACompanyRequired.obbligatoria), se manca la prende dal Login
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  (obbligatoria, non unica)
     * @param icona        (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(Company company, int ordine, String code, String sigla, String descrizione, int icona, boolean obbligatoria) {
        if (nonEsiste(company, code)) {
            try { // prova ad eseguire il codice
                return (Funzione) save(newEntity(company, ordine, code, sigla, descrizione, icona, obbligatoria));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return findByCompanyAndCode(company, code);
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
    public Funzione newEntity() {
        return newEntity((Company) null, 0, "", "", "", 0, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Properties obbligatorie
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(int ordine, String code, String sigla, String descrizione, int icona, boolean obbligatoria) {
        return newEntity((Company) null, 0, code, sigla, descrizione, 0, false);
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
     * @param company      di riferimento (obbligatoria=EACompanyRequired.obbligatoria), se manca la prende dal Login
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  (obbligatoria, non unica)
     * @param icona        (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(Company company, int ordine, String code, String sigla, String descrizione, int icona, boolean obbligatoria) {
        Funzione entity = null;

        if (nonEsiste(company, code)) {
            entity = Funzione.builder().ordine(ordine != 0 ? ordine : this.getNewOrdine()).code(code).sigla(sigla).descrizione(descrizione).icona(icona).obbligatoria(obbligatoria).build();
            entity.company = company != null ? company : login.getCompany();
        } else {
            entity = findByCompanyAndCode(company, code);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company ACompanyRequired.obbligatoria
     * @param code    di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(Company company, String code) {
        return findByCompanyAndCode(company, code) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company ACompanyRequired.obbligatoria
     * @param code    di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Company company, String code) {
        return findByCompanyAndCode(company, code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByCode(String code) {
        return this.findByCompanyAndCode((Company) null, code);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param code    di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByCompanyAndCode(Company company, String code) {
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
            return repository.findByCompanyOrderByOrdineAsc(company);
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
    public AEntity save(AEntity entityBean) {
        Company company = ((ACEntity) entityBean).getCompany();
        String code = ((Funzione) entityBean).getCode();

        if (text.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, code)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente per questa company");
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

        List<Funzione> lista = repository.findTop1ByCompanyOrderByOrdineDesc(login.getCompany());
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method

}// end of class
