package it.algos.springvaadin.entity.address;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.form.IAForm;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.IAList;
import it.algos.springvaadin.presenter.APresenter;
import it.algos.springvaadin.service.IAService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-17_11:26:25
 * Estende la Entity astratta APresenter che gestisce la business logic
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con dei link @Autowired di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_ADD)
@AIScript(sovrascrivibile = true)
public class AddressPresenter extends APresenter {

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Use @Lazy to avoid the Circular Dependency
     * A simple way to break the cycle is saying Spring to initialize one of the beans lazily.
     * That is: instead of fully initializing the bean, it will create a proxy to inject it into the other bean.
     * The injected bean will only be fully created when it’s first needed.
     * Regola il modello-dati specifico nella chiamata al costruttore della superclasse
     *
     * @param service iniettato da Spring
     * @param list iniettato da Spring
     * @param form iniettato da Spring
     */
    public AddressPresenter(
            @Lazy @Qualifier(ACost.TAG_ADD) IAService service,
            @Lazy @Qualifier(ACost.TAG_ADD) IAList list,
            @Lazy @Qualifier(ACost.TAG_ADD) IAForm form) {
        super(Address.class, service, list, form);
    }// end of Spring constructor


}// end of class
