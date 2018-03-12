package it.algos.springwam.tabellone;

import com.vaadin.data.ValueProvider;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.menu.IAMenu;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.ADateService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springvaadin.service.ASessionService;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoPresenter;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.time.LocalDate;
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

    @Autowired
    public ALogin login;

    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ADateService dateService;

    /**
     * Contenitore grafico per la barra di menu
     * Componente grafico obbligatorio
     */
    @Autowired
    @Qualifier(AppCost.TAG_TAB)
    private IAMenu tabMenuLayout;


    @Autowired
    private TurnoService turnoService;

    @Autowired
    private TurnoPresenter turnoPresenter;

    @Autowired
    private TabelloneService tabelloneService;

    @Autowired
    private AResourceService resource;

    /**
     * Publisher degli eventi a livello Application
     */
    @Autowired
    private ApplicationEventPublisher publisher;

    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;


    private final static String COLUMN_SERVIZIO_ID = "serv";
    private final static int LAR_SERVIZIO = 250;
    private final static int LAR_COLONNE = 220;

    public static final String W_COLONNE_TURNI = "8em";   // larghezza fissa delle colonne turni
    public static final String H_ISCRIZIONI = "1.6em";   // altezza fissa delle celle iscrizione

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
     * @param gestore iniettato da Spring come sottoclasse concreta specificata dal @Qualifier
     */
    public Tabellone(@Lazy @Qualifier(AppCost.TAG_TAB) TabellonePresenter gestore) {
        super(gestore, null);
        addStyleName("ctabellone");
    }// end of Spring constructor


    /**
     * Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * Un eventuale menuBar specifica può essere iniettata dalla sottoclasse concreta
     * Le sottoclassi possono aggiungere/modificare i menu che verranno ripristinati all'uscita della view
     * Componente grafico obbligatorio
     *
     * @return MenuLayout
     */
    protected IAMenu creaMenu() {
        tabMenuLayout.start();
        return tabMenuLayout;
    }// end of method

    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
//        super.caption = login.getCompany().getDescrizione() + " - Situazione dei turni previsti per i servizi dal 24 nov al 31 nov";
        super.caption = login.getCompany().getDescrizione() + " - Situazione dei turni previsti";
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
        super.creaBody(entityClazz, columns, items);
        int alt = 120;
        grid.getGrid().setRowHeight(alt);
        grid.getGrid().setSelectionMode(Grid.SelectionMode.NONE);


        grid.getGrid().setHeightMode(HeightMode.ROW);
        grid.getGrid().setHeightByRows(2 * items.size());
        grid.getGrid().setHeight(alt * (items.size() + 1), Unit.PIXELS);


//        addListener(); //@todo il layout della cella lo 'copre' e non prende il click
        columnsTurni(items);
    }// end of method


//    /**
//     * Crea i listeners per i clicks nelle singole celle
//     * Estrae la riga
//     * Estrae la colonna
//     * Elabora il turno interessato
//     */
//    private void addListener() {
//        grid.getGrid().addItemClickListener(event -> {
//            Object obj = event.getItem();
//            Riga riga;
//            String columnId;
//
//            if (obj instanceof Riga) {
//                riga = (Riga) obj;
//                columnId = event.getColumn().getId();
//
//                if (!columnId.equals(COLUMN_SERVIZIO_ID)) {
//                    clickCellTurno(riga, columnId);
//                }// end of if/else cycle
//            }// end of if/else cycle
//        });//end of lambda expressions
//    }// end of method
//

    /**
     * Crea le colonne (di tipo Component) per visualizzare i turni
     * Estrae la data iniziale dalla prima riga
     * Elabora le date successive dal numero di turni presenti nella prima riga (sono sempre gli stessi, anche se nulli)
     */
    private void columnsTurni(List items) {
        Riga riga = null;
        int colonne = 0;
        LocalDate giornoInizio = null;

        if (items == null) {
            return;
        }// end of if cycle

        if (items instanceof List && items.size() > 0) {
            Object obj = items.get(0);

            if (obj != null && obj instanceof Riga) {
                riga = ((Riga) obj);
                colonne = riga.getTurni().size();
                giornoInizio = riga.getGiornoIniziale();
            }// end of if cycle
        }// end of if cycle

        columnServizio(giornoInizio);

        if (colonne > 0) {
            for (int k = 0; k < colonne; k++) {
                columnTurno(giornoInizio, k);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare il servizio
     */
    private void columnServizio(LocalDate giornoInizio) {
//        Grid.Column colonna = grid.getGrid().addComponentColumn(riga -> {
//            return new ServizioDisplay(((Riga) riga).getServizio());
//        });//end of lambda expressions

        Grid.Column colonna = grid.getGrid().addComponentColumn(new ServizioCell());
        fixColumn(colonna, COLUMN_SERVIZIO_ID, "Servizio", LAR_SERVIZIO);
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare il turno
     */
    private void columnTurno(LocalDate giornoInizio, int delta) {
        LocalDate giorno = giornoInizio.plusDays(delta);
        int userId = giorno.getDayOfYear();
        TurnoCell cella = new TurnoCell(publisher, turnoService, turnoPresenter, (TabellonePresenter) source, tabelloneService, resource, giorno);
        Grid.Column colonna = grid.getGrid().addComponentColumn(cella);
        fixColumn(colonna, userId + "", dateService.getWeekLong(giorno), LAR_COLONNE);
    }// end of method


    /**
     * Regola alcuni aspetti della colonna e la larghezza della grid
     */
    private void fixColumn(Grid.Column colonna, String id, String caption, int width) {
        colonna.setId(id);
        colonna.setCaption(caption);
        colonna.setWidth(width);
        float lar = grid.getGrid().getWidth();
        grid.getGrid().setWidth(lar + width, Unit.PIXELS);
    }// end of method


//    /**
//     * Fire event
//     * entityBean Opzionale (entityBean) in elaborazione
//     * Rimanda a TabellonePresenter
//     */
//    private void clickCellTurno(Riga riga, String columnId) {
//        int dayOfYear;
//        LocalDate giorno;
//        Turno turno;
//
//        try { // prova ad eseguire il codice
//            dayOfYear = Integer.decode(columnId);
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//            return;
//        }// fine del blocco try-catch
//
//        giorno = dateService.getLocalDateByDay(dayOfYear);
//        turno = getTurnoFromGiorno(riga, giorno);
//
//        //apre una scheda (form) in edit o new
//        Notification.show("Click nella Grid, TURNO -> " + riga.getServizio().getCode() + " " + giorno);
//        publisher.publishEvent(new AActionEvent(EATypeAction.editLink, gestore, turnoPresenter, turno));
//    }// end of method


    /**
     * Recupera il turno dalla riga
     *
     * @param riga   con i turni del servizio selezionato
     * @param giorno del turno da recuperare
     */
    private Turno getTurnoFromGiorno(Riga riga, LocalDate giorno) {
        Turno turno = null;
        List<Turno> turni = riga.getTurni();
        int day = giorno.getDayOfYear();

        for (Turno turnoTmp : turni) {
            if (turnoTmp != null) {
                if (turnoTmp.getGiorno().getDayOfYear() == day) {
                    turno = turnoTmp;
                    break;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return turno;
    }// end of method


}// end of class

