package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.bottone.AButtonType;
import it.algos.springvaadin.label.LabelBold;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 07:38
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_LINK)
public class ALinkField extends AField {


    //--componente grafico del field per visualizzare il toString() dell'istanza rappresentata nel field
    public Label label = null;

    private AButton buttonEdit;
    private AButton buttonDelete;

    /**
     * Costruttore @Autowired (nella superclasse)
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public ALinkField() {
        super();
    }// end of @Autowired constructor


    /**
     * Regolazioni varie DOPO aver creato l'istanza
     * L'istanza può essere creata da Spring o con clone(), ma necessita comunque di questi due parametri
     */
    @Override
    public void inizializza(String publicFieldName, ApplicationListener source) {
        super.inizializza(publicFieldName, source);
        if (label == null) {
            label = new LabelBold();
        }// end of if cycle

        if (buttonEdit != null) {
            buttonEdit.setSource(source);
        }// end of if cycle
        if (buttonDelete != null) {
            buttonDelete.setSource(source);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        if (entityBean != null) {
            label.setValue(entityBean.toString());
        }// end of if cycle

        if (buttonEdit != null && label != null) {
            if (buttonDelete != null) {
                String pippoz = label.getValue();
                if (label.getValue().equals("")) {
                    buttonDelete.setEnabled(false);
                } else {
                    buttonDelete.setEnabled(true);
                }// end of if/else cycle
                return new HorizontalLayout(buttonEdit, buttonDelete, label);
            } else {
                return new HorizontalLayout(buttonEdit, label);
            }// end of if/else cycle
        } else {
            return buttonEdit;
        }// end of if/else cycle
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        if (value != null && value instanceof AEntity) {
            entityBean = (AEntity) value;
        }// end of if cycle

        if (label != null) {
            if (value != null ) {
                label.setValue(value.toString());
            } else {
                label.setValue("");
            }// end of if/else cycle
        }// end of if cycle
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    public Object getValue() {
        if (entityBean != null) {
            return entityBean;
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    @Override
    public void setButton(AButton button) {
    }// end of method

    public void setButtonEdit(AButton buttonEdit) {
        this.buttonEdit = buttonEdit;
    }// end of method

    public void setButtonDelete(AButton buttonDelete) {
        this.buttonDelete = buttonDelete;
    }// end of method

    /**
     * ATTENZIONE - per il bottone Delete, source e target sono invertiti
     */
    public void setSource(ApplicationListener source) {
        if (buttonEdit != null) {
            if (source != null) {
                buttonEdit.setSource(source);
            }// end of if cycle

            if (entityBean != null) {
                buttonEdit.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
        if (buttonDelete != null) {
            if (source != null) {
                buttonDelete.setSource(source);
            }// end of if cycle

            if (entityBean != null) {
                buttonDelete.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
    }// end of method

    /**
     * ATTENZIONE - per il bottone Delete, source e target sono invertiti
     */
    public void setTarget(ApplicationListener target) {
        this.target = target;
        if (buttonEdit != null) {
            if (target != null) {
                buttonEdit.setTarget(target);
            }// end of if cycle

            if (entityBean != null) {
                buttonEdit.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
        if (buttonDelete != null) {
            if (target != null) {
                buttonDelete.setTarget(target);
            }// end of if cycle

            if (entityBean != null) {
                buttonDelete.setEntityBean(entityBean);
            }// end of if cycle
        }// end of if cycle
    }// end of method


    public void setTypeLink(AButtonType typeLink) {
        if (button != null) {
//            ((AButtonEditLink) button).setTypeLink(typeLink);
        }// end of if cycle
    }// end of method


    public void refreshFromDialogLinkato(AEntity entityBean) {
        doSetValue(entityBean);
    }// end of method


}// end of class

