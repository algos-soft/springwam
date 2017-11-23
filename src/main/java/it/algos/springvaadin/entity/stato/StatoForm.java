package it.algos.springvaadin.entity.stato;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.LibReflection;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 10-ago-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_STA)
public class StatoForm extends AlgosFormImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public StatoForm(@Qualifier(Cost.TAG_STA) AlgosService service,
                     @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                     @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


    /**
     * Trasferisce i valori dai campi dell'annotation alla entityBean
     * Esegue la (eventuale) validazione dei dati
     * Esegue la (eventuale) trasformazione dei dati
     *
     * @return la entityBean del Form
     */
    @Override
    public AEntity commit() {
        byte[] imgBytes = ((Stato) entityBean).getBandiera();
        super.commit();
//        ((Stato) entityBean).setBandiera(imgBytes);

        return entityBean;
    }// end of method


}// end of class

