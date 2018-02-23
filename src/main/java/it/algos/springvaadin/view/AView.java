package it.algos.springvaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.button.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.menu.IAMenu;
import it.algos.springvaadin.panel.APanel;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AArrayService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springvaadin.toolbar.IAToolbar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 21:23
 * Implementazione standard dell'interfaccia IAView
 * Questa vista 'normalmente' può essere di due tipi:
 * AList - Grid per presentare la lista sintetica di tutte le entoties
 * AForm - Per presentare una serie di fields coi valori della singola entity
 */
@Slf4j
public abstract class AView extends VerticalLayout implements IAView {


    @Autowired
    protected ALogin login;

    /**
     * Service (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected ATextService text;


    /**
     * Service (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected AHtmlService htlm;


    /**
     * Service (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    @Autowired
    protected AArrayService array;


    /**
     * Gestore principale per la 'business logic' del modulo, iniettato da Spring nel costruttore
     */
    public IAPresenter gestore;


    /**
     * Chiamante di questa view (di solito il gestore)
     */
    public IAPresenter source;


    /**
     * Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * Un eventuale menuBar specifica può essere iniettata dalla sottoclasse concreta
     * Le sottoclassi possono aggiungere/modificare i menu che verranno ripristinati all'uscita della view
     * Componente grafico obbligatorio
     */
    @Autowired
    @Qualifier(ACost.TAG_MENU_LAYOUT)
    protected IAMenu menuLayout;


    /**
     * Contenitore grafico per la caption
     * Eventuali scritte esplicative come collezione usata, records trovati, ecc
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     */
    protected VerticalLayout topLayout;


    @Autowired
    public LabelRosso labelRosso;

    /**
     * Caption sovrastante il body della view
     * Valore che può essere regolato nella classe specifica
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     */
    protected String caption;


    /**
     * Corpo centrale della view
     * Espanso. Usando un Panel si inserisce automaticamente uno scorrevole quando serve.
     * Nel Panel viene inserita una Grid oppure una lista di Fields... a seconda delle sottoclassi di tipo view
     * Componente grafico obbligatorio
     */
    @Autowired
    protected APanel bodyLayout;


    /**
     * Barra dei bottoni inferiore
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     */
    protected VerticalLayout bottomLayout;


    protected IAToolbar toolbar;


    /**
     * Costruttore @Autowired (nella sottoclasse concreta)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     *
     * @param gestore iniettato da Spring
     */
    public AView(IAPresenter gestore, IAToolbar toolbar) {
        super();
        this.gestore = gestore;
        this.toolbar = toolbar;
    }// end of Spring constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
        //@todo RIMETTERE
//        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
//        }// end of if cycle

        if (ACost.DEBUG) {// @TODO costante provvisoria da sostituire con preferenzeService
            this.addStyleName("blueBg");
        }// end of if cycle

        this.setMargin(false);
        this.setWidth("100%");
        this.setHeight("100%");
    }// end of method


    /**
     * Metodo di ingresso nella view (nella sottoclasse concreta)
     * Viene invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Passa il controllo al presenter che gestisce questa view (individuato nel costruttore della sottoclasse concreta)
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View oldView = event.getOldView();

        if (oldView instanceof IAView) {
            ((IAView) oldView).removeComponents();
        }// end of if cycle
    }// end of method


    /**
     * Contenitore grafico per la barra di menu principale e per il menu/bottone del Login
     * Un eventuale menuBar specifica può essere iniettata dalla sottoclasse concreta
     * Le sottoclassi possono aggiungere/modificare i menu che verranno ripristinati all'uscita della view
     * Componente grafico obbligatorio
     *
     * @return MenuLayout
     */
    protected IAMenu creaMenu() {
        menuLayout.start();
        return menuLayout;
    }// end of method


    /**
     * Caption sovrastante il body della view
     * Valore che può essere regolato nella classe specifica
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     *
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param items       da visualizzare nella Grid
     */
    protected VerticalLayout creaTop(Class<? extends AEntity> entityClazz, List items) {
        VerticalLayout topLayout = null;

        //--gestione delle scritte in rosso sopra il body
        this.fixCaption(entityClazz, items);
        if (text.isValid(caption)) {
            labelRosso.setValue(caption);
            topLayout = new VerticalLayout();
            topLayout.setMargin(false);
            topLayout.setHeightUndefined();
            topLayout.addComponent(labelRosso);

            //@todo RIMETTERE
//            if (LibParams.usaAvvisiColorati()) {
//                topLayout.addComponent(new LabelRosso(caption));
//            } else {
//                topLayout.addComponent(new Label(caption));
//            }// end of if/else cycle
        }// end of if cycle

        return topLayout;
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     *
     * @param gestore     presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    protected void creaBody(IAPresenter gestore, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
    }// end of method


    /**
     * Crea il corpo centrale della view
     * Componente grafico obbligatorio
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     *
     * @param source              presenter di riferimento per i componenti da cui vengono generati gli eventi
     * @param reflectedJavaFields previsti nel modello dati della Entity più eventuali aggiunte della sottoclasse
     */
    protected void creaBody(IAPresenter source, List<Field> reflectedJavaFields) {
    }// end of method


    /**
     * Crea la barra inferiore dei bottoni di comando
     * Chiamato ogni volta che la finestra diventa attiva
     * Componente grafico facoltativo. Normalmente presente (AList e AForm), ma non obbligatorio.
     * Sovrascritto nella sottoclasse della view specifica (AList, AForm, ...)
     */
    protected VerticalLayout creaBottom(IAPresenter gestore, List<EATypeButton> typeButtons) {
        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setMargin(false);
        bottomLayout.setHeightUndefined();
//        inizializzaToolbar(source, typeButtons);
//        fixToolbar();

//        if (pref.isTrue(Cost.KEY_USE_DEBUG)) {
//            this.addStyleName("rosso");
//            grid.addStyleName("verde");
//        }// fine del blocco if

//        bottomLayout.addComponent((ListToolbar) toolbar);
        return bottomLayout;
    }// end of method


    /**
     * Crea la scritta esplicativa
     * Può essere sovrascritto per un'intestazione specifica (caption) della grid
     */
    protected void fixCaption(Class<? extends AEntity> entityClazz, List items) {
    }// end of method


    /**
     * Elimina il menuLayout dalla vista 'uscente'
     */
    @Override
    public void removeComponents() {
        if (menuLayout != null) {
            this.removeComponent(menuLayout.getMenu());
        }// end of if cycle
    }// end of method


    /**
     * Recupera il bottone del tipo specifico
     * Ce ne può essere uno solo nella toolbar
     *
     * @param type del bottone, secondo la Enumeration AButtonType
     */
    public AButton getButton(EATypeButton type) {
        return toolbar.getButton(type);
    }// end of method

    /**
     * Recupera la classe del modello dati (Entity)
     *
     * @return la classe di Entity
     */
    @Override
    public AEntity getEntityClass() {
        return null;
    }// end of method

    /**
     * Componente concreto di questa interfaccia
     */
    @Override
    public AView getView() {
        return null;
    }// end of method

}// end of class
