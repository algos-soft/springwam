package it.algos.springvaadin.grid;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.SingleSelect;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AArrayService;
import it.algos.springvaadin.service.AColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:51
 */
@Slf4j
@SpringComponent
@Scope("prototype")
public class AGrid extends Grid implements IAGrid {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AArrayService array;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public AColumnService columnService;


    /**
     * Property iniettata nel costruttore
     */
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * Presenter di riferimento per i componenti da cui vengono generati gli eventi
     */
    protected IAPresenter presenter;

    private AGridToolSet gridToolSet;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     */
    public AGrid(AGridToolSet gridToolSet, ApplicationEventPublisher applicationEventPublisher) {
        this.gridToolSet = gridToolSet;
        this.applicationEventPublisher = applicationEventPublisher;
    }// end of @Autowired constructor

//    /**
//     * Metodo invocato da AList
//     *
//     * @param beanType    modello dei dati
//     * @param columns     visibili ed ordinate della Grid
//     * @param items       da visualizzare nella Grid
//     * @param numeroRighe da visualizzare nella Grid
//     */
//    @Override
//    public void inizia(Class<? extends AEntity> beanType, List<Field> columns, List items, int numeroRighe) {
//        this.setBeanType(beanType);
//        this.setRowHeight(50);
//        this.addColumn("Alfa");
//
//        if (items != null && items.size() > 0) {
//            this.setItems(items);
//            this.setHeightMode(HeightMode.ROW);
//            this.setHeightByRows(items.size());
//        }// end of if cycle
//
//        //--Aggiunge alla grid tutti i listener previsti
////        addAllListeners();
//    }// end of method

    /**
     * Metodo invocato da AlgosListImpl
     *
     * @param beanType    modello dei dati
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param numeroRighe da visualizzare nella Grid
     */
    public void inizia(IAPresenter presenter, Class<? extends AEntity> beanType, List<Field> columns, List items, int numeroRighe) {
        this.presenter = presenter;
//        this.setBeanType(beanType != null ? beanType : Role.class);
        this.setBeanType(beanType );
        this.setRowHeight(40);
        this.setWidth(0,Unit.EM);
        this.addColumns(columns);

        if (items != null && items.size() > 0) {
            this.setItems(items);
            this.setHeightMode(HeightMode.ROW);
            this.setHeightByRows(items.size());
        }// end of if cycle

        //--Aggiunge alla grid tutti i listener previsti
        addAllListeners();

        //--Aggiunge a tutte le azioni i IAListener  che generano ed ascoltano gli eventi
        gridToolSet.addAllSourcesTargets(presenter, presenter);
    }// end of method


    /**
     * Aggiunge alla grid tutti i listener previsti
     * Le azioni verranno intercettate e gestite dal presenter (recuperato nel 'fire' dell'azione)
     */
    public void addAllListeners() {
        //--recupera il presenter
//        AlgosPresenterImpl presenter = LibSpring.getPresenter();

        //@todo RIMETTERE
//        //--modello di selezione righe
//        //--lancia (fire) un evento per la condizione iniziale di default della selezione nella Grid
//        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
//            this.setSelectionMode(SelectionMode.MULTI);
//            applicationEventPublisher.publishEvent(new AActionEvent(TypeAction.multiSelectionChanged, presenter));
//        } else {
//            this.setSelectionMode(Grid.SelectionMode.SINGLE);
//            applicationEventPublisher.publishEvent(new AActionEvent(TypeAction.singleSelectionChanged, presenter));
//        }// end of if/else cycle

        this.setSelectionMode(SelectionMode.SINGLE);

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

        if (array.isValid(columns)) {
            for (Field field : columns) {
                colonna = columnService.add(this, field);
                if (colonna!=null) {
                    lar += columnService.regolaAnnotationAndGetLarghezza(colonna, field);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

//        if (columns != null && columns.size() > 0) {
//            for (Field field : columns) {
//                try { // prova ad eseguire il codice
//                    colonna = columnService.add(this, field);
//                } catch (Exception unErrore) { // intercetta l'errore
//                    log.error(unErrore.toString());
//                }// fine del blocco try-catch
//                lar += columnService.regolaAnnotationAndGetLarghezza(colonna, field);
//            }// end of for cycle
//        }// end of if cycle

        //--spazio per la colonna automatica di selezione
        //@todo RIMETTERE
//        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
//            lar += 50;
//        }// end of if cycle

        this.setWidth(lar + "px");
    }// end of method


    /**
     * Una sola riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    public boolean isUnaSolaRigaSelezionata() {
        boolean selezionata = false;
        HasValue selezione;


        selezione = this.asSingleSelect();
        if (selezione instanceof SingleSelect) {
            selezionata = !selezione.isEmpty();
        }// end of if cycle

        //@todo RIMETTERE
//        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
//            try { // prova ad eseguire il codice
//                selezione = this.asMultiSelect();
//                if (selezione instanceof MultiSelect) {
//                    selezionata = ((MultiSelect) selezione).getSelectedItems().size() == 1;
//                }// end of if cycle
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//        } else {
//            try { // prova ad eseguire il codice
//                selezione = this.asSingleSelect();
//                if (selezione instanceof SingleSelect) {
//                    selezionata = !selezione.isEmpty();
//                }// end of if cycle
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//        }// end of if/else cycle

        return selezionata;
    }// end of method


    public AEntity getEntityBean() {
        List<AEntity> beanList = null;
        AEntity entityBean;
        Object[] matrice;

        entityBean = (AEntity) this.asSingleSelect().getValue();

        //@todo RIMETTERE
//        if (pref.isTrue(Cost.KEY_USE_SELEZIONE_MULTIPLA_GRID)) {
//            try { // prova ad eseguire il codice
//                matrice = this.asMultiSelect().getSelectedItems().toArray();
//                beanList = new ArrayList();
//                for (Object obj : matrice) {
//                    beanList.add((AEntity) obj);
//                }// end of for cycle
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//            return beanList;
//        } else {
//            try { // prova ad eseguire il codice
//                entityBean = (AEntity) this.asSingleSelect().getValue();
//                beanList = new ArrayList();
//                beanList.add(entityBean);
//            } catch (Exception unErrore) { // intercetta l'errore
//            }// fine del blocco try-catch
//            return beanList;
//        }// end of if/else cycle

        return entityBean;
    }// end of method

    /**
     * Componente concreto di questa interfaccia
     */
    @Override
    public AGrid getGrid() {
        return this;
    }// end of method

}// end of class
