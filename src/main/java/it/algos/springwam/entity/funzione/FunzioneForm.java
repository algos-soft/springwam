package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.event.AActionEvent;
import it.algos.springvaadin.event.TypeAction;
import it.algos.springvaadin.field.AIconField;
import it.algos.springvaadin.field.ALinkField;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibResource;
import it.algos.springvaadin.lib.LibVaadin;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import it.algos.springvaadin.service.AlgosService;
import it.algos.springvaadin.toolbar.AToolbarImpl;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.AImageField;
import it.algos.springvaadin.form.AlgosFormImpl;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.toolbar.AToolbar;
import it.algos.springvaadin.toolbar.FormToolbar;
import it.algos.springwam.migration.Migration;
import it.algos.webbase.web.field.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by gac on 24-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_FUN)
public class FunzioneForm extends AlgosFormImpl {


    private final static String ICON_FIELD_NAME = "icona";


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     *
     * @param service     iniettata da Spring
     * @param toolbar     iniettata da Spring
     * @param toolbarLink iniettata da Spring
     */
    public FunzioneForm(@Qualifier(AppCost.TAG_FUN) AlgosService service,
                        @Qualifier(Cost.BAR_FORM) AToolbar toolbar,
                        @Qualifier(Cost.BAR_LINK) AToolbar toolbarLink) {
        super(service, toolbar, toolbarLink);
    }// end of Spring constructor


//    /**
//     * Eventuali regolazioni specifiche per i fields
//     * <p>
//     * Indirizzo è un linkField che usa DBRef e quindi il bottone deve essere BottoneRegistraLink
//     */
////    @Override
//    protected void fixFields() {
//        AField field = getField(ICON_FIELD_NAME);
//
//        if (field != null && field instanceof AIconField) {
//            addListenerBottoneIcona((AIconField) field);
//        }// end of if cycle
//    }// end of method


    /**
     * Aggiunge un listener specifico
     */
    private void addListenerBottoneIcona(AField field) {
        field.getButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                SelectIconDialog dialog = new SelectIconDialog();
                dialog.addCloseListener(new SelectIconDialog.CloseListener() {
                    @Override
                    public void dialogClosed(SelectIconDialog.DialogEvent event) {
                        switch (event.getExitcode()) {
                            case 0:   // close, no action
                                break;
                            case 1:   // icon selected
                                setVaadinIcon(event.getCodepoint());
                                break;
                            case 2:   // rewove icon
                                setVaadinIcon(0);
                                break;
                        } // fine del blocco switch
                    }// end of inner method
                });// end of anonymous inner class
                dialog.show();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Registra una icona (codePoint di VaadinIcons) nel field
     */
    private void setVaadinIcon(int codePoint) {
        int oldCodePoint = (int) super.getFieldValue(ICON_FIELD_NAME);
        super.setFieldValue(ICON_FIELD_NAME, codePoint);

        if (codePoint != oldCodePoint) {
            super.toolbar.enableButton(AButtonType.registra, true);
        }// end of if cycle

    }// end of method


}// end of class

