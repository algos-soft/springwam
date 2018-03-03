package it.algos.springvaadin.list;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.grid.AGrid;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.menu.AMenu;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AAnnotationService;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.toolbar.AListToolbar;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 23:07
 * Implementazione standard dell'interfaccia IAList
 * Questa vista 'normalmente' si compone di:
 * Top - MenuBar di navigazione tra le views gestita da SpringNavigator
 * Caption - Eventuali scritte esplicative come collezione usata, records trovati, ecc
 * Body - Grid. Scorrevole
 * Footer - Barra dei bottoni di comando per lanciare eventi
 * Annotated with Lombok @Slf4j (facoltativo)
 */
@Slf4j
public abstract class AList extends AView implements IAList {


    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected AColumnService column;


    /**
     * Contenitore grafico (Grid) per visualizzare i dati
     * Un eventuale Grid specifico può essere iniettato dalla sottoclasse concreta
     */
    @Autowired
    protected IAGrid grid;



//    @Autowired
//    private APanel panel;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param source  gestore principale per la 'business logic' del modulo, iniettato da Spring
     * @param toolbar iniettato da Spring
     */
    public AList(IAPresenter source, @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(source, toolbar);
    }// end of Spring constructor


    /**
     * Metodo di ingresso nella view (nella sottoclasse concreta)
     * Viene invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Passa il controllo al gestore che gestisce questa view (individuato nel costruttore della sottoclasse concreta)
     * Questa classe diversifica la chiamata al presenter a seconda del tipo di view (List, Form, ... altro)
     * Il gestore prepara/elabora i dati e poi ripassa il controllo al metodo AList.start() di questa view
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //--Regolazioni comuni a tutte le views (list e form)
        super.enter(event);

        //--Passa il controllo al gestore che gestisce questa view
        source.setList();
    }// end of method



    /**
     * Creazione di una view (AList) contenente una Grid
     * Metodo invocato dal gestore (presenter( (dopo che ha elaborato i dati da visualizzare)
     * Ricrea tutto ogni volta che la view diventa attiva
     * 1) Menu: Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * 2) Top: Contenitore grafico per la caption
     * 3) Body: Corpo centrale della view. Utilizzando una Grid dentro un Panel, si ottine l'effetto scorrevole
     * 4) Bottom - Barra dei bottoni inferiore
     *
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     * @param typeButtons lista di (tipi di) bottoni visibili nella toolbar della view AList
     */
    @Override
    public void start(Class<? extends AEntity> entityClazz, List<Field> columns, List items, List<EATypeButton> typeButtons) {
        this.removeAllComponents();

        //--componente grafico obbligatorio
        menuLayout = creaMenu();
        this.addComponent(menuLayout.getMenu());

        //--componente grafico facoltativo
        topLayout = creaTop(entityClazz, items);
        if (topLayout != null) {
            this.addComponent(topLayout);
        }// end of if cycle

        //--componente grafico obbligatorio
        this.creaBody(entityClazz, columns, items);
        this.addComponent(bodyLayout);

        //--componente grafico facoltativo
        if (typeButtons != null) {
            bottomLayout = creaBottom(typeButtons);
            if (bottomLayout != null) {
                this.addComponent(bottomLayout);
            }// end of if cycle
        }// end of if cycle

        this.setExpandRatio(bodyLayout, 1); //@todo da rimettere
    }// end of method


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    @Override
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
        String className = entityClazz != null ? entityClazz.getSimpleName() : null;

        caption = className != null ? className + " - " : "";

        if (array.isValid(items)) {
            if (items.size() == 1) {
                caption += "Elenco di 1 sola scheda ";
            } else {
                caption += "Elenco di " + items.size() + " schede ";
            }// end of if/else cycle

            if (login != null && login.getCompany() != null) {
                caption += "di " + login.getCompany().getCode().toUpperCase();
            } else {
                caption += "di tutte le company ";
            }// end of if/else cycle
        } else {
            caption += "Al momento non c'è nessuna scheda. ";
        }// end of if/else cycle
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
        grid.inizia(source, entityClazz, columns, items, 50);
        bodyLayout.setContent((Component) grid);
//        VerticalLayout layout = new VerticalLayout();
//        layout.addComponent((Component)grid);
    }// end of method


    /**
     * Crea la barra inferiore dei bottoni di comando
     * Chiamato ogni volta che la finestra diventa attiva
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     */
    @Override
    protected VerticalLayout creaBottom(List<EATypeButton> typeButtons) {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(false);
        bottomLayout.setHeightUndefined();
        toolbar.inizializza(source, target, typeButtons);

//        fixToolbar();

        bottomLayout.addComponent((AListToolbar) toolbar);
        return bottomLayout;
    }// end of method


    /**
     * Una sola riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    @Override
    public boolean isUnaSolaRigaSelezionata() {
        return grid.isUnaSolaRigaSelezionata();
    }// end of method


    /**
     * Sposta una colonna nella posizione richiesta
     *
     * @param columnID      della colonna da spostare
     * @param columnIdPrima della colonna prima della quale devo inserire la colonna da spostare
     */
    protected void spostaColonnaPrima(String columnID, String columnIdPrima) {
        spostaColonna(columnID, columnIdPrima, false);
    }// end of method


    /**
     * Sposta una colonna nella posizione richiesta
     *
     * @param columnID     della colonna da spostare
     * @param columnIdDopo della colonna dopo la quale devo inserire la colonna da spostare
     */
    protected void spostaColonnaDopo(String columnID, String columnIdDopo) {
        spostaColonna(columnID, columnIdDopo, true);
    }// end of method


    /**
     * Sposta una colonna nella posizione richiesta
     *
     * @param columnID    della colonna da spostare
     * @param columnCheck della colonna prima/dopo la quale devo inserire la colonna da spostare
     */
    private void spostaColonna(String columnID, String columnCheck, boolean dopo) {
        String[] matrice = null;
        List<Grid.Column> listaColonne = grid.getGrid().getColumns();
        ArrayList lista = new ArrayList();
        for (Grid.Column col : listaColonne) {
            lista.add(col.getId());
        }// end of for cycle
        lista.remove(columnID);
        int pos = lista.indexOf(columnCheck);
        if (pos != -1) {
            if (dopo) {
                pos++;
            }// end of if cycle
            lista.add(pos, columnID);
            matrice = (String[]) lista.toArray(new String[lista.size()]);
            grid.getGrid().setColumnOrder(matrice);
        }// end of if cycle
    }// end of method

    /**
     * Componente concreto di questa interfaccia
     */
    @Override
    public AList getList() {
        return this;
    }// end of method


    /**
     * Componente Grid della List
     */
    @Override
    public AGrid getGrid() {
        return this.grid.getGrid();
    }// end of method

}// end of class
