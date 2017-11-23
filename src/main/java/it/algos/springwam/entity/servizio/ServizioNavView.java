package it.algos.springwam.entity.servizio;

import it.algos.springvaadin.annotation.AIEntity;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springwam.application.AppCost;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringView (obbligatorio)
 */
@SpringView(name = AppCost.TAG_SER)
@AIEntity(roleTypeVisibility = ARoleType.user)
public class ServizioNavView extends AlgosNavView {


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
    public ServizioNavView(@Qualifier(AppCost.TAG_SER) AlgosPresenterImpl presenter) {
        super(presenter);
    }// end of Spring constructor


}// end of class
