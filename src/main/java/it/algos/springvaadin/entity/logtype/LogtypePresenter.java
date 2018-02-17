package it.algos.springvaadin.entity.logtype;

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
 * Created by gac on TIMESTAMP
 * Estende la Entity astratta APresenter che gestisce la business logic
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Costruttore con dei link @Autowired di tipo @Lazy per evitare un loop nella injection
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_LOGTYPE)
@AIScript(sovrascrivibile = true)
public class LogtypePresenter extends APresenter {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public LogtypePresenter(
                @Lazy @Qualifier(ACost.TAG_LOGTYPE) IAService service,
                @Lazy @Qualifier(ACost.TAG_LOGTYPE) IAList list,
                @Lazy @Qualifier(ACost.TAG_LOGTYPE) IAForm form) {
        super(service, list, form);
        super.entityClass = Logtype.class;
     }// end of Spring constructor


}// end of class
