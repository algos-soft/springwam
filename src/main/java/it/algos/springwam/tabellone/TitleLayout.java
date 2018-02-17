package it.algos.springwam.tabellone;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.webbase.web.lib.LibColor;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 11-feb-2018
 * Time: 08:40
 * CSS Layout che contiene la label con il titolo
 * Serve per regolare dinamicamente il colore tramite CSS
 * in base al colore definito nel Servizio
 */
public class TitleLayout extends CssLayout {

    private Servizio servizio;


    public TitleLayout(Servizio servizio) {
        this.servizio = servizio;
        this.setWidth("100%");
    }// end of constructor


    @Override
    protected String getCss(Component comp) {

        if (comp instanceof Label) {
            Color bg = new Color(servizio.getColore());
            Color fg = LibColor.getForeground(bg);
            String bgHex = Integer.toHexString(bg.getRGB()).substring(2);
            String fgHex = Integer.toHexString(fg.getRGB()).substring(2);
            String css = "background: #" + bgHex + "; color: #" + fgHex + ";";

            return css;
        }// end of if cycle

        return null;
    }// end of method

}// end of class
