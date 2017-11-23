package it.algos.springvaadin.azione;

import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
@Qualifier(Cost.TAG_AZ_SINGLE_SELECTION)
public class AzioneSingleSelection extends Azione {


    public AzioneSingleSelection(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor

    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.tipo = TypeAction.singleSelectionChanged;
    }// end of method

    /**
     * Aggiunge alla Grid il listener per la singola azione
     * Sovrascritto
     */
    @SuppressWarnings("all")
    public void addListener(AlgosGrid grid) {
        GridSelectionModel selection = grid.getSelectionModel();

        if (selection instanceof SingleSelectionModel) {
            ((SingleSelectionModel) selection).addSingleSelectionListener(new SingleSelectionListener() {
                @Override
                public void selectionChange(SingleSelectionEvent singleSelectionEvent) {
                    fire(singleSelectionEvent);
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

    }// end of method

}// end of class
