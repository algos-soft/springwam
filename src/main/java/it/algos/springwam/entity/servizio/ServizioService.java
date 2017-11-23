package it.algos.springwam.entity.servizio;

import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by gac on 30-ott-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_SER)
@Slf4j
public class ServizioService extends AlgosServiceImpl {

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
        return findOrCrea(0, (Company) null, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param company     (obbligatoria, se manca usa quella presente in LibSession)
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Servizio findOrCrea(Company company, String code, String descrizione) {
        return findOrCrea(0, company, code, descrizione, 0, false, 0, 0, 0, 0, true, null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param company     (obbligatoria, se manca usa quella presente in LibSession)
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione (obbligatoria, non unica)
     * @param oraInizio   prevista di inizio turno (obbligatoria, se orario è true)
     * @param oraFine     prevista di fine turno (obbligatoria, se orario è true)
     *
     * @return la entity trovata o appena creata
     */
    public Servizio findOrCrea(Company company, String code, String descrizione, int oraInizio, int oraFine) {
        return findOrCrea(0, company, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, null);
    }// end of method


    /**
     * Ricerca una entity
     * Se la trova, la cancella
     * Crea una nuova entity
     * Properties obbligatorie
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param company      (obbligatoria, se manca usa quella presente in LibSession)
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione  (obbligatoria, non unica)
     * @param colore       del servizio (facoltativo)
     * @param orario       predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * @param oraInizio    prevista di inizio turno (obbligatoria, se orario è true)
     * @param minutiInizio previsti di inizio turno (facoltativo, standard è zero)
     * @param oraFine      prevista di fine turno (obbligatoria, se orario è true)
     * @param minutiFine   previsti di fine turno (facoltativo, standard è zero)
     * @param visibile     nel tabellone (facoltativo, default true) può essere disabilitato per servizi deprecati
     *
     * @return la entity trovata o appena creata
     */
    public Servizio deleteAndCrea(
            int ordine,
            Company company,
            String code,
            String descrizione,
            int colore,
            boolean orario,
            int oraInizio,
            int minutiInizio,
            int oraFine,
            int minutiFine,
            boolean visibile) {
        Servizio servizio;

        if (esiste(company, code)) {
            servizio = findByCompanyAndCode(company, code);
            delete(servizio);
        }// end of if cycle

        return findOrCrea(ordine, company, code, descrizione, colore, orario, oraInizio, minutiInizio, oraFine, minutiFine, visibile, null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     *
     * @param ordine       di presentazione nelle liste (obbligatorio, unico nella company,
     *                     con controllo automatico prima del save se è zero,  modificabile da developer ed admin)
     * @param companyTmp   (obbligatoria, se manca usa quella presente in LibSession)
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
            Company companyTmp,
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
        Company company = companyTmp != null ? companyTmp : LibSession.getCompany();

        if (company != null) {
            if (nonEsiste(company, code)) {
                try { // prova ad eseguire il codice
                    return (Servizio) save(newEntity(
                            ordine,
                            company,
                            code,
                            descrizione,
                            colore,
                            orario,
                            oraInizio,
                            minutiInizio,
                            oraFine,
                            minutiFine,
                            visibile,
                            funzioni));
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
        return newEntity(0, (Company) null, code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, null);
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
            Company companyTmp,
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
        Servizio entity = null;
        Company company = companyTmp != null ? companyTmp : LibSession.getCompany();

        if (company != null) {
            if (nonEsiste(company, code)) {
                entity = new Servizio(
                        ordine == 0 ? this.getNewOrdine(company) : ordine,
                        code,
                        descrizione,
                        colore,
                        orario,
                        oraInizio,
                        minutiInizio,
                        oraFine, minutiFine,
                        visibile,
                        funzioni);
                entity.setCompany(company);
            } else {
                return repository.findByCompanyAndCode(company, code);
            }// end of if/else cycle
        } else {
            log.error("newEntity senza Company");
        }// end of if/else cycle

        return entity;
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
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
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
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByCompanyAndCode(String code) {
        return repository.findByCompanyAndCode(LibSession.getCompany(), code);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company ACompanyRequired.obbligatoria
     * @param code    di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByCompanyAndCode(Company company, String code) {
        return repository.findByCompanyAndCode(company, code);
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
    public AEntity save(AEntity entityBean) throws Exception {
        Croce company = (Croce) ((ACompanyEntity) entityBean).getCompany();
        String code = ((Servizio) entityBean).getCode();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            fixFunzioni((Servizio) entityBean);
            return super.save(entityBean);
        } else {
            if (nonEsiste(company, code)) {
                return super.save(entityBean);
            } else {
                LibAvviso.warn("Esiste già un servizio con code " + LibText.setRossoBold(code) + " che deve essere unico");
                log.error("save un servizio già esistente per questa company");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method


    /**
     * In ServizioFieldFunzioni si usa una Grid con una riga finale vuota
     * che contiene un coboBox per inserire nuove funzioni
     * Va eliminata dall lista delle funzioni valide
     *
     * @param entityBean da salvare
     */
    private void fixFunzioni(Servizio entityBean) {
        List<Funzione> lista = entityBean.getFunzioni();
        Funzione lastFunz = null;

        if (lista != null && lista.size() > 0) {
            lastFunz = lista.get(lista.size() - 1);
            if (LibText.isEmpty(lastFunz.getId())) {
                lista.remove(lista.size() - 1);
                entityBean.setFunzioni(lista);
            }// end of if cycle
        }// end of if cycle

    }// end of method


    /**
     * L'ordine di presentazione (obbligatorio, unico nella company), viene calcolato in automatico prima del persist
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    private int getNewOrdine(Company croce) {
        int ordine = 0;

        List<Servizio> lista = repository.findTop1ByCompanyOrderByOrdineDesc(croce);

        if (lista != null && lista.size() == 1) {
            ordine = lista.get(0).getOrdine();
        }// end of if cycle

        return ordine + 1;
    }// end of method


    /**
     * Ritorna una stringa che rappresenta l'orario dalle... alle...
     *
     * @param entityBean da elaborare
     */
    public String getStrOrario(Servizio entityBean) {
        String value = "";
        String tagPunti = ":";
        String tagSep = " - ";

        if (entityBean.isOrario()) {
            value += strHM(entityBean.getOraInizio());
            value += tagPunti;
            value += strHM(entityBean.getMinutiInizio());
            value += tagSep;
            value += strHM(entityBean.getOraFine());
            value += tagPunti;
            value += strHM(entityBean.getMinutiFine());
        } else {
            value = "Variabile";
        }// end of if/else cycle

        return value;
    }// end of method

    /**
     * @return il numero di ore o minuti formattato su 2 caratteri fissi
     */
    private String strHM(int num) {
        String s = "" + num;

        if (s.length() == 1) {
            s = "0" + s;
        }// end of if cycle

        return s;
    }// end of method

}// end of class
