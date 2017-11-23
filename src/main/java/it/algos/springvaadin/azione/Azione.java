package it.algos.springvaadin.azione;

import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.context.ApplicationEventPublisher;

import java.util.EventObject;

/**
 * Created by gac on 26/7/17
 * <p>
 * Classe astratta per le azioni generate da grid e Fields
 * Nel costruttore viene iniettato il riferimento al singleton ApplicationEventPublisher
 * Nel @PostConstruct vengono regolati i parametri specifici di questo bottone
 * Nella superclasse viene aggiunto il listener per il click sul bottone
 * Il click recupera il presenter attivo al momento, costruisce un evento e lo lancia
 * Le azioni sono ''prototype'', cioè ne viene generata una per ogni xxxPresenter -> xxxView
 */
public abstract class Azione {

    /**
     * Property iniettata nel costruttore
     */
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Enumeration utilizzata per 'marcare' una azione, in fase di generazione
     * Enumeration utilizzata per 'riconoscerla' nel metodo onApplicationEvent()
     */
    public TypeAction tipo;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     */
    public Azione(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }// end of @Autowired constructor


    /**
     * Aggiunge alla Grid il listener per la singola azione
     * Sovrascritto
     */
    public void addListener(AlgosGrid grid) {
    }// end of constructor


    /**
     * Recupera il presenter dalla 'catena' grafica attiva
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     */
    protected void fire(EventObject event) {
        fire(event, (AEntity) null);
    }// end of method

    /**
     * Recupera il presenter dalla 'catena' grafica attiva
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     */
    protected void fire(EventObject event, AEntity entityBean) {
        AlgosPresenterImpl presenter = LibVaadin.getCurrentPresenter();

        if (presenter != null) {
            applicationEventPublisher.publishEvent(new AActionEvent(tipo, presenter, null, entityBean));
        }// end of if cycle
    }// end of method


}// end of abstract class
