package it.algos.springwam.tabellone;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.label.ALabel;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.servizio.Servizio;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 11-feb-2018
 * Time: 08:47
 * Componente con l'elenco ordinato delle funzioni
 */
@Slf4j
public class CompFunzioni extends VerticalLayout {

    private Servizio servizio;

    /**
     * Costruttore completo
     */
    public CompFunzioni(Servizio servizio) {
        this.servizio = servizio;
        iniziaUI();
        addFunzioni();
    }// end of constructor


    private void iniziaUI() {
        setWidth("6em");
        setSpacing(false);
        setMargin(false);
        addStyleName("greenBg");
    }// end of method


    private void addFunzioni() {
        boolean obbligatoria=false;
        Component comp;

        // se orario variabile, prima riga vuota per allinearsi con i turni che in questo caso avranno un titolo
        if (!servizio.isOrario()) {
            ALabel label = new ALabel("&nbsp;");
            addComponent(label);
        }// end of if cycle

        List<Funzione> funzioni = servizio.getFunzioni();
//        Collections.sort(lista);
        for (Funzione funz : funzioni) {
//            obbligatoria = serFun.isObbligatoria();

            comp = addFunzione(funz.getSigla());
            if (obbligatoria) {
                comp.addStyleName("cfunzioneobblig");
            }// end of if cycle
        }// end of for cycle

    }// end of method


    /**
     * Aggiunge un componente grafico rappresentante una funzione
     *
     * @param nome nome della funzione
     *
     * @return il componente grafico aggiuntp
     */
    private Component addFunzione(String nome) {
        Label label = new Label(nome, ContentMode.HTML);
        label.setHeight(Tabellone.H_ISCRIZIONI);
        label.addStyleName("cfunzione");
        addComponent(label);
        return label;
    }// end of method


}// end of class
