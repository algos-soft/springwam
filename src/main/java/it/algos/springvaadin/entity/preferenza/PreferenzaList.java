package it.algos.springvaadin.entity.preferenza;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.label.LabelVerde;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibColumn;
import it.algos.springvaadin.lib.LibDate;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 16-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PRE)
@Slf4j
public class PreferenzaList extends AlgosListImpl {


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public PreferenzaList(@Qualifier(Cost.TAG_PRE) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor

    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.fixCaption(className, items);
            caption += "</br>Lista visibile solo all'buttonAdmin che vede SOLO le schede della sua company";
            caption += "</br>Usa la company (se AlgosApp.USE_MULTI_COMPANY=true) che è facoltativa";
            caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Preferenze di funzionamento del programma";
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione della grid
     * Ricrea tutto ogni volta che diventa attivo
     *
     * @param source      di riferimento per gli eventi
     * @param entityClazz di riferimento, sottoclasse concreta di AEntity
     * @param columns     visibili ed ordinate della Grid
     * @param items       da visualizzare nella Grid
     */
    @Override
    public void restart(AlgosPresenterImpl source, Class<? extends AEntity> entityClazz, List<Field> columns, List items) {
        super.restart(source, entityClazz, columns, items);

        //--aggiunge una colonna calcolata
        Grid.Column colonna = grid.addComponentColumn(
                preferenza -> {
                    PrefType type = ((Preferenza) preferenza).getType();
                    byte[] bytes = (byte[]) ((Preferenza) preferenza).getValue();
                    Object value = null;
                    Component comp = null;

                    switch (type) {
                        case string:
                        case integer:
                        case email:
                            try { // prova ad eseguire il codice
                                value = type.bytesToObject(bytes);
                                comp = new Label((String) value);
                            } catch (Exception unErrore) { // intercetta l'errore
                                log.error(unErrore.toString());
                            }// fine del blocco try-catch
                            break;
                        case bool:
                            try { // prova ad eseguire il codice
                                value = type.bytesToObject(bytes);
                                if (value instanceof Boolean) {
                                    if ((Boolean) value) {
                                        comp = new LabelVerde(VaadinIcons.CHECK);
                                    } else {
                                        comp = new LabelRosso(VaadinIcons.CLOSE);
                                    }// end of if/else cycle
                                }// end of if cycle
                            } catch (Exception unErrore) { // intercetta l'errore
                                log.error(unErrore.toString());
                            }// fine del blocco try-catch
                            break;
                        case bool2:
                            try { // prova ad eseguire il codice
                                value = type.bytesToObject(bytes);
                                if (value instanceof Boolean) {
                                    comp = new CheckBox();
                                    ((CheckBox) comp).setEnabled(false);
                                    ((CheckBox) comp).setValue((Boolean) value);
                                }// end of if cycle
                            } catch (Exception unErrore) { // intercetta l'errore
                                log.error(unErrore.toString());
                            }// fine del blocco try-catch
                            break;
                        case date:
                            try { // prova ad eseguire il codice
                                value = type.bytesToObject(bytes);
                                value = LibDate.getShort((LocalDateTime) value);
                                comp = new Label((String) value);
                            } catch (Exception unErrore) { // intercetta l'errore
                                log.error(unErrore.toString());
                            }// fine del blocco try-catch
                            break;
                        default:
                            log.warn("Switch - caso non definito");
                            break;
                    } // end of switch statement
                    return comp;
                });//end of lambda expressions
        colonna.setCaption("Value");
        float lar = grid.getWidth();
        grid.setWidth(lar + LibColumn.WIDTH_BYTE, Unit.PIXELS);
    }// end of method

}// end of class
