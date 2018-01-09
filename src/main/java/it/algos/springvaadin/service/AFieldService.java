package it.algos.springvaadin.service;


import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIField;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.field.AComboField;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.field.IAFieldFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

import javax.persistence.metamodel.Attribute;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gac on 18 ott 2016.
 * Creazione dei field dalla annotation
 * In Spring le librerie NON possono essere astratte, altrimenti si perde @PostConstruct e @Autowired
 */
@SpringComponent
@Scope("singleton")
public class AFieldService {

    @Autowired
    private IAFieldFactory fieldFactory;

    @Autowired
    private ApplicationContext context;

    @Autowired
    public ATextService text;

    @Autowired
    public AAnnotationService annotation;


    @Autowired
    private AMongoService mongoService;

    /**
     * Create a single field.
     * The field type is chosen according to the annotation @AIField.
     *
     * @param source             presenter di riferimento da cui vengono generati gli eventi
     * @param reflectedJavaField di riferimento per estrarre le Annotation
     */
    @SuppressWarnings("all")
    public AField create(IAListener source, Field reflectedJavaField, AEntity entityBean) {
        AField algosField = null;
        List items = null;
        boolean nuovaEntity = text.isEmpty(entityBean.id);
        EAFieldType type = annotation.getFormType(reflectedJavaField);
        String caption = annotation.getFormFieldName(reflectedJavaField);
        AIField fieldAnnotation = annotation.getAIField(reflectedJavaField);
        String width = annotation.getWidthEM(reflectedJavaField);
        boolean required = annotation.isRequired(reflectedJavaField);
        boolean focus = annotation.isFocus(reflectedJavaField);
        boolean enabled = annotation.isFieldEnabled(reflectedJavaField, nuovaEntity);
        Class targetClazz = annotation.getComboClass(reflectedJavaField);
//        boolean visible = annotation.isFieldVisibile(reflectedJavaField, nuovaEntity);

        //@todo RIMETTERE
//        int rows = annotation.getNumRows(reflectionJavaField);
//        boolean nullSelection = annotation.isNullSelectionAllowed(reflectionJavaField);
//        boolean newItems = annotation.isNewItemsAllowed(reflectionJavaField);

        if (type == EAFieldType.text.noone) {
            return null;
        }// end of if cycle

        //@todo RIMETTERE
//        //--non riesco (per ora) a leggere le Annotation da una classe diversa (AEntity)
//        if (fieldAnnotation == null && reflectionJavaField.getName().equals(Cost.PROPERTY_ID)) {
//            type = EAFieldType.id;
//        }// end of if cycle

        if (type != null) {
            algosField = fieldFactory.crea(source, type, reflectedJavaField, entityBean);
        }// end of if cycle

        if (type == EAFieldType.combo && targetClazz != null && algosField != null) {
            items = mongoService.findAll(targetClazz);
            ((AComboField) algosField).fixCombo(items, false, false);
        }// end of if cycle


        //@todo RIMETTERE
//        if (type == EAFieldType.enumeration && targetClazz != null && algosField != null) {
//            if (targetClazz.isEnum()) {
//                items = new ArrayList(Arrays.asList(targetClazz.getEnumConstants()));
//            }// end of if cycle
//
//            if (algosField != null && algosField instanceof AComboField && items != null) {
//                ((AComboField) algosField).fixCombo(items, false, false);
//            }// end of if cycle
//        }// end of if cycle

        //@todo RIMETTERE
//        if (type == EAFieldType.radio && targetClazz != null && algosField != null) {
//            //@todo PATCH - PATCH - PATCH
//            if (reflectionJavaField.getName().equals("attivazione") && entityBean.getClass().getName().equals(Preferenza.class.getName())) {
//                items = new ArrayList(Arrays.asList(PrefEffect.values()));
//            }// end of if cycle
//            //@todo PATCH - PATCH - PATCH
//
//            if (items != null) {
//                ((ARadioField) algosField).fixRadio(items);
//            }// end of if cycle
//        }// end of if cycle

        //@todo RIMETTERE
//        if (type == EAFieldType.link && targetClazz != null && algosField != null) {
//            String lowerName = text.primaMinuscola(targetClazz.getSimpleName());
//            Object bean = context.getBean(lowerName);
//            algosField.setTarget((ApplicationListener) bean);
//        }// end of if cycle
//
        if (algosField != null && fieldAnnotation != null) {
//            algosField.setVisible(visible);
            algosField.setEnabled(enabled);
            algosField.setRequiredIndicatorVisible(required);
            algosField.setCaption(caption);
//            if (text.isValid(width)) {
            algosField.setWidth(width);
//            }// end of if cycle
//            if (rows > 0) {
//                algosField.setRows(rows);
//            }// end of if cycle
            algosField.setFocus(focus);
//
//            if (LibParams.displayToolTips()) {
//                algosField.setDescription(fieldAnnotation.help());
//            }// end of if cycle
//
//            if (type == EAFieldType.dateNotEnabled) {
//                algosField.setEnabled(false);
//            }// end of if cycle
        }// end of if cycle

        return algosField;
    }// end of method


    /**
     * Crea una (eventuale) lista di validator, basato sulle @Annotation della Entity
     * Lista dei validators da utilizzare PRIMA dei converters
     */
    public List<AbstractValidator> creaValidatorsPre(Field reflectionField) {
        List<AbstractValidator> lista = new ArrayList();
        List<AValidator> listaTmp = creaValidators(reflectionField);

        for (AValidator validator : listaTmp) {
            if (validator.posizione == Posizione.prima) {
                lista.add(validator.validator);
            }// end of if cycle
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Crea una (eventuale) lista di validator, basato sulle @Annotation della Entity
     * Lista dei validators da utilizzare DOPO i converters
     */
    public List<AbstractValidator> creaValidatorsPost(Field reflectionField) {
        List<AbstractValidator> lista = new ArrayList();
        List<AValidator> listaTmp = creaValidators(reflectionField);

        for (AValidator validator : listaTmp) {
            if (validator.posizione == Posizione.dopo) {
                lista.add(validator.validator);
            }// end of if cycle
        }// end of for cycle

        return lista;
    }// end of method


    /**
     * Crea una (eventuale) lista di validator, basato sulle @Annotation della Entity
     * Lista base, indifferenziata
     */
    private List<AValidator> creaValidators(Field reflectedField) {
        List<AValidator> lista = new ArrayList<>();
//        AbstractValidator validator = null;
//        AIField fieldAnnotation = LibAnnotation.getFieldAnnotation(reflectedField);
//        AFieldType type = null;
//        String fieldName = LibText.primaMaiuscola(reflectedField.getName());
//        fieldName = LibText.setRossoBold(fieldName);
//        String message = "";
//        int min = 0;
//        int max = 0;
//        boolean notNull = LibAnnotation.isNotNull(reflectedField);
//        boolean notEmpty = LibAnnotation.isNotEmpty(reflectedField);
//        boolean checkSize = LibAnnotation.isSize(reflectedField);
//        boolean checkUnico = LibAnnotation.isUnico(reflectedField);
//        boolean checkOnlyNumber = LibAnnotation.isOnlyNumber(reflectedField);
//        boolean checkOnlyLetter = LibAnnotation.isOnlyLetter(reflectedField);
//        Object oldValue;
//
//        if (fieldAnnotation != null) {
//            type = fieldAnnotation.type();
//            Object a = type;
//            min = LibAnnotation.getMin(reflectedField);
//            max = LibAnnotation.getMax(reflectedField);
//
//            switch (type) {
//                case text:
//                    if (checkUnico) {
////                        oldValue = LibReflection.getValue(entityBean, reflectedField.getName());
////                        validator = new AlgosUniqueValidator(clazz, reflectedField.getName(), oldValue);
////                        lista.add(new AValidator(validator, Posizione.prima));
//                    }// end of if cycle
//                    if (notEmpty) {
//                        String messageEmpty = LibAnnotation.getNotEmptyMessage(reflectedField);
//                        validator = new StringLengthValidator(messageEmpty, 1, 10000);
//                        lista.add(new AValidator(validator, Posizione.prima));
//                    }// end of if cycle
//                    if (checkSize) {
//                        String messageSize = LibAnnotation.getSizeMessage(reflectedField, notEmpty);
//                        validator = new AlgosStringLengthValidator(messageSize, min, max);
//                        lista.add(new AValidator(validator, Posizione.dopo));
//                    }// end of if cycle
//                    if (checkOnlyNumber) {
//                        validator = new AlgosNumberOnlyValidator(reflectedField.getName());
//                        lista.add(new AValidator(validator, Posizione.dopo));
//                    }// end of if cycle
//                    if (checkOnlyLetter) {
//                        validator = new AlgosLetterOnlyValidator(reflectedField.getName());
//                        lista.add(new AValidator(validator, Posizione.dopo));
//                    }// end of if cycle
//                    break;
//                case integer:
//                    addAnte(lista, new AlgosNumberNotNullValidator(reflectedField.getName()));
//                    break;
//                case integernotzero:
//                    addAnte(lista, new AlgosNumberNotNullValidator(reflectedField.getName()));
//                    addAnte(lista, new AlgosNumberNotZeroValidator(reflectedField.getName()));
//                    break;
//                case email:
//                    if (notEmpty) {
//                        String messageEmpty = LibAnnotation.getNotEmptyMessage(reflectedField);
//                        validator = new StringLengthValidator(messageEmpty, 1, 10000);
//                        lista.add(new AValidator(validator, Posizione.prima));
//                    }// end of if cycle
//                    addAnte(lista, new AlgosEmailValidator(reflectedField.getName()));
//                    break;
//                case checkbox:
//                    break;
//                case date:
//                    break;
//                case time:
//                    break;
//                case password:
//                    break;
//                case combo:
//                    break;
//                case textarea:
//                    if (notEmpty) {
//                        String messageEmpty = LibAnnotation.getNotEmptyMessage(reflectedField);
//                        validator = new StringLengthValidator(messageEmpty, 1, 10000);
//                        lista.add(new AValidator(validator, Posizione.prima));
//                    }// end of if cycle
//                    break;
//                case enumeration:
//                    if (notEmpty) {
//                        String messageEmpty = LibAnnotation.getNotEmptyMessage(reflectedField);
//                        validator = new StringLengthValidator(messageEmpty, 1, 10000);
//                        lista.add(new AValidator(validator, Posizione.prima));
//                    }// end of if cycle
//
//                    break;
//                default: // caso non definito
//            } // fine del blocco switch
//        }// end of if cycle
//
        return lista;
    }// end of method

    private void addAnte(List<AValidator> lista, AbstractValidator validator) {
        lista.add(new AValidator(validator, Posizione.prima));
    }// end of method

    private void addPost(List<AValidator> lista, AbstractValidator validator) {
        lista.add(new AValidator(validator, Posizione.dopo));
    }// end of method


//    /**
//     * Crea una (eventuale) lista di converter, basato sulle @Annotation della Entity
//     */
//    public List<AlgosConverter> creaConverters(  Field reflectedField) {
//        List<AlgosConverter> lista = new ArrayList<>();
//        AlgosConverter converter = null;
//        AIField fieldAnnotation = LibAnnotation.getFieldAnnotation(reflectedField);
//        boolean checkFirstCapital = LibAnnotation.isFirstCapital(reflectedField);
//        boolean checkUpper = LibAnnotation.isAllUpper(reflectedField);
//        boolean checkLower = LibAnnotation.isAllLower(reflectedField);
//
//        if (fieldAnnotation != null) {
//            switch (fieldAnnotation.type()) {
//                case text:
//                    if (checkFirstCapital) {
//                        converter = new FirstCapitalConverter();
//                        lista.add(converter);
//                    }// end of if cycle
//                    if (checkUpper) {
//                        converter = new UpperConverter();
//                        lista.add(converter);
//                    }// end of if cycle
//                    if (checkLower) {
//                        converter = new LowerConverter();
//                        lista.add(converter);
//                    }// end of if cycle
//                    break;
//                case integer:
//                    break;
//                case email:
//                    break;
//                case checkbox:
//                    break;
//                case date:
//                    break;
//                case time:
//                    break;
//                case password:
//                    break;
//                case combo:
//                    break;
//                case enumeration:
//                    break;
//                default: // caso non definito
//            } // fine del blocco switch
//        }// end of if cycle
//
//        return lista;
//    }// end of method


    /**
     * Restituisce una singola Annotation.
     *
     * @param attr the metamodel Attribute
     *
     * @return valore dell'Annotation - Valore di default, se non la trova
     */
    public AIField getAnnotation(Attribute attr) {
        AIField fieldAnnotation = null;
        Field javaField = null;
        Annotation annotation = null;

        try { // prova ad eseguire il codice
            javaField = attr.getJavaMember().getDeclaringClass().getDeclaredField(attr.getName());
            annotation = javaField.getAnnotation(AIField.class);
            fieldAnnotation = (AIField) annotation;
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return fieldAnnotation;
    }// end of method


    private class AValidator {
        AbstractValidator validator;
        Posizione posizione;

        public AValidator(AbstractValidator validator, Posizione posizione) {
            this.validator = validator;
            this.posizione = posizione;
        }// end of constructor
    }// end of internal class

    private enum Posizione {
        prima, dopo
    }// end of internal enumerationù

}// end of abstract static class
