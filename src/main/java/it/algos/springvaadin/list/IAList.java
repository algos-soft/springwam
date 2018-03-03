package it.algos.springvaadin.list;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.grid.AGrid;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.view.IAView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 23:08
 */
public interface IAList extends IAView {

    /**
     * Creazione di una view (AList) contenente una Grid
     * Metodo invocato dal gestore (presenter( (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param typeButtons lista di (tipi di) bottoni visibili nella toolbar della view AList
     */
    public void start(Class<? extends AEntity> entityClazz, List<Field> columns, List items, List<EATypeButton> typeButtons);


    /**
     * Una sola riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    public boolean isUnaSolaRigaSelezionata();


    /**
     * Componente concreto di questa interfaccia
     */
    public AList getList();


    /**
     * Componente Grid della List
     */
    public AGrid getGrid();

}// end of interface
