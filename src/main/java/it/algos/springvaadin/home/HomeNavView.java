package it.algos.springvaadin.home;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.annotation.AIEntity;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.view.AlgosView;
import it.algos.springvaadin.view.ErrorView;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 07/07/17
 * <p>
 * Layer di collegamento tra Spring e MVP
 * Serve SOLO per intercettare le selezioni del menu e 'lanciare' il corrispondente AlgosPresenter
 * <p>
 * La selezione del menu nella UI di partenza, invoca lo SpringNavigator che rimanda qui
 * Passa il controllo alla classe xxxPresenter che gestisce la business logic
 * La classe xxxPresenter costruisce (iniezione) tutte le classi necessarie, tra cui xxxView
 * Il metodo getLinkedView() fornisce, tramite xxxPresenter,
 * la view effettiva da visualizzare richiesta da AlgosUI.showView()
 */
@SpringView(name = Cost.TAG_HOME)
@AIEntity(roleTypeVisibility = ARoleType.user)
public class HomeNavView extends AlgosNavView {


    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.HOME;


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Questa classe (View) è la prima del gruppo (modulo) invocata da SpringNavigator
     * Deve quindi iniettarsi il riferimento al gestore principale (xxxPresenter)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     *
     * @param presenter iniettato da Spring, della sottoclasse indicata da @Qualifier
     */
    public HomeNavView(@Qualifier(Cost.TAG_HOME) AlgosPresenterImpl presenter) {
        super(presenter);
    }// end of Spring constructor



//    /**
//     * Metodo invocato (da SpringBoot) ogni volta che si richiama la view dallo SpringNavigator
//     * Passa il controllo alla classe xxxPresenter che gestisce la business logic
//     */
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        Label label = new Label("Homeview");
//        addComponent(label);
//    }// end of method
//
//    /**
//     * Vista effettiva da usare/visualizzare
//     * Richiesta da AlgosUI.showView()
//     */
//    public AlgosView getLinkedView() {
//        Label label = new Label("Homeview");
//        addComponent(label);
//
//
//        return null;
//    }// end of method

}// end of class
