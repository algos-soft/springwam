package it.algos.springvaadin.presenter;

import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.event.FormListener;
import it.algos.springvaadin.event.GridListener;
import it.algos.springvaadin.event.ListListener;
import org.springframework.context.ApplicationListener;

/**
 * Created by gac on 14/06/17
 * <p>
 * Gestisce la business logic di una 'collection' (ex modulo)
 * Viene creata la prima volta dalla xxxNavView (injected) che a sua volta viene creata dallo SpringNavigator
 * <p>
 * Conosce la vista specifica (di tipo AlgosView), iniettata nel costruttore
 * Conosce il service specifico (di tipo AlgosService), iniettata nel costruttore
 * Conosce la entityClass, che gli viene passata dal costruttore della sottoclasse specifica
 * Utilizza una xxxView (da non confondersi con xxxNavView) specifica, iniettata
 * -(che a sua volta usa una xxxList per la Grid ed una xxxForm per la scheda)
 * Utilizza un xxxService specifico, iniettato
 * -(che a sua volta usa una AEntity ed eventualmente una Repository)
 * <p>
 * Riceve gli eventi, lanciati da Bottoni ed Azioni, e li gestisce nella sottoclasse specializzata AlgosPresenterEvents
 */
public interface AlgosPresenter extends GridListener, ListListener, FormListener, ApplicationListener<AEvent> {

    /**
     * Metodo invocato dalla view ogni volta che questa diventa attiva
     */
    public void enter();

    /**
     * Metodo invocato da un daemon per spedire mail
     */
    public void daemonMail();

}// end of interface
