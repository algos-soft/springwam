package it.algos.springvaadin.azione;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.grid.IAGrid;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by gac on 26/7/17
 * <p>
 * Classe astratta per le azioni generate da AGrid e e dai Fields
 * Le azioni sono ''prototype'', cioè ne viene generata una per ogni xxxPresenter -> xxxView
 */
public abstract class AAction implements IAAction {


    /**
     * Property iniettata nel costruttore
     */
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * Gestore principale per la 'business logic' modulo, iniettato dalla AView che costruisce questa azione
     * Presenter, form, field, window, dialog,...  che ha generato l'azione
     */
    protected IAListener source;


    /**
     * Gestore principale per la 'business logic' modulo, iniettato dalla AView che costruisce questa azione
     * Presenter, form, field, window, dialog,...  a cui indirizzare l'azione
     */
    protected IAListener target;


    /**
     * Enumeration utilizzata per 'marcare' una azione, in fase di generazione
     * Enumeration utilizzata per 'riconoscerla' nel metodo onApplicationEvent()
     */
    public EATypeAction type;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     */
    public AAction(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }// end of Spring constructor


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    public void addListener(IAGrid algosGrid) {
    }// end of constructor


    /**
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     */
    protected void fire() {
        fire((AEntity) null);
    }// end of method


    /**
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     *
     * @param type       enumeration utilizzata per 'marcare' una azione, in fase di generazione
     * @param entityBean in elaborazione
     */
    protected void fire(EATypeAction type, AEntity entityBean) {
        if (source != null && target != null) {
            applicationEventPublisher.publishEvent(new AActionEvent(type, source, target, entityBean));
        }// end of if cycle
    }// end of method


    /**
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     *
     * @param entityBean in elaborazione
     */
    protected void fire(AEntity entityBean) {
        if (source != null && target != null) {
            applicationEventPublisher.publishEvent(new AActionEvent(type, source, target, entityBean));
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge il IAListener alla AAction
     *
     * @param source di riferimento del componente che genera gli eventi
     */
    public void setSource(IAListener source) {
        this.source = source;
    }// end of method


    /**
     * Aggiunge il IAListener alla AAction
     *
     * @param target di riferimento per il componente che ascolta gli eventi
     */
    public void setTarget(IAListener target) {
        this.target = target;
    }// end of method

    /**
     * Aggiunge il source IAListener alla AAction
     * Aggiunge il target IAListener alla AAction
     *
     * @param source di riferimento del componente che genera gli eventi
     * @param target di riferimento per il componente che ascolta gli eventi
     */
    public void setSourceTarget(IAListener source, IAListener target) {
        this.setSource(source);
        this.setTarget(target);
    }// end of method

}// end of abstract class
