package it.algos.springvaadin.list;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.grid.AGrid;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AAnnotationService;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springvaadin.toolbar.AListToolbar;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.IAToolbar;
import it.algos.springvaadin.view.AView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
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
     * Contenitore grafico (Grid) per visualizzare i dati
     * Un eventuale Grid specifico può essere iniettato dalla sottoclasse concreta
     */
    @Autowired
    protected IAGrid grid;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AReflectionService reflection;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AAnnotationService annotation;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AColumnService column;


    @Autowired
    protected ALogin login;

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
     * @param presenter iniettato da Spring
     * @param toolbar   iniettato da Spring
     */
    public AList(IAPresenter presenter, @Qualifier(ACost.BAR_LIST) IAToolbar toolbar) {
        super(presenter, toolbar);
    }// end of Spring constructor


    /**
     * Metodo invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Aggiunge il riferimento al menuLayout nella view 'entrante' (this)
     * Passa il controllo al Presenter
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //--Regolazioni comuni a tutte le views
        super.enter(event);

        //--Passa il controllo al Presenter
        //--Il punto di ingresso invocato da SpringNavigator è unico e gestito dalla supeclasse AView
        //--Questa classe diversifica la chiamata al Presenter a seconda del tipo di view (List, Form, ... altro)
        //--Il Presenter prepara/elabora i dati e poi ripassa il controllo al metodo AList.start() di questa view
        if (presenter != null) {
            presenter.setList();
        }// end of if cycle
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

            //@todo RIMETTERE
//            if (LibSession.isCompanyValida()) {
//                caption += "della company " + LibSession.getCompany().getCode();
//            } else {
//                caption += "di tutte le company ";
//            }// end of if/else cycle
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
    protected void creaBody(IAPresenter source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
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
    protected VerticalLayout creaBottom(IAPresenter source, List<EAButtonType> typeButtons) {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(false);
        bottomLayout.setHeightUndefined();
        toolbar.inizializza(source, typeButtons);

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


//    /**
//     * Abilita o disabilita lo specifico bottone della Toolbar
//     *
//     * @param type   del bottone, secondo la Enumeration EAButtonType
//     * @param status abilitare o disabilitare
//     */
//    @Override
//    public void enableButton(EAButtonType type, boolean status){
//        toolbar.enableButton(type, status);
//    }// end of method


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
