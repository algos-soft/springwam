package it.algos.springvaadin.button;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.event.AButtonEvent;
import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;

/**
 * Created by gac on 26/7/17
 * <p>
 * Bottoni utilizzati nelle varie Views: List e Form
 * Nel costruttore viene iniettato il riferimento al singleton ApplicationEventPublisher
 * Nel @PostConstruct vengono regolati i parametri specifici di questo bottone
 * Nella superclasse viene aggiunto il listener per il click sul bottone
 * Il click recupera il presenter attivo al momento, costruisce un evento e lo lancia
 * I bottoni sono ''prototype'', cioè ne viene generato uno per ogni xxxPresenter -> xxxView
 */
@Slf4j
@SpringComponent
public class AButton extends Button {


    /**
     * Service (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected ATextService text;


    public final static String NORMAL_WIDTH = "8em";
    public final static String ICON_WIDTH = "3em";


    /**
     * Classe che gestisce gli eventi a livello Application
     */
    protected ApplicationEventPublisher publisher;


    /**
     * Enumeration utilizzata per 'marcare' un evento, in fase di generazione
     * Enumeration utilizzata per 'riconoscerlo' nel metodo onApplicationEvent()
     */
    protected EAButtonType type;


    /**
     * Source (presenter, form, field, window, dialog,... ) che ha generato l'evento
     */
    protected IAListener source;


    /**
     * Target (presenter, form, field, window, dialog,... ) a cui indirizzare l'evento
     */
    protected IAListener target;


    /**
     * Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni bottoni
     */
    protected AEntity entityBean;


    //--Opzionale (field) in elaborazione. Ha senso solo per alcuni bottoni
    protected AField sourceField;


    /**
     * Costruttore base senza parametri
     * Viene utilizzato dalla Funzione -> BottoneFactory in AlgosConfiguration
     */
    public AButton() {
    }// fine del metodo costruttore base


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     * Aggiunge il listener al bottone
     */
    @PostConstruct
    private void inizia() {
        this.addListener();
    }// end of method


    /**
     * Aggiunge il listener al bottone
     * Handle the event with an anonymous class
     */
    private void addListener() {
        this.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fire(clickEvent);
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Metodo invocato da parte di IAButtonFactory subito dopo la nuovo del bottone
     * Non parte dal costruttore, perché IAButtonFactory usa un costruttore SENZA parametri
     * <p>
     * Regola il 'type' di bottone
     * Inietta il publisher
     * Il bottone usa il sorce (di tipo presenter, di solito) per identificare 'dove' gestire l'evento generato
     * Regola lo style del bottone.
     * Forza a maiuscola la prima lettera del testo del bottone
     * Non si poteva fare prima perché la LibParams non è 'visibile' durante la fase iniziale gestita  da Spring
     *
     * @param publisher   degli eventi a livello Application
     * @param type        del bottone, secondo la Enumeration EAButtonType
     * @param source      dell'evento generato dal bottone
     * @param target      a cui indirizzare l'evento generato dal bottone
     * @param sourceField che contiene il bottone bottone
     */
    void inizializza(ApplicationEventPublisher publisher, EAButtonType type, IAListener source, IAListener target, AField sourceField, AEntity entityBean) {
        this.setPublisher(publisher);
        this.setType(type);
        this.setSource(source);
        this.setTarget(target);
        this.setSourceField(sourceField);
        this.setEntityBean(entityBean);
        this.regolaParametri();

        //@todo RIMETTERE
//        if (LibParams.usaBottoniPrimaMaiuscola()) {
        this.setCaption(text.primaMaiuscola(getCaption()));
//        }// end of if cycle
    }// end of method


    /**
     * Regola i parametri del bottone specifico (usando la Enumeration dei bottoni)
     */
    private void regolaParametri() {
        super.setCaption(type.getCaption());
        super.setIcon(type.getIcon());
        super.setEnabled(type.isEnabled());
        super.setWidth(type.getWidth());
        super.setClickShortcut(type.getKeyCode(), type.getModifier());

        //@todo RIMETTERE
//        if (LibParams.usaBottoniColorati()) {
//            super.addStyleName(type.getStyle());
//        }// end of if cycle
    }// end of method


    /**
     * Costruisce e lancia l'evento che viene pubblicato dal singleton ApplicationEventPublisher
     * L'evento viene intercettato nella classe AlgosPresenterEvents->onApplicationEvent(AEvent event),
     * oppure in qualsiasi altra classe che implementa ApplicationListener
     */
    protected void fire(Button.ClickEvent clickEvent) {
        if (type == null) {
            log.error("AButton: manca il type nel bottone");
            return;
        }// end of if cycle

        if (publisher == null) {
            log.error("AButton: manca il publisher nel bottone " + type);
            return;
        }// end of if cycle

        if (source == null) {
            log.error("AButton: manca il source nel bottone " + type);
            return;
        }// end of if cycle

        if (target == null) {
            log.error("AButton: manca il target nel bottone " + type);
            return;
        }// end of if cycle


        AButtonEvent evento = new AButtonEvent(type, source, target, entityBean, sourceField);
        publisher.publishEvent(evento);
    }// end of method


    private void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }// end of method


    public void setSource(IAListener source) {
        this.source = source;
    }// end of method

    public void setTarget(IAListener target) {
        this.target = target;
    }// end of method


    public void setType(EAButtonType type) {
        this.type = type;
    }// end of method

    public void setEntityBean(AEntity entityBean) {
        this.entityBean = entityBean;
    }// end of method

    public void setSourceField(AField sourceField) {
        this.sourceField = sourceField;
    }// end of method

    public EAButtonType getType() {
        return type;
    }// end of method

}// end of abstract class
