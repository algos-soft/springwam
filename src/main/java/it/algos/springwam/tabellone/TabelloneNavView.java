package it.algos.springwam.tabellone;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import it.algos.springvaadin.annotation.AIEntity;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springwam.application.AppCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 08:35
 * Annotated with @SpringView (obbligatorio)
 */
@SpringView(name = AppCost.TAG_TAB)
@AIEntity(roleTypeVisibility = ARoleType.user)
public class TabelloneNavView extends AlgosNavView {


    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.ASTERISK;


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
    public TabelloneNavView(@Qualifier(AppCost.TAG_TAB)AlgosPresenterImpl presenter) {
        super(presenter);
    }// end of Spring constructor


}// end of class
