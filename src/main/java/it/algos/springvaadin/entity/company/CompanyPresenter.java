package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.ALinkField;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 01/06/17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_COMP)
@Slf4j
public class CompanyPresenter extends AlgosPresenterImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public CompanyPresenter(@Qualifier(Cost.TAG_COMP) AlgosView view, @Qualifier(Cost.TAG_COMP) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Company.class;
    }// end of Spring constructor



    @Override
    public void fieldModificato(ApplicationListener source, AEntity entityBean, AField sourceField) {
        ((ALinkField) sourceField).refreshFromDialogLinkato(entityBean);
        this.view.enableButtonForm(AButtonType.registra,true);
    }// end of method

}// end of class
