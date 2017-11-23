package it.algos.springwam.entity.croce;

import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springwam.application.AppCost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gac on 31-ott-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Service
@Qualifier(AppCost.TAG_CRO)
@Slf4j
public class CroceService extends AlgosServiceImpl {

    private CroceRepository repository;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public CroceService(@Qualifier(AppCost.TAG_CRO) MongoRepository repository) {
        super(repository);
        this.repository = (CroceRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     *
     * @return la entity trovata o appena creata
     */
    public Croce findOrCrea(String code, String descrizione) {
        return findOrCrea((Organizzazione) null, (Persona) null, code, descrizione, (Persona) null, "", "", (Indirizzo) null);
    }// end of method

    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * All properties
     *
     * @param organizzazione (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contact        persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param email          della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     *
     * @return la entity trovata o appena creata
     */
    public Croce findOrCrea(Organizzazione organizzazione, Persona presidente, String code, String descrizione, Persona contact, String telefono, String email, Indirizzo indirizzo) {
        if (nonEsiste(code)) {
            try { // prova ad eseguire il codice
                return (Croce) save(newEntity(organizzazione, presidente, code, descrizione, contact, telefono, email, indirizzo));
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            return repository.findByCode(code);
        }// end of if cycle
    }// end of method

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Croce newEntity() {
        return newEntity("", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Properties obbligatorie
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param code        di riferimento interno (obbligatorio ed unico)
     * @param descrizione ragione sociale o descrizione della company (visibile - obbligatoria)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Croce newEntity(String code, String descrizione) {
        return newEntity((Organizzazione) null, (Persona) null, code, descrizione, (Persona) null, "", "", (Indirizzo) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param organizzazione (facoltativo)
     * @param presidente     (facoltativo)
     * @param code           di riferimento interno (obbligatorio ed unico)
     * @param descrizione    ragione sociale o descrizione della company (visibile - obbligatoria)
     * @param contact        persona di riferimento (facoltativo)
     * @param telefono       della company (facoltativo)
     * @param email          della company (facoltativo)
     * @param indirizzo      della company (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Croce newEntity(Organizzazione organizzazione, Persona presidente, String code, String descrizione, Persona contact, String telefono, String email, Indirizzo indirizzo) {
        Croce croce = null;
        if (nonEsiste(code)) {
            try { // prova ad eseguire il codice
                croce = new Croce(organizzazione, presidente);
                croce.setCode(code);
                croce.setDescrizione(descrizione);
                croce.setContatto(contact);
                croce.setTelefono(telefono);
                croce.setEmail(email);
                croce.setIndirizzo(indirizzo);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
                return null;
            }// fine del blocco try-catch
        } else {
            croce =  repository.findByCode(code);
        }// end of if/else cycle

        return croce;
    }// end of method

    /**
     * Controlla che non esista una istanza della Entity usando la property specifica (obbligatoria ed unica)
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return vero se non esiste, false se trovata
     */
    public boolean nonEsiste(String code) {
        return repository.findByCode(code) == null;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di riferimento interno (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Croce findByCode(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Returns all instances of the type
     * Non usa MultiCompany, ma va comunque filtrata su se stessa
     * Lista ordinata
     *
     * @return lista di tutte le entities
     */
    public List findAll() {
        if (LibSession.isCompanyValida()) {
            List lista = new ArrayList();
            lista.add(LibSession.getCompany());
            return lista;
        } else {
            if (LibSession.isDeveloper()) {
                return repository.findByOrderByCodeAsc();
            } else {
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

    public List<Croce> findAllAll() {
        return repository.findByOrderByCodeAsc();
    }// end of method


}// end of class
