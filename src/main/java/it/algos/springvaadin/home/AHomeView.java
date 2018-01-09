package it.algos.springvaadin.home;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.role.RoleForm;
import it.algos.springvaadin.entity.role.RoleList;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.lib.LibImage;
import it.algos.springvaadin.lib.LibResource;
import it.algos.springvaadin.list.AList;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.menu.MenuHome;
import it.algos.springvaadin.menu.MenuLayout;
import it.algos.springvaadin.panel.APanel;
import it.algos.springvaadin.presenter.IAPresenter;
import it.algos.springvaadin.service.AResourceService;
import it.algos.springvaadin.view.AView;
import it.algos.springvaadin.view.IAView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 21:52
 */
@Slf4j
@Scope("session")
@SpringView(name = ACost.VIEW_HOME)
public class AHomeView extends AView {

    /**
     * Inietta da Spring come 'singleton'
     */
    @Autowired
    public ALogin login;


    @Autowired
    public AResourceService resource;


    /**
     * Contenitore grafico per la barra di menu principale
     * Un eventuale menuBar specifica può essere iniettata dalla sottoclasse concreta
     * Le sottoclassi possono aggiungere/modificare i menu che verranno ripristinati all'uscita della view
     * Componente grafico obbligatorio
     */
    @Autowired
    protected MenuHome menuLayoutHome;


    //--icona del Menu
    public static final Resource VIEW_ICON = VaadinIcons.HOME;

    /**
     * Classe da aggiungere a menuLayout solo per questo modulo
     * Viene eliminata dal menuLayout quando la view ''esce'
     */
    private Class<? extends IAView> viewForThisModuleOnly = RoleForm.class;

    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public AHomeView() {
        super(null, null);
    }// end of Spring constructor


    /**
     * Metodo invocato (dalla SpringNavigator di SpringBoot) ogni volta che la view diventa attiva
     * Elimina il riferimento al menuLayout nella view 'uscente' (oldView) perché il menuLayout è un 'singleton'
     * Elimina tutti i componenti della view 'entrante' (this)
     * Aggiunge il riferimento al menuLayout nella view 'entrante' (this)
     * Passa il controllo al Presenter
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.start();
    }// end of method


    /**
     * Creazione di una view con splash screen
     */
    public void start() {
        this.removeAllComponents();

        //--componente grafico obbligatorio
        if (AlgosApp.USE_SECURITY) {
            if (login.isLogged()) {
                menuLayout = creaMenu();
                this.addComponent(menuLayout);
            } else {
                menuLayoutHome.start();
                this.addComponent(menuLayoutHome);
            }// end of if/else cycle
        } else {
            menuLayout = creaMenu();
            this.addComponent(menuLayout);
        }// end of if/else cycle

        //--componente grafico obbligatorio
        this.bodyLayout.setContent(getImage());
        this.addComponent(bodyLayout);

        this.setExpandRatio(bodyLayout, 1);
    }// end of method


    private Layout getImage() {
        String nomeRisorsaConSuffisso="amb3.jpg";
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSizeFull();
        int larImage = 650;
        int altImage = 400;
        double delta = 1.5;
        int lar = ((Double)(larImage * delta)).intValue();
        int alt =  ((Double)(altImage * delta)).intValue();

        Image image = resource.getImage(nomeRisorsaConSuffisso);
        image.setWidth(lar, Unit.PIXELS);
        image.setHeight(alt, Unit.PIXELS);
//        layout.setWidth(650 * 2, Unit.PIXELS);
//        layout.setHeight(400 * 2, Unit.PIXELS);

        layout.addComponent(image);
        layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

        return layout;
    }// end of method


}// end of class
