package it.algos.springvaadin.entity.preferenza;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibArray;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 16-ott-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(Cost.TAG_PRE)
@Slf4j
public class PreferenzaService extends AlgosServiceImpl {

    private PreferenzaRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public PreferenzaService(@Qualifier(Cost.TAG_PRE) MongoRepository repository) {
        super(repository);
        this.repository = (PreferenzaRepository) repository; //casting per uso locale
    }// end of Spring constructor


    /**
     * Ricerca e creazione di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     *
     * @return la entity trovata o appena creata
     */
    public Preferenza findOrCrea(String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio) {
        return findOrCrea((Company) null, code, type, level, descrizione, value, riavvio, true);
    }// end of method


    /**
     * Ricerca e creazione di una entity (la crea se non la trova)
     * All properties
     *
     * @param company     (facoltativa)
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     * @param replica     per ogni company (facoltativo, di default true)
     *
     * @return la entity trovata o appena creata
     */
    public Preferenza findOrCrea(Company company, String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio, boolean replica) {
        if (nonEsiste(company, code)) {
            try { // prova ad eseguire il codice
                return (Preferenza) save(newEntity(0, company, code, type, level, descrizione, value, riavvio, replica));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return repository.findByCode(code);
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
    public Preferenza newEntity() {
        return newEntity(0, (Company) null, "", (PrefType) null, (ARoleType) null, "", (Object) null, PrefEffect.subito, true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio) {
        return newEntity(code, type, level, descrizione, value, riavvio, true);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     * @param replica     per ogni company (facoltativo, di default true)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio, boolean replica) {
        return newEntity(0, (Company) null, code, type, level, descrizione, value, riavvio, replica);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param company     (facoltativa)
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(Company company, String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio) {
        return newEntity(0, (Company) null, code, type, level, descrizione, value, riavvio, true);
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param ordine      (facoltativo, modificabile con controllo automatico prima del save se è zero)
     * @param company     (facoltativa)
     * @param code        sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     * @param type        di dato memorizzato (obbligatorio)
     * @param level       di accesso alla preferenza (obbligatorio)
     * @param descrizione visibile (obbligatoria)
     * @param value       valore della preferenza (obbligatorio)
     * @param riavvio     attivazione del programma per avere effetto (obbligatorio, di default false)
     * @param replica     per ogni company (facoltativo, di default true)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Preferenza newEntity(int ordine, Company company, String code, PrefType type, ARoleType level, String descrizione, Object value, PrefEffect riavvio, boolean replica) {
        Preferenza preferenza = new Preferenza(
                ordine == 0 ? this.getNewOrdine() : ordine,
                code,
                type,
                level,
                descrizione,
                type.objectToBytes(value),
                riavvio,
                replica);

        if (company != null) {
            preferenza.setCompany(company);
        } else {
            if (LibSession.isCompanyValida()) {
                preferenza.setCompany(LibSession.getCompany());
            }// end of if cycle
        }// end of if/else cycle

        return preferenza;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param code sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean nonEsiste(Company company, String code) {
        return repository.findByCompanyAndCode(company, code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     * Filtrato sulla azienda corrente.
     *
     * @param code sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Preferenza findByCode(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma il developer può vedere anche tutto
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAll() {
        if (LibSession.isDeveloper()) {
            return repository.findByOrderByOrdineAsc();
        }// end of if cycle

        return null;
    }// end of method


    /**
     * Returns all instances of the type
     * Usa MultiCompany, ma non obbligatoria -> ACompanyRequired.facoltativa
     * Tutte le entitiese che NON hanno indicata una company (valgono per l'applicazione e non per la company)
     * Lista ordinata
     *
     * @return lista ordinata di tutte le entities
     */
    public List findAllSenzaCompany() {
        return repository.findByCompanyOrderByOrdineAsc((Company) null);
    }// end of method


    /**
     * Returns all instances of the type.
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
    public List findAllByCompany(Company company) {
        ArrayList<Preferenza> listaAdmin;
        ArrayList<Preferenza> listaUser;

        if (LibSession.isDeveloper()) {
            return repository.findByCompanyOrderByOrdineAsc(company);
        } else {
            if (LibSession.isAdmin()) {
                listaAdmin = (ArrayList) repository.findByCompanyAndLivelloOrderByOrdineAsc(company, ARoleType.admin);
                listaUser = (ArrayList) repository.findByCompanyAndLivelloOrderByOrdineAsc(company, ARoleType.user);
                return LibArray.somma(listaAdmin, listaUser);
            } else {
                return repository.findByCompanyAndLivelloOrderByOrdineAsc(company, ARoleType.user);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * L'ordine di presentazione (obbligatorio, unico per tutte le company), viene calcolato in automatico prima del persist
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    private int getNewOrdine() {
        int ordine = 0;

        List<Preferenza> lista = repository.findTop1ByOrderByOrdineDesc();
        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    /**
     * Ricerca della preferenza col codice indicato
     *
     * @param code della preferenza specifica
     *
     * @return valore della preferenza, nella classe prevista da PrefType
     */
    public Boolean isTrue(String code) {
        return isTrue(code, false);
    } // end of method


    /**
     * Ricerca della preferenza col codice indicato
     *
     * @param code            della preferenza specifica
     * @param valoreSuggerito se la preferenza non viene trovata
     *
     * @return valore della preferenza, nella classe prevista da PrefType, se viene trovata
     * valoreSuggerito, se non trova la preferenza
     */
    public Boolean isTrue(String code, boolean valoreSuggerito) {
        return isTrue(LibSession.getCompany(), code, valoreSuggerito);
    } // end of method


    /**
     * Ricerca della preferenza col codice indicato
     * 1) Cerca per la company prevista
     * 2) Cerca per la company della sessione
     * 3) Cerca per company col valore nullo
     * 4) Usa il valoreSuggerito
     *
     * @param company         ACompanyRequired.facoltativa, se è nulla cerca nella sessione la company valida
     * @param code            della preferenza specifica
     * @param valoreSuggerito se la preferenza non viene trovata
     *
     * @return valore della preferenza, nella classe prevista da PrefType, se viene trovata
     * valoreSuggerito, se non trova la preferenza
     */
    public Boolean isTrue(Company company, String code, boolean valoreSuggerito) {
        boolean status = valoreSuggerito;
        Preferenza pref;
        PrefType type;
        byte[] value;

        if (company == null) {
            company = LibSession.getCompany();
        }// end of if cycle

        pref = repository.findByCompanyAndCode(company, code);

        if (pref != null) {
            type = pref.getType();
            if (type == PrefType.bool) {
                value = pref.getValue();
                status = (boolean) type.bytesToObject(value);
            }// end of if cycle
        }// end of if cycle

        return status;
    } // end of method

}// end of class
