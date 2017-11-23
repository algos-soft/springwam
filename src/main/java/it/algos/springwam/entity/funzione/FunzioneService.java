package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.croce.Croce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 24-set-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_FUN)
@Slf4j
public class FunzioneService extends AlgosServiceImpl {

    private FunzioneRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public FunzioneService(@Qualifier(AppCost.TAG_FUN) FunzioneRepository repository) {
        super(repository);
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
        return findOrCrea(0, (Company) null, code, sigla, descrizione, null, false);
    }// end of method

    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param company     (obbligatoria, se manca usa quella presente in LibSession)
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(Company company, String code, String sigla, String descrizione) {
        return findOrCrea(0, company, code, sigla, descrizione, null, false);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param company     (obbligatoria, se manca usa quella presente in LibSession)
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     * @param icona       (facoltativa)
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(Company company, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        return findOrCrea(0, company, code, sigla, descrizione, icona, obbligatoria);
    }// end of method


    /**
     * Ricerca una entity
     * Se la trova, la cancella
     * Crea una nuova entity
     * All properties
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param company      (obbligatoria, se manca usa quella presente in LibSession, se manca anche quella non esegue)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  (obbligatoria, non unica)
     * @param icona        (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la entity trovata o appena creata
     */
    public Funzione deleteAndCrea(int ordine, Company company, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        Funzione funzione;

        if (esiste(company, code)) {
            funzione = findByCompanyAndCode(company, code);
            delete(funzione);
        }// end of if cycle

        return findOrCrea(ordine, company, code, sigla, descrizione, icona, obbligatoria);
    }// end of method

    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param companyTmp   (obbligatoria, se manca usa quella presente in LibSession)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  (obbligatoria, non unica)
     * @param icona        (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(int ordine, Company companyTmp, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        Company company = companyTmp != null ? companyTmp : LibSession.getCompany();

        if (company != null) {
            if (nonEsiste(company, code)) {
                try { // prova ad eseguire il codice
                    return (Funzione) save(newEntity(ordine, company, code, sigla, descrizione, icona, obbligatoria));
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                    return null;
                }// fine del blocco try-catch
            } else {
                return findByCompanyAndCode(company, code);
            }// end of if/else cycle
        } else {
            log.error("findOrCrea senza Company");
            return null;
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
        return newEntity(0, (Company) null, "", "", "", null, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(String code, String sigla, String descrizione) {
        return newEntity(0, (Company) null, code, sigla, descrizione, null, false);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param companyTmp   (obbligatoria, se manca usa quella presente in LibSession)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  (obbligatoria, non unica)
     * @param icona        (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(int ordine, Company companyTmp, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        Funzione funzione = null;
        Company company = companyTmp != null ? companyTmp : LibSession.getCompany();

        if (company != null) {
            if (nonEsiste(company, code)) {
                funzione = new Funzione(
                        ordine == 0 ? this.getNewOrdine(company) : ordine,
                        code,
                        sigla,
                        descrizione,
                        icona != null ? icona.getCodepoint() : 0,
                        obbligatoria);
                funzione.setCompany(company);
            } else {
                return repository.findByCompanyAndCode(company, code);
            }// end of if/else cycle
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return funzione;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param company ACompanyRequired.obbligatoria
     * @param code    sigla di riferimento interna (interna, obbligatoria ed unica per la company)
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
     * @param code    sigla di riferimento interna (interna, obbligatoria ed unica per la company)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Company company, String code) {
        return findByCompanyAndCode(company, code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param keyId di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findById(String keyId) {
        return repository.findById(keyId);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByCode(String code) {
        return findByCompanyAndCode(LibSession.getCompany(), code);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company ACompanyRequired.obbligatoria
     * @param code    di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByCompanyAndCode(Company company, String code) {
        return repository.findByCompanyAndCode(company, code);
    }// end of method


    /**
     * @return lista di code
     */
    public List<String> findAllCode() {
        return findAllCodeByCompany(LibSession.getCompany());
    }// end of method


    /**
     * @return lista di code
     */
    public List<String> findAllCodeByCompany(Company company) {
        List lista = new ArrayList();
        List<Funzione> listaFunz = findAllByCompany(company);

        for (Funzione funz : listaFunz) {
            lista.add(funz.getCode());
        }// end of for cycle

        return lista;
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
            return repository.findByOrderByCompanyAscOrdineAsc();
        }// end of if cycle

        return null;
    }// end of method


    /**
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List findAllByCompany() {
        return findAllByCompany(LibSession.getCompany());
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
            if (LibSession.isDeveloper()) {
                return findAll();
            } else {
                return null;
            }// end of if/else cycle
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
    public AEntity save(AEntity entityBean) throws Exception {
        Croce company = (Croce) ((ACompanyEntity) entityBean).getCompany();
        String code = ((Funzione) entityBean).getCode();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, code)) {
                return super.save(entityBean);
            } else {
                LibAvviso.warn("Esiste già una funzione con code " + LibText.setRossoBold(code) + " che deve essere unico");
                log.error("save una funzione già esistente per questa company");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method

    /**
     * L'ordine di presentazione (obbligatorio, unico nella company), viene calcolato in automatico prima del persist
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    private int getNewOrdine(Company company) {
        int ordine = 0;

        List<Funzione> lista = repository.findTop1ByCompanyOrderByOrdineDesc(company);

        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method

}// end of class
