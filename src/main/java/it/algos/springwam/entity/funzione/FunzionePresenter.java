package it.algos.springwam.entity.funzione;

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
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_17:07:10
 * Estende la Entity astratta APresenter che gestisce la business logic
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Costruttore con dei link @Autowired di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_FUN)
@AIScript(sovrascrivibile = true)
public class FunzionePresenter extends APresenter {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public FunzionePresenter(
            @Lazy @Qualifier(AppCost.TAG_FUN) IAService service,
            @Lazy @Qualifier(AppCost.TAG_FUN) IAList list,
            @Lazy @Qualifier(AppCost.TAG_FUN) IAForm form) {
        super(Funzione.class, service, list, form);
    }// end of Spring constructor


}// end of class
