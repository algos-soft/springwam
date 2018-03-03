package it.algos.springwam.entity.servizio;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.annotation.AIView;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springwam.entity.funzione.Funzione;
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
 * Date: 2018-01-16_08:50:45
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
@Qualifier(AppCost.TAG_SER)
@SpringView(name = AppCost.VIEW_SER_LIST)
@AIScript(sovrascrivibile = true)
public class ServizioList extends AList {


    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = AppCost.TAG_SER;


    /**
     * Icona visibile nel menu (facoltativa)
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;

    @Autowired
    private AResourceService resource;

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
     * @param toolbar iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public ServizioList(
            @Lazy @Qualifier(AppCost.TAG_SER) IAPresenter presenter,
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
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    protected void creaBody(Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        super.creaBody( entityClazz, columns, items);
//        grid.getGrid().setRowHeight(47);
//        addColumnOrarioBool();
//        addColumnOrarioText();
//        addColumnVisibile();
        addColumnFunzioni();
    }// end of method


//    /**
//     * Crea la colonna orario (di tipo CheckBox)
//     */
//    private void addColumnOrarioBool() {
//        String idKey = "orarioBool";
//        Grid.Column colonna = grid.addComponentColumn(servizio -> {
//            if (((Servizio) servizio).isOrario()) {
//                return new LabelVerde(VaadinIcons.CHECK);
//            } else {
//                return new LabelRosso(VaadinIcons.CLOSE);
//            }// end of if/else cycle
//        });//end of lambda expressions
//
//        fixColumn(colonna, idKey, "Fix", LibColumn.WIDTH_CHECK_BOX);
//        spostaColonnaPrima(idKey, "oraInizio");
//    }// end of method


//    /**
//     * Crea la colonna orario (di tipo String)
//     */
//    private void addColumnOrarioText() {
//        String idKey = "orarioText";
//        Grid.Column colonna = grid.addColumn(servizio -> {
//            return service.getStrOrario((Servizio) servizio);
//        });//end of lambda expressions
//
//        fixColumn(colonna, idKey, "Orario", 160);
//        spostaColonnaPrima(idKey, "visibile");
//    }// end of method


//    /**
//     * Crea la colonna tabellone (di tipo CheckBox)
//     */
//    private void addColumnVisibile() {
//        String idKey = "tab";
//        Grid.Column colonna = grid.addComponentColumn(servizio -> {
//            if (((Servizio) servizio).isVisibile()) {
//                return new LabelVerde(VaadinIcons.CHECK);
//            } else {
//                return new LabelRosso(VaadinIcons.CLOSE);
//            }// end of if/else cycle
//        });//end of lambda expressions
//
//        fixColumn(colonna, idKey, "Tab", 68);
//        spostaColonnaDopo("tab", "oraFine");
//    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void addColumnFunzioni() {
        Grid.Column colonna = grid.getGrid().addComponentColumn(servizio -> {
            String valueLabel = "";
            String valueFunz;
            String tag = " - ";

            List<Funzione> funzioni = ((Servizio) servizio).getFunzioni();
            if (funzioni != null && funzioni.size() > 0) {
                for (Funzione funz : funzioni) {
                    if (funz != null) {
                        valueFunz=resource.getVaadinIcon(funz.getIcona()).getHtml();
                        valueFunz += " ";
                        valueFunz += funz.getSigla();
                        if (funz.isObbligatoria()) {
                            valueFunz = html.setRossoBold(valueFunz);
                        } else {
                            valueFunz = html.setBluBold(valueFunz);
                        }// end of if/else cycle
                        valueLabel += valueFunz + tag;
                    }// end of if cycle
                }// end of for cycle
                valueLabel = text.levaCoda(valueLabel, tag);
                return new Label(valueLabel, ContentMode.HTML);
            } else {
                return null;
            }// end of if/else cycle
        });//end of lambda expressions

//        fixColumn(colonna, "funzioni", "Funzioni del servizio", 350);//@todo ricreare un metodo generico
        colonna.setId("funzioni");
        colonna.setCaption("Funzioni del servizio");
        colonna.setWidth(400);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + 400, Unit.PIXELS);

    }// end of method

}// end of class
