package it.algos.springwam.entity.iscrizione;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.view.AlgosViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_ISC)
public class IscrizioneView extends AlgosViewImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public IscrizioneView(@Qualifier(AppCost.TAG_ISC) AlgosList list, @Qualifier(AppCost.TAG_ISC) AlgosForm form) {
        super(list, form);
    }// end of Spring constructor

}// end of class
