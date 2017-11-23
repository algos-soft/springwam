package it.algos.springwam.entity.croce;

import it.algos.springwam.application.AppCost;
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
 * Created by gac on 31-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_CRO)
@Slf4j
public class CrocePresenter extends AlgosPresenterImpl {

    private CroceService service;

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public CrocePresenter(@Qualifier(AppCost.TAG_CRO) AlgosView view, @Qualifier(AppCost.TAG_CRO) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Croce.class;
        this.service = (CroceService) service;
    }// end of Spring constructor


}// end of class
