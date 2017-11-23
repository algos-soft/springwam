package it.algos.springvaadin.service;

import com.vaadin.ui.Notification;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.exception.*;
import it.algos.springvaadin.form.FormButton;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.list.ListButton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 07/07/17
 * Implementazione standard dell'annotation AlgosService
 */
@Slf4j
public abstract class AlgosServiceImpl implements AlgosService {


    //--la repository dei dati viene iniettata dal costruttore della sottoclasse concreta
    public MongoRepository repository;


    //--il modello-dati specifico viene regolato dalla sottoclasse nel costruttore
    protected Class<? extends AEntity> entityClass;

    /**
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public AlgosServiceImpl(MongoRepository repository) {
        this.repository = repository;
    }// end of Spring constructor


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     * <p>
     * Controlla se l'applicazione usa le company - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company
     *
     * @param entityBean da salvare
     *
     * @return the saved entity
     */
    public AEntity save(AEntity entityBean) throws Exception {
        entityBean = checkDate(entityBean);
        ACompanyRequired tableCompanyRequired = LibAnnotation.companyType(entityBean.getClass());

        //--opportunità di usare una idKey specifica
        if (LibText.isEmpty(entityBean.id)) {
            creaIdKeySpecifica(entityBean);
        }// end of if cycle

        if (LibParams.useMultiCompany()) {
            switch (tableCompanyRequired) {
                case nonUsata:
                    return (AEntity) repository.save(entityBean);
                case facoltativa:
                    return (AEntity) repository.save(entityBean);
                case obbligatoria:
                    if (checkCompany(entityBean, false)) {
                        return (AEntity) repository.save(entityBean);
                    } else {
                        log.warn("Entity non creata perché manca la Company (obbligatoria)");
                        return null;
                    }// end of if/else cycle
                default:
                    log.warn("Switch - caso non definito");
                    return (AEntity) repository.save(entityBean);
            } // end of switch statement
        } else {
            return (AEntity) repository.save(entityBean);
        }// end of if/else cycle
    }// end of method


    /**
     * Opportunità di usare una idKey specifica.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean da salvare
     */
    protected void creaIdKeySpecifica(AEntity entityBean) {
    }// end of method


    private AEntity checkDate(AEntity entityBeanIn) {
        AEntity entityBeanOut = entityBeanIn;

        if (entityBeanOut.dataCreazione == null) {
            entityBeanOut.dataCreazione = LocalDateTime.now();
        }// end of if cycle
        entityBeanOut.dataModifica = LocalDateTime.now();

        return entityBeanOut;
    }// end of method


    /**
     * Controlla che la entity estenda ACompanyEntity
     * Se manca la company, cerca di usare quella della sessione (se esiste)
     * Se la campany manca ancora, lancia l'eccezione
     * Controlla che la gestione della chiave unica sia soddisfatta
     */
    private boolean checkCompany(AEntity entity, boolean usaCodeCompanyUnico) throws Exception {
        ACompanyEntity companyEntity;
        Company company;
        String codeCompanyUnico;

        if (entity instanceof ACompanyEntity) {
            companyEntity = (ACompanyEntity) entity;
            company = companyEntity.getCompany();
        } else {
            throw new NotCompanyEntityException(entityClass);
        }// end of if/else cycle

        if (company == null) {
            company = LibSession.getCompany();
            companyEntity.setCompany(company);
        }// end of if cycle

        if (companyEntity.getCompany() == null) {
            throw new NullCompanyException();
        }// end of if cycle

        return true;
    }// end of method


    /**
     * Retrieves an entity by its id.
     *
     * @param serializable must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Object findOne(Serializable serializable) {
        return (AEntity) repository.findOne(serializable);
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
    public List<AEntity> findAll() {
        return repository.findAll();
    }// end of method

    @Override
    public List<AEntity> findAllByCompany() {
        Company company = LibSession.getCompany();

        if (company != null) {
            return findAllByCompany(company);
        } else {
            return findAll();
        }// end of if/else cycle

    }// end of method


    public List<AEntity> findAllByCompany(Company company) {
        return null;
    }// end of method


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
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public boolean delete(AEntity entityBean) {
        repository.delete(entityBean.getId());

        if (repository.findOne(entityBean.getId()) == null) {
            Notification.show("Delete", "Cancellato il record: " + entityBean, Notification.Type.HUMANIZED_MESSAGE);
        } else {
            Notification.show("Delete", "Non sono riuscito a cancellare il record: " + entityBean, Notification.Type.WARNING_MESSAGE);
        }// end of if/else cycle

        return true;
    }// end of method

    /**
     * Deletes all entities of the collection.
     */
    @Override
    public boolean deleteAll() {
        repository.deleteAll();
        return repository.count() == 0;
    }// end of method


    /**
     * Colonne visibili (e ordinate) nella Grid
     * Sovrascrivibile
     * La colonna ID normalmente non si visualizza
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIList(columns = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIList, usa tutti i campi della AEntity (senza ID)
     * 4) Se trova AEntity->@AIList(showsID = true), questo viene aggiunto, indipendentemente dalla lista
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity
     *
     * @return lista di nomi di colonne visibili nella Grid
     */
    @Override
    @Deprecated
    public List<String> getListVisibleColumnNames() {
        List<String> listaNomi = null;
        boolean useID = false;
        boolean useCompany = false;

        //--Se la classe Entity->@Annotation prevede una lista specifica, usa quella lista (con o senza ID)
        listaNomi = LibAnnotation.getListColumns(entityClass);

        //--Se non trova nulla, usa tutti i campi (senza ID, a meno che la classe Entity->@Annotation preveda l'ID)
        if (listaNomi == null) {
            useID = LibAnnotation.isListShowsID(entityClass);
            useCompany = this.displayCompany();
            listaNomi = LibReflection.getListVisibleColumnNames(entityClass, useID, useCompany);
        }// end of if cycle

        //--il developer vede tutto (si potrebbe migliorare)
        if (LibSession.isDeveloper()) {
            listaNomi = LibReflection.getListVisibleColumnNames(entityClass, true, true);
        }// end of if cycle

        return listaNomi;
    }// end of method


    /**
     * Colonne visibili (e ordinate) nella Grid
     * Sovrascrivibile
     * La colonna ID normalmente non si visualizza
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIList(columns = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIList, usa tutti i campi della AEntity (senza ID)
     * 4) Se trova AEntity->@AIList(showsID = true), questo viene aggiunto, indipendentemente dalla lista
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity
     *
     * @return lista di fields visibili nella Grid
     */
    @Override
    public List<Field> getListFields() {
        List<Field> listaField = null;
        List<String> listaNomi = null;
        boolean useCompany = false;

        //--Se la classe Entity->@Annotation prevede una lista specifica, usa quella lista (con o senza ID)
        listaNomi = LibAnnotation.getListColumns(entityClass);

        //--Se non trova nulla, usa tutti i campi (senza ID, a meno che la classe Entity->@Annotation preveda l'ID)
        listaField = LibReflection.getListColumns(entityClass, listaNomi, displayCompany());

//        //--il developer vede tutto (si potrebbe migliorare)
//        if (LibSession.isDeveloper()) {
//            listaNomi = LibReflection.getListVisibleColumnNames(entityClass, true, true);
//        }// end of if cycle

        return listaField;
    }// end of method


    /**
     * Fields visibili (e ordinati) nel Form - Lista di Java Reflected Field, previsti nel modello dati della Entity
     * La property keyId normalmente non viene visualizzata
     * Il developer vede SEMPRE la keyId, non Enabled
     * 1) Questo metodo può essere sovrascritto completamente
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIForm(fields = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIForm, usa tutti i campi della AEntity (senza ID)
     * 4) Se trova AEntity->@AIForm(showsID = true), questo viene aggiunto, indipendentemente dalla lista
     * 5) Vengono considerati anche i campi di tutte le superclassi fino alla classe AEntity
     * Ad esempio: company della classe ACompanyEntity
     *
     * @return lista di fields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     */
    @Override
    public List<Field> getFormFields() {
        List<Field> listaField = null;
        List<String> listaNomi = null;

        //--Se la classe AEntity->@Annotation prevede una lista specifica, usa quella lista (con o senza ID)
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaNomi = getFormFieldsName();

        //--Se non trova nulla, usa tutti i campi
        //--Nel Form il developer vede SEMPRE la keyId, non Enabled
        //--rimanda ad un metodo separato per poterlo sovrascrivere
        listaField = getFormFields(listaNomi);

        return listaField;
    }// end of method


    /**
     * Nomi dei fields da considerare per estrarre i Java Reflected Field dalle @Annotation della Entity
     * Se la classe AEntity->@Annotation prevede una lista specifica, usa quella lista (con o senza ID)
     * Sovrascrivibile
     *
     * @return nomi dei fields, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    protected List<String> getFormFieldsName() {
        return LibAnnotation.getFormFieldsName(entityClass);
    }// end of method


    /**
     * Java Reflected Fields estratti dalle @Annotation della Entity
     * Se la lista nomi è nulla, usa tutte le properties (fields)
     * Nel Form il developer vede SEMPRE la keyId, non Enabled
     * Sovrascrivibile
     *
     * @param listaNomi per estrarre i Java Reflected Fields dalle @Annotation della Entity
     *
     * @return fields, oppure null se non esiste l'Annotation specifica @AIForm() nella Entity
     */
    protected List<Field> getFormFields(List<String> listaNomi) {
        return LibReflection.getFormFields(entityClass, listaNomi, displayCompany());
    }// end of method


    /**
     * Fields visibili (e ordinati) nel Form
     * Sovrascrivibile
     * Esclude le date di creazione e modifica che NON servono se usaSeparateFormDialog
     * Il campo key ID normalmente non viene visualizzato
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIForm(fields = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIForm, usa tutti i campi della AEntity (senza ID)
     * 4) Se trova AEntity->@AIForm(showsID = true), questo viene aggiunto, indipendentemente dalla lista
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity
     *
     * @return lista di fields visibili nel Form
     */
    @Override
    public List<Field> getFormFieldsLink() {
        List<String> listaNomi = null;
        boolean useID = false;
        boolean useCompany = false;

        //--Se la classe AEntity->@Annotation prevede una lista specifica, usa quella lista (con o senza ID)
        listaNomi = LibAnnotation.getFormFieldsName(entityClass);

        //--Se trova AEntity->@AIForm(showsID = true), questo viene aggiunto, indipendentemente dalla lista
        //--Se non trova AEntity->@AIForm, usa tutti i campi della AEntity (con o senza ID)
        useID = LibAnnotation.isFormShowsID(entityClass);
        useCompany = this.displayCompany();

        //--il developer vede tutto (si potrebbe migliorare)
        if (LibSession.isDeveloper()) {
            listaNomi = LibReflection.getListVisibleColumnNames(entityClass, true, true);
            useID = true;
            useCompany = true;
        }// end of if cycle

        if (listaNomi != null && listaNomi.size() > 0) {
            if (listaNomi.contains("dataCreazione")) {
                listaNomi.remove(listaNomi.remove(listaNomi.indexOf("dataCreazione")));
            }// end of if cycle
            if (listaNomi.contains("dataModifica")) {
                listaNomi.remove(listaNomi.remove(listaNomi.indexOf("dataModifica")));
            }// end of if cycle
        }// end of if cycle

        return LibReflection.getFields(entityClass, listaNomi, useID, useCompany);
    }// end of method


    /**
     * Bottoni nella toolbar della Grid
     *
     * @return lista di bottoni visibili nella toolbar
     */
    public List<String> getListBottonNames() {
        ListButton listaBottoni = LibAnnotation.getListBotton(entityClass);
        String[] matrice = null;

        if (listaBottoni != null) {
            switch (listaBottoni) {
                case standard:
                    matrice = new String[]{Cost.TAG_BOT_NEW, Cost.TAG_BOT_EDIT, Cost.TAG_BOT_DELETE, Cost.TAG_BOT_SEARCH};
                    break;
                case noSearch:
                    matrice = new String[]{Cost.TAG_BOT_NEW, Cost.TAG_BOT_EDIT, Cost.TAG_BOT_DELETE};
                    break;
                case noCreate:
                    matrice = new String[]{Cost.TAG_BOT_EDIT, Cost.TAG_BOT_SEARCH};
                    break;
                case edit:
                    matrice = new String[]{Cost.TAG_BOT_EDIT};
                    break;
                case show:
                    matrice = new String[]{Cost.TAG_BOT_SHOW};
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return LibArray.fromString(matrice);
    }// end of method


    /**
     * Bottoni nella toolbar del Form
     *
     * @return lista di bottoni visibili nella toolbar
     */
    public List<String> getFormBottonNames() {
        FormButton listaBottoni = LibAnnotation.getFormBotton(entityClass);
        String[] matrice = null;

        if (listaBottoni != null) {
            switch (listaBottoni) {
                case standard:
                    matrice = new String[]{Cost.TAG_BOT_ANNULLA, Cost.TAG_BOT_REVERT, Cost.TAG_BOT_SAVE};
                    break;
                case show:
                    matrice = new String[]{Cost.TAG_BOT_ANNULLA};
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement
        }// end of if cycle

        return LibArray.fromString(matrice);
    }// end of method


    /**
     * Flag.
     * Deve essere true il flag useMultiCompany
     * La Entity deve estendere ACompanyEntity
     * L'buttonUser collegato deve essere developer
     */
    @Override
    public boolean displayCompany() {

        //--Deve essere true il flag useMultiCompany
        if (!LibParams.useMultiCompany()) {
            return false;
        }// end of if cycle

        //--La Entity deve estendere ACompanyEntity
        if (!ACompanyEntity.class.isAssignableFrom(entityClass)) {
            return false;
        }// end of if cycle

        //--L'buttonUser collegato deve essere developer
        if (!LibSession.isDeveloper()) {
            return false;
        }// end of if cycle

        return true;
    }// end of static method


    public void setEntityClass(Class<? extends AEntity> entityClass) {
        this.entityClass = entityClass;
    }// end of method


}// end of class
