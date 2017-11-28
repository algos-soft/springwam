package it.algos.springwam.entity.iscrizione;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_ISC)
public class IscrizioneList extends AlgosListImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public IscrizioneList(@Qualifier(AppCost.TAG_ISC) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            caption = className + " - ";
            caption += "Non dovrebbero esserci schede.";
            caption += "</br>Lista visibile solo al developer";
            caption += "</br>NON usa la company che è già presente nel Turno di riferimento";
            caption += "</br>L'entity è 'embedded' nelle collezioni che la usano (no @Annotation property DbRef)";
            caption += "</br>In pratica questa lista non dovrebbe mai essere usata (serve come test)";
        }// end of if cycle
    }// end of method

}// end of class
