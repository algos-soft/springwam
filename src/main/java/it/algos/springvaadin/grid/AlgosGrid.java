package it.algos.springvaadin.grid;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.SingleSelect;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.event.*;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 02/06/17.
 * Semplifica la costruzione di una Grid
 * Costruisce i listener che lanciano (fire) gli eventi
 */
@SpringComponent
@Scope("prototype")
@Slf4j
public class AlgosGrid extends Grid {

    private final static int NUMERO_RIGHE_DEFAULT = 12;
    private final static double ALTEZZA_RIGHE_DEFAULT = 50;
    private final GridToolSet gridToolSet;

    @Autowired
    private PreferenzaService pref;

    /**
     * Property iniettata nel costruttore
     */
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     */
    public AlgosGrid(GridToolSet gridToolSet, ApplicationEventPublisher applicationEventPublisher) {
        this.gridToolSet = gridToolSet;
        this.applicationEventPublisher = applicationEventPublisher;
    }// end of @Autowired constructor


    /**
     * Metodo invocato da AlgosListImpl
     *
     * @param beanType modello dei dati
     * @param columns  visibili ed ordinate della Grid
     * @param items    da visualizzare nella Grid
     */
    public void inizia(Class<? extends AEntity> beanType, List<Field> columns, List items) {
        this.inizia(beanType, columns, items, NUMERO_RIGHE_DEFAULT);
    }// end of method


    /**
     * Metodo invocato da AlgosListImpl
     *
     * @param beanType    modello dei dati
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param numeroRighe da visualizzare nella Grid
     */
    public void inizia(Class<? extends AEntity> beanType, List<Field> columns, List items, int numeroRighe) {
        this.setBeanType(beanType);
        this.setRowHeight(ALTEZZA_RIGHE_DEFAULT);
        this.addColumns(columns);

        if (items != null && items.size() > 0) {
            this.setItems(items);
            this.setHeightMode(HeightMode.ROW);
            this.setHeightByRows(items.size());
        }// end of if cycle

        //--Aggiunge alla grid tutti i listener previsti
        addAllListeners();
    }// end of method

    /**
     * Aggiunge alla grid tutti i listener previsti
     * Le azioni verranno intercettate e gestite dal presenter (recuperato nel 'fire' dell'azione)
     */
    public void addAllListeners() {
        //--recupera il presenter
        AlgosPresenterImpl presenter = LibSpring.getPresenter();

        //--modello di selezione righe
        //--lancia (fire) un evento per la condizione iniziale di default della selezione nella Grid
        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            this.setSelectionMode(SelectionMode.MULTI);
            applicationEventPublisher.publishEvent(new AActionEvent(TypeAction.multiSelectionChanged, presenter));
        } else {
            this.setSelectionMode(Grid.SelectionMode.SINGLE);
            applicationEventPublisher.publishEvent(new AActionEvent(TypeAction.singleSelectionChanged, presenter));
        }// end of if/else cycle

        gridToolSet.addAllListeners(this);
    }// end of method


    /**
     * Utilizza l'Annotation @AIColumn del Model per regolare le caratteristiche delle colonne
     * type() default AFieldType.text;
     * header() default "";
     * width() default 80;
     * prompt() default "";
     * help() default "";
     *
     * @param columns visibili ed ordinate della Grid
     */
    public void addColumns(List<Field> columns) {
        Grid.Column colonna = null;
        Class<? extends AEntity> clazz = this.getBeanType();
        int lar = 0;

        if (this.getColumns() != null && this.getColumns().size() > 0) {
            this.removeAllColumns();
        }// end of if cycle

        if (columns != null && columns.size() > 0) {
            for (Field field : columns) {
                try { // prova ad eseguire il codice
                    colonna = LibColumn.add(this, field);
                } catch (Exception unErrore) { // intercetta l'errore
                    log.error(unErrore.toString());
                }// fine del blocco try-catch
                lar += LibColumn.regolaAnnotationAndGetLarghezza(colonna, field);
            }// end of for cycle
        }// end of if cycle

        //--spazio per la colonna automatica di selezione
        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            lar += 50;
        }// end of if cycle

        this.setWidth(lar + "px");
    }// end of method


    public int numRigheSelezionate() {
        int numRighe = 0;

        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            numRighe = this.asMultiSelect().getSelectedItems().size();
        } else {
            if (!this.asSingleSelect().isEmpty()) {
                numRighe = 1;
            }// end of if cycle
        }// end of if/else cycle

        return numRighe;
    }// end of method


    public boolean unaRigaSelezionata() {
        boolean selezionata = false;
        HasValue selezione;

        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            try { // prova ad eseguire il codice
                selezione = this.asMultiSelect();
                if (selezione instanceof MultiSelect) {
                    selezionata = ((MultiSelect) selezione).getSelectedItems().size() == 1;
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        } else {
            try { // prova ad eseguire il codice
                selezione = this.asSingleSelect();
                if (selezione instanceof SingleSelect) {
                    selezionata = !selezione.isEmpty();
                }// end of if cycle
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// end of if/else cycle

        return selezionata;
    }// end of method


    public List<AEntity> getEntityBeans() {
        List<AEntity> beanList = null;
        AEntity entityBean;
        Object[] matrice;

        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            try { // prova ad eseguire il codice
                matrice = this.asMultiSelect().getSelectedItems().toArray();
                beanList = new ArrayList();
                for (Object obj : matrice) {
                    beanList.add((AEntity) obj);
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            return beanList;
        } else {
            try { // prova ad eseguire il codice
                entityBean = (AEntity) this.asSingleSelect().getValue();
                beanList = new ArrayList();
                beanList.add(entityBean);
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            return beanList;
        }// end of if/else cycle

    }// end of method


    public AEntity getEntityBean() {
        AEntity entityBean = null;
        Object[] matrice;

        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
            matrice = this.asMultiSelect().getSelectedItems().toArray();
            if (matrice.length == 1) {
                entityBean = (AEntity) matrice[0];
            }// end of if cycle
        } else {
            entityBean = (AEntity) this.asSingleSelect().getValue();
        }// end of if/else cycle

        return entityBean;
    }// end of method

//    public void setBeanType(Class beanType) {
//        this.setBeanType(beanType);
//    }// end of method

}// end of class
