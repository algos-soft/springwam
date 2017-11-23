package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_USE)
@Slf4j
public class UserPresenter extends AlgosPresenterImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public UserPresenter(@Qualifier(Cost.TAG_USE) AlgosView view, @Qualifier(Cost.TAG_USE) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = User.class;
     }// end of Spring constructor


}// end of class
