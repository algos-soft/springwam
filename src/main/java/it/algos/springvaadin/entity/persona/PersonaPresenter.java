package it.algos.springvaadin.entity.persona;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.presenter.APresenter;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.IAService;
import it.algos.springvaadin.list.IAList;
import it.algos.springvaadin.form.IAForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-14_06:46:57
 * Estende la Entity astratta APresenter che gestisce la business logic
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Costruttore con dei link @Autowired di tipo @Lazy per evitare un loop nella injection
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_PER)
@AIScript(sovrascrivibile = true)
public class PersonaPresenter extends APresenter {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public PersonaPresenter(
                @Lazy @Qualifier(ACost.TAG_PER) IAService service,
                @Lazy @Qualifier(ACost.TAG_PER) IAList list,
                @Lazy @Qualifier(ACost.TAG_PER) IAForm form) {
        super(service, list, form);
        super.entityClass = Persona.class;
     }// end of Spring constructor


}// end of class
