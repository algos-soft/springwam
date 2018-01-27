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


    private ServizioRepository repository;

    @Autowired
    public ATextService text;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public ServizioService(@Qualifier(AppCost.TAG_SER) MongoRepository repository) {
        super(repository);
        super.entityClass = Servizio.class;
        this.repository = (ServizioRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione  (obbligatoria, non unica)
     * @param oraInizio    prevista di inizio turno (obbligatoria, se orario è true)
     * @param oraFine      prevista di fine turno (obbligatoria, se orario è true)
     *
     * @return la entity trovata o appena creata
     */
    public Servizio findOrCrea(String code, String descrizione, int oraInizio, int oraFine) {
        return findOrCrea( (Company) null, 0,code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, (List<Funzione>)null);
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova)
     * All properties
     * La company può essere facoltativa
     * Diventa obbligatoria se l'applicazione è AlgosApp.USE_MULTI_COMPANY
     * Se manca la prende dal Login
     * Se è obbligatoria e manca anche nel Login, va in errore
     *
     * @param company      di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
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
            Company company,
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

        if (nonEsiste(company, code)) {
            try { // prova ad eseguire il codice
                return (Servizio) save(newEntity(
                        company,
                        ordine,
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
        return newEntity("","",0,0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code         di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param descrizione  (obbligatoria, non unica)
     * @param oraInizio    prevista di inizio turno (obbligatoria, se orario è true)
     * @param oraFine      prevista di fine turno (obbligatoria, se orario è true)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Servizio newEntity(String code, String descrizione, int oraInizio, int oraFine) {
        return newEntity( (Company) null, 0,code, descrizione, 0, true, oraInizio, 0, oraFine, 0, true, (List<Funzione>)null);
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
     * @param company      di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
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
            Company company,
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

        Servizio entity = null;

        if (nonEsiste(company, code)) {
            entity = Servizio.builder()
                    .ordine(ordine)
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
                    .oraFine(oraFine)
                    .build();
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
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Servizio findByCode(String code) {
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
    public Servizio findByCompanyAndCode(Company company, String code) {
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
            return repository.findByCompanyOrderByCodeAsc(login.getCompany());
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
            return repository.findByCompanyOrderByCodeAsc(company);
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
        String code = ((Servizio) entityBean).getCode();

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


}// end of class
