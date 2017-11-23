package it.algos.springvaadin.entity.indirizzo;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.search.AlgosSearch;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 07-ago-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_IND)
@Slf4j
public class IndirizzoPresenter extends AlgosPresenterImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public IndirizzoPresenter(@Qualifier(Cost.TAG_IND) AlgosView view, @Qualifier(Cost.TAG_IND) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Indirizzo.class;
    }// end of Spring constructor


    @Override
    protected List<Field> getFormFieldsLink() {
        List<Field> reflectedFields = service.getFormFieldsLink();
        reflectedFields.remove(0); //--rimuove il campo idKey

        return reflectedFields;
    }// end of method

}// end of class
