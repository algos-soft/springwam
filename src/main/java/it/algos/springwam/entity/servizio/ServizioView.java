package it.algos.springwam.entity.servizio;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.view.AlgosViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_SER)
public class ServizioView extends AlgosViewImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public ServizioView(@Qualifier(AppCost.TAG_SER) AlgosList list, @Qualifier(AppCost.TAG_SER) AlgosForm form) {
        super(list, form);
    }// end of Spring constructor

}// end of class
