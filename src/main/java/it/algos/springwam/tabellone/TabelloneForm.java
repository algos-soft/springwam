package it.algos.springwam.tabellone;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 14:19
 */
@SpringComponent
@Qualifier(AppCost.TAG_TAB)
public class TabelloneForm extends AlgosFormImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public TabelloneForm(@Qualifier(AppCost.TAG_TAB) AlgosService service,
                         @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                         @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


}// end of class

