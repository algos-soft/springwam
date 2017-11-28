package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_USE)
public class UserList extends AlgosListImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public UserList(@Qualifier(Cost.TAG_USE) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.fixCaption(className, items);
            caption += "</br>Lista visibile a tutti";
            caption += "</br>Usa la company che è ACompanyRequired.obbligatoria";
            caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Utente previste.";
        }// end of if/else cycle
    }// end of method

}// end of class
