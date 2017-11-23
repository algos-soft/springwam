package it.algos.springvaadin.azione;

import com.vaadin.event.selection.MultiSelectionEvent;
import com.vaadin.event.selection.MultiSelectionListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
@Qualifier(Cost.TAG_AZ_MULTI_SELECTION)
public class AzioneMultiSelection extends Azione {


    public AzioneMultiSelection(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor

    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.tipo = TypeAction.multiSelectionChanged;
    }// end of method

    /**
     * Aggiunge alla Grid il listener per la singola azione
     * Sovrascritto
     */
    @Override
    @SuppressWarnings("all")
    public void addListener(AlgosGrid grid) {
        GridSelectionModel selection = grid.getSelectionModel();

        if (selection instanceof MultiSelectionModel) {
            ((MultiSelectionModel) selection).addMultiSelectionListener(new MultiSelectionListener() {
                @Override
                public void selectionChange(MultiSelectionEvent multiSelectionEvent) {
                    fire(multiSelectionEvent);
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

    }// end of constructor


}// end of class
