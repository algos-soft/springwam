package it.algos.springvaadin.entity.versione;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by gac on 01/06/17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(Cost.TAG_VERS)
@Slf4j
public class VersioneService extends AlgosServiceImpl {


    //--il service (contenente la repository) viene iniettato nel costruttore
    public VersioneRepository repository;


    //--il service (contenente la repository) viene iniettato nel costruttore
    private LogService logger;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public VersioneService(
            @Qualifier(Cost.TAG_VERS) MongoRepository repository,
            @Qualifier(Cost.TAG_LOG) AlgosService logger) {
        super(repository);
        this.repository = (VersioneRepository) repository;
        this.logger = (LogService) logger;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     * L'esistenza di una entity di questa collezione è già stata controllata, quindi NON viene ricontrollata
     * L'ordine viene inserito automaticamente da newEntity()
     *
     * @param progetto    (obbligatorio, non unica)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Versione crea(String progetto, String gruppo, String descrizione) {
        return crea(progetto, (Company) null, gruppo, descrizione);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     * L'esistenza di una entity di questa collezione è già stata controllata, quindi NON viene ricontrollata
     * L'ordine viene inserito automaticamente da newEntity()
     *
     * @param progetto    (obbligatorio, non unica)
     * @param company     (facoltativa)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Versione crea(String progetto, Company company, String gruppo, String descrizione) {
        try { // prova ad eseguire il codice
            return (Versione) save(newEntity(progetto, company, gruppo, descrizione));
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
    public Versione newEntity() {
        return newEntity("", (Company) null, 0, "", "", (LocalDate) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param progetto    (obbligatorio, non unica)
     * @param company     (facoltativa)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(String progetto, Company company, String gruppo, String descrizione) {
        return newEntity(progetto, company, 0, gruppo, descrizione, (LocalDate) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param progetto    (obbligatorio, non unica)
     * @param company     (facoltativa)
     * @param ordine      (obbligatorio, unico indipendentemente dalla company,
     *                    con controllo automatico prima del save se è zero, non modificabile)
     * @param gruppo      codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     * @param evento      momento in cui si effettua la edit della versione (obbligatoria, non unica, non modificabile)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(String progetto, Company company, int ordine, String gruppo, String descrizione, LocalDate evento) {
        Versione versione = new Versione(
                progetto,
                ordine == 0 ? this.getNewOrdine(progetto) : ordine,
                gruppo,
                descrizione,
                evento != null ? evento : LocalDate.now());

        if (company != null) {
            versione.setCompany(company);
        } else {
            if (LibSession.isCompanyValida()) {
                versione.setCompany(LibSession.getCompany());
            }// end of if cycle
        }// end of if/else cycle

        return versione;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param progetto (obbligatorio, non unica)
     * @param ordine   (obbligatorio, unico indipendentemente dalla company,
     *                 con controllo automatico prima del save se è zero, non modificabile)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(String progetto, int ordine) {
        return findByProgettoAndOrdine(progetto, ordine) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param progetto (obbligatorio, non unica)
     * @param ordine   (obbligatorio, unico indipendentemente dalla company,
     *                 con controllo automatico prima del save se è zero, non modificabile)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String progetto, int ordine) {
        return findByProgettoAndOrdine(progetto, ordine) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param progetto (obbligatorio, non unica)
     * @param ordine   di versione (obbligatorio, unico, con controllo automatico prima del save se è zero, non modificabile)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Versione findByProgettoAndOrdine(String progetto, int ordine) {
        return repository.findByProgettoAndOrdine(progetto, ordine);
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma il developer può vedere anche tutto
     * Lista ordinata
     *
     * @return lista di tutte le entities
     */
    public List findAll() {
        if (LibSession.isDeveloper()) {
            return repository.findByOrderByProgetto();
        }// end of if cycle

        return null;
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
        return repository.findByCompanyOrderByOrdineAsc(company);
    }// end of method


    /**
     * Controlla se esiste il numero di versione da installare
     * I numeri delle versione eventualmente cancellate NON vengono sostituiti
     * Non cerca quindi numeroVersioneDaInstallare ma lo confronta col massimo esistente
     *
     * @param progetto                   (obbligatorio, non unica)
     * @param numeroVersioneDaInstallare per vedere che sia superiore al massimo attuale
     *
     * @return true se la versione non è mai esistita (ne adesso ne dopo cancellazione)
     */
    public boolean versioneNonAncoraUsata(String progetto, int numeroVersioneDaInstallare) {
        boolean installa = false;
        int numeroVersioneEsistente = getNewOrdine(progetto) - 1;

        if (numeroVersioneDaInstallare > numeroVersioneEsistente) {
            installa = true;
        }// fine del blocco if

        return installa;
    }// end of static method


    /**
     * L'ordine di presentazione (obbligatorio, unico per tutte le company), viene calcolato in automatico prima del persist
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     *
     * @param progetto (obbligatorio, non unica)
     */
    private int getNewOrdine(String progetto) {
        int ordine = 0;

        List<Versione> lista = repository.findTop1ByProgettoOrderByOrdineDesc(progetto);
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


}// end of class
