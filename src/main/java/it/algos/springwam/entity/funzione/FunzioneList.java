package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ButtonRenderer;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.lib.LibColumn;
import it.algos.springvaadin.lib.LibDate;
import it.algos.springvaadin.lib.LibResource;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.renderer.IconRenderer;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbarImpl;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.toolbar.ListToolbar;
import it.algos.springwam.migration.Migration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 24-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Slf4j
@Qualifier(AppCost.TAG_FUN)
public class FunzioneList extends AlgosListImpl {

    private final static String ICON_FIELD_NAME = "Icon";


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public FunzioneList(@Qualifier(AppCost.TAG_FUN) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
    }// end of Spring constructor


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
        columnIcona();
    }// end of method


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.fixCaption(className, items);
            caption += "</br>Lista visibile a tutti";
            caption += "</br>Usa la company che è ACompanyRequired.obbligatoria";
            caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Funzioni previste. Ogni volontario può essere abilitato per una o più funzioni";
        }// end of if/else cycle

        chekFunzioniEsistenti();
    }// end of method


    /**
     * Crea una collezione di funzioni
     * Controlla se la collezione esiste già
     */
    public void chekFunzioniEsistenti() {
        int code = VaadinIcons.CHECK.getCodepoint();
        String beta = VaadinIcons.CHECK.getHtml();
        int codePoint = FontAwesome.CHECK.getCodepoint();
        String alfa = FontAwesome.CHECK.getHtml();
        log.info("La collezione di funzioni è presente");
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare l'icona
     */
    private void columnIcona() {
        IconRenderer renderIcon = new IconRenderer();
        Grid.Column colonna = grid.addComponentColumn(funzione -> {
            int codePoint = ((Funzione) funzione).getIcona();

            if (codePoint > 0) {
                VaadinIcons icona = LibResource.getVaadinIcon(codePoint);
                Button button = new Button(icona);
                button.setHeight("2em");
                button.setWidth("3em");
                return button;
            } else {
                return null;
            }// end of if/else cycle

        });//end of lambda expressions

        colonna.setId(ICON_FIELD_NAME);
        colonna.setCaption(ICON_FIELD_NAME);
        colonna.setWidth(LibColumn.WIDTH_ICON);
        float lar = grid.getWidth();
        grid.setWidth(lar + LibColumn.WIDTH_ICON, Unit.PIXELS);
        if (LibSession.isDeveloper()) {
            grid.setColumnOrder("ordine", "company", "code", ICON_FIELD_NAME, "sigla", "descrizione");
        } else {
            grid.setColumnOrder("ordine", ICON_FIELD_NAME, "sigla", "descrizione");
        }// end of if/else cycle

    }// end of method

}// end of class
