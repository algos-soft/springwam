package it.algos.springvaadin.entity.role;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on 11-nov-17
 * Estende la Entity astratta AList di tipo AView per visualizzare la Grid
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIView (facoltativo) per selezionarne la 'visibilità' secondo il ruolo dell'User collegato
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(ACost.TAG_ROL)
@SpringView(name = ACost.VIEW_ROL_LIST)
@AIScript(sovrascrivibile = true)
public class RoleList extends AList {


    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = ACost.TAG_ROL;


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;


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
     * @param toolbar   iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public RoleList(
            @Lazy @Qualifier(ACost.TAG_ROL) IAPresenter presenter,
            @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


}// end of class
