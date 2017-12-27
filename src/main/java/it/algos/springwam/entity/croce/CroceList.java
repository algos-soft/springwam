package it.algos.springwam.entity.croce;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.toolbar.AToolbarImpl;
import it.algos.springvaadin.ui.AlgosUI;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.grid.AlgosGrid;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.AlgosListImpl;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.ListToolbar;
import it.algos.springwam.migration.Migration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by gac on 31-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_CRO)
public class CroceList extends AlgosListImpl {

    @Autowired
    private Migration migration;

    private CroceService croceService;

    /**
     * Costruttore @Autowired (nella superclasse)
     */
    public CroceList(@Qualifier(AppCost.TAG_CRO) AlgosService service, AlgosGrid grid, ListToolbar toolbar) {
        super(service, grid, toolbar);
        croceService = (CroceService) service;
    }// end of Spring constructor


    /**
     * Chiamato ogni volta che la finestra diventa attiva
     * Può essere sovrascritto per un'intestazione (caption) della grid
     */
    @Override
    protected void fixCaption(String className, List items) {
        if (LibSession.isDeveloper()) {
            super.fixCaption(className, items);
            super.caption += "</br>Lista visibile solo all'admin. Filtrata su una sola company";
            super.caption += "</br>NON usa la company (ovvio)";
            super.caption += "</br>Estende la superclasse Company";
            super.caption += "</br>Solo il developer vede queste note";
        } else {
            super.caption = "Croci di riferimento. Visualizza solo la croce selezionata al login.";
        }// end of if/else cycle
    }// end of method


    /**
     * Prepara la toolbar
     * <p>
     * Seleziona i bottoni da mostrare nella toolbar
     * Crea i bottoni (iniettandogli il publisher)
     * Aggiunge i bottoni al contenitore grafico
     * Inietta nei bottoni il parametro obbligatorio (source)
     *
     * @param source       dell'evento generato dal bottone
     * @param typeButtons da visualizzare
     */
    protected void inizializzaToolbar(ApplicationListener source, List<AButtonType> typeButtons) {
        toolbar.inizializza(source, typeButtons);
        if (LibSession.isDeveloper()) {
            this.addChangeCompanyButton();
            this.addCurrenteCompanyMigration(source);
            this.addAllCompaniesMigration(source);
        }// end of if cycle
    }// end of method


    private void addChangeCompanyButton() {
        List companyList = croceService.findAllAll();

        if (companyList == null) {
            return;
        }// end of if cycle
        companyList.add(0, "all");

        final ComboBox companies = new ComboBox("", companyList);
        companies.setEmptySelectionAllowed(false);
        if (LibSession.isCompanyValida()) {
            Company company = LibSession.getCompany();
            companies.setValue(company);
        } else {
            companies.setValue("all");
        }// end of if/else cycle
        toolbar.addComp(companies);

        companies.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                Object event = valueChangeEvent.getValue();

                if (event != null && event instanceof Company) {
                    LibSession.setCompany((Company) event);
                } else {
                    LibSession.setCompany(null);
                }// end of if/else cycle

                Object ui = getUI();
                if (ui instanceof AlgosUI) {
                    ((AlgosUI) ui).footer.setAppMessage("SpringVaadin 1.0");
                }// end of if cycle

            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Aggiunge un bottone alla toolbar
     * Effettua la migrazione per la company corrente
     */
    private void addCurrenteCompanyMigration(ApplicationListener source) {
        AButton button = toolbar.creaAddButtonSecondaRiga(AButtonType.importa, source);

        if (LibSession.isCompanyValida()) {
            button.setCaption(LibText.primaMaiuscola(LibSession.getCompany().getCode()));
            button.setEnabled(true);
        } else {
            button.setCaption("Empty");
            button.setEnabled(false);
        }// end of if/else cycle

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                migration.importCroce(LibSession.getCompany().getCode());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Aggiunge un bottone alla toolbar
     * Effettua la migrazione per tutte le company
     */
    private void addAllCompaniesMigration(ApplicationListener source) {
        AButton button = toolbar.creaAddButtonSecondaRiga(AButtonType.importa, source);
        button.setCaption("All");

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                migration.importAllCroci();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


//    /**
//     * Aggiunge un bottone alla toolbar
//     */
//    private void addTestButton() {
//        AButton button = new AButton();
//        button.setIcon(VaadinIcons.MAILBOX);
//        button.setCaption("Import per la company selezionata");
//        ((AToolbarImpl) toolbar).addComponent(button);
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                Company company= LibSession.getCompany();
//                if (company!=null) {
//                    migration.importCompany(company.getCode());
//                }// end of if cycle
//            }// end of inner method
//        });// end of anonymous inner class
//    }// end of method
//    /**
//     * Aggiunge un bottone alla toolbar
//     */
//    private void addTestButtonAll() {
//        AButton button = new AButton();
//        button.setIcon(VaadinIcons.MAILBOX);
//        button.setCaption("Import per tutte le company");
//        ((AToolbarImpl) toolbar).addComponent(button);
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                migration.importCompany("crf");
//                migration.importCompany("gaps");
//                migration.importCompany("pap");
//                migration.importCompany("crpt");
//            }// end of inner method
//        });// end of anonymous inner class
//    }// end of method
//
//
//    /**
//     * Aggiunge un bottone alla toolbar
//     */
//    private void addTestButton() {
//        AButton button = new AButton();
//        button.setIcon(VaadinIcons.MAILBOX);
//        button.setCaption("Import");
//        ((AToolbarImpl) toolbar).addComponent(button);
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                migration.importCompany("crf");
//                migration.importCompany("gaps");
//                migration.importCompany("pap");
//                migration.importCompany("crpt");
//            }// end of inner method
//        });// end of anonymous inner class
//    }// end of method


}// end of class
