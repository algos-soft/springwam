package it.algos.springvaadin.grid;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.azione.AAction;
import it.algos.springvaadin.azione.IAAction;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.presenter.IAPresenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 26.7.17
 * <p>
 * Set di azioni della Grid
 */
@SpringComponent
@Scope("prototype")
public class AGridToolSet {


    private final IAAction azioneAttach;
//    private final IAAction azioneClick;
    private final IAAction azioneSingleClick;
    private final IAAction azioneDoppioClick;
    private final IAAction azioneSingleSelection;
    private final IAAction azioneMultiSelection;


    public AGridToolSet(
            @Qualifier(ACost.TAG_AZ_ATTACH) IAAction azioneAttach,
//            @Qualifier(Cost.TAG_AZ_CLICK) IAAction azioneClick,
            @Qualifier(ACost.TAG_AZ_SINGLE_CLICK) IAAction azioneSingleClick,
            @Qualifier(ACost.TAG_AZ_DOPPIO_CLICK) IAAction azioneDoppioClick,
            @Qualifier(ACost.TAG_AZ_SINGLE_SELECTION) IAAction azioneSingleSelection,
            @Qualifier(ACost.TAG_AZ_MULTI_SELECTION) IAAction azioneMultiSelection) {
        this.azioneAttach = azioneAttach;
//        this.azioneClick = azioneClick;
        this.azioneSingleClick = azioneSingleClick;
        this.azioneDoppioClick = azioneDoppioClick;
        this.azioneSingleSelection = azioneSingleSelection;
        this.azioneMultiSelection = azioneMultiSelection;
    }// end of @Autowired constructor


    /**
     * Aggiunge alla Grid tutti i listener previsti
     *
     * @param algosGrid di riferimento
     */
    public void addAllListeners(IAGrid algosGrid) {
        azioneAttach.addListener(algosGrid);
//        azioneClick.addListener(algosGrid);
        azioneSingleClick.addListener(algosGrid);
        azioneDoppioClick.addListener(algosGrid);
        azioneSingleSelection.addListener(algosGrid);
        azioneMultiSelection.addListener(algosGrid);
    }// end of method


    /**
     * Aggiunge a tutte le azioni i IAListener  che generano ed ascoltano gli eventi
     *
     * @param source di riferimento del componente che genera gli eventi
     * @param target di riferimento per il componente che ascolta gli eventi
     */
    public void addAllSourcesTargets(IAListener source, IAListener target) {
        azioneAttach.setSourceTarget(source, target);
//        azioneClick.setSourceTarget(source, target);
        azioneSingleClick.setSourceTarget(source, target);
        azioneDoppioClick.setSourceTarget(source, target);
        azioneSingleSelection.setSourceTarget(source, target);
        azioneMultiSelection.setSourceTarget(source, target);
    }// end of method

}// end of class
