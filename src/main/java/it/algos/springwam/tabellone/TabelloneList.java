package it.algos.springwam.tabellone;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.components.grid.GridSelectionModel;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibParams;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 08:56
 */
@SpringComponent
@Slf4j
@Qualifier(AppCost.TAG_TAB)
public class TabelloneList extends AlgosListImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public TabelloneList(@Qualifier(AppCost.TAG_CRO) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        super.caption = LibSession.getCompany().getDescrizione()+" - Situazione dei turni previsti per i servizi dal 24 nov al 31 nov";
    }// end of method

    /**
     * Creazione della grid
     * Ricrea tutto ogni volta che diventa attivo
     *
     * @param source      di riferimento per gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    public void restart(AlgosPresenterImpl source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        Label label;
        this.setMargin(false);
        List<String> listaBottoni;
        this.removeAllComponents();

        //--gestione delle scritte in rosso sopra la Grid
        fixCaption(entityClazz.getSimpleName(), items);
        if (LibText.isValid(caption)) {
            if (LibParams.usaAvvisiColorati()) {
                label = new LabelRosso(caption);
            } else {
                label = new Label(caption);
            }// end of if/else cycle
            this.addComponent(label);
        }// end of if cycle

        grid.inizia(entityClazz, columns, items);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.addComponent(grid);

        //--Prepara la toolbar e la aggiunge al contenitore grafico
        listaBottoni = service.getListBottonNames();
        inizializzaToolbar(source, listaBottoni);
        fixToolbar();
        this.addComponent((ListToolbar) toolbar);

        if (pref.isTrue(Cost.KEY_USE_DEBUG)) {
            this.addStyleName("rosso");
            grid.addStyleName("verde");
        }// fine del blocco if


        columnsTurni(items);
    }// end of method


    /**
     * Crea le colonne (di tipo Component) per visualizzare i turni
     */
    private void columnsTurni(List items) {
        Riga riga = null;
        int colonne = 0;
        LocalDate giornoInizio = null;

        if (items == null) {
            return;
        }// end of if cycle

        if (items instanceof List && items.size() > 0) {
            Object obj = items.get(0);

            if (obj != null && obj instanceof Riga) {
                riga = ((Riga) obj);
                colonne = riga.getTurni().size();
                giornoInizio = riga.getGiorno();
            }// end of if cycle
        }// end of if cycle

        if (colonne > 0) {
            for (int k = 0; k < colonne; k++) {
                columnTurno(k);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare il turno
     */
    private void columnTurno(int k) {
        Component comp;
        Grid.Column colonna = grid.addComponentColumn(riga -> {
            String name;
            List<Turno> turni = ((Riga) riga).getTurni();
//            name = turni.get(k).getServizio().getCode();
            return new Label("Pippoz");
        });//end of lambda expressions

        fixColumn(colonna, k + "a", k + "b", 110);
    }// end of method


//    /**
//     * Creazione della grid
//     * Ricrea tutto ogni volta che diventa attivo
//     *
//     * @param mappa righe/colonne da visualizzare nella Grid
//     */
//    public void restart(List<LinkedHashMap<String, String>> mappa) {
//        Grid<LinkedHashMap<String, String>> grid = null;
//
//        this.removeAllComponents();
//
//        // Add the grid to the page
//        grid = addGrid(mappa);
//        if (grid!=null) {
//            this.addComponent(addGrid(mappa));
//        } else {
//            log.warn("Non sono riuscito a costruire il tabellone");
//        }// end of if/else cycle
//
//    }// end of method


//    public Grid addGrid(List<LinkedHashMap<String, String>> mappa) {
//        int rowHeith = 80;
//
//        if (mappa == null || mappa.size() == 0) {
//            return null;
//        }// end of if cycle
//
//        // Create the grid
//        Grid<LinkedHashMap<String, String>> grid = new Grid<>();
//        this.setWidth("66.5em");
//        grid.setWidth("66.5em");
//        grid.setHeightByRows(mappa.size());
//        grid.setRowHeight(rowHeith);
//
//        //set its items
//        grid.setItems(mappa);
//
//        // Add the columns based on the first row
//        HashMap<String, String> s = mappa.get(0);
//        int k = 1;
//        for (Map.Entry<String, String> entry : s.entrySet()) {
//            Grid.Column colonna = grid.addColumn(h -> h.get(entry.getKey())).setCaption(entry.getKey());
//            if (k == 1) {
//                colonna.setWidth(250);
//            } else {
//                colonna.setWidth(135);
//            }// end of if/else cycle
//            k++;
//        }// end of method
//
//        return grid;
//    }// end of method


}// end of class
