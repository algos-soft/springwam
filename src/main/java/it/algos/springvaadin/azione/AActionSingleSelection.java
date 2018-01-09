package it.algos.springvaadin.azione;

import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
@Qualifier(ACost.TAG_AZ_SINGLE_SELECTION)
public class AActionSingleSelection extends AAction {


    /**
     * Costruttore @Autowired
     *
     * @param applicationEventPublisher iniettato da Spring
     */
    public AActionSingleSelection(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor


    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.type = EATypeAction.singleSelectionChanged;
    }// end of method


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    @Override
    public void addListener(IAGrid algosGrid) {
        GridSelectionModel selection = algosGrid.getGrid().getSelectionModel();

        if (selection instanceof SingleSelectionModel) {
            ((SingleSelectionModel) selection).addSingleSelectionListener(new SingleSelectionListener() {
                @Override
                public void selectionChange(SingleSelectionEvent singleSelectionEvent) {
                    fire();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle
    }// end of method

}// end of class
