package it.algos.springwam.tabellone;

import com.vaadin.data.ValueProvider;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;
import it.algos.springvaadin.component.AHorizontalLayout;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.webbase.web.lib.LibColor;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 11-feb-2018
 * Time: 07:44
 */
@Slf4j
public class ServizioCell implements ValueProvider {




    public ServizioCell() {
    }

    @Override
    public Object apply(Object obj) {
        Riga riga;
        Servizio servizio;
        AHorizontalLayout servizioLayout = new AHorizontalLayout();
        servizioLayout.setSpacing(false);
        servizioLayout.addStyleName("cservizio");
//        servizioLayout.addLayoutClickListener(listener);

        if (obj instanceof Riga) {
            riga = (Riga) obj;
            servizio = riga.getServizio();
            servizioLayout.addComponent(new CompServizio(servizio));
            servizioLayout.addComponent(new CompFunzioni(servizio));

            return servizioLayout;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    private LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
        @Override
        public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
            //apre una scheda (form) in edit o new
            Notification.show("Click nella cella, SERVIZIO -> "  );
        }
    };

}// end of class
