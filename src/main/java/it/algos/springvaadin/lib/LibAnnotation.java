package it.algos.springvaadin.lib;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.form.FormButton;
import it.algos.springvaadin.list.ListButton;
import it.algos.springvaadin.login.ARoleType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gac on 12/07/17
 * Libreria di metodo per gestire le interfacce specifiche di Springvaadin: AIColumn e AIField
 */
public abstract class LibAnnotation {


    /**
     * Get a map of all annotations of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the Annotations for the specific field
     */
    @SuppressWarnings("all")
    public static Map getMap(final Class<? extends AEntity> clazz, final String publicFieldName) {
        Map mappa = null;
        Annotation[] annotations = getAll(clazz, publicFieldName);

        if (annotations != null) {
            mappa = new HashMap();

            for (Annotation ann : annotations) {
                mappa.put(ann.annotationType().getSimpleName(), ann);
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// end of static method

    /**
     * Get all annotations of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the Annotations for the specific field
     */
    @SuppressWarnings("all")
    public static Annotation[] getAll(final Class<? extends AEntity> clazz, final String publicFieldName) {
        Annotation[] annotations = null;
        Field javaField = LibReflection.getField(clazz, publicFieldName);

        if (javaField != null) {
            annotations = javaField.getAnnotations();
        }// end of if cycle

        return annotations;
    }// end of static method


    /**
     * Get the field annotation of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static AIField getField(final Class<? extends AEntity> clazz, final String publicFieldName) {
        AIField annotation = null;
        String tag = "AIField";
        Map mappa = getMap(clazz, publicFieldName);

        if (mappa != null && mappa.containsKey(tag)) {
            annotation = (AIField) mappa.get(tag);
        }// end of if cycle

        return annotation;
    }// end of static method


    /**
     * Get the column annotation of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the Annotation for the specific column
     */
    @SuppressWarnings("all")
    public static AIColumn getColumn(final Class<? extends AEntity> clazz, final String publicFieldName) {
        AIColumn annotation = null;
        String tag = "AIColumn";
        Map mappa = getMap(clazz, publicFieldName);

        //--recupera l'annotation della entity corrente
        mappa = getMap(clazz, publicFieldName);


        if (mappa != null && mappa.containsKey(tag)) {
            annotation = (AIColumn) mappa.get(tag);
        }// end of if cycle

        return annotation;
    }// end of static method


    /**
     * Recupera l'Annotation specifica
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return annotation
     */
    public static AIColumn getColumnAnnotation(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(AIColumn.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    /**
     * Recupera l'Annotation specifica
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    public static AIField getFieldAnnotation(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(AIField.class);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method

    /**
     * Get the type (field) of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the type for the specific column
     */
    @SuppressWarnings("all")
    public static AFieldType getTypeField(final Class<? extends AEntity> clazz, final String publicFieldName) {
        AFieldType type = null;
        AIField fieldAnnotation = getField(clazz, publicFieldName);

        if (fieldAnnotation != null) {
            type = fieldAnnotation.type();
        }// end of if cycle

        return type;
    }// end of static method


    /**
     * Get the type (field) of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the type for the specific column
     */
    @SuppressWarnings("all")
    public static AFieldType getFormType(Field reflectionField) {
        AFieldType type = null;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            type = fieldAnnotation.type();
        }// end of if cycle

        return type;
    }// end of static method


    /**
     * Get the type (column) of the property.
     * Se manca, usa il type del Field
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the type for the specific column
     */
    @Deprecated
    @SuppressWarnings("all")
    public static AFieldType getTypeColumn(final Class<? extends AEntity> clazz, final String publicFieldName) {
        AFieldType type = null;
        AIColumn columnAnnotation = getColumn(clazz, publicFieldName);

        if (columnAnnotation != null) {
            type = columnAnnotation.type();
        }// end of if cycle

        if (type == AFieldType.ugualeAlField) {
            type = getTypeField(clazz, publicFieldName);
        }// end of if cycle

        return type;
    }// end of static method

    /**
     * Get the type (column) of the property.
     * Se manca, usa il type del Field
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the type for the specific column
     */
    @SuppressWarnings("all")
    public static AFieldType getColumnType(Field reflectionField) {
        AFieldType type = null;
        AIColumn columnAnnotation = getColumnAnnotation(reflectionField);

        if (columnAnnotation != null) {
            type = columnAnnotation.type();
        }// end of if cycle

        if (type == AFieldType.ugualeAlField) {
            type = getFormType(reflectionField);
        }// end of if cycle

        return type;
    }// end of static method


    /**
     * Get the status required of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isRequired(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.required();
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Get the status required of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isRequiredWild(Field reflectionField) {
        return isNotEmpty(reflectionField) || isNotNull(reflectionField) || isRequired(reflectionField);
    }// end of static method

//    /**
//     * Get the status enabled of the property.
//     *
//     * @param clazz           the entity class
//     * @param publicFieldName the name of the property
//     *
//     * @return status of field
//     */
//    @SuppressWarnings("all")
//    public static ATypeEnabled getTypeEnabled(final Class<? extends AEntity> clazz, final String publicFieldName) {
//        ATypeEnabled typeEnabled = ATypeEnabled.always;
//        AIField fieldAnnotation = getField(clazz, publicFieldName);
//
//        if (fieldAnnotation != null) {
//            typeEnabled = fieldAnnotation.typeEnabled();
//        }// end of if cycle
//
//        return typeEnabled;
//    }// end of static method

    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFieldAccessibility(Field reflectionField) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.never;

        if (LibSession.isDeveloper()) {
            fieldAccessibility = getFieldAccessibilityDev(reflectionField);
        } else {
            if (LibSession.isAdmin()) {
                fieldAccessibility = getFieldAccessibilityAdmin(reflectionField);
            } else {
                if (true) {
                    fieldAccessibility = getFieldAccessibilityUser(reflectionField);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return fieldAccessibility;
    }// end of static method


    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFieldAccessibilityDev(Field reflectionField) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.dev();
        }// end of if cycle

        if (fieldAccessibility == FieldAccessibility.asForm) {
            fieldAccessibility = getFormAccessibilityDev(reflectionField.getClass());
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method


    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFieldAccessibilityAdmin(Field reflectionField) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.admin();
        }// end of if cycle

        if (fieldAccessibility == FieldAccessibility.asForm) {
            fieldAccessibility = getFormAccessibilityAdmin(reflectionField.getClass());
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method


    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFieldAccessibilityUser(Field reflectionField) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.user();
        }// end of if cycle

        if (fieldAccessibility == FieldAccessibility.asForm) {
            fieldAccessibility = getFormAccessibilityUser(reflectionField.getClass());
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method



    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFormAccessibilityDev(Class clazz) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIForm fieldAnnotation = getFormAnnotation(clazz);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.fieldsDev();
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method

    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFormAccessibilityAdmin(Class clazz) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIForm fieldAnnotation = getFormAnnotation(clazz);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.fieldsAdmin();
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method

    /**
     * Get the status enabled of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static FieldAccessibility getFormAccessibilityUser(Class clazz) {
        FieldAccessibility fieldAccessibility = FieldAccessibility.allways;
        AIForm fieldAnnotation = getFormAnnotation(clazz);

        if (fieldAnnotation != null) {
            fieldAccessibility = fieldAnnotation.fieldsUser();
        }// end of if cycle

        return fieldAccessibility;
    }// end of static method


    /**
     * Get the status focus of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isFocus(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.focus();
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Get the status FirstCapital of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isFirstCapital(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.firstCapital();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status allUpper of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isAllUpper(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.allUpper();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status nullSelectionAllowed of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isNullSelectionAllowed(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.nullSelectionAllowed();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status newItemsAllowed of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isNewItemsAllowed(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.newItemsAllowed();
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Get the status allLower of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isAllLower(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.allLower();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status onlyNumber of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isOnlyNumber(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.onlyNumber();
        }// end of if cycle

        return status;
    }// end of static method

    /**
     * Get the status onlyLetter of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return status of field
     */
    @SuppressWarnings("all")
    public static boolean isOnlyLetter(Field reflectionField) {
        boolean status = true;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            status = fieldAnnotation.onlyLetter();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the name (field) of the property.
     * Se manca, usa il nome della property
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the name (column) of the field
     */
    @Deprecated
    @SuppressWarnings("all")
    public static String getNameField(final Class<? extends AEntity> clazz, final String publicFieldName) {
        String name = "";
        AIField fieldAnnotation = getField(clazz, publicFieldName);

        if (fieldAnnotation != null) {
            name = fieldAnnotation.name();
        }// end of if cycle

        if (name.equals("")) {
            name = LibText.primaMaiuscola(publicFieldName);
        }// end of if cycle

        return name;
    }// end of static method


    /**
     * Get the name (field) of the property.
     * Se manca, usa il nome della property
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the name (rows) of the field
     */
    @SuppressWarnings("all")
    public static String getFormFieldName(Field reflectionField) {
        String name = reflectionField.getName();
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            name = fieldAnnotation.name();
        }// end of if cycle

        if (LibText.isEmpty(name)) {
            name = reflectionField.getName();
        }// end of if cycle

        return LibText.primaMaiuscola(name);
    }// end of static method


    /**
     * Get the name (column) of the property.
     * Se manca, usa il nome del Field
     * Se manca, usa il nome della property
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the name (column) of the field
     */
    @SuppressWarnings("all")
    public static String getColumnName(final Class<? extends AEntity> clazz, final String publicFieldName) {
        Field reflectionField = LibReflection.getField(clazz, publicFieldName);
        return getColumnName(reflectionField);
    }// end of static method


    /**
     * Get the name (column) of the property.
     * Se manca, usa il nome del Field
     * Se manca, usa il nome della property
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the name (column) of the field
     */
    public static String getColumnName(Field reflectionField) {
        String name = "";
        AIColumn columnAnnotation = getColumnAnnotation(reflectionField);

        if (columnAnnotation != null) {
            name = columnAnnotation.name();
        }// end of if cycle

        if (LibText.isEmpty(name)) {
            name = getFormFieldName(reflectionField);
        }// end of if cycle

        return name;
    }// end of static method


    /**
     * Get the widthEM of the property.
     *
     * @param field reflectionField di riferimento per estrarre le Annotation
     *
     * @return the width of the field expressed in em
     */
    @SuppressWarnings("all")
    public static String getWidthEM(Field field) {
        String width = "";
        int widthInt = 0;
        String tag = "em";
        AIField fieldAnnotation = getFieldAnnotation(field);

        if (fieldAnnotation != null) {
            widthInt = fieldAnnotation.widthEM();
            if (widthInt > 0) {
                width = widthInt + tag;
            }// end of if cycle
        }// end of if cycle

        return width;
    }// end of static method


    /**
     * Get the number of rows of the property.
     *
     * @param field reflectionField di riferimento per estrarre le Annotation
     *
     * @return the number of rows of the field expressed in int
     */
    @SuppressWarnings("all")
    public static int getNumRows(Field field) {
        int rows = 1;
        AIField fieldAnnotation = getFieldAnnotation(field);

        if (fieldAnnotation != null) {
            rows = fieldAnnotation.numRowsTextArea();
        }// end of if cycle

        return rows;
    }// end of static method


    /**
     * Get the width of the property.
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the width of the column expressed in int
     */
    @SuppressWarnings("all")
    public static int getColumnWith(final Class<? extends AEntity> clazz, final String publicFieldName) {
        int width = 0;
        AIColumn columnAnnotation = getColumn(clazz, publicFieldName);

        if (columnAnnotation != null) {
            width = columnAnnotation.width();
        }// end of if cycle

        return width;
    }// end of static method


    /**
     * Get the width of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the width of the column expressed in int
     */
    @SuppressWarnings("all")
    public static int getColumnWith(Field reflectionField) {
        int width = 0;
        AIColumn columnAnnotation = getColumnAnnotation(reflectionField);

        if (columnAnnotation != null) {
            width = columnAnnotation.width();
        }// end of if cycle

        return width;
    }// end of static method


    /**
     * Get the specific annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static Size getSize(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(Size.class);
        } else {
            return null;
        }// end of if/else cycle
//        Size annotation = null;
//        String tag = "Size";
//        Map mappa = getMap(clazz, publicFieldName);
//
//        if (mappa != null && mappa.containsKey(tag)) {
//            annotation = (Size) mappa.get(tag);
//        }// end of if cycle
//
//        return annotation;
    }// end of static method


    /**
     * Get the existence of the Size annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return true if the Size Annotation exists
     */
    @SuppressWarnings("all")
    public static boolean isSize(Field reflectionField) {
        boolean sizeEsiste = false;
        Size sizeAnnotation = getSize(reflectionField);

        if (sizeAnnotation != null) {
            sizeEsiste = true;
        }// end of if cycle

        return sizeEsiste;
    }// end of static method


    /**
     * Get the message of the Size annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the specific message
     */
    @SuppressWarnings("all")
    public static String getSizeMessage(Field reflectionField, boolean notEmpty) {
        String message = "";
        Size annotation = getSize(reflectionField);
        String oppure = notEmpty ? "" : "vuoto oppure ";
        int min = LibAnnotation.getMin(reflectionField);
        int max = LibAnnotation.getMax(reflectionField);
        boolean maxEccessivo = max > 10000;
        String fieldName = LibText.primaMaiuscola(reflectionField.getName());
        fieldName = LibText.setRossoBold(fieldName);

        if (annotation != null) {
            message = annotation.message();
        }// end of if cycle

        if (message.equals("{javax.validation.constraints.Size.message}")) {
            message = fieldName + " non può essere vuoto";
        }// end of if cycle

        if (min == max) {
            message = fieldName + " deve essere " + oppure + "uguale a " + min + " caratteri";
        } else {
            if (maxEccessivo) {
                message = fieldName + " deve essere " + oppure + " maggiore di " + min + " caratteri";
            } else {
                message = fieldName + " deve essere " + oppure + "compreso tra " + min + " e " + max + " caratteri";
            }// end of if/else cycle
        }// end of if/else cycle

        return message;
    }// end of static method


    /**
     * Get the min length of the string property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the min length of the string property
     */
    @SuppressWarnings("all")
    public static int getMin(Field reflectionField) {
        int length = 0;
        Size annotation = getSize(reflectionField);

        if (annotation != null) {
            length = annotation.min();
        }// end of if cycle

        return length;
    }// end of static method


    /**
     * Get the max length of the string property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the max length of the string property
     */
    @SuppressWarnings("all")
    public static int getMax(Field reflectionField) {
        int length = 0;
        Size annotation = getSize(reflectionField);

        if (annotation != null) {
            length = annotation.max();
        }// end of if cycle

        return length;
    }// end of static method


    /**
     * Recupera l'Annotation specifica
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static NotEmpty getNotEmpty(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(NotEmpty.class);
        } else {
            return null;
        }// end of if/else cycle

//        NotEmpty notEmpty = null;
//        String tag = "NotEmpty";
//        Map mappa = getMap(clazz, publicFieldName);
//
//        if (mappa != null && mappa.containsKey(tag)) {
//            notEmpty = (NotEmpty) mappa.get(tag);
//        }// end of if cycle
//
//        return notEmpty;
    }// end of static method


    /**
     * Get the message of the NotEmpty annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the specific message
     */
    @SuppressWarnings("all")
    public static String getNotEmptyMessage(Field reflectionField) {
        String message = "";
        NotEmpty notEmpty = getNotEmpty(reflectionField);
        String fieldName = LibText.primaMaiuscola(reflectionField.getName());
        fieldName = LibText.setRossoBold(fieldName);

        if (notEmpty != null) {
            message = notEmpty.message();
        }// end of if cycle

        if (message.equals("{org.hibernate.validator.constraints.NotEmpty.message}")) {
            message = fieldName + " non può essere vuoto";
        }// end of if cycle

        return message;
    }// end of static method


    /**
     * Get the existence of the notEmpty annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return true if the notEmpty Annotation exists
     */
    @SuppressWarnings("all")
    public static boolean isNotEmpty(Field reflectionField) {
        boolean nonVuota = false;
        NotEmpty notEmpty = getNotEmpty(reflectionField);

        if (notEmpty != null) {
            nonVuota = true;
        }// end of if cycle

        return nonVuota;
    }// end of static method


    /**
     * Get the specific annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static NotNull getNotNull(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(NotNull.class);
        } else {
            return null;
        }// end of if/else cycle

//        NotNull notNull = null;
//        String tag = "NotNull";
//        Map mappa = getMap(clazz, publicFieldName);
//
//        if (mappa != null && mappa.containsKey(tag)) {
//            notNull = (NotNull) mappa.get(tag);
//        }// end of if cycle
//
//        return notNull;
    }// end of static method


    /**
     * Get the message of the NotNull annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the specific message
     */
    @SuppressWarnings("all")
    public static String getNotNullMessage(Field reflectionField) {
        String message = "";
        NotNull notNull = getNotNull(reflectionField);
        String fieldName = LibText.primaMaiuscola(reflectionField.getName());
        fieldName = LibText.setRossoBold(fieldName);

        if (notNull != null) {
            message = notNull.message();
        }// end of if cycle

        if (message.equals("{javax.validation.constraints.NotNull.message}")) {
            message = fieldName + " non può essere nullo";
        }// end of if cycle

        return message;
    }// end of static method


    /**
     * Get the existence of the NotNull annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return true if the NotNull Annotation exists
     */
    @SuppressWarnings("all")
    public static boolean isNotNull(Field reflectionField) {
        boolean nonVuota = false;
        NotNull notNull = getNotNull(reflectionField);

        if (notNull != null) {
            nonVuota = true;
        }// end of if cycle

        return nonVuota;
    }// end of static method


    /**
     * Get the specific annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static Indexed getIndexed(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(Indexed.class);
        } else {
            return null;
        }// end of if/else cycle
//        Indexed indexed = null;
//        String tag = "Indexed";
//        Map mappa = getMap(clazz, publicFieldName);
//
//        if (mappa != null && mappa.containsKey(tag)) {
//            indexed = (Indexed) mappa.get(tag);
//        }// end of if cycle
//
//        return indexed;
    }// end of static method


    /**
     * Get the value of unique field of Indexed annotation
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return true if the Indexed Annotation exists and is true
     */
    @SuppressWarnings("all")
    public static boolean isUnico(Field reflectionField) {
        boolean unico = false;
        Indexed indexed = getIndexed(reflectionField);

        if (indexed != null) {
            unico = indexed.unique();
        }// end of if cycle

        return unico;
    }// end of static method


    /**
     * Get the class of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the class for the specific column
     */
    @SuppressWarnings("all")
    public static Class getClass(Field reflectionField) {
        Class linkClazz = null;
        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            linkClazz = fieldAnnotation.clazz();
        }// end of if cycle

        return linkClazz != Object.class ? linkClazz : null;
    }// end of static method


    /**
     * Get the specific annotation of the class.
     *
     * @param clazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public static AIList getAIList(final Class<? extends AEntity> clazz) {
        return clazz.getAnnotation(AIList.class);
    }// end of static method


    /**
     * Colonne visibili (e ordinate) nella Grid
     *
     * @param clazz the entity class
     *
     * @return lista di colonne visibili nella Grid
     */
    @SuppressWarnings("all")
    public static List<String> getListColumns(final Class<? extends AEntity> clazz) {
        List<String> lista = null;
        String[] columns = null;
        AIList listAnnotation = getAIList(clazz);

        if (listAnnotation != null) {
            columns = listAnnotation.columns();
        }// end of if cycle

        if (columns != null && columns.length > 0 && !columns[0].equals("")) {
            lista = Arrays.asList(columns);
        }// end of if cycle

        return lista;
    }// end of static method


    /**
     * Fields visibili (e ordinati) nel Form
     *
     * @param clazz the entity class
     *
     * @return lista di fields visibili nel Form
     */
    @SuppressWarnings("all")
    public static List<String> getFormFieldsName(final Class<? extends AEntity> clazz) {
        List<String> lista = null;
        String[] fields = null;
        AIForm formAnnotation = getFormAnnotation(clazz);

        if (formAnnotation != null) {
            fields = formAnnotation.fields();
        }// end of if cycle

        if (fields != null && fields.length > 0 && !fields[0].equals("")) {
            lista = Arrays.asList(fields);
        }// end of if cycle

        return lista;
    }// end of static method


    /**
     * Get the status listShowsID of the class.
     *
     * @param clazz the entity class
     *
     * @return status of class - default false
     */
    @SuppressWarnings("all")
    public static boolean isListShowsID(final Class<? extends AEntity> clazz) {
        boolean status = false;
        AIList listAnnotation = getAIList(clazz);

        if (listAnnotation != null) {
            status = listAnnotation.showsID();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the width of the property.
     *
     * @param clazz the entity class
     *
     * @return the width of the column expressed in int
     */
    @SuppressWarnings("all")
    public static int getListWidthID(final Class<? extends AEntity> clazz) {
        int width = 290;
        AIList listAnnotation = getAIList(clazz);

        if (listAnnotation != null) {
            width = listAnnotation.widthID();
        }// end of if cycle

        return width;
    }// end of static method


    /**
     * Get the specific annotation of the class.
     *
     * @param clazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public static AIForm getFormAnnotation(final Class<? extends AEntity> clazz) {
        return clazz.getAnnotation(AIForm.class);
    }// end of static method


    /**
     * Get the status formShowsID of the class.
     *
     * @param clazz the entity class
     *
     * @return status of class - default false
     */
    @SuppressWarnings("all")
    public static boolean isFormShowsID(final Class<? extends AEntity> clazz) {
        boolean status = false;
        AIForm formAnnotation = getFormAnnotation(clazz);

        if (formAnnotation != null) {
            status = formAnnotation.showsID();
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the width of the property.
     *
     * @param clazz the entity class
     *
     * @return the width of the field expressed in int
     */
    @SuppressWarnings("all")
    public static String getFormWithID(final Class<? extends AEntity> clazz) {
        String width = "";
        int widthInt = 0;
        String tag = "em";
        AIForm formAnnotation = getFormAnnotation(clazz);

        if (formAnnotation != null) {
            widthInt = formAnnotation.widthIDEM();
            width = widthInt + tag;
        }// end of if cycle

        return width;
    }// end of static method


    /**
     * Get the specific annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the Annotation for the specific field
     */
    @SuppressWarnings("all")
    public static DBRef getDBRef(Field reflectionField) {
        if (reflectionField != null) {
            return reflectionField.getAnnotation(DBRef.class);
        } else {
            return null;
        }// end of if/else cycle
//        DBRef dbRef = null;
//        String tag = "DBRef";
//        Map mappa = getMap(clazz, publicFieldName);
//
//        if (mappa != null && mappa.containsKey(tag)) {
//            dbRef = (DBRef) mappa.get(tag);
//        }// end of if cycle
//
//        return dbRef;
    }// end of static method

    /**
     * Get the existence of the DBRef annotation of the property.
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return true if the notEmpty Annotation exists
     */
    @SuppressWarnings("all")
    public static boolean isDBRef(Field reflectionField) {
        boolean usaDBRef = false;
        DBRef dbRef = getDBRef(reflectionField);

        if (dbRef != null) {
            usaDBRef = true;
        }// end of if cycle

        return usaDBRef;
    }// end of static method

    /**
     * Get the specific annotation of the class.
     *
     * @param clazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public static SpringView getSpringView(final Class<? extends View> clazz) {
        return clazz.getAnnotation(SpringView.class);
    }// end of static method


    /**
     * Get the name of the spring-view.
     *
     * @param clazz the entity class
     *
     * @return the name of the spring-view
     */
    @SuppressWarnings("all")
    public static String getNameView(final Class<? extends View> clazz) {
        String name = "";
        SpringView viewAnnotation = getSpringView(clazz);

        if (viewAnnotation != null) {
            name = viewAnnotation.name();
        }// end of if cycle

        return name;
    }// end of static method


    /**
     * Get the specific annotation of the class.
     *
     * @param clazz the entity class
     *
     * @return the Annotation for the specific class
     */
    public static AIEntity getAIEntity(final Class<? extends AEntity> clazz) {
        return clazz.getAnnotation(AIEntity.class);
    }// end of static method


    /**
     * Get the status companyNotNull of the class.
     *
     * @param clazz the entity class
     */
    @SuppressWarnings("all")
    public static ACompanyRequired companyType(final Class<? extends AEntity> clazz) {
        ACompanyRequired companyRequired = ACompanyRequired.nonUsata;
        AIEntity entityAnnotation = getAIEntity(clazz);

        if (entityAnnotation != null) {
            companyRequired = entityAnnotation.company();
        }// end of if cycle

        return companyRequired;
    }// end of static method

    /**
     * Get the status of use of ACompanyEntity.
     * Controlla se l'applicazione usa le company - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company
     *
     * @param clazz the entity class
     *
     * @return status - default true
     */
    @SuppressWarnings("all")
    public static boolean useCompany(final Class<? extends AEntity> clazz) {
        boolean status = true;

        if (!AlgosApp.USE_MULTI_COMPANY) {
            return false;
        }// end of if cycle

        if (LibAnnotation.companyType(clazz) == ACompanyRequired.nonUsata) {
            return false;
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status of visibility for the field of ACompanyEntity.
     * Controlla se l'applicazione usa le company - flag  AlgosApp.USE_MULTI_COMPANY=true
     * Controlla se la collection (table) usa la company
     * Controlla se l'buttonUser collegato è un developer
     *
     * @param clazz the entity class
     *
     * @return status - default true
     */
    @SuppressWarnings("all")
    public static boolean useCompanyFields(final Class<? extends AEntity> clazz) {
        boolean status = true;

        if (!AlgosApp.USE_MULTI_COMPANY) {
            return false;
        }// end of if cycle

        if (LibAnnotation.companyType(clazz) == ACompanyRequired.nonUsata) {
            return false;
        }// end of if cycle

        if (!LibSession.isDeveloper()) {
            return false;
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the status of visibility for the optional field of ACompanyEntity.
     * Definiti in Cost.COMPANY_OPTIONAL
     *
     * @param clazz the entity class
     *
     * @return status - default true
     */
    @SuppressWarnings("all")
    public static boolean useCompanyOptionalFields(final Class<? extends AEntity> clazz) {
        boolean status = true;

        if (!useCompanyFields(clazz)) {
            return false;
        }// end of if cycle

        if (LibAnnotation.companyType(clazz) == ACompanyRequired.facoltativa) {
            return false;
        }// end of if cycle

        if (LibAnnotation.companyType(clazz) == ACompanyRequired.facoltativa) {
            return false;
        }// end of if cycle

        return status;
    }// end of static method


    /**
     * Get the roleTypeVisibility of the property.
     * Se manca, usa il ruolo Guest
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the ARoleType of the field
     */
    @SuppressWarnings("all")
    public static ARoleType getFieldRoleType(Field reflectionField) {
        ARoleType roleTypeVisibility = ARoleType.guest;

        AIField fieldAnnotation = getFieldAnnotation(reflectionField);

        if (fieldAnnotation != null) {
            roleTypeVisibility = fieldAnnotation.roleTypeVisibility();
        }// end of if cycle

        return roleTypeVisibility;
    }// end of static method

    /**
     * Get the visibility of the field.
     * Controlla il ruolo dell'buttonUser connesso
     * Controlla il grado di accesso consentito
     * Di default true
     *
     * @param field reflectionField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFieldVisibile(Field reflectionField, boolean nuovaEntity) {
        boolean visibile = true;

        visibile = isFieldVisibileRole(reflectionField);
        if (visibile) {
            visibile = isFieldVisibileAccess(reflectionField, nuovaEntity);
        }// end of if cycle

        return visibile;
    }// end of static method

    /**
     * Get the visibility of the field.
     * Controlla il ruolo dell'buttonUser connesso
     *
     * @param field reflectionField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFieldVisibileRole(Field reflectionField) {
        boolean visibile = false;
        ARoleType roleTypeVisibility = getFieldRoleType(reflectionField);

        switch (roleTypeVisibility) {
            case nobody:
                visibile = false;
                break;
            case developer:
                if (LibSession.isDeveloper()) {
                    visibile = true;
                }// end of if cycle
                break;
            case admin:
                if (LibSession.isAdmin()) {
                    visibile = true;
                }// end of if cycle
                break;
            case user:
                visibile = true;
                break;
            case guest:
                visibile = true;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Get the visibility of the field.
     * Controlla il grado di accesso consentito
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFieldVisibileAccess(Field reflectionField, boolean nuovaEntity) {
        boolean visibile = true;
        FieldAccessibility fieldAccessibility = getFieldAccessibility(reflectionField);

        switch (fieldAccessibility) {
            case allways:
                visibile = true;
                break;
            case newOnly:
                visibile = true;
                break;
            case showOnly:
                visibile = true;
                break;
            case never:
                visibile = false;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Get the enabled state of the field.
     * Controlla la visibilità del field
     * Controlla il grado di accesso consentito
     * Di default true
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFieldEnabled(Field reflectionField, boolean nuovaEntity) {
        boolean enabled = true;
        boolean visibile = isFieldVisibileRole(reflectionField);

        if (visibile) {
            enabled = isFieldEnabledAccess(reflectionField, nuovaEntity);
        }// end of if cycle

        return enabled;
    }// end of static method


    /**
     * Get the enabled state of the field.
     * Controlla il grado di accesso consentito
     *
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFieldEnabledAccess(Field reflectionField, boolean nuovaEntity) {
        boolean enabled = true;
        FieldAccessibility fieldAccessibility = getFieldAccessibility(reflectionField);

        switch (fieldAccessibility) {
            case allways:
                enabled = true;
                break;
            case newOnly:
                enabled = nuovaEntity;
                break;
            case showOnly:
                enabled = false;
                break;
            case never:
                enabled = false;
                break;
            default:
                enabled = true;
                break;
        } // end of switch statement

        return enabled;
    }// end of static method

    /**
     * Get the roleTypeVisibility of the property.
     * Se manca, usa il ruolo Guest
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the ARoleType of the field
     */
    @SuppressWarnings("all")
    public static ARoleType getColumnRoleType(final Class<? extends AEntity> clazz, final String publicFieldName) {
        ARoleType roleTypeVisibility = ARoleType.guest;

        AIColumn fieldAnnotation = getColumn(clazz, publicFieldName);

        if (fieldAnnotation != null) {
            roleTypeVisibility = fieldAnnotation.roleTypeVisibility();
        }// end of if cycle

        return roleTypeVisibility;
    }// end of static method

    /**
     * Get the visibility of the column.
     * Di default true
     *
     * @param javaField to check
     *
     * @return the visibility of the column
     */
    @SuppressWarnings("all")
    public static boolean isColumnVisibile(Field javaField) {
        boolean visibile = false;
        ARoleType roleTypeVisibility = ARoleType.nobody;
        AIColumn annotation = javaField.getAnnotation(AIColumn.class);

        if (annotation != null) {
            roleTypeVisibility = annotation.roleTypeVisibility();
        }// end of if cycle

        switch (roleTypeVisibility) {
            case nobody:
                visibile = false;
                break;
            case developer:
                if (LibSession.isDeveloper()) {
                    visibile = true;
                }// end of if cycle
                break;
            case admin:
                if (LibSession.isAdmin()) {
                    visibile = true;
                }// end of if cycle
                break;
            case user:
                visibile = true;
                break;
            case guest:
                visibile = true;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Get the visibility of the field of the Form.
     * Di default true
     *
     * @param javaField to check
     *
     * @return the visibility of the field
     */
    @SuppressWarnings("all")
    public static boolean isFormFieldVisibile(Field javaField) {
        boolean visibile = false;
        ARoleType roleTypeVisibility = ARoleType.nobody;
        AIField annotation = javaField.getAnnotation(AIField.class);

        if (annotation != null) {
            roleTypeVisibility = annotation.roleTypeVisibility();
        }// end of if cycle

        switch (roleTypeVisibility) {
            case nobody:
                visibile = false;
                break;
            case developer:
                if (LibSession.isDeveloper()) {
                    visibile = true;
                }// end of if cycle
                break;
            case admin:
                if (LibSession.isAdmin()) {
                    visibile = true;
                }// end of if cycle
                break;
            case user:
                visibile = true;
                break;
            case guest:
                visibile = true;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Get the visibility of the column.
     * Di default true
     *
     * @param clazz           the entity class
     * @param publicFieldName the name of the property
     *
     * @return the visibility of the column
     */
    @Deprecated
    @SuppressWarnings("all")
    public static boolean isColumnVisibile(final Class<? extends AEntity> clazz, final String publicFieldName) {
        boolean visibile = false;
        ARoleType roleTypeVisibility = getColumnRoleType(clazz, publicFieldName);

        switch (roleTypeVisibility) {
            case nobody:
                visibile = false;
                break;
            case developer:
                if (LibSession.isDeveloper()) {
                    visibile = true;
                }// end of if cycle
                break;
            case admin:
                if (LibSession.isAdmin()) {
                    visibile = true;
                }// end of if cycle
                break;
            case user:
                visibile = true;
                break;
            case guest:
                visibile = true;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Get the roleTypeVisibility of the collection (modulo-table).
     * Se manca, usa il ruolo Guest
     *
     * @param clazz the entity class
     *
     * @return the ARoleType of the collection
     */
    @SuppressWarnings("all")
    public static ARoleType getEntityRoleType(final Class<? extends AEntity> clazz) {
        ARoleType roleTypeVisibility = ARoleType.guest;
        AIEntity entityAnnotation = getAIEntity(clazz);

        if (entityAnnotation != null) {
            roleTypeVisibility = entityAnnotation.roleTypeVisibility();
        }// end of if cycle

        return roleTypeVisibility;
    }// end of static method


    /**
     * Get the roleTypeVisibility of the collection (modulo-table).
     * Se manca, usa il ruolo Guest
     *
     * @param clazz the entity class
     *
     * @return the ARoleType of the collection
     */
    @SuppressWarnings("all")
    public static ARoleType getViewRoleType(final Class<? extends View> clazz) {
        ARoleType roleTypeVisibility = ARoleType.guest;
        AIEntity entityAnnotation = clazz.getAnnotation(AIEntity.class);

        if (entityAnnotation != null) {
            roleTypeVisibility = entityAnnotation.roleTypeVisibility();
        }// end of if cycle

        return roleTypeVisibility;
    }// end of static method


    /**
     * Get the visibility of the collection (modulo-table).
     * Di default true
     *
     * @param clazz the entity class
     *
     * @return the visibility of the collection
     */
    @SuppressWarnings("all")
    public static boolean isEntityClassVisibile(final Class<? extends AEntity> clazz) {
        boolean visibile = false;
        ARoleType roleTypeVisibility = getEntityRoleType(clazz);

        switch (roleTypeVisibility) {
            case nobody:
                visibile = false;
                break;
            case developer:
                if (LibSession.isDeveloper()) {
                    visibile = true;
                }// end of if cycle
                break;
            case admin:
                if (LibSession.isAdmin()) {
                    visibile = true;
                }// end of if cycle
                break;
            case user:
                visibile = true;
                break;
            case guest:
                visibile = true;
                break;
            default:
                visibile = true;
                break;
        } // end of switch statement

        return visibile;
    }// end of static method


    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static ListButton getListBotton(final Class<? extends AEntity> clazz) {
        ListButton listaNomi = ListButton.standard;

        if (LibSession.isDeveloper()) {
            listaNomi = getListBottonDev(clazz);
        } else {
            if (LibSession.isAdmin()) {
                listaNomi = getListBottonAdmin(clazz);
            } else {
                if (true) {
                    listaNomi = getListBottonUser(clazz);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return listaNomi;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static ListButton getListBottonDev(final Class<? extends AEntity> clazz) {
        ListButton listaNomi = null;
        AIList annotation = getAIList(clazz);

        if (annotation != null) {
            listaNomi = annotation.dev();
        }// end of if cycle

        return listaNomi;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static ListButton getListBottonAdmin(final Class<? extends AEntity> clazz) {
        ListButton listaNomi = null;
        AIList annotation = getAIList(clazz);

        if (annotation != null) {
            listaNomi = annotation.admin();
        }// end of if cycle

        return listaNomi;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static ListButton getListBottonUser(final Class<? extends AEntity> clazz) {
        ListButton listaNomi = null;
        AIList annotation = getAIList(clazz);

        if (annotation != null) {
            listaNomi = annotation.user();
        }// end of if cycle

        return listaNomi;
    }// end of static method


    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static FormButton getFormBotton(final Class<? extends AEntity> clazz) {
        FormButton listaNomi = FormButton.standard;

        if (LibSession.isDeveloper()) {
            listaNomi = getFormBottonDev(clazz);
        } else {
            if (LibSession.isAdmin()) {
                listaNomi = getFormBottonAdmin(clazz);
            } else {
                if (true) {
                    listaNomi = getFormBottonUser(clazz);
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return listaNomi;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static FormButton getFormBottonDev(final Class<? extends AEntity> clazz) {
        FormButton listaNomiBottoni = null;
        AIForm annotation = getFormAnnotation(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsDev();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static FormButton getFormBottonAdmin(final Class<? extends AEntity> clazz) {
        FormButton listaNomiBottoni = null;
        AIForm annotation = getFormAnnotation(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsAdmin();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of static method

    /**
     * Bottoni visibili nella toolbar
     *
     * @param clazz the entity class
     *
     * @return lista di bottoni visibili nella toolbar
     */
    @SuppressWarnings("all")
    public static FormButton getFormBottonUser(final Class<? extends AEntity> clazz) {
        FormButton listaNomiBottoni = null;
        AIForm annotation = getFormAnnotation(clazz);

        if (annotation != null) {
            listaNomiBottoni = annotation.buttonsUser();
        }// end of if cycle

        return listaNomiBottoni;
    }// end of static method

}// end of static class
