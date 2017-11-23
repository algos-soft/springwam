package it.algos.springvaadin.entity.role;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.dialog.ConfirmDialog;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibParams;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 11-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_ROL)
@Slf4j
public class RolePresenter extends AlgosPresenterImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public RolePresenter(@Qualifier(Cost.TAG_ROL) AlgosView view, @Qualifier(Cost.TAG_ROL) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Role.class;
     }// end of Spring constructor


}// end of class
