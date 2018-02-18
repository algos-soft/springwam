package it.algos.springvaadin.form;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.view.IAView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 23:23
 */
public interface IAForm extends IAView {




    /**
     * Creazione di una view (AForm) contenente i fields
     * Metodo invocato dal Presenter (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * La view comprende:
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param entityClazz         di riferimento, sottoclasse concreta di AEntity
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param typeButtons         lista di (tipi di) bottoni visibili nella toolbar della view AList
     */
    public void start( Class<? extends AEntity> entityClazz, List<Field> reflectedJavaFields, List<EATypeButton> typeButtons);


    /**
     * Creazione di una view (AForm) contenente i fields
     * Metodo invocato dal Presenter (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * La view comprende:
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param gestore              di riferimento per gli eventi
     * @param entityClazz         di riferimento, sottoclasse concreta di AEntity
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     * @param typeButtons         lista di (tipi di) bottoni visibili nella toolbar della view AList
     */
    public void start(IAPresenter gestore, Class<? extends AEntity> entityClazz, List<Field> reflectedJavaFields, List<EATypeButton> typeButtons);


    /**
     * Trasferisce i valori dai fields del Form alla entityBean
     * Esegue la (eventuale) validazione dei dati
     * Esegue la (eventuale) trasformazione dei dati
     *
     * @return la entityBean del Form
     */
    public AEntity commit();


    /**
     * Esegue il 'rollback' del Form
     * Revert (ripristina) button pressed in form
     * Usa la entityBean già presente nel form, ripristinando i valori iniziali
     */
    public void revert() ;


    /**
     * Componente concreto di questa interfaccia
     */
    public AForm getForm();


    /**
     * Entity bean
     */
    public AEntity getBean();

}// end of interface
