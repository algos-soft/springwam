package it.algos.springwam.entity.funzione;

import com.sun.deploy.panel.ExceptionListDialog;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.LibResource;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.annotation.AIView;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.renderer.IconRenderer;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.enumeration.EARoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_17:07:10
 * Estende la Entity astratta AList di tipo AView per visualizzare la Grid
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @SpringView (obbligatorio) per gestire la visualizzazione di questa view con SprinNavigator
 * Annotated with @AIView (facoltativo) per selezionarne la 'visibilità' secondo il ruolo dell'User collegato
 * Costruttore con un link @Autowired al IAPresenter, di tipo @Lazy per evitare un loop nella injection
 */
@SpringComponent
@Scope("session")
@Qualifier(AppCost.TAG_FUN)
@SpringView(name = AppCost.VIEW_FUN_LIST)
@AIView(roleTypeVisibility = EARoleType.user)
@AIScript(sovrascrivibile = true)
public class FunzioneList extends AList {


    @Autowired
    private AResourceService resource;

    private final static String ICON_FIELD_NAME = "Icona";

    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = AppCost.TAG_FUN;


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
    public FunzioneList(
            @Lazy @Qualifier(AppCost.TAG_FUN) IAPresenter presenter,
            @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    @Override
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
        super.fixCaption(entityClazz, items);
        if (login.isDeveloper()) {
            caption += "</br>Lista visibile a tutti";
            caption += "</br>Solo il developer vede queste note";
        }// end of if cycle
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     *
     * @param source
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    protected void creaBody(IAPresenter source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        super.creaBody(source, entityClazz, columns, items);
        grid.getGrid().setRowHeight(47);
        addColumnIcona();
    }// end of method

    /**
     * Crea la colonna (di tipo Component) per visualizzare l'icona
     */
    private void addColumnIcona() {
        IconRenderer renderIcon = new IconRenderer();

        Grid.Column colonna = grid.getGrid().addComponentColumn(funzione -> {
            int codePoint = ((Funzione) funzione).getIcona();

            if (codePoint > 0) {
                VaadinIcons icona = resource.getVaadinIcon(codePoint);
                Button button = new Button(icona);
                button.setHeight("2em");
                button.setWidth("3em");
                return button;
            } else {
                return null;
            }// end of if/else cycle

        });//end of lambda expressions

        colonna.setId(ICON_FIELD_NAME);
        colonna.setCaption(ICON_FIELD_NAME);
        colonna.setWidth(AColumnService.WIDTH_ICON);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + AColumnService.WIDTH_ICON ,Unit.PIXELS);
        if (true) {
            grid.getGrid().setColumnOrder("ordine", ICON_FIELD_NAME, "sigla", "descrizione");
        } else {
            grid.getGrid().setColumnOrder("ordine", ICON_FIELD_NAME, "sigla", "descrizione");
        }// end of if/else cycle

    }// end of method

}// end of class
