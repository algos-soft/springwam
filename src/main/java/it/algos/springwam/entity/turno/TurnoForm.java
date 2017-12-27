package it.algos.springwam.entity.turno;

import com.vaadin.ui.Layout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.ATextField;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.entity.servizio.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_TUR)
public class TurnoForm extends AlgosFormImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public TurnoForm(@Qualifier(AppCost.TAG_TUR) AlgosService service,
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
        String caption;

        if (entityBean != null && entityBean.getId() != null) {
            caption = "Modifica turno esistente";
        } else {
            caption = "Creazione nuovo turno";
        }// end of if/else cycle

        return caption;
    }// end of method

    /**
     * Regolazioni specifiche per i fields di una entity, dopo aver trascritto la entityBean nel binder
     */
    @Override
    protected void fixFieldsAllways() {
        Turno turno = null;
        Servizio servizio = null;

        if (entityBean != null && entityBean instanceof Turno) {
            turno = (Turno) entityBean;
            servizio = turno.getServizio();
        }// end of if cycle

        if (servizio != null && !servizio.isOrario()) {
            AField fieldTitolo = (ATextField) getField("titoloExtra");
        }// end of if cycle

    }// end of method

//    /**
//     * Prepara la toolbar e la aggiunge al contenitore grafico
//     * Sovrascrivibile
//     */
//    protected void fixToolbar(Layout layout) {
//        List<String> listaBottoni = service.getFormBottonNames();
//        toolbar.inizializza(source, listaBottoni);
//        layout.addComponent(toolbar.get());
//    }// end of method

}// end of class

