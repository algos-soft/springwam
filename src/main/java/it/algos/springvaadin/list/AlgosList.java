package it.algos.springvaadin.list;

import com.vaadin.ui.Component;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 09/07/17
 * Presenta i dati di una lista di entities, usando una Grid
 * Conosce solo la entityClass, gli items e le columns che gli vengono passati dal presenter nel metodo restart
 * Responsabile di presentare i dati e lanciare gli eventi necessari
 * Si inietta (nella classe concreta) la AlgosGrid che contiene le azioni che generano gli eventi
 * Si inietta (nella classe concreta) la ListToolbar che contiene i bottoni che generano gli eventi
 * I bottoni e le azioni sono ''prototype'', cioè ne viene generato uno per ogni xxxPresenter -> xxxView -> xxxList
 * La AlgosGrid è ''prototype'', cioè ne viene generata una diversa per ogni xxxPresenter -> xxxView -> xxxList
 */
public interface AlgosList {

    /**
     * Creazione della grid
     * Ricrea tutto ogni volta che diventa attivo
     *
     * @param presenter   di riferimento per gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    public void restart(AlgosPresenterImpl presenter, Class<? extends AEntity> entityClazz, List<Field> columns, List items);


    /**
     * Righe selezionate nella Grid
     *
     * @return numero di righe selezionate
     */
    public int numRigheSelezionate();


    /**
     * Una riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    public boolean isUnaRigaSelezionata();


    /**
     * Abilita o disabilita lo specifico bottone della Toolbar
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    public void enableButton(AButtonType type, boolean status);


//    /**
//     * Abilita il bottone Create della Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableNew(boolean status);
//
//
//    /**
//     * Abilita il bottone Edit della Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableEdit(boolean status);
//
//
//    /**
//     * Abilita il bottone Delete della Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableDelete(boolean status);
//
//
//    /**
//     * Abilita il bottone Search della Grid
//     *
//     * @param status true se abilitato, false se disabilitato
//     */
//    public void enableSearch(boolean status);


    /**
     * Una lista di entity selezionate nella Grid, in funzione di Grid.SelectionMode()
     * Lista nulla, se nessuna riga è selezionata
     * Lista di un elemento, se è Grid.SelectionMode.SINGLE
     * Lista di uno o più elementi, se è Grid.SelectionMode.MULTI
     *
     * @return lista di una o più righe selezionate, null se nessuna riga è selezionata
     */
    public List<AEntity> getEntityBeans();


    /**
     * Elemento selezionato nella Grid
     *
     * @return entityBean
     */
    public AEntity getEntityBean();


    /**
     * Restituisce il componente concreto
     *
     * @return il componente (window o panel)
     */
    public Component getComponent();

    public void setPresenter(AlgosPresenterImpl presenter);

}// end of interface
