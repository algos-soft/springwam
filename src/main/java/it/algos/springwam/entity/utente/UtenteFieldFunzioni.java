package it.algos.springwam.entity.utente;

import com.vaadin.data.HasValue;
import com.vaadin.ui.*;
import it.algos.springvaadin.field.ACheckBoxField;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.service.FieldService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.servizio.ServizioFieldFunzioni;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: sab, 18-nov-2017
 * Time: 12:23
 */
@Slf4j
public class UtenteFieldFunzioni extends AField {


    private final static String CAPTION = "Controllo";
    private Panel pannello;
    private Layout placeholder;
    private CheckBox checkBoxApertura = null;
    private List<Funzione> funzioni;
    private List<CheckBox> checks;
    private int larFunzioni = 0;

    /**
     * Funzione specificata in AlgosConfiguration
     */
    @Autowired
    private Function<Class<? extends AField>, AField> fieldFactory;

    /**
     * Property iniettata da Spring
     */
    @Autowired
    protected FunzioneService funzioneService;


    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    public void creaContent() {
        pannello = new Panel();
        placeholder = new VerticalLayout();
        funzioni = funzioneService.findAllByCompany();
        checks = new ArrayList<>();
        CheckBox checkBox = null;

        if (funzioni != null && funzioni.size() > 0) {
            for (Funzione funz : funzioni) {
                checkBox = new CheckBox(funz.getSigla());
                checkBox.addValueChangeListener(new HasValue.ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent valueChangeEvent) {
                        publish();
                    }// end of inner method
                });// end of anonymous inner class
                checks.add(checkBox);
            }// end of for cycle
        }// end of if cycle

    }// end of method

    @Override
    public void setWidth(String width) {

        if (placeholder != null) {
            pannello.setWidth(width);
            placeholder.setWidth(width);
        }// end of if cycle

    }// end of method


    @Override
    public Component initContent() {
        int numRighe = 4;
        int numColonne = 4;
        int x = 0;
        HorizontalLayout horizLayout = new HorizontalLayout();

        placeholder.removeAllComponents();

        if (numRighe > 0 && numColonne > 0) {
            for (int k = 0; k < numRighe; k++) {
                horizLayout = new HorizontalLayout();
                for (int y = 0; y < numColonne; y++) {
                    x = k * numColonne + y;
                    if (x < checks.size()) {
                        horizLayout.addComponent(checks.get(x));
                    }// end of if cycle
                }// end of for cycle

                if (horizLayout.getComponentCount() > 0) {
                    placeholder.addComponent(horizLayout);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        pannello.setContent(placeholder);
        return pannello;
    }// end of method

    @Override
    public List getValue() {
        List<Funzione> funzioniAbilitate = new ArrayList<>();

        if (checks.size() == funzioni.size()) {
            for (int k = 0; k < checks.size(); k++) {
                if (checks.get(k).getValue()) {
                    funzioniAbilitate.add(funzioni.get(k));
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        if (funzioniAbilitate.size() < 1) {
            funzioniAbilitate = null;
        }// end of if cycle

        return funzioniAbilitate;
    }// end of method

    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        List<Funzione> lista = null;
        int pos = 0;

        if (value != null && value instanceof List && ((List) value).size() > 0 && ((List) value).get(0) instanceof Funzione) {
            lista = (List<Funzione>) value;
            if (checks.size() == funzioni.size()) {

                for (Funzione funz : lista) {
                    if (funzioni.contains(funz)) {
                        pos = funzioni.indexOf(funz);
                        checks.get(pos).setValue(true);
                    }// end of if cycle
                }// end of for cycle

            }// end of if cycle
        }// end of if cycle

    }// end of method

}// end of class
