package it.algos.springwam.entity.turno;

import com.vaadin.data.HasValue;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.iscrizione.IscrizioneService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.webbase.web.field.AIField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mar, 13-feb-2018
 * Time: 10:11
 */
@Slf4j
@SpringComponent
@Scope("prototype")
public class TurnoFieldIscrizioni extends AField {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AHtmlService html;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AColumnService column;


    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private MiliteService militeService;

    private Grid grid;

    private List<Iscrizione> items;

    private int larghezzaGrid;

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    @Override
    public void creaContent() {

        grid = new Grid(Iscrizione.class);
        grid.setWidth("100%");
        grid.setRowHeight(47);
        grid.setHeightByRows(5);
        grid.setStyleName("");
        grid.removeAllColumns();

        int larFunzione = 120;
        int larMilite = 270;
        int larDurata = 70;
        larghezzaGrid = larFunzione + larMilite + larDurata;

        //--aggiunge una colonna calcolata
        Grid.Column colonnaFunzione = grid.addComponentColumn(iscrizione -> {
            Funzione funz = ((Iscrizione) iscrizione).getFunzione();
            Milite milite = ((Iscrizione) iscrizione).getMilite();
            Label label = new Label("", ContentMode.HTML);
            String labelTxt = "";

            if (funz == null) {
                labelTxt = html.setRossoBold("Manca funz.");
                label.setValue(labelTxt);
                return label;
            }// end of if cycle

            labelTxt = funz.getSigla();
            if (milite != null) {
                labelTxt = html.setVerdeBold(labelTxt);
            } else {
                if (funz.isObbligatoria()) {
                    labelTxt = html.setRossoBold(labelTxt);
                } else {
                    labelTxt = html.setBluBold(labelTxt);
                }// end of if/else cycle
            }// end of if/else cycle
            label.setValue(labelTxt);

            return label;
        });//end of lambda expressions
        colonnaFunzione.setCaption("Funzione");
        colonnaFunzione.setId("funzione");
        colonnaFunzione.setWidth(larFunzione);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaComboMilite = grid.addComponentColumn(iscrizione -> {
            Funzione funz = ((Iscrizione) iscrizione).getFunzione();
            Milite milite = ((Iscrizione) iscrizione).getMilite();
            List<Milite> listaMilitiDellaCroceAbilitatiPerLaFunzione = militeService.findAllByFunzione(funz);
            int num = listaMilitiDellaCroceAbilitatiPerLaFunzione.size();
            ComboBox combo = new ComboBox();
            combo.setWidth(larMilite - 40, Sizeable.Unit.PIXELS);
            combo.setItems(listaMilitiDellaCroceAbilitatiPerLaFunzione);
            combo.setValue(milite);

            combo.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                    militeChanged(funz, valueChangeEvent.getOldValue(), valueChangeEvent.getValue());
                }// end of inner method
            });// end of anonymous inner class

            return combo;
        });//end of lambda expressions
        colonnaComboMilite.setCaption("Milite");
        colonnaComboMilite.setId("milite");
        colonnaComboMilite.setWidth(larMilite);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaDurata = grid.addColumn(iscrizione -> {
            return ((Iscrizione) iscrizione).getDurata();
        });//end of lambda expressions
        colonnaDurata.setCaption("Ore");
        colonnaDurata.setId("durata");
        colonnaDurata.setWidth(larDurata);

        ArrayList lista = new ArrayList();
        lista.add("funzione");
        lista.add("milite");
        lista.add("durata");
        grid.setColumns((String[]) lista.toArray(new String[lista.size()]));
        grid.setWidth(larghezzaGrid, Sizeable.Unit.PIXELS);

        grid.setStyleGenerator(new StyleGenerator() {
            @Override
            public String apply(Object o) {
                return "error_row";
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    @Override
    public void setWidth(String width) {
        if (grid != null) {
            grid.setWidth(larghezzaGrid, Sizeable.Unit.PIXELS);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        if (items != null && items.size() > 0) {
            grid.setItems(items);
        }// end of if cycle

        return grid;
    }// end of method

    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        return items;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
    }// end of method


    /**
     * Cambiato il milite selezionato per una iscrizione
     */
    private void militeChanged(Funzione funz, Object oldValue, Object newValue) {
        for (Iscrizione iscr : items) {
            if (iscr.getFunzione() == funz) {
                iscr.setMilite((Milite) newValue);
            }// end of if cycle
        }// end of for cycle

        grid.getDataProvider().refreshAll();
    }// end of method


    public void setItems(List<Iscrizione> items) {
        this.items = items;
    }// end of method

    public List<Iscrizione> getItems() {
        return items;
    }// end of method

}// end of class

