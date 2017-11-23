package it.algos.springvaadin.entity.preferenza;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.view.AlgosViewImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 16-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PRE)
public class PreferenzaView extends AlgosViewImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public PreferenzaView(@Qualifier(Cost.TAG_PRE) AlgosList list, @Qualifier(Cost.TAG_PRE) AlgosForm form) {
        super(list, form);
    }// end of Spring constructor

}// end of class
