package it.algos.springvaadin.toolbar;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.button.AButtonFactory;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 03/06/17
 * <p>
 * Barra di comando con bottoni, specializzata per la lista (Grid)
 * Nel ciclo restart() di List, la toolbar costruisce i bottoni ("prototype") usando la factory AButtonFactory
 * Viene poi iniettato il parametro obbligatorio (source)
 * Ulteriori parametri (target, entityBean), vengono iniettati direttamente solo in alcuni bottoni
 * Tutti i bottoni possono essere abilitati/disabilitati
 * I bottoni standard sono 3 o 4
 * Eventuali bottoni aggiuntivi, possono essere aggiunti nella classe specifica xxxList.toolbarInizializza()
 * Tutti i bottoni possono essere abilitati/disabilitati
 */
@SpringComponent
@Scope("prototype")
@Qualifier(ACost.BAR_LIST)
public class AListToolbar extends AToolbar {


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     */
    public AListToolbar(AButtonFactory buttonFactory) {
        super(buttonFactory);
    }// end of @Autowired constructor



}// end of class
