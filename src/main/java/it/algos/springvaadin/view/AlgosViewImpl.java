package it.algos.springvaadin.view;

import com.vaadin.data.ValidationResult;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosList;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.presenter.AlgosPresenter;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.ui.AlgosUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by gac on 07/07/17
 * Implementazione standard dell'annotation AlgosView
 * La vista sia List che Form, 'normalmente' si compone di:
 * Top - eventuali scritte esplicative, collezione usata, records trovati, tipo di modifica, ecc
 * Body - Grid o fields del Form. Scorrevole
 * Footer - barra dei bottoni
 */
public abstract class AlgosViewImpl extends VerticalLayout implements AlgosView {

    //--la lista specifica viene iniettata dal costruttore della sottoclasse concreta
    private AlgosList list;

    //--il form specifico viene iniettato dal costruttore della sottoclasse concreta
    private AlgosForm form;


    protected AlgosPresenterImpl presenter;

    @Autowired
    protected MenuLayout menuLayout;

    @Autowired
    public PreferenzaService pref;

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    @Autowired
    public AlgosViewImpl(AlgosList list, AlgosForm form) {
        this.list = list;
        this.form = form;
    }// end of Spring constructor


    /**
     * Metodo @PostConstruct invocato (da Spring) subito DOPO il costruttore (si può usare qualsiasi firma)
     */
    @PostConstruct
    private void inizia() {
    }// end of method

    /**
     * Metodo inserito per compatibilità con l'annotation View, ma non utilizzato
     * Il flusso dello SpringNavigator passa dal corrispondente metodo della classe AlgosNavView
     * Metodo invocato (da SpringBoot) ogni volta che si richiama la view dallo SpringNavigator
     * Passa il controllo alla classe xxxPresenter che gestisce la business logic
     */
    @Override
    @Deprecated
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }// end of method


    /**
     * Costruisce una Grid
     *
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    public void setList(Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
//        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
//            this.addStyleName("greenBg");
//        }// end of if cycle
//        removeAllComponents();
//        this.setMargin(false);
//        this.setWidth("100%");
//        this.setHeight("100%");

        fixGUI();
        fixMenu();
        list.restart(presenter, entityClazz, columns, items);
        addComponent(list.getComponent());
    }// end of method


    /**
     * Opzione per personalizzare il menu
     * Sovrascritto
     */
    protected void fixMenu() {
        AlgosUI algosUI = getUI();
        menuLayout.start();
        if (algosUI != null) {
            algosUI.menuPlaceholder.removeAllComponents();
            algosUI.menuPlaceholder.addComponent(menuLayout);
        }// end of if cycle
    }// end of method


    /**
     * Creazione di un form
     *
     * @param entityBean            istanza da elaborare
     * @param reflectedFields       campi del form da visualizzare
     *                              previsti nel modello dati della Entity
     *                              più eventuali aggiunte della sottoclasse
     */
    @Override
    public void setForm(ApplicationListener source, AEntity entityBean, List<Field> reflectedFields) {
        removeAllComponents();
        form.restart(source, reflectedFields, entityBean);
        addComponent(form.getComponent());
        enableButtonForm(AButtonType.revert, false);
        enableButtonForm(AButtonType.registra, false);
    }// end of method


    /**
     * Creazione di un form di un altro modulo/collezione
     * Solo finestra popup
     *
     * @param entityBean         istanza da elaborare
     * @param sourceField        di un altro modulo che ha richiesto, tramite bottone, la visualizzazione del form
     * @param reflectedFields    campi del form da visualizzare
     *                           previsti nel modello dati della Entity
     *                           più eventuali aggiunte della sottoclasse
     * @param usaBottoneRegistra utilizzo del ButtonRegistra, che registra subito
     *                           oppure ButtonAccetta, che demanda la registrazione alla scheda chiamante
     */
    @Override
    public void setFormLink(ApplicationListener source, ApplicationListener target, AEntity entityBean, AField sourceField, List<Field> reflectedFields, AButtonType type) {
        fixGUI();
        form.restartLink(source, target, sourceField, entityBean, reflectedFields, type);
        addComponent(form.getComponent());
        enableButtonForm(AButtonType.revert, false);
        enableButtonForm(AButtonType.registra, false);
    }// end of method


    /**
     * Regola l'aspetto grafico di questo contenitore
     *
     */
    public void fixGUI() {
        if (pref.isTrue(Cost.KEY_USE_DEBUG, false)) {
            this.addStyleName("greenBg");
        }// end of if cycle
        removeAllComponents();
        this.setMargin(false);
        this.setWidth("100%");
        this.setHeight("100%");
    }// end of method


    /**
     * Righe selezionate nella Grid
     * Rimanda al metodo della Lista
     *
     * @return numero di righe selezionate
     */
    @Override
    public int numRigheSelezionate() {
        return list.numRigheSelezionate();
    }// end of method

    /**
     * Una riga selezionata nella grid
     *
     * @return true se è selezionata una ed una sola riga nella Grid
     */
    @Override
    public boolean isUnaRigaSelezionata() {
        return list.isUnaRigaSelezionata();
    }// end of method


    /**
     * Abilita o disabilita lo specifico bottone della Toolbar della List
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    @Override
    public void enableButtonList(AButtonType type, boolean status) {
        list.enableButton(type, status);
    }// end of method

    /**
     * Abilita o disabilita lo specifico bottone della Toolbar del Form
     *
     * @param type   del bottone, secondo la Enumeration AButtonType
     * @param status abilitare o disabilitare
     */
    @Override
    public void enableButtonForm(AButtonType type, boolean status) {
        form.enableButton(type, status);
    }// end of method


    /**
     * Chiude la (eventuale) finestra utilizzata nel Form
     */
    @Override
    public void closeFormWindow() {
        form.closeWindow();
    }// end of method

    /**
     * Esegue il 'rollback' nel Form
     * Revert (ripristina) button pressed in form
     * Rimane nel form SENZA registrare e ripristinando i valori iniziali della entity
     */
    @Override
    public void revertEntity() {
        form.revert();
    }// end of method

    /**
     * Checks all current validation errors
     * Se ce ne sono, rimane nel form SENZA registrare
     *
     * @return ci sono errori in almeno una delle property della entity
     */
    @Override
    public boolean entityHasError() {
        return form.entityHasError();
    }// end of method

    /**
     * Checks if the entity has no current validation errors at all
     * Se la entity è OK, può essere registrata
     *
     * @return tutte le property della entity sono valide
     */
    @Override
    public boolean entityIsOk() {
        return form.entityIsOk();
    }// end of method


    /**
     * All fields errors
     *
     * @return errors
     */
    @Override
    public List<ValidationResult> getEntityErrors() {
        return form.getEntityErrors();
    }// end of method

    /**
     * Esegue il 'commit' nel Form.
     * Trasferisce i valori dai campi alla entityBean
     * Esegue la validazione dei dati
     * Esegue la trasformazione dei dati
     *
     * @return la entity del Form
     */
    @Override
    public AEntity commit() {
        return form.commit();
    }// end of method

    /**
     * L'interfaccia grafica principlae
     */
    public AlgosUI getUI() {
        AlgosUI algosUI = null;
        UI vaadinUI = super.getUI();

        if (vaadinUI instanceof AlgosUI) {
            algosUI = (AlgosUI) vaadinUI;
        }// end of if cycle

        return algosUI;
    }// end of method


    /**
     * La entity del Form
     *
     * @return la entity del Form
     */
    @Override
    public AEntity getEntityForm() {
        return form.getEntityBean();
    }// end of method

    /**
     * Una lista di entity selezionate nella Grid, in funzione di Grid.SelectionMode()
     * Lista nulla, se nessuna riga è selezionata
     * Lista di un elemento, se è Grid.SelectionMode.SINGLE
     * Lista di più elementi, se è Grid.SelectionMode.MULTI
     *
     * @return lista di una o più righe selezionate, null se nessuna riga è selezionata
     */
    @Override
    public List<AEntity> getEntityBeans() {
        return list.getEntityBeans();
    }// end of method


    /**
     * Elemento selezionato nella Grid
     *
     * @return entityBean
     */
    public AEntity getListEntityBean() {
        return list.getEntityBean();
    }// end of method

    /**
     * Elemento corrente nel Form
     *
     * @return entityBean
     */
    @Override
    public AEntity getFormEntityBean() {
        return form.getEntityBean();
    }// end of method

    public void setPresenter(AlgosPresenterImpl presenter) {
        this.presenter = presenter;

        if (list != null) {
            list.setPresenter(this.presenter);
        }// end of if cycle
    }// end of method

    /**
     * Registra eventuali dipendenze di un field del Form
     */
    @Override
    public void saveSons() {
        form.saveSons();
    }// end of method

    @Override
    public AlgosForm getForm() {
        return form;
    }// end of method

}// end of class
