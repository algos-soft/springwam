package it.algos.springvaadin.entity.company;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.annotation.AIEntity;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by gac on 07/07/17
 * Annotated with @SpringView (obbligatorio)
 */
@SpringView(name = Cost.TAG_COMP)
@AIEntity(roleTypeVisibility = ARoleType.developer)
public class CompanyNavView extends AlgosNavView {


    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.DIPLOMA;


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
    public CompanyNavView(@Qualifier(Cost.TAG_COMP) AlgosPresenterImpl presenter) {
        super(presenter);
    }// end of Spring constructor


}// end of class
