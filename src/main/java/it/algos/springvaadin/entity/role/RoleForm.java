package it.algos.springvaadin.entity.role;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springvaadin.service.AlgosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 11-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_ROL)
public class RoleForm extends AlgosFormImpl {


     /**
      * Costruttore @Autowired (nella superclasse)
      * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
      *
      * @param service     iniettata da Spring
      * @param toolbar     iniettata da Spring
      * @param toolbarLink iniettata da Spring
      */
     public RoleForm(@Qualifier(Cost.TAG_ROL) AlgosService service,
                        @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                        @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
         super(service, toolbar, toolbarLink);
     }// end of Spring constructor


}// end of class

