package it.algos.springvaadin.toolbar;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.springvaadin.bottone.*;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.lib.Cost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

import java.util.List;

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
@Qualifier(Cost.BAR_LIST)
public class ListToolbar extends AToolbarImpl {


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     */
    public ListToolbar(AButtonFactory buttonFactory) {
        super(buttonFactory);
    }// end of @Autowired constructor



}// end of class
