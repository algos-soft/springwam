package it.algos.springwam.entity.milite;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.annotation.AIView;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.label.ALabelRosso;
import it.algos.springvaadin.label.ALabelRossoBold;
import it.algos.springvaadin.label.ALabelVerde;
import it.algos.springvaadin.label.ALabelVerdeBold;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.servizio.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-31_15:21:40
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
@Qualifier(AppCost.TAG_MIL)
@SpringView(name = AppCost.VIEW_MIL_LIST)
@AIScript(sovrascrivibile = true)
public class MiliteList extends AList {

    /**
     * Property iniettata da Spring
     */
    @Autowired
    protected FunzioneService funzioneService;


    public final static int WIDTH_CHECK_BOX = 70;
    public final static int WIDTH_NICK = 220;
    public final static int WIDTH_FUNZIONI = 105;

    /**
     * Label del menu (facoltativa)
     * SpringNavigator usa il 'name' della Annotation @SpringView per identificare (internamente) e recuperare la view
     * Nella menuBar appare invece visibile il MENU_NAME, indicato qui
     * Se manca il MENU_NAME, di default usa il 'name' della view
     */
    public static final String MENU_NAME = AppCost.TAG_MIL;


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
    public MiliteList(
            @Lazy @Qualifier(AppCost.TAG_MIL) IAPresenter presenter,
            @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


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
        super.creaBody(entityClazz, columns, items);

        columnAdmin();
        columnEnabled();
        columnDipendente();
        columnInfermiere();
        columnNickname();

        if (login.isLogged() && login.getCompany() != null) {
            columnsFunzioni();
        }// end of if cycle
    }// end of method


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
     * Crea la colonna admin (di tipo CheckBox)
     */
    private void columnAdmin() {
        Grid.Column colonna = grid.getGrid().addComponentColumn(milite -> {
            if (((Milite) milite).getRole().getCode().equals(EARoleType.admin.toString())) {
                Label labelVerde = new Label("<strong>" + "SI" + "</strong>", ContentMode.HTML);
                labelVerde.addStyleName("verde");
                return labelVerde;
            } else {
                Label labelRosso = new Label("<strong>" + "-" + "</strong>", ContentMode.HTML);
                labelRosso.addStyleName("rosso");
                return labelRosso;
            }// end of if/else cycle
        });//end of lambda expressions

        colonna.setId("admin");
        colonna.setCaption("Ad");
        colonna.setWidth(WIDTH_CHECK_BOX);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_CHECK_BOX, Unit.PIXELS);
        spostaColonnaPrima("admin", "nome");
    }// end of method


    /**
     * Crea la colonna enabled (di tipo CheckBox)
     */
    private void columnEnabled() {
        Grid.Column colonna = grid.getGrid().addComponentColumn(milite -> {
            if (((Milite) milite).isEnabled()) {
                Label labelVerde = new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML);
                labelVerde.addStyleName("verde");
                return labelVerde;
            } else {
                Label labelRosso = new Label(VaadinIcons.CLOSE.getHtml(), ContentMode.HTML);
                labelRosso.addStyleName("rosso");
                return labelRosso;
            }// end of if/else cycle
        });//end of lambda expressions

        colonna.setId("enabled2");
        colonna.setCaption("OK");
        colonna.setWidth(WIDTH_CHECK_BOX);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_CHECK_BOX, Unit.PIXELS);
    }// end of method


    /**
     * Crea la colonna dipendente (di tipo CheckBox)
     */
    private void columnDipendente() {
        Grid.Column colonna = grid.getGrid().addComponentColumn(milite -> {
            if (((Milite) milite).isDipendente()) {
                Label labelVerde = new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML);
                labelVerde.addStyleName("verde");
                return labelVerde;
            } else {
                Label labelRosso = new Label(VaadinIcons.CLOSE.getHtml(), ContentMode.HTML);
                labelRosso.addStyleName("rosso");
                return labelRosso;
            }// end of if/else cycle
        });//end of lambda expressions

        colonna.setId("dipendente2");
        colonna.setCaption("Dip");
        colonna.setWidth(WIDTH_CHECK_BOX);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_CHECK_BOX, Unit.PIXELS);
    }// end of method


    /**
     * Crea la colonna infermiere (di tipo CheckBox)
     */
    private void columnInfermiere() {
        Grid.Column colonna = grid.getGrid().addComponentColumn(milite -> {
            if (((Milite) milite).isInfermiere()) {
                Label labelVerde = new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML);
                labelVerde.addStyleName("verde");
                return labelVerde;
            } else {
                Label labelRosso = new Label(VaadinIcons.CLOSE.getHtml(), ContentMode.HTML);
                labelRosso.addStyleName("rosso");
                return labelRosso;
            }// end of if/else cycle
        });//end of lambda expressions

        colonna.setId("infermiere2");
        colonna.setCaption("Inf");
        colonna.setWidth(WIDTH_CHECK_BOX);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_CHECK_BOX, Unit.PIXELS);
    }// end of method


    /**
     * Crea la colonna ickname (di tipo String)
     */
    private void columnNickname() {
        Grid.Column colonna = grid.getGrid().addColumn(milite -> {
            return ((Milite) milite).getNickname();
        });//end of lambda expressions

        colonna.setId("nickname2");
        colonna.setCaption("Nick");
        colonna.setWidth(WIDTH_NICK);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_NICK, Unit.PIXELS);
    }// end of method

    /**
     * Crea le colonne (di tipo Component) per visualizzare le funzioni abilitate per il singolo milite
     */
    private void columnsFunzioni() {
        List<Funzione> funzioni = funzioneService.findAll();

        for (Funzione funz : funzioni) {
            columnFunzione(funz);
        }// end of for cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare le funzioni
     */
    private void columnFunzione(Funzione funzione) {
        Grid.Column colonna = grid.getGrid().addComponentColumn(milite -> {
            List<Funzione> funzioniUtenteAbilitate = ((Milite) milite).getFunzioni();
            if (funzioniUtenteAbilitate != null && funzioniUtenteAbilitate.size() > 0 && contiene(funzioniUtenteAbilitate, funzione)) {
                Label labelVerde = new Label(VaadinIcons.CHECK.getHtml(), ContentMode.HTML);
                labelVerde.addStyleName("verde");
                return labelVerde;
            } else {
                Label labelRosso = new Label(VaadinIcons.CLOSE.getHtml(), ContentMode.HTML);
                labelRosso.addStyleName("rosso");
                return labelRosso;
            }// end of if/else cycle
        });//end of lambda expressions

        colonna.setId(funzione.getCode());
        colonna.setCaption(funzione.getCode());
        colonna.setWidth(WIDTH_FUNZIONI);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + WIDTH_FUNZIONI, Unit.PIXELS);
//        fixColumn(colonna, funzioneCroce.getCode(), funzioneCroce.getCode(), 110);
    }// end of method


    /**
     * Controlla l'esistenza della funzione tra quelle abilitate per l'utente
     */
    private boolean contiene(List<Funzione> funzioniUtenteAbilitate, Funzione funzione) {
        boolean contiene = false;

        for (Funzione funz : funzioniUtenteAbilitate) {
            if (funz.getCode().equals(funzione.getCode())) {
                contiene = true;
                break;
            }// end of if cycle
        }// end of for cycle

        return contiene;
    }// end of method

}// end of class
