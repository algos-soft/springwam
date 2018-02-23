package it.algos.springwam.entity.turno;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.presenter.APresenter;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.IAService;
import it.algos.springvaadin.list.IAList;
import it.algos.springvaadin.form.IAForm;
import it.algos.springwam.tabellone.TabellonePresenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
 * Estende la Entity astratta APresenter che gestisce la business logic
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con dei link @Autowired di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_TUR)
@AIScript(sovrascrivibile = true)
public class TurnoPresenter extends APresenter {

//    @Lazy
//    @Autowired
//    private TabellonePresenter source;

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public TurnoPresenter(
            @Lazy @Qualifier(AppCost.TAG_TUR) IAService service,
            @Lazy @Qualifier(AppCost.TAG_TUR) IAList list,
            @Lazy @Qualifier(AppCost.TAG_TUR) IAForm form) {
        super(Turno.class, service, list, form);
    }// end of Spring constructor

}// end of class
