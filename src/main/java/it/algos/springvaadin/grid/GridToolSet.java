package it.algos.springvaadin.grid;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.azione.Azione;
import it.algos.springvaadin.lib.Cost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 26.7.17
 * <p>
 * Set di azioni della Grid
 */
@SpringComponent
@Scope("prototype")
public class GridToolSet {


    private final Azione azioneAttach;
    private final Azione azioneClick;
    private final Azione azioneDoppioClick;
    private final Azione azioneSingleSelection;
    private final Azione azioneMultiSelection;


    public GridToolSet(
            @Qualifier(Cost.TAG_AZ_ATTACH) Azione azioneAttach,
            @Qualifier(Cost.TAG_AZ_CLICK) Azione azioneClick,
            @Qualifier(Cost.TAG_AZ_DOPPIO_CLICK) Azione azioneDoppioClick,
            @Qualifier(Cost.TAG_AZ_SINGLE_SELECTION) Azione azioneSingleSelection,
            @Qualifier(Cost.TAG_AZ_MULTI_SELECTION) Azione azioneMultiSelection) {
        this.azioneAttach = azioneAttach;
        this.azioneClick = azioneClick;
        this.azioneDoppioClick = azioneDoppioClick;
        this.azioneSingleSelection = azioneSingleSelection;
        this.azioneMultiSelection = azioneMultiSelection;
    }// end of @Autowired constructor

    /**
     * Aggiunge alla Grid tutti i listener previsti
     */
    public void addAllListeners(AlgosGrid grid) {
        azioneAttach.addListener(grid);
        azioneClick.addListener(grid);
        azioneDoppioClick.addListener(grid);
        azioneSingleSelection.addListener(grid);
        azioneMultiSelection.addListener(grid);
    }// end of constructor

}// end of class
