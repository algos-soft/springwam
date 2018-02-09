package it.algos.springwam.tabellone;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: gio, 08-feb-2018
 * Time: 22:55
 * Estende la generica AView per visualizzare la Grid
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIView (facoltativo) per selezionarne la 'visibilità' secondo il ruolo dell'User collegato
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@Slf4j
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_TAB)
@SpringView(name = AppCost.VIEW_TAB_LIST)
@AIScript(sovrascrivibile = false)
public class Tabellone extends AList {

    /**
     * Contenitore grafico per la barra di menu
     * Componente grafico obbligatorio
     */
    @Autowired
    private TabelloneMenuLayout tabMenuLayout;

    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;

    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = AppCost.TAG_TAB;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Use @Lazy to avoid the Circular Dependency
     * A simple way to break the cycle is saying Spring to initialize one of the beans lazily.
     * That is: instead of fully initializing the bean, it will create a proxy to inject it into the other bean.
     * The injected bean will only be fully created when it’s first needed.
     *
     * @param presenter iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public Tabellone(@Lazy @Qualifier(AppCost.TAG_TAB) IAPresenter presenter) {
        super(presenter, null);
    }// end of Spring constructor



    /**
     * Creazione di una view (AList) contenente una Grid
     * Metodo invocato dal Presenter (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * La view comprende:
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param source      presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param typeButtons lista di (tipi di) bottoni visibili nella toolbar della view AList
     */
    public void start(IAPresenter source, Class<? extends AEntity> entityClazz, List<Field> columns, List items, List<EAButtonType> typeButtons) {
        this.removeAllComponents();
        tabMenuLayout.start();
        this.addComponent(tabMenuLayout);

        //--componente grafico obbligatorio
        tabMenuLayout.start();
        this.addComponent(tabMenuLayout);

        //--componente grafico obbligatorio
        this.creaBody(source, entityClazz, columns, items);
        this.addComponent(bodyLayout);

        this.setExpandRatio(bodyLayout, 1);
    }// end of method


}// end of class

