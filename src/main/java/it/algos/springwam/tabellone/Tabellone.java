package it.algos.springwam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.menu.IAMenu;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * Contenitore grafico per la barra di menu
     * Componente grafico obbligatorio
     */
    @Autowired
    @Qualifier(AppCost.TAG_TAB)
    private IAMenu tabMenuLayout;

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
    }// end of method



//    /**
//     * Crea le colonne (di tipo Component) per visualizzare i turni
//     * Estrae la data iniziale dalla prima riga
//     * Elabora le date successive dal numero di turni presenti nella prima riga (sono sempre gli stessi, anche se nulli)
//     */
//    private void columnsTurni(List items) {
//        Riga riga = null;
//        int colonne = 0;
//        LocalDate giornoInizio = null;
//
//        if (items == null) {
//            return;
//        }// end of if cycle
//
//        if (items instanceof List && items.size() > 0) {
//            Object obj = items.get(0);
//
//            if (obj != null && obj instanceof Riga) {
//                riga = ((Riga) obj);
//                colonne = riga.getTurni().size();
//                giornoInizio = riga.getGiorno();
//            }// end of if cycle
//        }// end of if cycle
//
//        if (colonne > 0) {
//            for (int k = 0; k < colonne; k++) {
//                columnTurno(giornoInizio, k);
//            }// end of for cycle
//        }// end of if cycle
//    }// end of method


//    /**
//     * Crea la colonna (di tipo Component) per visualizzare il turno
//     */
//    private void columnTurno(LocalDate giornoInizio, int delta) {
//        LocalDate giorno = LibDate.add(giornoInizio, delta);
//
//        Grid.Column colonna = grid.addComponentColumn(riga -> {
//            Turno turno;
//            Servizio servizio = ((Riga) riga).getServizio();
//            List<Turno> turni = ((Riga) riga).getTurni();
//
//            if (turni != null && turni.size() > 0) {
//                try { // prova ad eseguire il codice
//                    turno = turni.get(delta);
//                    VerticalLayout layout = new VerticalLayout();
//                    layout.setMargin(false);
//
//                    if (turno != null) {
//                        layout.addComponent(new LabelRosso("Esiste"));
//                    } else {
//                        layout.addComponent(new Label(TURNO_VUOTO, ContentMode.HTML));
//                    }// end of if/else cycle
//
//                    layout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//                        @Override
//                        public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
//                            clickCell(turno != null ? turno : turnoService.newEntity(giorno, servizio));
//                        }// end of inner method
//                    });// end of anonymous inner class
//
//                    return layout;
//                } catch (Exception unErrore) { // intercetta l'errore
//                    log.error(unErrore.toString());
//                }// fine del blocco try-catch
//            } else {
//            }// end of if/else cycle
//
//            return new Label("Pippoz");
//        });//end of lambda expressions
//
//        fixColumn(colonna, delta + "", LibDate.getWeekLong(giorno), LAR_COLONNE);
//    }// end of method


}// end of class

