package it.algos.springvaadin.entity.versione;


import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 01/06/17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_VERS)
public class VersionePresenter extends AlgosPresenterImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public VersionePresenter(@Qualifier(Cost.TAG_VERS) AlgosView view, @Qualifier(Cost.TAG_VERS) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Versione.class;
    }// end of Spring constructor


}// end of class
