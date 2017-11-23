package it.algos.springvaadin.view;

import com.vaadin.data.ValidationResult;
import com.vaadin.navigator.View;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.presenter.AlgosPresenter;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 07/07/17
 * <p>
 * Contratto d'annotation con i metodi che la View rende disponibili all'applicazione,
 * in particolare ad AlgosPresenter
 * Implementati nella classe concreta AlgosViewImpl
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
public interface AlgosView extends View {


    /**
     * Costruisce una Grid
     *
     * @param entityClazz   di riferimento, sottoclasse concreta di AEntity
     * @param columns visibili ed ordinate della Grid
     * @param items   da visualizzare nella Grid
     */
    public void setList(Class<? extends AEntity> entityClazz, List<Field> columns, List items);


    /**
     * Creazione di un form
     *
     * @param entityBean            istanza da elaborare
     * @param reflectFields         campi del form da visualizzare
     * @param usaSeparateFormDialog barra alternativa di bottoni per gestire il ritorno ad altro modulo
     */
    public void setForm(ApplicationListener source, AEntity entityBean, List<Field> reflectFields, boolean usaSeparateFormDialog);


    /**
     * Creazione di un form di un altro modulo/collezione
     * Solo finestra popup
     *
     * @param entityBean         istanza da elaborare
     * @param sourceField        di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     * @param reflectFields      campi del form da visualizzare
     * @param usaBottoneRegistra utilizzo del ButtonRegistra, che registra subito
     *                           oppure ButtonAccetta, che demanda la registrazione alla scheda chiamante
     */
    public void setFormLink(ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField, List<Field> reflectFields, AButtonType type);


    /**
     * Righe selezionate nella Grid
     *
     * @return numero di righe selezionate
     */
    public int numRigheSelezionate();


    /**
     * Una riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    public boolean isUnaRigaSelezionata();


    /**
     * Abilita o disabilita lo specifico bottone della Toolbar della List
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    public void enableButtonList(AButtonType type, boolean status);


    /**
     * Abilita o disabilita lo specifico bottone della Toolbar del Form
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    public void enableButtonForm(AButtonType type, boolean status);

//    /**
//     * Abilita il bottone Edit dela Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableEdit(boolean status);
//
//
//    /**
//     * Abilita il bottone Delet della Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableDelete(boolean status);


    /**
     * Checks all current validation errors
     * Se ce ne sono, rimane nel form SENZA registrare
     *
     * @return ci sono errori in almeno una delle property della entity
     */
    public boolean entityHasError();


    /**
     * Checks if the entity has no current validation errors at all
     * Se la entity è OK, può essere registrata
     *
     * @return tutte le property della entity sono valide
     */
    public boolean entityIsOk();


    /**
     * All fields errors
     *
     * @return errors
     */
    public List<ValidationResult> getEntityErrors();


    /**
     * Esegue il 'rollback' nel Form
     * Revert (ripristina) button pressed in form
     * Rimane nel form SENZA registrare e ripristinando i valori iniziali della entity
     */
    public void revertEntity();


    /**
     * Esegue il 'commit' nel Form.
     * Trasferisce i valori dai campi alla entityBean
     * Esegue la validazione dei dati
     * Esegue la trasformazione dei dati
     *
     * @return la entity del Form
     */
    public AEntity commit();


    /**
     * Chiude la (eventuale) finestra utilizzata nel Form
     */
    public void closeFormWindow();


    /**
     * La entity del Form
     *
     * @return la entity del Form
     */
    public AEntity getEntityForm();


    /**
     * Una lista di entity selezionate nella Grid, in funzione di Grid.SelectionMode()
     * Lista nulla, se nessuna riga è selezionata
     * Lista di un elemento, se è Grid.SelectionMode.SINGLE
     * Lista di più elementi, se è Grid.SelectionMode.MULTI
     *
     * @return lista di una o più righe selezionate, null se nessuna riga è selezionata
     */
    public List<AEntity> getEntityBeans();


    /**
     * Elemento selezionato nella Grid
     *
     * @return entityBean
     */
    public AEntity getListEntityBean();


    /**
     * Elemento corrente nel Form
     *
     * @return entityBean
     */
    public AEntity getFormEntityBean();


    /**
     * Registra eventuali dipendenze di un field del Form
     */
    public void saveSons();

    public AlgosForm getForm();

    public void setPresenter(AlgosPresenterImpl presenter);

}// end of interface
