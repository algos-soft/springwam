package it.algos.springvaadin.grid;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.IAPresenter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-dic-2017
 * Time: 07:52
 */
public interface IAGrid {

    /**
     * Metodo invocato da AList
     *
     * @param beanType    modello dei dati
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param numeroRighe da visualizzare nella Grid
     */
    public void inizia(IAPresenter presenter, Class<? extends AEntity> beanType, List<Field> columns, List items, int numeroRighe);


    /**
     * Una sola riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    public boolean isUnaSolaRigaSelezionata();


    public AGrid getGrid();

}// end of interface
