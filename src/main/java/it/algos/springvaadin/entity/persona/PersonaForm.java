package it.algos.springvaadin.entity.persona;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 11-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PER)
public class PersonaForm extends AlgosFormImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public PersonaForm(@Qualifier(Cost.TAG_PER) AlgosService service,
                       @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                       @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor

    /**
     * Label di informazione
     *
     * @param entityBean istanza da presentare
     *
     * @return la label a video
     */
    protected String fixCaption(AEntity entityBean) {
        String caption = entityBean.getClass().getSimpleName() + " - ";

        if (entityBean != null && entityBean instanceof Persona && LibText.isValid(((Persona)entityBean).getCognome())) {
            caption += CAPTION_EDIT;
        } else {
            caption += CAPTION_CREATE;
        }// end of if/else cycle

        return caption;
    }// end of method

}// end of class

