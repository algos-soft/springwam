package it.algos.springvaadin.entity.persona;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.dialog.ConfirmDialog;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.AField;
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
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 11-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PER)
@Slf4j
public class PersonaPresenter extends AlgosPresenterImpl {

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public PersonaPresenter(@Qualifier(Cost.TAG_PER) AlgosView view, @Qualifier(Cost.TAG_PER) AlgosService service, AlgosSearch search) {
        super(view, service, search);
        super.entityClass = Persona.class;
     }// end of Spring constructor


    @Override
    protected List<Field> getFormFieldsLink() {
        List<Field> reflectedFields = service.getFormFieldsLink();
        reflectedFields.remove(0); //--rimuove il campo idKey
        reflectedFields.remove(0); //--rimuove il campo company

        return reflectedFields;
    }// end of method

}// end of class
