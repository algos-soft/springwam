package it.algos.springwam.tabellone;

import apple.laf.JRSUIConstants;
import com.vaadin.data.ValueProvider;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.component.AHorizontalLayout;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.label.ALabel;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.milite.Milite;
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
@Scope("prototype")
public class TurnoCell implements ValueProvider {

    /**
     * Publisher degli eventi a livello Application
     */
    private ApplicationEventPublisher publisher;


    private TurnoService turnoService;

    private TurnoPresenter presenterSourceGestione;
    private TabellonePresenter presenterTargetBack;
    private TabelloneService tabelloneService;

    private final static String TURNO_VUOTO = "Turno non<br>(ancora)<br>previsto";
    private LocalDate giorno;

    private AResourceService resource;

    TurnoCell(ApplicationEventPublisher publisher,
              TurnoService turnoService,
              TurnoPresenter presenterSourceGestione,
              TabellonePresenter presenterTargetBack,
              TabelloneService tabelloneService,
              AResourceService resource,
              LocalDate giorno) {
        this.publisher = publisher;
        this.turnoService = turnoService;
        this.presenterSourceGestione = presenterSourceGestione;
        this.presenterTargetBack = presenterTargetBack;
        this.tabelloneService = tabelloneService;
        this.resource = resource;
        this.giorno = giorno;
    }// end of constructor


    @Override
    public Object apply(Object obj) {
        Riga riga;
        Turno turno;
        Servizio servizio = null;
        List<Funzione> funzioni;
        List<Iscrizione> iscrizioni;
        Label label;
        int pos = 0;
        Funzione funz;
        Milite milite;
        VaadinIcons icona;

//        List<Turno> turni = ((Riga) riga).getTurni();
//        AHorizontalLayout turnoLayout = new AHorizontalLayout();
        VerticalLayout turnoLayout = new VerticalLayout();
        turnoLayout.setHeight(120, Sizeable.Unit.PIXELS);
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
            servizio = turno.getServizio();
            funzioni = servizio.getFunzioni();
            iscrizioni = turnoService.getIscrizioni(turno);
            for (Iscrizione iscr : iscrizioni) {
                funz = funzioni.get(pos);
                icona = resource.getVaadinIcon(funz.getIcona());
                if (iscr != null) {
                    milite = iscr.getMilite();
                    if (milite != null) {
                        label = new Label(icona.getHtml() + " " + "<strong>" + milite.getNickname() + "</strong>", ContentMode.HTML);
                        label.addStyleName("verde");
                    } else {
                        if (funz.isObbligatoria()) {
                            label = new Label(icona.getHtml() + " " + "<strong>" + funz.getSigla() + "</strong>", ContentMode.HTML);
                            label.addStyleName("rosso");
                        } else {
                            label = new Label(icona.getHtml() + " " + funz.getSigla(), ContentMode.HTML);
                            label.addStyleName("blue");
                        }// end of if/else cycle
                    }// end of if/else cycle
                } else {
                    if (funz.isObbligatoria()) {
                        label = new Label(icona.getHtml() + " " + "<strong>" + funz.getSigla() + "</strong>", ContentMode.HTML);
                        label.addStyleName("rosso");
                    } else {
                        label = new Label(icona.getHtml() + " " + funz.getSigla(), ContentMode.HTML);
                        label.addStyleName("blue");
                    }// end of if/else cycle
                }// end of if/else cycle
                turnoLayout.addComponent(label);
                pos++;
            }// end of for cycle


//            Label labelBlu = new Label("<strong>" + turno.getTitoloExtra() + "</strong>", ContentMode.HTML);
//            labelBlu.addStyleName("blue");
//            turnoLayout.addComponent(labelBlu);
//            Label labelBlu2 = new Label("<strong>" + turno.getLocalitaExtra() + "</strong>", ContentMode.HTML);
//            labelBlu2.addStyleName("blue");
//            turnoLayout.addComponent(labelBlu2);

        } else {
            turnoLayout.addComponent(new ALabel(TURNO_VUOTO));
        }// end of if/else cycle

        turnoLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
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
        publisher.publishEvent(new AActionEvent(EATypeAction.editLink, presenterSourceGestione, presenterTargetBack, entityBean));
    }// end of method

}// end of class
