package it.algos.springvaadin.list;

import com.vaadin.ui.*;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.panel.AlgosPanel;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.ListToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 20/06/17
 * Implementazione standard dell'annotation AlgosList
 * Questa vista 'normalmente' si compone di:
 * Top - Eventuali scritte esplicative come collezione usata, records trovati, ecc
 * Body - Grid. Scorrevole
 * Bottom - Barra dei bottoni
 */
public abstract class AlgosListImpl extends VerticalLayout implements AlgosList {

    @Autowired
    protected PreferenzaService pref;

    //--il service (contenente la repository) viene iniettato dal costruttore della sottoclasse concreta
    public AlgosService service;

    //--Top - Eventuali scritte esplicative come collezione usata, records trovati, ecc
    protected VerticalLayout topLayout;

    //--valore che può essere regolato nella classe specifica
    //--usando un metodo @PostConstruct
    protected String caption;

    //--Body - Grid. Scorrevole
    @Autowired
    protected AlgosPanel bodyPanel;

    //--AlgosGrid, iniettata dal costruttore
    //--un eventuale Grid specifico verrebbe iniettato dal costruttore della sottoclasse concreta
    protected AlgosGrid grid;

    //--Bottom - Barra dei bottoni
    protected VerticalLayout bottomLayout;

    //--toolbar coi bottoni, iniettato dal costruttore
    //--un eventuale Toolbar specifica verrebbe iniettata dal costruttore della sottoclasse concreta
    protected AToolbar toolbar;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param service iniettata da Spring
     * @param grid    iniettata da Spring
     * @param toolbar iniettata da Spring
     */
    public AlgosListImpl(AlgosService service, AlgosGrid grid, @Qualifier(Cost.BAR_LIST) AToolbar toolbar) {
        this.service = service;
        this.grid = grid;
        this.toolbar = toolbar;
    }// end of Spring constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
    }// end of method


    /**
     * Creazione della grid
     * Ricrea tutto ogni volta che diventa attivo
     *
     * @param source      di riferimento per gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    public void restart(AlgosPresenterImpl source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
            this.addStyleName("blueBg");
        }// end of if cycle

        this.setMargin(false);
        this.setWidth("100%");
        this.setHeight("100%");
        this.removeAllComponents();

        topLayout = creaTop(entityClazz, items);
        this.addComponent(topLayout);

        bodyPanel = creaBody(entityClazz, columns, items);
        this.addComponent(bodyPanel);

        bottomLayout = creaBottom(source);
        this.addComponent(bottomLayout);
        this.setExpandRatio(bodyPanel, 1);
    }// end of method


    /**
     * Crea la scritta esplicativa
     * Chiamato ogni volta che la finestra diventa attiva
     */
    protected VerticalLayout creaTop(Class<? extends AEntity> entityClazz, List items) {
        VerticalLayout topLayout = new VerticalLayout();
        topLayout.setMargin(false);
        topLayout.setHeightUndefined();

        //--gestione delle scritte in rosso sopra la Grid
        this.fixCaption(entityClazz.getSimpleName(), items);
        if (LibText.isValid(caption)) {
            if (LibParams.usaAvvisiColorati()) {
                topLayout.addComponent(new LabelRosso(caption));
            } else {
                topLayout.addComponent(new Label(caption));
            }// end of if/else cycle
        }// end of if cycle

        return topLayout;
    }// end of method


    /**
     * Crea la Grid
     * Chiamato ogni volta che la finestra diventa attiva
     * Inserisce la Grid in un pannello scorrevole
     */
    protected AlgosPanel creaBody(Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
            bodyPanel.addStyleName("redBg");
        }// end of if cycle

        grid.inizia(entityClazz, columns, items);
        bodyPanel.setContent(grid);

        return bodyPanel;
    }// end of method


    /**
     * Prepara la barra dei bottoni di comando
     * Chiamato ogni volta che la finestra diventa attiva
     */
    protected VerticalLayout creaBottom(AlgosPresenterImpl source) {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(false);
        bottomLayout.setHeightUndefined();
        List<AButtonType> typeButtons = service.getListTypeButtons();
        inizializzaToolbar(source, typeButtons);
        fixToolbar();

        if (pref.isTrue(Cost.KEY_USE_DEBUG)) {
            this.addStyleName("rosso");
            grid.addStyleName("verde");
        }// fine del blocco if

        bottomLayout.addComponent((ListToolbar) toolbar);
        return bottomLayout;
    }// end of method

    /**
     * Crea la scritta esplicativa
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    protected void fixCaption(String className, List items) {
        caption = className + " - ";

        if (items != null && items.size() > 0) {
            if (items.size() == 1) {
                caption += "Elenco di 1 sola scheda ";
            } else {
                caption += "Elenco di " + items.size() + " schede ";
            }// end of if/else cycle
            if (LibSession.isCompanyValida()) {
                caption += "della company " + LibSession.getCompany().getCode();
            } else {
                caption += "di tutte le company ";
            }// end of if/else cycle
        } else {
            caption += "Al momento non c'è nessuna scheda. ";
        }// end of if/else cycle
    }// end of method


    protected void fixToolbar() {
    }// end of method

    /**
     * Prepara la toolbar
     * <p>
     * Seleziona i bottoni da mostrare nella toolbar
     * Crea i bottoni (iniettandogli il publisher)
     * Aggiunge i bottoni al contenitore grafico
     * Inietta nei bottoni il parametro obbligatorio (source)
     *
     * @param source       dell'evento generato dal bottone
     * @param typeButtons da visualizzare
     */
    protected void inizializzaToolbar(ApplicationListener source, List<AButtonType> typeButtons) {
        toolbar.inizializza(source, typeButtons);
    }// end of method


    /**
     * Righe selezionate nella Grid
     *
     * @return numero di righe selezionate
     */
    @Override
    public int numRigheSelezionate() {
        return grid.numRigheSelezionate();
    }// end of method


    /**
     * Una riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    @Override
    public boolean isUnaRigaSelezionata() {
        return grid.unaRigaSelezionata();
    }// end of method


    /**
     * Abilita o disabilita lo specifico bottone della Toolbar
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    @Override
    public void enableButton(AButtonType type, boolean status) {
        toolbar.enableButton(type, status);
    }// end of method


    /**
     * Una lista di entity selezionate nella Grid, in funzione di Grid.SelectionMode()
     * Lista nulla, se nessuna riga è selezionata
     * Lista di un elemento, se è Grid.SelectionMode.SINGLE
     * Lista di uno o più elementi, se è Grid.SelectionMode.MULTI
     *
     * @return lista di una o più righe selezionate, null se nessuna riga è selezionata
     */
    @Override
    public List<AEntity> getEntityBeans() {
        return grid.getEntityBeans();
    }// end of method


    /**
     * Elemento selezionato nella Grid
     *
     * @return entityBean
     */
    public AEntity getEntityBean() {
        return grid.getEntityBean();
    }// end of method

    /**
     * Restituisce il componente concreto
     *
     * @return il componente (window o panel)
     */
    @Override
    public Component getComponent() {
        return this;
    }// end of method


    public void setPresenter(AlgosPresenterImpl presenter) {
//        toolbar.regolaBottoni(presenter);
    }// end of method


    /**
     * Regola alcuni aspetti della colonna e la larghezza della grid
     */
    protected void fixColumn(Grid.Column colonna, String id, String caption, int width) {
        colonna.setId(id);
        colonna.setCaption(caption);
        colonna.setWidth(width);
        float lar = grid.getWidth();
        grid.setWidth(lar + width, Unit.PIXELS);
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
        List<Grid.Column> listaColonne = grid.getColumns();
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
            grid.setColumnOrder(matrice);
        }// end of if cycle
    }// end of method

}// end of class
