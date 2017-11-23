package it.algos.springvaadin.service;

import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.LibParams;
import it.algos.springvaadin.lib.LibSession;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 07/07/17
 * <p>
 * Contratto d'annotation con i metodi che il Service rende disponibili all'applicazione,
 * in particolare ad AlgosPresenter
 * Implementati nella classe concreta AlgosServiceImpl
 * <p>
 * La selezione del menu nella UI di partenza, invoca lo SpringNavigator che rimanda qui
 * SpringBoot inietta le sottoclassi specifiche (xxxPresenter, xxxList e xxxForm)
 * Nel metodo @PostConstruct, viene effettuato il casting alle property più generiche
 * Passa il controllo alla classe AlgosPresenter che gestisce la business logic
 * <p>
 * Riceve i comandi ed i dati da xxxPresenter (sottoclasse di AlgosPresenter)
 * Gestisce due modalità di presentazione dei dati: List e Form
 * Presenta i componenti grafici passivi
 * Presenta i componenti grafici attivi: azioni associate alla Grid e bottoni coi listener
 */
public interface AlgosService {


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    public AEntity newEntity();


    /**
     * Retrieves an entity by its id.
     *
     * @param serializable must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public Object findOne(Serializable serializable);

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
    public List<AEntity> findAll();

    public List<AEntity> findAllByCompany();

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public int count();


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param entityBean to be saved
     *
     * @return the saved entity
     */
    public AEntity save(AEntity entityBean) throws Exception;

    /**
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public boolean delete(AEntity entityBean);


    /**
     * Deletes all entities of the collection.
     */
    public boolean deleteAll();


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
    @Deprecated
    public List<String> getListVisibleColumnNames();


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
    public List<Field> getListFields();


    /**
     * Fields visibili (e ordinati) nel Form
     * Sovrascrivibile
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
    public List<Field> getFormFields();


    /**
     * Fields visibili (e ordinati) nel Form
     * Esclude le date di creazione e modifica che NON servono se usaSeparateFormDialog
     * Sovrascrivibile
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
    public List<Field> getFormFieldsLink();


    /**
     * Bottoni nella toolbar della Grid
     *
     * @return lista di bottoni visibili nella toolbar
     */
    public List<String> getListBottonNames();


    /**
     * Bottoni nella toolbar della Grid
     *
     * @return lista di bottoni visibili nella toolbar
     */
    public List<String> getFormBottonNames();

    /**
     * Flag per visualizzare il field company
     * <p>
     * Deve essere true il flag useMultiCompany
     * La Entity deve estendere ACompanyEntity
     * L'buttonUser collegato deve essere developer
     */
    public boolean displayCompany();


    public void setEntityClass(Class<? extends AEntity> entityClass);

}// end of interface

