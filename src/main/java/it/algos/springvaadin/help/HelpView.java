package it.algos.springvaadin.help;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.view.AlgosViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 01/06/17
 * <p>
 * La selezione del menu nella UI di partenza, invoca lo SpringNavigator che rimanda qui
 * SpringBoot inietta le sottoclassi specifiche (xxxPresenter, xxxList e xxxForm)
 * Nel metodo @PostConstruct, viene effettuato il casting alle property più generiche
 * Passa il controllo alla classe AlgosPresenter che gestisce la business logic
 * <p>
 * Riceve i comandi ed i dati da xxxPresenter (sottoclasse di AlgosPresenter)
 * Gestisce due modalità di presentazione dei dati: List e Form
 * Presenta i componenti grafici passivi
 * Presenta i componenti grafici attivi: azioni associate alla Grid e bottoni coi listener
 */
@SpringComponent
@Qualifier(Cost.TAG_HELP)
public class HelpView extends AlgosViewImpl {

//    /**
//     * Costruttore @Autowired (nella superclasse)
//     * Si usa un @Qualifier(), per avere la sottoclasse specifica
//     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
//     */
//    public HelpView(@Qualifier(Cost.TAG_HELP) AlgosList list, @Qualifier(Cost.TAG_HELP) AlgosField form) {
//        super(list, form);
//    }// end of Spring constructor

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public HelpView() {
        super(null, null);
    }// end of Spring constructor

}// end of class
