package it.algos.springvaadin.azione;

import com.vaadin.server.ClientConnector;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.presenter.IAPresenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope("prototype")
@Qualifier(ACost.TAG_AZ_ATTACH)
public class AActionAttach extends AAction {


    /**
     * Costruttore @Autowired
     *
     * @param applicationEventPublisher iniettato da Spring
     */
    public AActionAttach(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor


    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.type = EATypeAction.attach;
    }// end of method


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    @Override
    public void addListener(IAGrid algosGrid) {
        algosGrid.getGrid().addAttachListener(new ClientConnector.AttachListener() {
            @Override
            public void attach(ClientConnector.AttachEvent attachEvent) {
                fire();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

}// end of class
