package it.algos.springvaadin.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import it.algos.springvaadin.annotation.ATypeEnabled;
import it.algos.springvaadin.entity.log.Log;
import it.algos.springvaadin.entity.log.LogLevel;
import it.algos.springvaadin.entity.log.LogType;
import it.algos.springvaadin.entity.preferenza.PrefEffect;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.entity.preferenza.Preferenza;
import it.algos.springvaadin.field.*;
import it.algos.springvaadin.annotation.AIField;
import it.algos.springvaadin.lib.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.login.ARoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: lun, 28-ago-2017
 * Time: 14:25
 */
@SpringComponent
@Slf4j
public class ViewField {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private AFieldFactory fieldFactory;


    @Autowired
    private ApplicationContext context;

    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     */
    public ViewField(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }// end of @Autowired constructor


    /**
     * Create a single field.
     * The field type is chosen according to the annotation @AIField.
     *
     * @param source presenter di riferimento da cui vengono generati gli eventi
     * @param attr   the metamodel Attribute
     */
    @SuppressWarnings("all")
    public AField create(ApplicationListener source, final Class<? extends AEntity> clazz, final String publicFieldName, AEntity entityBean) {
        AField field = null;
//        Object[] items = null;
//        boolean nuovaEntity = LibText.isEmpty(entityBean.id);
//        AFieldType type = LibAnnotation.getTypeField(clazz, publicFieldName);
//        String caption = LibAnnotation.getNameField(clazz, publicFieldName);
//        AIField fieldAnnotation = LibAnnotation.getField(clazz, publicFieldName);
//        String width = LibAnnotation.getWidthEM(clazz, publicFieldName);
//        int rows = LibAnnotation.getNumRows(clazz, publicFieldName);
//        boolean required = LibAnnotation.isRequiredWild(clazz, publicFieldName);
//        boolean focus = LibAnnotation.isFocus(clazz, publicFieldName);
//        boolean visible = LibAnnotation.isFieldVisibile(clazz, publicFieldName, nuovaEntity);
//        boolean enabled = LibAnnotation.isFieldEnabled(clazz, publicFieldName, nuovaEntity);
//        boolean nullSelection = LibAnnotation.isNullSelectionAllowed(clazz, publicFieldName);
//        Class targetClazz = LibAnnotation.getClass(clazz, publicFieldName);
//
//        //--non riesco (per ora) a leggere le Annotation da una classe diversa (AEntity)
//        if (fieldAnnotation == null && publicFieldName.equals(Cost.PROPERTY_ID)) {
//            type = AFieldType.id;
//        }// end of if cycle
//
//        if (type != null) {
//            field = fieldFactory.crea(clazz, type, source, publicFieldName, entityBean);
//        }// end of if cycle
//
//        if (type == AFieldType.combo && targetClazz != null && field != null) {
//            items = LibMongo.findAll(targetClazz).toArray();
//            ((AComboField) field).fixCombo(items, nullSelection);
//        }// end of if cycle
//
//        if (type == AFieldType.enumeration && targetClazz != null && field != null) {
//            if (targetClazz.isEnum()) {
//                items = targetClazz.getEnumConstants();
//            }// end of if cycle
//
//            if (field != null && field instanceof AComboField && items != null) {
//                ((AComboField) field).fixCombo(items, false);
//            }// end of if cycle
//
//        }// end of if cycle
//
//        if (type == AFieldType.radio && targetClazz != null && field != null) {
//            //@todo PATCH - PATCH - PATCH
//            if (publicFieldName.equals("attivazione") && clazz.getName().equals(Preferenza.class.getName())) {
//                items = PrefEffect.values();
//            }// end of if cycle
//            //@todo PATCH - PATCH - PATCH
//
//            ((ARadioField) field).fixRadio(items);
//        }// end of if cycle
//
//        if (type == AFieldType.link && targetClazz != null && field != null) {
//            String lowerName = LibText.primaMinuscola(targetClazz.getSimpleName());
//            Object bean = context.getBean(lowerName);
//            field.setTarget((ApplicationListener) bean);
//        }// end of if cycle
//
//        if (field != null && fieldAnnotation != null) {
//            field.setVisible(visible);
//            field.setEnabled(enabled);
//            field.setRequiredIndicatorVisible(required);
//            field.setCaption(caption);
//            if (LibText.isValid(width)) {
//                field.setWidth(width);
//            }// end of if cycle
//            if (rows > 0) {
//                field.setRows(rows);
//            }// end of if cycle
//            field.setFocus(focus);
//
//            if (LibParams.displayToolTips()) {
//                field.setDescription(fieldAnnotation.help());
//            }// end of if cycle
//
//            if (type == AFieldType.dateNotEnabled) {
//                field.setEnabled(false);
//            }// end of if cycle
//        }// end of if cycle

        return field;
    }// end of method

}// end of class

