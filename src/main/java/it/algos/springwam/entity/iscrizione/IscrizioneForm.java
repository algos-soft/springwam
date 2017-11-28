package it.algos.springwam.entity.iscrizione;

import it.algos.springvaadin.field.AComboField;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.utente.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_ISC)
public class IscrizioneForm extends AlgosFormImpl {


    @Autowired
    private UtenteService utenteService;

    @Autowired
    private FunzioneService funzioneService;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public IscrizioneForm(@Qualifier(AppCost.TAG_ISC) AlgosService service,
                          @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                          @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor

    /**
     * Regolazioni specifiche per i fields di una entity, dopo aver trascritto la entityBean nel binder
     */
    @Override
    protected void fixFieldsAllways() {
        AField fieldUtenti = (AComboField) getField("utente");
        List utenti = utenteService.findAllByCompany();
        ((AComboField) fieldUtenti).fixCombo(utenti);

        AField fieldFunzioni = (AComboField) getField("funzione");
        List funzioni = funzioneService.findAllByCompany();
        ((AComboField) fieldFunzioni).fixCombo(funzioni);
    }// end of method

}// end of class

