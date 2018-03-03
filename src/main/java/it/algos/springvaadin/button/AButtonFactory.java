package it.algos.springvaadin.button;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.presenter.IAPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-set-2017
 * Time: 15:34
 * <p>
 * Factory class per la nuovo dei bottoni
 * Crea ogni bottone del tpo richiesto e previsto nella Enumeration EATypeButton
 * Nella nuovo viene iniettato il parametro obbligatorio della 'sorgente' dell'evento generato dal bottone
 * Eventuali altri parametri facoltativi (target, entityBean), possono essere aggiunti. Da altre classi.
 */
@SpringComponent
@Slf4j
public class AButtonFactory implements IAButtonFactory {


    /**
     * Funzione specificata in AlgosConfiguration
     */
    private Function<Class<? extends AButton>, AButton> buttonFactory;


    /**
     * Publisher degli eventi a livello Application
     */
    private ApplicationEventPublisher publisher;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param buttonFactory specificata in AlgosConfiguration
     * @param publisher     degli eventi a livello Application
     */
    public AButtonFactory(
            Function<Class<? extends AButton>,
                    AButton> buttonFactory,
            ApplicationEventPublisher publisher) {
        this.buttonFactory = buttonFactory;
        this.publisher = publisher;
    }// end of @Autowired constructor


    /**
     * Creazione di un bottone
     *
     * @param type        del bottone, secondo la Enumeration EATypeButton
     * @param source      dell'evento generato dal bottone
     * @param target      a cui indirizzare l'evento generato dal bottone
     * @param sourceField che contiene il bottone bottone
     *
     * @return il bottone creato
     */
    @Override
    public AButton crea(EATypeButton type, IAPresenter source, IAPresenter target, AField sourceField) {
        return crea(type, source, target, sourceField, null);
    }// end of method


    /**
     * Creazione di un bottone
     *
     * @param type        del bottone, secondo la Enumeration EATypeButton
     * @param source      dell'evento generato dal bottone
     * @param target      a cui indirizzare l'evento generato dal bottone
     * @param sourceField che contiene il bottone bottone
     *
     * @return il bottone creato
     */
//    @Override
    public AButton crea(EATypeButton type, IAPresenter source, IAPresenter target, AField sourceField, AEntity entityBean) {
        AButton button = buttonFactory.apply(AButton.class);

        if (button != null) {
            button.inizializza(publisher, type, source, target, sourceField, entityBean);
        }// end of if cycle

        return button;
    }// end of method


}// end of class


