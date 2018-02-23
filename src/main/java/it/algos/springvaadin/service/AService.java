package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.log.LogService;
import it.algos.springvaadin.enumeration.*;
import it.algos.springvaadin.exception.DuplicateException;
import it.algos.springvaadin.exception.NotCompanyEntityException;
import it.algos.springvaadin.exception.NullCompanyException;
import it.algos.springvaadin.login.ALogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:36
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public abstract class AService implements IAService {


    @Autowired
    public AAnnotationService annotation;


    @Autowired
    public AReflectionService reflection;


    @Autowired
    public ASessionService session;


    @Autowired
    public AArrayService array;


    @Autowired
    public ATextService text;


    /**
     * Inietta da Spring come 'session'
     */
    @Autowired
    public ALogin login;

    @Autowired
    private LogService logger;

    //--la repository dei dati viene iniettata dal costruttore della sottoclasse concreta
    public MongoRepository repository;


    //--il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
    public Class<? extends AEntity> entityClass;


    /**
     * Default constructor
     */
    public AService( ) {
    }// end of constructor


    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public AService(MongoRepository repository) {
        this.repository = repository;
    }// end of Spring constructor


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    @Override
    public int count() {
        return (int) repository.count();
    }// end of method


    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public AEntity find(String id) {
        return (AEntity) repository.findOne(id);
    }// end of method


    /**
     * Returns all entities of the type.
     * <p>
     * Senza filtri
     * Ordinati per ID
     * <p>
     * Methods of this library return Iterable<T>, while the rest of my code expects Collection<T>
     * L'annotation standard di JPA prevede un ritorno di tipo Iterable, mentre noi usiamo List
     * Eseguo qui la conversione, che rimane trasparente al resto del programma
     *
     * @return all entities
     */
    @Override
    public List<? extends AEntity> findAll() {
        return repository.findAll();
    }// end of method


    /**
     * Colonne visibili (e ordinate) nella Grid
     * Sovrascrivibile
     * La colonna key ID normalmente non si visualizza
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIList(columns = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIList, usa tutti i campi della AEntity (senza ID)
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity (se è previsto e se è un developer)
     *
     * @return lista di fields visibili nella Grid
     */
    @Override
    public List<Field> getListFields() {
        List<Field> listaField = null;
        List<String> listaNomi = null;

        //--Se la classe AEntity->@AIList prevede una lista specifica, usa quella lista (con o senza ID)
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaNomi = this.getListFieldsName();

        //--Se non trova nulla, usa tutti i campi (senza ID, senza note, senza creazione, senza modifica)
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaField = getListFields(listaNomi);

        return listaField;
    }// end of method


    /**
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@AIList prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIList() nella Entity
     */
    protected List<String> getListFieldsName() {
        return annotation.getListFieldsName(entityClass);
    }// end of method


    /**
     * Fields dichiarati nella Entity, da usare come columns della Grid (List)
     * Se listaNomi è nulla, usa tutti i campi (senza ID, senza note, senza creazione, senza modifica)
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Se la company è prevista (AlgosApp.USE_MULTI_COMPANY, login.isDeveloper() e entityClazz derivata da ACEntity),
     * la posiziona come prima colonna a sinistra
     *
     * @param listaNomi dei fields da considerare. Tutti, se listaNomi=null
     *
     * @return lista di fields visibili nella Grid
     */
    protected List<Field> getListFields(List<String> listaNomi) {
        return reflection.getListFields(entityClass, listaNomi);
    }// end of method


    /**
     * Fields visibili (e ordinati) nel Form
     * Sovrascrivibile
     * Il campo key ID normalmente non viene visualizzato
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIForm(fields = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIForm, usa tutti i campi della AEntity (senza ID)
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity (se è previsto e se è un developer)
     *
     * @return lista di fields visibili nel Form
     */
    @Override
    public List<Field> getFormFields() {
        List<Field> listaField = null;
        List<String> listaNomi = null;

        //--Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaNomi = this.getFormFieldsName();

        //--Se non trova nulla, usa tutti i campi:
        //--user = senza ID, senza note, senza creazione, senza modifica
        //--developer = con ID, con note, con creazione, con modifica
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaField = this.getFormFields(listaNomi);

        return listaField;
    }// end of method


    /**
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@AIForm prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    protected List<String> getFormFieldsName() {
        return annotation.getFormFieldsName(entityClass);
    }// end of method


    /**
     * Fields dichiarati nella Entity, da usare come campi del Form
     * Se listaNomi è nulla, usa tutti i campi:
     * user = senza ID, senza note, senza creazione, senza modifica
     * developer = con ID, con note, con creazione, con modifica
     * Comprende la entity e tutte le superclassi (fino a ACEntity e AEntity)
     * Se la company è prevista (AlgosApp.USE_MULTI_COMPANY, login.isDeveloper() e entityClazz derivata da ACEntity),
     * la posiziona come secondo campo in alto, dopo la keyID
     *
     * @param listaNomi dei fields da considerare. Tutti, se listaNomi=null
     *
     * @return lista di fields visibili nel Form
     */
    protected List<Field> getFormFields(List<String> listaNomi) {
        return reflection.getFormFields(entityClass, listaNomi);
    }// end of method


    /**
     * Lista di bottoni presenti nella toolbar (footer) della view AList
     * Legge la enumeration indicata nella @Annotation della AEntity
     *
     * @return lista (type) di bottoni visibili nella toolbar della view AList
     */
    public List<EATypeButton> getListTypeButtons() {
        EAListButton listaBottoni = annotation.getListBotton(entityClass);
        EATypeButton[] matrice = null;

        if (listaBottoni != null) {
            switch (listaBottoni) {
                case standard:
                    matrice = new EATypeButton[]{EATypeButton.create, EATypeButton.edit, EATypeButton.delete, EATypeButton.search};
                    break;
                case noSearch:
                    matrice = new EATypeButton[]{EATypeButton.create, EATypeButton.edit, EATypeButton.delete};
                    break;
                case noCreate:
                    matrice = new EATypeButton[]{EATypeButton.edit, EATypeButton.delete};
                    break;
                case edit:
                    matrice = new EATypeButton[]{EATypeButton.edit};
                    break;
                case show:
                    matrice = new EATypeButton[]{EATypeButton.show};
                    break;
                case noButtons:
                    matrice = new EATypeButton[]{};
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return Arrays.asList(matrice);
    }// end of method


    /**
     * Lista di bottoni presenti nella toolbar (footer) della view AForm
     * Legge la enumeration indicata nella @Annotation della AEntity
     *
     * @return lista (type) di bottoni visibili nella toolbar della view AForm
     */
    public List<EATypeButton> getFormTypeButtons() {
        EAFormButton listaBottoni = annotation.getFormBotton(entityClass);
        EATypeButton[] matrice = null;

        if (listaBottoni != null) {
            switch (listaBottoni) {
                case standard:
                    matrice = new EATypeButton[]{EATypeButton.annulla, EATypeButton.revert, EATypeButton.registra};
                    break;
                case show:
                    matrice = new EATypeButton[]{EATypeButton.annulla};
                    break;
                case conferma:
                    matrice = new EATypeButton[]{EATypeButton.annulla, EATypeButton.conferma};
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return Arrays.asList(matrice);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AEntity newEntity() {
        return null;
    }// end of method


    /**
     * Se la nuova entity usa la company, la recupera dal login
     * Se la campany manca, lancia l'eccezione
     *
     * @param entityBean da creare
     */
    protected AEntity addCompany(AEntity entityBean) {
        Company company;
        EACompanyRequired tableCompanyRequired = annotation.getCompanyRequired(entityBean.getClass());

        //--se la EntityClass non estende ACCompany, nopn deve fare nulla
        if (!(entityBean instanceof ACEntity)) {
            return entityBean;
        }// end of if cycle

        //--controlla l'obbligatorietà della Company
        if (AlgosApp.USE_MULTI_COMPANY) {
            tableCompanyRequired = annotation.getCompanyRequired(entityBean.getClass());
            switch (tableCompanyRequired) {
                case nonUsata:
                    log.error("C'è una discrepanza tra 'extends ACEntity' della classe " + entityBean.getClass().getSimpleName() + " e l'annotation @AIEntity della classe stessa");
                    break;
                case facoltativa:
                    company = login.getCompany();
                    if (company != null) {
                        ((ACEntity) entityBean).company = company;
                    } else {
                        log.info("Nuova scheda senza company (facoltativa)");
                    }// end of if/else cycle
                    break;
                case obbligatoria:
                    company = login.getCompany();
                    if (company != null) {
                        ((ACEntity) entityBean).company = company;
                    } else {
                        entityBean = null;
                        log.error(NullCompanyException.MESSAGE);
                        Notification.show("Nuova scheda", NullCompanyException.MESSAGE, Notification.Type.ERROR_MESSAGE);
                    }// end of if/else cycle
                    break;
                default:
                    break;
            } // end of switch statement
        }// end of if cycle

        return entityBean;
    }// end of method

    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * <p>
     * Controlla se l'applicazione usa le company - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company: può essere
     * a)EACompanyRequired.nonUsata
     * b)EACompanyRequired.facoltativa
     * c)EACompanyRequired.obbligatoria
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    public AEntity save(AEntity entityBean) {
        return save((AEntity) null, entityBean);
    }// end of method


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param oldBean      previus state
     * @param modifiedBean to be saved
     *
     * @return the saved entity
     */
    public AEntity save(AEntity oldBean, AEntity modifiedBean) {
        AEntity savedBean = null;
        EACompanyRequired tableCompanyRequired = annotation.getCompanyRequired(modifiedBean.getClass());
        Map mappa = null;
        boolean nuovaEntity = oldBean == null || text.isEmpty(modifiedBean.id);

        //--opportunità di controllare (per le nuove schede) che la key unica non esista già.
        if (nuovaEntity) {
            if (isEsisteEntityKeyUnica(modifiedBean)) {
                Notification.show("Nuova scheda", DuplicateException.MESSAGE, Notification.Type.ERROR_MESSAGE);
                log.error("Ha cercato di salvare una entity già esistente, ma unica");
                return null;
            }// end of if cycle
        }// end of if cycle

        //--opportunità di usare una idKey specifica
        if (text.isEmpty(modifiedBean.id)) {
            creaIdKeySpecifica(modifiedBean);
        }// end of if cycle

        //--inserisce il valore della data attuale per una nuova scheda
        if (modifiedBean.creazione == null) {
            modifiedBean.creazione = LocalDateTime.now();
        }// end of if cycle

        // --inserisce il valore della data attuale per la modifica di una scheda
        modifiedBean.modifica = LocalDateTime.now();

        //--Controlla se l'applicazione usa le company
        if (AlgosApp.USE_MULTI_COMPANY) {
            switch (tableCompanyRequired) {
                case nonUsata:
                    savedBean = (AEntity) repository.save(modifiedBean);
                    break;
                case facoltativa:
                    savedBean = (AEntity) repository.save(modifiedBean);
                    break;
                case obbligatoria:
                    try { // prova ad eseguire il codice
                        if (checkCompany(modifiedBean, false)) {
                            savedBean = (AEntity) repository.save(modifiedBean);
                        } else {
                            log.warn("Entity non creata perché manca la Company (obbligatoria)");
                            savedBean = null;
                        }// end of if/else cycle
                    } catch (Exception unErrore) { // intercetta l'errore
                        log.error(unErrore.toString());
                    }// fine del blocco try-catch
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    savedBean = (AEntity) repository.save(modifiedBean);
                    break;
            } // end of switch statement
        } else {
            savedBean = (AEntity) repository.save(modifiedBean);
        }// end of if/else cycle

        if (!modifiedBean.getClass().getSimpleName().equals("Log")) {
            if (nuovaEntity) {
                logNewBean(modifiedBean);
            } else {
                mappa = chekDifferences(oldBean, modifiedBean);
                logDifferences(mappa, modifiedBean);
            }// end of if/else cycle
        }// end of if cycle

        return savedBean;
    }// end of method

    /**
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean nuova da creare
     */
    protected boolean isEsisteEntityKeyUnica(AEntity entityBean) {
        return false;
    }// end of method


    public void logNewBean(AEntity modifiedBean) {
        String note;
        String clazz = text.primaMaiuscola(modifiedBean.getClass().getSimpleName());

        note = "";
        note += clazz;
        note += ": ";
        note += modifiedBean;

        if (AlgosApp.SETUP_TIME) {
            logger.logSetup(modifiedBean, note);
        } else {
            logger.logNew(modifiedBean, note);
        }// end of if/else cycle
    }// end of method


    public void logDifferences(Map<String, String> mappa, AEntity modifiedBean) {
        String note;
        String aCapo = "\n";
        String clazz = text.primaMaiuscola(modifiedBean.getClass().getSimpleName());

        for (String publicFieldName : mappa.keySet()) {
            note = "";
            note += clazz;
            note += ": ";
            note += modifiedBean;
            note += aCapo;
            note += "Property: ";
            note += text.primaMaiuscola(publicFieldName);
            note += aCapo;
            note += mappa.get(publicFieldName);

            if (AlgosApp.SETUP_TIME) {
                logger.logSetup(modifiedBean, note);
            } else {
                logger.logEdit(modifiedBean, note);
            }// end of if/else cycle
        }// end of for cycle
    }// end of method


    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean) {
        return chekDifferences(oldBean, modifiedBean, (EAPrefType) null);
    }// end of method


    protected Map<String, String> chekDifferences(AEntity oldBean, AEntity modifiedBean, EAPrefType type) {
        Map<String, String> mappa = new LinkedHashMap();
        List<String> listaNomi = reflection.getAllFieldsNameNoCrono(oldBean.getClass());
        Object oldValue;
        Object newValue;
        String descrizione = "";

        for (String publicFieldName : listaNomi) {
            oldValue = reflection.getPropertyValue(oldBean, publicFieldName);
            newValue = reflection.getPropertyValue(modifiedBean, publicFieldName);
            descrizione = text.getModifiche(oldValue, newValue, type);
            if (text.isValid(descrizione)) {
                mappa.put(publicFieldName, descrizione);
            }// end of if cycle
        }// end of for cycle

        return mappa;
    }// end of method

    /**
     * Opportunità di usare una idKey specifica.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean da salvare
     */
    protected void creaIdKeySpecifica(AEntity entityBean) {
    }// end of method


    /**
     * Controlla che la entity estenda ACompanyEntity
     * Se manca la company, cerca di usare quella del login (se esiste)
     * Se la campany manca ancora, lancia l'eccezione
     * //     * Controlla che la gestione della chiave unica sia soddisfatta
     *
     * @param entity da salvare
     */
    @Deprecated
    private boolean checkCompany(AEntity entity, boolean usaCodeCompanyUnico) throws Exception {
        ACEntity companyEntity;
        Company company;
        String codeCompanyUnico;

        if (entity instanceof ACEntity) {
            companyEntity = (ACEntity) entity;
            company = companyEntity.getCompany();
        } else {
            throw new NotCompanyEntityException(entityClass);
        }// end of if/else cycle

        if (company == null) {
//            company = LibSession.getCompany();@todo rimettere
//            companyEntity.setCompany(company);
        }// end of if cycle

        if (companyEntity.getCompany() == null) {
            throw new NullCompanyException();
        }// end of if cycle

        return true;
    }// end of method

    /**
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    public boolean delete(AEntity entityBean) {
        repository.delete(entityBean.getId());
        logDeleteBean(entityBean);

        //@todo aggiungere controllo se il record è stato cancellato
        return true;
    }// end of method


    public void logDeleteBean(AEntity deletedBean) {
        String note;
        String clazz = text.primaMaiuscola(deletedBean.getClass().getSimpleName());

        note = "";
        note += clazz;
        note += ": ";
        note += deletedBean;

        logger.logDelete(deletedBean, note);
    }// end of method

    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        repository.deleteAll();
        return repository.count() == 0;
    }// end of method

}// end of class
