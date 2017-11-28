package it.algos.springwam.entity.iscrizione;

import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.utente.Utente;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_ISC)
@Slf4j
public class IscrizioneService extends AlgosServiceImpl {

    private IscrizioneRepository repository;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public IscrizioneService(@Qualifier(AppCost.TAG_ISC) MongoRepository repository) {
        super(repository);
        this.repository = (IscrizioneRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * Properties obbligatorie
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param turno    di riferimento (obbligatorio)
     * @param utente   di riferimento (obbligatorio)
     * @param funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     *
     * @return la entity trovata o appena creata
     */
    public Iscrizione findOrCrea(Turno turno, Utente utente, Funzione funzione) {
        return findOrCrea(turno, utente, funzione, (LocalDateTime) null, 0);
    }// end of method


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * All properties
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param turno     di riferimento (obbligatorio)
     * @param utente    di riferimento (obbligatorio)
     * @param funzione  per cui il milite/volontario/utente si iscrive (obbligatorio)
     * @param timestamp di creazione (obbligatorio, inserito in automatico)
     * @param durata    effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     *
     * @return la entity trovata o appena creata
     */
    public Iscrizione findOrCrea(Turno turno, Utente utente, Funzione funzione, LocalDateTime timestamp, int durata) {
        return newEntity(turno, utente, funzione, timestamp, durata);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    @Override
    public Iscrizione newEntity() {
        return newEntity((Turno) null, (Utente) null, (Funzione) null, (LocalDateTime) null, 0);
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param turno    di riferimento (obbligatorio)
     * @param utente   di riferimento (obbligatorio)
     * @param funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity(Turno turno, Utente utente, Funzione funzione) {
        return newEntity(turno, utente, funzione, (LocalDateTime) null, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param turno     di riferimento (obbligatorio)
     * @param utente    di riferimento (obbligatorio)
     * @param funzione  per cui il milite/volontario/utente si iscrive (obbligatorio)
     * @param timestamp di creazione (obbligatorio, inserito in automatico)
     * @param durata    effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Iscrizione newEntity(Turno turno, Utente utente, Funzione funzione, LocalDateTime timestamp, int durata) {
        Iscrizione entity = null;
        int durataPrevistaNelTurno = turno != null ? turno.getServizio().getOraInizio() : 0;

        if (nonEsiste(turno, utente)) {
            entity = new Iscrizione(
                    turno,
                    utente,
                    funzione,
                    timestamp != null ? timestamp : LocalDateTime.now(),
                    durata > 0 ? durata : durataPrevistaNelTurno,
                    false,
                    "",
                    false);
        } else {
            return findByTurnoAndUtente(turno, utente);
        }// end of if/else cycle

        return entity;
    }// end of method


    /**
     * Controlla che esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param turno  di riferimento (obbligatorio)
     * @param utente di riferimento (obbligatorio)
     *
     * @return vero se esiste, false se non trovata
     */
    public boolean esiste(Turno turno, Utente utente) {
        return findByTurnoAndUtente(turno, utente) != null;
    }// end of method


    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param turno  di riferimento (obbligatorio)
     * @param utente di riferimento (obbligatorio)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(Turno turno, Utente utente) {
        return findByTurnoAndUtente(turno, utente) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param turno  di riferimento (obbligatorio)
     * @param utente di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Iscrizione findByTurnoAndUtente(Turno turno, Utente utente) {
        return repository.findByTurnoAndUtente(turno, utente);
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
            return repository.findByOrderByTurnoAsc();
        }// end of if cycle

        return null;
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
        Turno turno = ((Iscrizione) entityBean).getTurno();
        Utente utente = ((Iscrizione) entityBean).getUtente();

        if (entityBean == null) {
            return null;
        }// end of if cycle

        if (LibText.isValid(entityBean.id)) {
            return super.save(entityBean);
        } else {
            if (nonEsiste(turno, utente)) {
                return super.save(entityBean);
            } else {
                log.error("Ha cercato di salvare una entity già esistente, ma unica");
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

}// end of class
