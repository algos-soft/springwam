package it.algos.springwam.entity.turno;

import com.vaadin.data.HasValue;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.enumeration.EATypeField;
import it.algos.springvaadin.event.AFieldEvent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.service.AColumnService;
import it.algos.springvaadin.service.AHtmlService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.iscrizione.IscrizioneService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.webbase.web.field.AIField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mar, 13-feb-2018
 * Time: 10:11
 */
@Slf4j
@SpringComponent
@Scope("prototype")
public class TurnoFieldIscrizioni extends AField {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AHtmlService html;


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    protected AColumnService column;


    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private MiliteService militeService;

    private Grid grid;

    private List<Iscrizione> iscrizioni;

    private List items;

    /**
     * Crea (o ricrea dopo una clonazione) il componente base
     */
    @Override
    public void creaContent() {
        int width = 170;
        grid = new Grid(Iscrizione.class);
        grid.setHeight("14em");
        grid.setStyleName("");
        grid.removeAllColumns();
//        grid.setRowHeight(28);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaFunzione = grid.addComponentColumn(iscrizione -> {
            Funzione funz = ((Iscrizione) iscrizione).getFunzione();
            Milite milite = ((Iscrizione) iscrizione).getMilite();
            String labelTxt = funz.getSigla();
            Label label = new Label("", ContentMode.HTML);
            if (milite != null) {
                labelTxt = html.setVerdeBold(labelTxt);
            } else {
                if (funz.isObbligatoria()) {
                    labelTxt = html.setRossoBold(labelTxt);
                } else {
                    labelTxt = html.setBluBold(labelTxt);
                }// end of if/else cycle
            }// end of if/else cycle
            label.setValue(labelTxt);

            return label;
        });//end of lambda expressions
        colonnaFunzione.setCaption("Funzione");
        colonnaFunzione.setId("funzione");
        colonnaFunzione.setWidth(120);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaMilite = grid.addComponentColumn(iscrizione -> {
            Funzione funz = ((Iscrizione) iscrizione).getFunzione();
            Milite milite = ((Iscrizione) iscrizione).getMilite();
//            List<Milite> listaMilitiDellaCroceAbilitatiPerLaFunzione = militeService.findByFunzione(funz);
            List<Milite> listaMilitiDellaCroceAbilitatiPerLaFunzione = militeService.findAll();//@todo SBAGLIATO
            ComboBox combo = new ComboBox();
            combo.setWidth("7em");
            combo.setItems(listaMilitiDellaCroceAbilitatiPerLaFunzione);

//            List<String> items = iscrizioneService.findAllCode();
            combo.setItems(items);
            combo.setValue(milite);

            combo.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
                @Override
                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
//                    codeChanged(idKey, valueChangeEvent.getOldValue(), valueChangeEvent.getValue());
                }// end of inner method
            });// end of anonymous inner class

            return combo;
        });//end of lambda expressions
        colonnaMilite.setCaption("Milite");
        colonnaMilite.setId("milite");
        colonnaMilite.setWidth(width);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaDescrizione = grid.addComponentColumn(funzione -> {
//            String descrizione = ((Funzione) funzione).getDescrizione();
//            Label label = new Label("", ContentMode.HTML);
//            String labelTxt = "";
//            if (((Funzione) funzione).isObbligatoria()) {
//                labelTxt = html.setRossoBold(descrizione);
//            } else {
//                labelTxt = html.setBluBold(descrizione);
//            }// end of if/else cycle
//            label.setValue(labelTxt);
//
//            return label;
//        });//end of lambda expressions
//        colonnaDescrizione.setCaption("Descrizione");
//        colonnaDescrizione.setId("descrizione2");
//        colonnaDescrizione.setWidth(350);


        //--aggiunge una colonna calcolata
        Grid.Column colonnaDurata = grid.addColumn(iscrizione -> {
            return ((Iscrizione) iscrizione).getDurata();
        });//end of lambda expressions
        colonnaDurata.setCaption("Ore");
        colonnaDurata.setId("durata");
        colonnaDurata.setWidth(80);


//        //--aggiunge una colonna calcolata
//        Grid.Column colonnaCheck = grid.addComponentColumn(funzione -> {
//            String idKey = ((Funzione) funzione).getId();
//            CheckBox checkBox = null;
//            if (text.isEmpty(((Funzione) funzione).getId())) {
//            } else {
//                boolean obbligatoria = ((Funzione) funzione).isObbligatoria();
//                checkBox = new CheckBox("", obbligatoria);
//            }// end of if/else cycle
//
//            if (checkBox != null) {
//                checkBox.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {
//                    @Override
//                    public void valueChange(HasValue.ValueChangeEvent<Boolean> valueChangeEvent) {
//                        checkChanged(idKey, valueChangeEvent.getValue());
//                    }// end of inner method
//                });// end of anonymous inner class
//            }// end of if cycle
//
//            return checkBox;
//        });//end of lambda expressions
//        colonnaCheck.setCaption("Obb.");
//        colonnaCheck.setId("obb");
//        colonnaCheck.setWidth(column.WIDTH_CHECK_BOX);

        ArrayList lista = new ArrayList();
        lista.add("funzione");
        lista.add("milite");
        lista.add("durata");
        grid.setColumns((String[]) lista.toArray(new String[lista.size()]));
        float lar = grid.getWidth();
//        grid.setWidth(lar + width + column.WIDTH_CHECK_BOX + 565, Sizeable.Unit.PIXELS);


        grid.setStyleGenerator(new StyleGenerator() {
            @Override
            public String apply(Object o) {
                return "error_row";
            }
        });
    }// end of method


    @Override
    public void setWidth(String width) {
        width = "28em";
        if (grid != null) {
            grid.setWidth(width);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        Funzione lastFunz = null;

        if (items != null && items.size() > 0) {
            grid.setItems(items);
//            this.setHeightMode(HeightMode.ROW);
//            this.setHeightByRows(items.size());
        }// end of if cycle


//        if (entityBean != null) {
//            funzioni = ((Servizio) entityBean).getFunzioni();
//            if (grid != null && funzioni != null) {
//                lastFunz = funzioni.get(funzioni.size() - 1);
//                if (text.isValid(lastFunz.getId())) {
//                    funzioni.add(funzioneService.newEntity());
//                }// end of if cycle
//                grid.setHeightByRows(funzioni.size());
//                grid.setItems(funzioni);
//            } else {
//                funzioni = new ArrayList<>();
//                funzioni.add(funzioneService.newEntity());
//                grid.setItems(funzioni);
//                grid.setHeightByRows(1);
//            }// end of if/else cycle
//        } else {
//            if (grid != null) {
//                grid.setItems("");
//                grid.setHeightByRows(1);
//            }// end of if cycle
//        }// end of if/else cycle

        return grid;
    }// end of method

    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public Object getValue() {
        return iscrizioni;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
    }// end of method

//    /**
//     * Cambiato il code di selezione della funzione
//     */
//    private void codeChanged(String idKey, String oldCode, String newCode) {
//
//        //--combo dell'ultima riga (vuota) della Grid
//        if (text.isEmpty(oldCode) && text.isValid(newCode)) {
//            aggiungeRiga(newCode);
//            return;
//        }// end of if cycle
//
//        //--modifica una riga
//        if (text.isValid(oldCode) && text.isValid(newCode)) {
//            modificaRiga(idKey, newCode);
//            return;
//        }// end of if cycle
//
//    }// end of method

//    /**
//     * Cambiato il check di obbligatorietà della funzione
//     */
//    private void checkChanged(String idKey, boolean newCode) {
//        Funzione funz = getFunz(idKey);
//        funz.setObbligatoria(newCode);
//        //@todo It's a bug. Grid doesn't update itself after changes were done in underlying container nor has any reasonable method to refresh. There are several hacks around this issue i.e.
//        grid.setItems(iscrizioni);
//
//        publish();
//    }// end of method


//    /**
//     * Cancella l'ultima riga
//     * Aggiunge la funzione corrispondente al code ricevuto
//     * Aggiunge un'ultima riga vuota
//     */
//    private void aggiungeRiga(String newCode) {
//        Funzione funz = funzioneService.findByKeyUnica(newCode);
//
//        if (funz != null) {
//            funzioni.remove(funzioni.size() - 1);
//            funzioni.add(funz);
//            funzioni.add(funzioneService.newEntity());
//            grid.setItems(funzioni);
//            grid.setHeightByRows(funzioni.size());
//
//            publish();
//        }// end of if cycle
//
//    }// end of method


    /**
     * Modifica la riga
     */
    private void modificaRiga(String idKey, String newCode) {
        Funzione oldFunz = null;
        Funzione newFunz = null;
        int oldPos = 0;

//        oldFunz = getFunz(idKey);
//        newFunz = funzioneService.findByKeyUnica(newCode);
//
//        if (oldFunz != null && newFunz != null) {
//            oldPos = funzioni.indexOf(oldFunz);
//            funzioni.remove(oldFunz);
//            funzioni.add(oldPos, newFunz);
//            grid.setItems(funzioni);
//            grid.setHeightByRows(funzioni.size());
//
//            publish();
//        }// end of if cycle

    }// end of method

//    /**
//     * Recupera la funzione selezionata dalla idKey
//     */
//    private Funzione getFunz(String idKey) {
//        for (Funzione funz : funzioni) {
//            if (funz.getId().equals(idKey)) {
//                return funz;
//            }// end of if cycle
//        }// end of for cycle
//
//        return null;
//    }// end of method


    /**
     * Fire event
     * source     Obbligatorio questo field
     * target     Obbligatorio (window, dialog, presenter) a cui indirizzare l'evento
     * entityBean Opzionale (entityBean) in elaborazione
     */
    public void publish() {
        if (source != null) {
            publisher.publishEvent(new AFieldEvent(EATypeField.fieldModificato, source, target, entityBean, this));
        }// end of if cycle
    }// end of method


    /**
     * Aggiunge il listener al field
     */
    protected void addListener() {
//        if (radio != null) {
//            radio.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
//                @Override
//                public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
//                    publish();
//                }// end of inner method
//            });// end of anonymous inner class
//        }// end of if cycle
    }// end of method


    public void setItems(List items) {
        this.items = items;
    }// end of method

}// end of class

