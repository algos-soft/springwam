package it.algos.springvaadin.home;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 01-ott-2017
 * Time: 20:25
 */
@SpringComponent
@Qualifier(Cost.TAG_HOME)
public class HomePresenter extends AlgosPresenterImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public HomePresenter(@Qualifier(Cost.TAG_HOME) AlgosView view) {
        super(view, null, null);
    }// end of Spring constructor


}// end of class
