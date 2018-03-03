package it.algos.springvaadin.azione;

import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.presenter.IAPresenter;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 12-dic-2017
 * Time: 08:17
 * Interfaccia per le azioni generate da AGrid e e dai Fields
 * Le azioni sono ''prototype'', cioè ne viene generata una per ogni xxxPresenter -> xxxView
 */
public interface IAAction {


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    public void addListener(IAGrid algosGrid);


    /**
     * Aggiunge il source IAListener alla AAction
     *
     * @param source di riferimento del componente che genera gli eventi
     */
    public void setSource(IAPresenter source);


    /**
     * Aggiunge il target IAListener alla AAction
     *
     * @param target di riferimento per il componente che ascolta gli eventi
     */
    public void setTarget(IAPresenter target);


    /**
     * Aggiunge il source IAListener alla AAction
     * Aggiunge il target IAListener alla AAction
     *
     * @param source di riferimento del componente che genera gli eventi
     * @param target di riferimento per il componente che ascolta gli eventi
     */
    public void setSourceTarget(IAPresenter source, IAPresenter target);

}// end of interface
