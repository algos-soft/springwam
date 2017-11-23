package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.indirizzo.IndirizzoPresenter;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.ALinkField;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;

/**
 * Created by gac on 13/06/17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_COMP)
public class CompanyForm extends AlgosFormImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public CompanyForm(@Qualifier(Cost.TAG_COMP) AlgosService service,
                       @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                       @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


}// end of class
