package it.algos.springwam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.label.ALabel;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.webbase.web.login.Login;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 11-feb-2018
 * Time: 08:44
 * Componente con la descrizione del servizio
 * Formato in verticale da orario
 * Formato in verticale da sigla del servizio
 */
@Slf4j
public class CompServizio extends VerticalLayout {

    private Servizio servizio;
    private Label labelTitolo;
    private CssLayout layoutTitle;

//    private LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
//        @Override
//        public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
//            int a=87;
//            if (Login.getLogin().isLogged()) {
////                    tabellone.cellClicked(CellType.SERVIZIO, x, y, servizio);
//                setCreaNuova(false);    // una volta che ho creato la nuova riga, questa riga perde la possibilità di crearne altre
//                log.info("pep");
//            } else {
//                Login.getLogin().showLoginForm();
//            }
//        }
//    };


    /**
     * Costruttore completo
     *
     * @param servizio il servizio da visualizzare
     */
    public CompServizio(Servizio servizio) {
        this.servizio = servizio;
        iniziaUI();
        addTitolo();
        addDescrizione();
    }// end of constructor


    private void iniziaUI() {
        setWidth("7em");
        setSpacing(false);
        setMargin(false);
    }// end of method


    private void addTitolo() {
        labelTitolo = new ALabel();
        // layout con dentro la label, serve per poterci attaccare il clicklistener e regolare il css
        layoutTitle = new TitleLayout(servizio);

        labelTitolo.addStyleName("cservizio-titolo");
        if (servizio.isOrario()) {
            labelTitolo.setValue(servizio.getStrOrario());
        } else {
//                if (LibSession.isAdmin()) {
            if (false) {
                setCreaNuova(true);
            } else {
//                    if (CompanyPrefs.creazioneTurniExtra.getBool()) {
//                        setCreaNuova(true);
//                    } else {
                labelTitolo.setValue(servizio.getStrOrario());
//                    }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle


        layoutTitle.addComponent(labelTitolo);
        addComponent(layoutTitle);
    }// end of method


    private void addDescrizione() {
        ALabel labelNome = new ALabel();
        labelNome.setHeight("100%");
        labelNome.addStyleName("cservizio-nome");

        String descrizione = servizio.getDescrizione();
//            if (CompanyPrefs.usaLabelServizioHtml.getBool()) {
        descrizione = descrizione.replace(" ", "<br/>");
//            }// end of if cycle

        if (descrizione.equals("")) {
            descrizione = "&nbsp;";// evita label con testo vuoto, danno problemi
        }// end of if cycle

        labelNome.setValue(descrizione);
        addComponent(labelNome);
        setExpandRatio(labelNome, 1);
    }// end of method


    /**
     * Mostra o nasconde il bottone che consente di creare una nuova riga
     * (significativo solo per servizi a orario variabile)
     *
     * @param crea true per mostrare il bottone, false per nasconderlo
     */
    private void setCreaNuova(boolean crea) {
        if (crea) {
//                if (LibSession.isAdmin() || CompanyPrefs.creazioneTurniExtra.getBool()) {
//                    labelTitolo.setValue(FontAwesome.PLUS_CIRCLE.getHtml() + " crea nuovo");
//                    layoutTitle.addLayoutClickListener(listener);
//                    layoutTitle.addStyleName("cpointer");
//                } else {
            labelTitolo.setValue(servizio.getStrOrario());
//                }// end of if/else cycle
        } else {
            labelTitolo.setValue("&nbsp;");
//            layoutTitle.removeLayoutClickListener(listener);
//            layoutTitle.removeStyleName("cpointer");
        }// end of if/else cycle
    }// end of method

}// end of class
