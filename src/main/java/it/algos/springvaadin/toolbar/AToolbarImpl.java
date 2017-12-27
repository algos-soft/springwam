package it.algos.springvaadin.toolbar;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonFactory;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * Created by gac on 03/06/17.
 * .
 * Superclasse astratta per le barre di comando con bottoni
 * Nel ciclo restart() di Form e List, le toolbar costruiscono i bottoni ("prototype") usando la factory AButtonFactory
 * Viene poi iniettato il parametro obbligatorio (source)
 * Ulteriori parametri (target, entityBean), vengono iniettati direttamente solo in alcuni bottoni
 * Eventuali bottoni aggiuntivi, oltre quelli standard, possono essere aggiunti sovrascrivendo AListImpl.inizializzaToolbar()
 * Tutti i bottoni possono essere abilitati/disabilitati
 * Sono previste due righe di bottoni: la prima standard, la seconda per bottoni extra specifici
 */
public abstract class AToolbarImpl extends VerticalLayout implements AToolbar {

    protected HorizontalLayout primaRiga = new HorizontalLayout();
    protected HorizontalLayout secondaRiga = new HorizontalLayout();

    /**
     * Factory per la nuovo dei bottoni
     * Autowired nel costruttore
     */
    private AButtonFactory buttonFactory;

    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     */
    public AToolbarImpl(AButtonFactory buttonFactory) {
        this.buttonFactory = buttonFactory;
        this.setMargin(false);
        this.addComponent(primaRiga);
    }// end of @Autowired constructor


    /**
     * Metodo invocato da restart() di Form e List
     * Seleziona i bottoni da mostrare nella toolbar
     * Crea i bottoni (iniettandogli il publisher)
     * Aggiunge i bottoni al contenitore grafico
     * Inietta nei bottoni il parametro obbligatorio (source)
     *
     * @param source  dell'evento generato dai bottoni
     * @param typeButtons da visualizzare
     */
    public void inizializza(ApplicationListener source, List<AButtonType> typeButtons) {
        this.deleteAllButtons();

        for (AButtonType singleTypeButton : typeButtons) {
            creaAddButton(singleTypeButton, source);
        }// end of for cycle

    }// end of method


    /**
     * Metodo invocato da restart() di Form, nella classe LinkToolbar
     * Crea i bottoni (iniettandogli il publisher)
     * Aggiunge i bottoni al contenitore grafico
     * Inietta nei bottoni il parametro obbligatorio (source)
     *
     * @param source      dell'evento generato dal bottone
     * @param target
     * @param sourceField di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     */
    @Override
    public void inizializza(ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
    }// end of method

    /**
     * Crea il bottone nella factory AButtonFactory (iniettandogli il publisher)
     * Inietta nei bottoni il parametro obbligatorio (source)
     * Aggiunge il bottone alla prima riga (default) del contenitore grafico
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param source dell'evento generato dal bottone
     */
    @Override
    public AButton creaAddButton(AButtonType type, ApplicationListener source) {
        return creaAddButton(type, source, source, (AEntity) null, (AField) null);
    }// end of method


    /**
     * Crea il bottone nella factory AButtonFactory (iniettandogli il publisher)
     * Inietta nei bottoni il parametro obbligatorio (source)
     * Aggiunge il bottone alla prima riga (default) del contenitore grafico
     *
     * @param sourceField di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     */
    @Override
    public AButton creaAddButton(AButtonType type, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
        AButton button = buttonFactory.crea(type, source, target, sourceField, entityBean);

        if (button != null) {
            primaRiga.addComponent(button);
        }// end of if cycle

        return button;
    }// end of method

    /**
     * Crea il bottone nella factory AButtonFactory (iniettandogli il publisher)
     * Inietta nei bottoni il parametro obbligatorio (source)
     * Aggiunge il bottone alla seconda riga (opzionale) del contenitore grafico
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param source dell'evento generato dal bottone
     */
    @Override
    public AButton creaAddButtonSecondaRiga(AButtonType type, ApplicationListener source) {
        if (this.getComponentCount() == 1) {
            this.addComponent(secondaRiga);
        }// end of if cycle

        return creaAddButtonSecondaRiga(type, source, source, (AEntity) null, (AField) null);
    }// end of method


    /**
     * Crea il bottone nella factory AButtonFactory (iniettandogli il publisher)
     * Inietta nei bottoni il parametro obbligatorio (source)
     * Aggiunge il bottone alla seconda riga (opzionale) del contenitore grafico
     *
     * @param sourceField di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     */
    @Override
    public AButton creaAddButtonSecondaRiga(AButtonType type, ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField) {
        AButton button = buttonFactory.crea(type, source, target, sourceField, entityBean);

        if (button != null) {
            secondaRiga.addComponent(button);
        }// end of if cycle

        return button;
    }// end of method

    /**
     * Cancella tutti i bottoni
     */
    public void deleteAllButtons() {
        primaRiga.removeAllComponents();
        secondaRiga.removeAllComponents();
    }// end of method

    /**
     * Inietta nel bottone il parametro
     * Il bottone è già stato inizializzato coi parametri standard
     *
     * @param entityBean in elaborazione
     */
    @Override
    public void inizializzaEdit(AEntity entityBean) {
    }// end of method


    /**
     * Inietta nel bottone i parametri
     * Il bottone è già stato inizializzato coi parametri standard
     *
     * @param entityBean in elaborazione
     * @param target     (window, dialog, presenter) a cui indirizzare l'evento
     */
    @Override
    public void inizializzaEditLink(AEntity entityBean, ApplicationListener target) {
    }// end of method

    /**
     * Aggiunge un componente (di solito un Button) alla prima riga (default) del contenitore grafico
     *
     * @param component da aggiungere
     */
    public void addComp(Component component) {
        primaRiga.addComponent(component);
    }// end of method


    /**
     * Aggiunge un componente (di solito un Button) alla prima riga (default) del contenitore grafico
     *
     * @param component da aggiungere
     */
    public void addCompSecondaRiga(Component component) {
        secondaRiga.addComponent(component);
    }// end of method

    /**
     * Abilita o disabilita lo specifico bottone
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    @Override
    public void enableButton(AButtonType type, boolean status) {
        AButton button = this.getButton(type);

        if (button != null) {
            button.setEnabled(status);
        }// end of if cycle
    }// end of method


    /**
     * Recupera il bottone del tipo specifico
     * Ce ne può essere uno solo per questa toolbar
     *
     * @param type del bottone, secondo la Enumeration AButtonType
     */
    private AButton getButton(AButtonType type) {
        Component comp = null;
        AButton button = null;

        for (int k = 0; k < primaRiga.getComponentCount(); k++) {
            comp = primaRiga.getComponent(k);
            if (comp != null && comp instanceof AButton) {
                button = (AButton) comp;
                if (button.getType() == type) {
                    return button;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        for (int k = 0; k < secondaRiga.getComponentCount(); k++) {
            comp = secondaRiga.getComponent(k);
            if (comp != null && comp instanceof AButton) {
                button = (AButton) comp;
                if (button.getType() == type) {
                    return button;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return button;
    }// end of method

    /**
     * Restituisce questa toolbar
     *
     * @return il componente concreto di questa interfaccia
     */
    public AToolbarImpl get() {
        return this;
    }// end of method

}// end of class
