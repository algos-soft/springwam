package it.algos.springvaadin.entity.stato;

import com.vaadin.data.ValueProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibArray;
import it.algos.springvaadin.lib.LibResource;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 10-ago-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_STA)
public class StatoList extends AlgosListImpl {


    private AButton buttonImport;


    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public StatoList(@Qualifier(Cost.TAG_STA) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
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
        columnBandiera();
    }// end of method

    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            caption = "Elenco di " + items.size() + " schede che valgono per tutte le company";
            caption += "</br>Lista visibile solo al developer";
            caption += "</br>NON usa la company";
            caption += "</br>La key property ID utilizza la property alfaTre";
            caption += "</br>Le property nome, alfaDue e alfaTre sono uniche e non possono essere nulle";
            caption += "</br>La property numerico può essere nulla";
        }// end of if cycle
    }// end of method


    /**
     * Crea la colonna (di tipo Component) per visualizzare la bandiera
     */
    private void columnBandiera() {
        Grid.Column colBandiera = grid.addComponentColumn(stato -> {
            byte[] bytes = ((Stato) stato).getBandiera();
            Image image = LibResource.getImage(bytes);
            image.setWidth("3em");
            image.setHeight("1.5em");
            return image;
        });//end of lambda expressions

        float lar = grid.getWidth();
        grid.setWidth(lar + 100, Unit.PIXELS);

        colBandiera.setCaption("Icon");
        colBandiera.setId("Icon");
        List<Grid.Column> listaColonne = grid.getColumns();
        ArrayList lista = new ArrayList();
        for (Grid.Column col : listaColonne) {
            lista.add(col.getId());
        }// end of for cycle
        lista.remove("Icon");
        lista.add(1,"Icon");
        grid.setColumnOrder((String[])lista.toArray(new String[lista.size()]));
    }// end of metho

    /**
     * Prepara la toolbar
     * <p>
     * Seleziona i bottoni da mostrare nella toolbar
     * Crea i bottoni (iniettandogli il publisher)
     * Aggiunge i bottoni al contenitore grafico
     * Inietta nei bottoni il parametro obbligatorio (source)
     *
     * @param source       dell'evento generato dal bottone
     * @param listaBottoni da visualizzare
     */
    protected void inizializzaToolbar(ApplicationListener source, List<AButtonType> listaBottoni) {
        super.inizializzaToolbar(source, listaBottoni);
        buttonImport = toolbar.creaAddButton(AButtonType.importa, source);
    }// end of method


    public void enableImport(boolean status) {
        if (buttonImport != null) {
            buttonImport.setEnabled(status);
        }// end of if cycle
    }// end of method


}// end of class
