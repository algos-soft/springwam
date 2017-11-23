package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.view.AlgosViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 01/06/17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_COMP)
public class CompanyView extends AlgosViewImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public CompanyView(@Qualifier(Cost.TAG_COMP) AlgosList list, @Qualifier(Cost.TAG_COMP) AlgosForm form) {
        super(list, form);
    }// end of Spring constructor

}// end of class
