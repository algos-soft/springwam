package it.algos.springvaadin.entity.log;

import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.logtype.Logtype;
import it.algos.springvaadin.entity.logtype.LogtypeService;
import it.algos.springvaadin.enumeration.EALogLevel;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.service.AService;
import it.algos.springvaadin.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Non esiste 'KeyUnica' e quindi non ha senso il metodo findOrCrea() (tipico degli altri XxxService) e si usa newCrea()
 */
@Slf4j
@Service
@Qualifier(ACost.TAG_LOG)
@AIScript(sovrascrivibile = false)
public class LogService extends AService {


    private LogRepository repository;


    @Autowired
    public LogtypeService typeService;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public LogService(@Qualifier(ACost.TAG_LOG) MongoRepository repository) {
        super(repository);
        this.repository = (LogRepository) repository;
        super.entityClass = Log.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     * Le entites di questa collezione non sono uniche, quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità di firma'. In realtà si potrebbe chiamare 'crea'
     *
     * @param livello:     rilevanza del log
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     *
     * @return la entity trovata o appena creata
     */
    public Log newCrea(EALogLevel livello, Logtype type, String descrizione) {
        return newCrea(livello, type, descrizione, "");
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param livello:     rilevanza del log
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     * @param note:        specifiche di dettaglio dell'evento
     *
     * @return la entity trovata o appena creata
     */
    public Log newCrea(EALogLevel livello, Logtype type, String descrizione, String note) {
        Log entity = newEntity(livello, type, descrizione, note);
        return (Log) save(entity);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Log newEntity() {
        return newEntity((EALogLevel) null, (Logtype) null, "", "");
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
     * @param livello:     rilevanza del log
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     * @param note:        specifiche di dettaglio dell'evento
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Log newEntity(EALogLevel livello, Logtype type, String descrizione, String note) {
        Log entity = null;

        entity = Log.builder()
                .livello(livello != null ? livello : EALogLevel.debug)
                .type(type)
                .descrizione(descrizione)
                .evento(LocalDateTime.now())
                .build();
        entity.note = note;

        return (Log) addCompany(entity);
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
            return repository.findByCompanyOrderByEventoDesc(company);
        }// end of if/else cycle
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
        return repository.findByOrderByEventoDesc();
    }// end of method

    /**
     * Registra un log di una entity modificata
     * Di default è di livello debug
     * Di default la company è quella di login
     *
     * @param modifiedBean: entity appena modificata
     * @param note:         singola modifica di una property
     */
    public void logSetup(AEntity modifiedBean, String note) {
        newCrea(EALogLevel.debug, typeService.getSetup(), modifiedBean.getClass().getSimpleName(), note);
    }// fine del metodo


    /**
     * Registra un log di una nuova entity
     * Di default è di livello info
     * Di default la company è quella di login
     *
     * @param newBean: nuova entity appena creata
     * @param note:    dettagli della scheda
     */
    public void logNew(AEntity newBean, String note) {
        newCrea(EALogLevel.info, typeService.getNew(), newBean.getClass().getSimpleName(), note);
    }// fine del metodo


    /**
     * Registra un log di una entity modificata
     * Di default è di livello info
     * Di default la company è quella di login
     *
     * @param modifiedBean: entity appena modificata
     * @param note:         singola modifica di una property
     */
    public void logEdit(AEntity modifiedBean, String note) {
        newCrea(EALogLevel.info, typeService.getEdit(), modifiedBean.getClass().getSimpleName(), note);
    }// fine del metodo


    /**
     * Registra un log della cancellazione di una entity
     * Di default è di livello info
     * Di default la company è quella di login
     *
     * @param deletedBean: entity appena cancellata
     * @param note:        dettagli della scheda
     */
    public void logDelete(AEntity deletedBean, String note) {
        newCrea(EALogLevel.info, typeService.getDelete(), deletedBean.getClass().getSimpleName(), note);
    }// fine del metodo

    /**
     * Registra un log di debug
     * Registra anche un log di sistema
     *
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     */
    public void debug(Logtype type, String descrizione) {
//        log.debug(logBase(EALogLevel.debug, type, descrizione));
    }// fine del metodo

    /**
     * Registra un log di info
     * Registra anche un log di sistema
     *
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     */
    public void info(Logtype type, String descrizione) {
//        log.info(logBase(EALogLevel.info, type, descrizione));
    }// fine del metodo


    /**
     * Registra un log di warn
     * Registra anche un log di sistema
     *
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     */
    public void warn(Logtype type, String descrizione) {
//        log.warn(logBase(EALogLevel.warn, type, descrizione));
    }// fine del metodo


    /**
     * Registra un log di error
     * Registra anche un log di sistema
     *
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     */
    public void error(Logtype type, String descrizione) {
//        log.error(logBase(EALogLevel.error, type, descrizione));
    }// fine del metodo


    /**
     * Spedisce una mail
     * Registra anche un log di sistema
     *
     * @param type:        raggruppamento logico dei log per categorie di eventi
     * @param descrizione: completa in forma testuale
     */
    public void mail(Logtype type, String descrizione) {
        // spedisce la mail @todo da realizzare
        log.info("Mail spedita: " + type + " - " + descrizione);
    }// fine del metodo

    /**
     * Registra un log del livello indicato
     * Registra anche un log di sistema
     * La data dell'evento di log viene inserita in automatico
     *
     * @param livello:         rilevanza del log
     * @param type:            raggruppamento logico dei log per categorie di eventi
     * @param newModifiedBean: nuova entity appena creata
     *
     * @return messaggio per il log di sistema
     */
    private String logBase(EALogLevel livello, Logtype type, AEntity newModifiedBean) {
        newCrea(livello, type, newModifiedBean.getClass().getSimpleName(), newModifiedBean.toString());
        return type + " - " + newModifiedBean.toString();
    }// fine del metodo

}// end of class
