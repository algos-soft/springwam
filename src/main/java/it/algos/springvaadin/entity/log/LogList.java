package it.algos.springvaadin.entity.log;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by gac on 30-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_LOG)
public class LogList extends AlgosListImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public LogList(@Qualifier(Cost.TAG_LOG) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void inizializza(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.inizializza(className, items);
            caption += "</br>Lista visibile solo all'admin che vede SOLO le schede della sua company";
            caption += "</br>Usa la company (se AlgosApp.USE_MULTI_COMPANY=true) che è obbligatoria";
            caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Operazioni effettuate";
        }// end of if/else cycle
    }// end of method


}// end of class
