package it.algos.springvaadin.presenter;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.event.IAListener;
import org.springframework.context.ApplicationEvent;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:28
 */
public interface IAPresenter extends IAListener {

    /**
     * Gestione di una Lista visualizzata con una Grid
     * Metodo invocato da:
     * 1) una view quando diventa attiva
     * 2) un Evento (azione) che necessita di aggiornare e ripresentare la Lista;
     * tipo dopo un delete, dopo un nuovo record, dopo la edit di un record
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera dal service le colonne da mostrare nella grid
     * Recupera dal service gli items (records) della collection, da mostrare nella grid
     * Passa il controllo alla view con i dati necessari
     */
    public void setList();


    /**
     * Gestione di un Form per presentare i fields
     * Metodo invocato da:
     * 1) una view quando diventa attiva
     * 2) un Evento (azione) che necessita di aggiornare e ripresentare il Form;
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera l'entityBean da mostrare/modificare. Nullo per una nuova scheda
     * Recupera dal service i fields (property) della collection, da mostrare nella view
     * Recupera dal service i bottoni comando da mostrare nella toolbar del footer (sotto i fields)
     * Passa il controllo alla view con i dati necessari
     */
    public void setForm();


    /**
     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
     */
    public void fireList();


    /**
     * Usa lo SpringNavigator per cambiare view ed andare ad AForm
     *
     * @param entityBean istanza da creare/elaborare
     */
    public void fireForm(AEntity entityBean);


    /**
     * Usa lo SpringNavigator per cambiare view ed andare ad AForm
     *
     * @param entityBean istanza da creare/elaborare
     * @param source     presenter che ha chiamato questo form
     */
    public void fireForm(AEntity entityBean, IAPresenter source);


    /**
     * Modificata la selezione della Grid
     * Controlla nella Grid quanti sono i records selezionati
     * Abilita e disabilita i bottoni Modifica ed Elimina della List
     */
    public void selectionChanged();


    /**
     * Creazione o modifica di un singolo record (entityBean)
     * If entityBean=null, create a new item and edit it in a form
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Passa il controllo alla view (Form) con i dati necessari
     *
     * @param entityBean istanza da elaborare
     */
    public void editBean(AEntity entityBean);


    /**
     * Revert (ripristina) button pressed in form
     * Rimane nella view (Form) SENZA registrare e ripristinando i valori iniziali del record (entityBean)
     */
    public void revert();


    /**
     * Cancellazione di un singolo record (entityBean)
     * If entityBean=null, recupera dal Form il record selezionato (se riesce)
     * Usa il service per cancellare il record selezionato
     * Ritorna il controllo alla view (List)
     *
     * @param entityBean istanza da cancellare
     */
    public void delete(AEntity entityBean);


    /**
     * Modificato il contenuto di un Field
     * Abilita e disabilita i bottoni Revert e Registra/Accetta del Form
     */
    public void valueChanged();


    /**
     * Evento 'save' (registra) button pressed in form
     * Esegue il 'commit' nel Form, trasferendo i valori dai campi alla entityBean
     * Esegue, nel Form, eventuale validazione e trasformazione dei dati
     * Registra le modifiche nel DB, tramite il service
     * Usa lo SpringNavigator per cambiare view ed andare alla view AList
     */
    public void registra();


}// end of interface
