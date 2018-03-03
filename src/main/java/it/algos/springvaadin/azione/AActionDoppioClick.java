package it.algos.springvaadin.azione;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.ItemClickListener;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
@Qualifier(ACost.TAG_AZ_DOPPIO_CLICK)
public class AActionDoppioClick extends AAction {


    /**
     * Costruttore @Autowired
     *
     * @param applicationEventPublisher iniettato da Spring
     */
    public AActionDoppioClick(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor


    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.type = EATypeAction.doppioClick;
    }// end of method


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    @Override
    public void addListener(IAGrid algosGrid) {
        algosGrid.getGrid().addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(Grid.ItemClick itemClick) {
                if (itemClick.getMouseEventDetails().isDoubleClick()) {
                    Object obj = itemClick.getItem();
                    if (obj instanceof AEntity) {
                        fire((AEntity) obj);
                    }// end of if cycle
                }// end of if cycle
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

}// end of class
