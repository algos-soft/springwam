package it.algos.springwam.tabellone;

import apple.laf.JRSUIConstants;
import com.vaadin.data.ValueProvider;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.component.AHorizontalLayout;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.label.ALabel;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoPresenter;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 11-feb-2018
 * Time: 20:09
 */
@Slf4j
@SpringComponent
@Scope("session")
public class TurnoCell implements ValueProvider {

    /**
     * Publisher degli eventi a livello Application
     */
    private ApplicationEventPublisher publisher;


    protected TurnoService turnoService;

    protected TabellonePresenter gestore;

    protected TurnoPresenter turnoPresenter;


    private final static String TURNO_VUOTO = "Turno non<br>(ancora)<br>previsto";
    private LocalDate giorno;


    TurnoCell(ApplicationEventPublisher publisher, TurnoService turnoService, TabellonePresenter gestore, TurnoPresenter turnoPresenter, LocalDate giorno) {
        this.publisher = publisher;
        this.turnoService = turnoService;
        this.gestore = gestore;
        this.turnoPresenter = turnoPresenter;
        this.giorno = giorno;
    }// end of constructor


    @Override
    public Object apply(Object obj) {
        Riga riga;
        Turno turno;
        Servizio servizio = null;
//        List<Turno> turni = ((Riga) riga).getTurni();
//        AHorizontalLayout turnoLayout = new AHorizontalLayout();
        VerticalLayout turnoLayout = new VerticalLayout();
        turnoLayout.setMargin(false);
        turnoLayout.setSpacing(false);

        if (obj instanceof Riga) {
            riga = (Riga) obj;
            servizio = riga.getServizio();
        } else {
            return null;
        }// end of if/else cycle

        turnoLayout.setSpacing(false);
        turno = getTurnoFromGiorno(riga, giorno);
        if (turno != null) {
//            turnoLayout.addComponent(new LabelRosso("Esiste"));

            if (turno.getIscrizioni() != null) {
                for (Iscrizione iscrizione : turno.getIscrizioni()) {
//                    turnoLayout.addComponent(new ALabel(iscrizione.getFunzione().getCode() + " - " + iscrizione.getMilite().getNickname()));
                    turnoLayout.addComponent(new ALabel(iscrizione.getMilite().getNickname()));
                }// end of for cycle

            }// end of if cycle

        } else {
            turnoLayout.addComponent(new ALabel(TURNO_VUOTO));
        }// end of if/else cycle

        turnoLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                Notification.show("Click nella cella; value: " + turno.getServizio() + " " + turno.getGiorno());
                clickCell(turno);
            }// end of inner method
        });// end of anonymous inner class

        return turnoLayout;
    }// end of method


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


    /**
     * Fire event
     * entityBean Opzionale (entityBean) in elaborazione
     * Rimanda a TabellonePresenter
     */
    private void clickCell(Turno entityBean) {
        publisher.publishEvent(new AActionEvent(EATypeAction.editLink, gestore, turnoPresenter, entityBean));
        Notification.show("Click nella cella; value: " + entityBean.getServizio() + " " + entityBean.getGiorno());
    }// end of method

}// end of class
