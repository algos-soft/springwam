package it.algos.springvaadin.field;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import it.algos.springvaadin.label.LabelBold;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: gio, 05-ott-2017
 * Time: 10:22
 */
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_IMAGE)
public class AIconField extends AField {


//    /**
//     * Regolazioni varie DOPO aver creato l'istanza
//     * L'istanza può essere creata da Spring o con clone(), ma necessita comunque di questi due parametri
//     */
//    protected void fixCaption(String publicFieldName, ApplicationListener source, ApplicationListener target) {
//        super.fixCaption(publicFieldName, source);
//        if (button != null) {
//            button.setIcon(VaadinIcons.CHECK);
//        }// end of if cycle
//    }// end of method


    @Override
    public Component initContent() {
        return button;
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Integer getValue() {
        int val = 0;
        VaadinIcons icona = null;

        if (button != null) {
            icona = (VaadinIcons) button.getIcon();
        }// end of if cycle

        if (icona != null) {
            val = icona.getCodepoint();
        }// end of if cycle

        return val;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        VaadinIcons icona = null;
        int codePoint = 0;

        if (value != null && value instanceof Integer) {
            codePoint = (int) value;
            icona = LibResource.getVaadinIcon(codePoint);
        }// end of if cycle

        button.setIcon(icona);
    }// end of method

}// end of class
