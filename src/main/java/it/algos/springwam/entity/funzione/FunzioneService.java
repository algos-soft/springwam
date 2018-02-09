package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
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

import java.util.ArrayList;
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


    /**
     * La repository viene iniettata dal costruttore, in modo che sia disponibile nella superclasse,
     * dove viene usata l'interfaccia MongoRepository
     * Spring costruisce al volo, quando serve, una implementazione di RoleRepository (come previsto dal @Qualifier)
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici
     */
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
        this.repository = (FunzioneRepository) repository;
        super.entityClass = Funzione.class;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova)
     * Properties obbligatorie
     *
     * @param code        di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * @param sigla       di codifica visibile (obbligatoria, non unica)
     * @param descrizione completa (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(String code, String sigla, String descrizione) {
        return findOrCrea(0, code, sigla, descrizione, (VaadinIcons) null, false);
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
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  completa (obbligatoria, non unica)
     * @param icona        VaadinIcons (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la entity trovata o appena creata
     */
    public Funzione findOrCrea(int ordine, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        Funzione entity = findByKeyUnica(code);

        if (entity == null) {
            entity = newEntity(ordine, code, sigla, descrizione, icona, obbligatoria);
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
    public Funzione newEntity() {
        return newEntity(0, "", "", "", (VaadinIcons) null, false);
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
     * @param sigla        di codifica visibile (obbligatoria, non unica)
     * @param descrizione  completa (obbligatoria, non unica)
     * @param icona        VaadinIcons (facoltativa)
     * @param obbligatoria (facoltativa) funzione obbligatoria per il servizio che la usa ('embedded')
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Funzione newEntity(int ordine, String code, String sigla, String descrizione, VaadinIcons icona, boolean obbligatoria) {
        Funzione entity = findByKeyUnica(code);

        if (entity == null) {
            entity = Funzione.builder()
                    .ordine(ordine != 0 ? ordine : this.getNewOrdine())
                    .code(code)
                    .sigla(sigla)
                    .descrizione(descrizione)
                    .icona(icona != null ? icona.getCodepoint() : 0)
                    .obbligatoria(obbligatoria)
                    .build();
        }// end of if cycle

        return (Funzione) addCompany(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param code di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByKeyUnica(String code) {
        return findByKeyUnica((Company) null, code);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica)
     *
     * @param company di riferimento (obbligatoria visto che è EACompanyRequired.obbligatoria)
     * @param code    di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Funzione findByKeyUnica(Company company, String code) {
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
     * @return lista di code
     */
    public List<String> findAllCode() {
        List lista = new ArrayList();
        List<Funzione> listaFunz = findAll();

        for (Funzione funz : listaFunz) {
            lista.add(funz.getCode());
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per tutte le eventuali company),
     * viene calcolato in automatico prima del persist sul database
     * Recupera il valore massimo della property
     * Incrementa di uno il risultato
     */
    public int getNewOrdine() {
        int ordine = 0;

        List<Funzione> lista = repository.findTop1ByCompanyOrderByOrdineDesc(login.getCompany());
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
            entityBean.id = ((Funzione) entityBean).getCompany().getCode() + ((Funzione) entityBean).getCode();
        }// end of if cycle

        return super.save(entityBean);
    }// end of method

}// end of class
