package it.algos.springwam.tabellone;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.preferenza.PreferenzaService;
import it.algos.springvaadin.event.*;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.view.AlgosView;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.turno.TurnoPresenter;
import it.algos.springwam.entity.turno.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 08:54
 */
@SpringComponent
@Qualifier(AppCost.TAG_TAB)
public class TabellonePresenter extends AlgosPresenterImpl {


    @Autowired
    protected TurnoPresenter turnoPresenter;

    @Autowired
    protected PreferenzaService pref;

    private TabelloneView view;
    private TabelloneService service;

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     * Regola il modello-dati specifico
     */
    public TabellonePresenter(@Qualifier(AppCost.TAG_TAB) AlgosView view, @Qualifier(AppCost.TAG_TAB) AlgosService service) {
        super(view, service, null);
        this.view = (TabelloneView) view;
        this.service = (TabelloneService) service;
        super.entityClass = Riga.class;
    }// end of Spring constructor


    /**
     * Metodo invocato dalla view ogni volta che questa diventa attiva
     * oppure
     * metodo invocato da un Evento (azione) che necessita di aggiornare e ripresentare la Lista
     * tipo dopo un delete, dopo un nuovo record, dopo la edit di un record
     * <p>
     * Recupera dal service tutti i dati necessari (aggiornati)
     * Recupera dal service le colonne da mostrare nella grid
     * Recupera dal service gli items (records) della collection, da mostrare nella grid
     * Passa il controllo alla view con i dati necessari
     */
    @Override
    protected void presentaLista() {
        List items = null;
        List<Field> columns = null;

        if (service != null) {
            columns = service.getListFields();
            items = creaRighe();
        }// end of if cycle

        view.setList(entityClass, columns, items);
    }// end of method


    public List<Riga> creaRighe() {
        return creaRighe(LocalDate.now(), 7);
    }// end of method


    public List<Riga> creaRighe(LocalDate giornoInizio, int giorni) {
        return service.creaRighe(giornoInizio, giorni);
    }// end of method


    protected void turnoNewAndEdit(AEntity entityBean) {
        if (pref.isTrue(Cost.KEY_USE_FORM_ALL_SCREEN, true)) {
        } else {
            turnoPresenter.editLink(this, entityBean, null, null);
        }// end of if/else cycle
    }// end of method

    /**
     * Handle an action event
     * Vedi enum TypeAction
     *
     * @param type       Obbligatorio specifica del tipo di evento
     * @param entityBean Opzionale (entityBean) in elaborazione. Ha senso solo per alcuni eventi
     */
    @Override
    protected void onGridAction(TypeAction type, AEntity entityBean) {
        if (type == TypeAction.click) {
            turnoNewAndEdit(entityBean);
        }// end of if cycle
    }// end of method

}// end of class
