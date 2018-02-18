package it.algos.springvaadin.view;

import com.vaadin.navigator.View;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.presenter.IAPresenter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 21:23
 */
public interface IAView extends View {

    /**
     * Elimina il menuLayout dalla vista 'uscente'
     */
    public void removeComponents();

//    /**
//     * Creazione di una view contenente una Grid
//     * Ricrea tutto ogni volta che la view diventa attiva
//     * La view comprende anche il menuLayout, una caption della Grid ed un footer di bottoni-comando
//     *
//     * @param source      di riferimento per gli eventi
//     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
//     * @param entityBean  di riferimento
//     * @param columns     visibili ed ordinate della Grid
//     * @param items       da visualizzare nella Grid
//     * @param typeButtons lista di (tipi di) bottoni visibili nella toolbar della view AList
//     */
//    public void start(IAPresenter source, Class<? extends AEntity> entityClazz, AEntity entityBean, List<Field> columns, List items, List<EATypeButton> typeButtons);


    /**
     * Recupera il bottone del tipo specifico
     * Ce ne può essere uno solo nella toolbar
     *
     * @param type del bottone, secondo la Enumeration AButtonType
     */
    public AButton getButton(EATypeButton type);


    /**
     * Recupera la classe del modello dati (Entity)
     *
     * @return la classe di Entity
     */
    public AEntity getEntityClass();


    /**
     * Componente concreto di questa interfaccia
     */
    public AView getView();

}// end of interface
