package it.algos.springvaadin.lib;

import com.vaadin.server.Resource;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.AEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by gac on 21/12/16.
 * .
 */
@Slf4j
public abstract class LibReflection {


    /**
     * Fields dichiarati nella Entity
     *
     * @param entityClazz da cui estrarre i fields
     *
     * @return lista di fields da considerare per List e Form
     */
    @SuppressWarnings("all")
    public static List<Field> getFields(Class<? extends AEntity> entityClazz) {
        return getFields(entityClazz, (List) null, true, true);
    }// end of static method


    /**
     * Fields dichiarati nella Entity
     *
     * @param entityClazz   da cui estrarre i fields
     * @param addKeyID      flag per aggiungere (per primo) il field keyId
     * @param addKeyCompany flag per aggiungere (per secondo) il field keyCompany
     *
     * @return lista di fields da considerare per List e Form
     */
    @Deprecated
    @SuppressWarnings("all")
    public static List<Field> getFields(Class<? extends AEntity> entityClazz, boolean addKeyID, boolean addKeyCompany) {
        return getFields(entityClazz, (List) null, addKeyID, addKeyCompany);
    }// end of static method


    /**
     * Fields per le Columns della List (Grid) dichiarati nella Entity
     * Compresa la entity
     * Comprese tutte le superclassi (fino a ACompanyEntity e AEntity)
     *
     * @param entityClazz   da cui estrarre i fields
     * @param listaNomi     dei fields da considerare. Tutti, se listaNomi=null
     * @param addKeyID      flag per aggiungere (per primo) il field keyId
     * @param addKeyCompany flag per aggiungere (per secondo) il field keyCompany
     *
     * @return lista di fields da considerare per List
     */
    @SuppressWarnings("all")
    public static List<Field> getListColumns(Class<? extends AEntity> entityClazz, List<String> listaNomi, boolean addKeyCompany) {
        ArrayList<Field> fieldsList = new ArrayList<>();
        boolean addKeyID = LibAnnotation.isListShowsID(entityClazz);
        List<Field> fieldsTmp = getFields(entityClazz, listaNomi, addKeyID, addKeyCompany);

        for (Field field : fieldsTmp) {
            if (LibAnnotation.isColumnVisibile(field)) {
                fieldsList.add(field);
            }// end of if cycle
        }// end of for cycle

        return fieldsList;
    }// end of static method


    /**
     * Fields per le Campi del Form dichiarati nella Entity
     * Compresa la entity
     * Comprese tutte le superclassi (fino a ACompanyEntity e AEntity)
     *
     * @param entityClazz   da cui estrarre i fields
     * @param listaNomi     dei fields da considerare. Tutti, se listaNomi=null
     * @param addKeyID      flag per aggiungere (per primo) il field keyId
     * @param addKeyCompany flag per aggiungere (per secondo) il field keyCompany
     *
     * @return lista di fields da considerare per Form
     */
    @SuppressWarnings("all")
    public static List<Field> getFormFields(Class<? extends AEntity> entityClazz, List<String> listaNomi, boolean addKeyCompany) {
        ArrayList<Field> fieldsList = new ArrayList<>();
        boolean addKeyID = LibAnnotation.isFormShowsID(entityClazz);
        List<Field> fieldsTmp = getFields(entityClazz, listaNomi, addKeyID, addKeyCompany);

        for (Field field : fieldsTmp) {
            if (LibAnnotation.isFormFieldVisibile(field)) {
                fieldsList.add(field);
            }// end of if cycle
        }// end of for cycle

        return fieldsList;
    }// end of static method

    /**
     * Fields dichiarati nella Entity
     * Compresa la entity
     * Comprese tutte le superclassi (fino a ACompanyEntity e AEntity)
     *
     * @param entityClazz   da cui estrarre i fields
     * @param listaNomi     dei fields da considerare. Tutti, se listaNomi=null
     * @param addKeyID      flag per aggiungere (per primo) il field keyId
     * @param addKeyCompany flag per aggiungere (per secondo) il field keyCompany
     *
     * @return lista di fields da considerare per List e Form
     */
    @SuppressWarnings("all")
    public static List<Field> getFields(Class<? extends AEntity> entityClazz, List<String> listaNomi, boolean addKeyID, boolean addKeyCompany) {
        ArrayList<Field> fieldsList = new ArrayList<>();
        Class<?> clazz = entityClazz;
        ArrayList<Field> fieldsTmp = new ArrayList<>();
        Field[] fieldsArray = null;
        Field fieldKeyId = null;
        Field fieldCompany = null;
        Field fieldOrdine = null;
        String fieldName;
        boolean useCompany = LibAnnotation.useCompanyFields(entityClazz);
        boolean useCompanyOptional = LibAnnotation.useCompanyOptionalFields(entityClazz);

        //--recupera tutti i fields della entity e di tutte le superclassi
        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                fieldsArray = clazz.getDeclaredFields();
                for (Field field : fieldsArray) {
                    if (field.getName().equals(Cost.PROPERTY_ID)) {
                        fieldKeyId = field;
                    }// end of if cycle
                    if (field.getName().equals(Cost.PROPERTY_COMPANY)) {
                        fieldCompany = field;
                    }// end of if cycle
                    if (field.getName().equals(Cost.PROPERTY_ORDINE)) {
                        fieldOrdine = field;
                    }// end of if cycle
                    if (!Cost.ESCLUSI.contains(field.getName())) {
                        fieldsTmp.add(field);
                    }// end of if cycle
                }// end of for cycle
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        //--controlla che i fields siano quelli richiesti
        //--se la lista dei nomi dei fields è nulla, li prende tutti
        //--controlla che i field siano visibili per il livello di developer/buttonAdmin/buttonUser corrente
        if (fieldsTmp != null && fieldsTmp.size() > 0) {
            if (listaNomi != null && listaNomi.size() > 0) {
                for (String nome : listaNomi) {
                    for (Field field : fieldsTmp) {
                        fieldName = field.getName();
                        if (LibText.isValid(fieldName) && !fieldName.equals(Cost.PROPERTY_SERIAL)) {
                            if (fieldName.equals(nome)) {
                                fieldsList.add(field);
                            }// end of if cycle
                        }// end of if cycle
                    }// end of for cycle
                }// end of for cycle
            } else {
                for (Field field : fieldsTmp) {
                    fieldName = field.getName();
                    if (LibText.isValid(fieldName) && !fieldName.equals(Cost.PROPERTY_SERIAL)) {
                        fieldsList.add(field);
                    }// end of if cycle
                }// end of for cycle
            }// end of if/else cycle
        }// end of if cycle

        //--se la entity è di tipo ACompanyEntity, aggiunge (all'inizio) il field di riferimento
        //--se esiste il field ''ordine'', company viene messo dopo ordine
        if (addKeyCompany && ACompanyEntity.class.isAssignableFrom(entityClazz)) {
            if (fieldCompany != null) {
                if (fieldOrdine != null) {
                    fieldsList.add(fieldsList.indexOf(fieldOrdine) + 1, fieldCompany);
                } else {
                    fieldsList.add(0, fieldCompany);
                }// end of if/else cycle
            } else {
                log.warn("Non ho trovato il field company");
            }// end of if/else cycle
        }// end of if cycle

        //--se il flag booleano addKeyID è true, aggiunge (all'inizio) il field keyId
        if (addKeyID || LibSession.isDeveloper()) {
            if (fieldKeyId != null) {
                fieldsList.add(0, fieldKeyId);
            } else {
                log.error("Non ho trovato il field keyId");
            }// end of if/else cycle
        }// end of if cycle

        return fieldsList;
    }// end of static method


    /**
     * All field names di una EntityClass
     *
     * @param entityClazz su cui operare la riflessione
     * @param useID       per comprendere anche il field key ID
     * @param useCompany  per comprendere anche il field company
     *
     * @return tutte i fieldNames, elencati in ordine di inserimento nella AEntity
     */
    public static List<String> getListVisibleColumnNames(final Class<? extends AEntity> entityClazz, boolean useID, boolean useCompany) {
        List<String> listaColonneVisibili = null;
        List<Field> fieldsList = null;

        fieldsList = getFields(entityClazz, useID, useCompany);
        if (fieldsList != null && fieldsList.size() > 0) {
            listaColonneVisibili = new ArrayList();
            for (Field field : fieldsList) {
                if (!listaColonneVisibili.contains(field.getName())) {
                    if (LibAnnotation.isColumnVisibile(field)) {
                        listaColonneVisibili.add(field.getName());
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaColonneVisibili;
    }// end of static method

    /**
     * All editable field names di una EntityClass
     *
     * @param entityClazz su cui operare la riflessione
     * @param useCompany  per comprendere anche il field company
     *
     * @return tutte i fieldNames editabili, elencati in ordine di inserimento nella AEntity
     */
    public static List<String> getAllEnabledFieldNames(final Class<? extends AEntity> entityClazz, boolean useCompany) {
        List<String> nameList = new ArrayList();
        List<Field> fieldList = getFormFields(entityClazz, null, useCompany);
//        List<String> nameListTmp = getListVisibleColumnNames(entityClazz, false, useCompany);

        for (Field field : fieldList) {
            if (LibAnnotation.isFieldEnabled(field, false)) {
                nameList.add(field.getName());
            }// end of if cycle

        }// end of for cycle
//        for (String publicFieldName : nameListTmp) {
//            if (LibAnnotation.isFieldEnabled(entityClazz, publicFieldName, false)) {
//                nameList.add(publicFieldName);
//            }// end of if cycle
//        }// end of for cycle

        return nameList;
    }// end of static method


    /**
     * All field names di una EntityClass
     *
     * @param entityClazz su cui operare la riflessione
     *
     * @return tutte i fieldNames, elencati in ordine di inserimento nella AEntity
     */
    public static List<String> getListVisibleColumnNames(final Class<? extends AEntity> entityClazz) {
        return getListVisibleColumnNames(entityClazz, false, false);
    }// end of static method


    /**
     * Valore della property statica di una classe
     *
     * @param clazz           classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    public static Resource getPropertyRes(final Class<?> clazz, final String publicFieldName) {
        Resource value = null;
        Object obj = getPropertyValus(clazz, publicFieldName);

        if (obj != null && obj instanceof Resource) {
            value = (Resource) obj;
        }// end of if cycle

        return value;
    }// end of static method

    /**
     * Valore statico del metodo 'getButtonMenu' di una view attiva
     *
     * @param clazz            classe su cui operare la riflessione
     * @param publicMethodName metodo statico e pubblico
     */
    public static Object getMethodButton(final Class<?> clazz, String publicMethodName) {
        Object value = null;
        Method method;

        try { // prova ad eseguire il codice
            method = clazz.getDeclaredMethod(publicMethodName);
            value = method.invoke(null);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return value;
    }// end of static method


    /**
     * Valore della property statica di una classe
     *
     * @param clazz           classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    public static Object getPropertyValus(final Class<?> clazz, final String publicFieldName) {
        Object value = "";

        try { // prova ad eseguire il codice
            value = clazz.getDeclaredField(publicFieldName).get(null);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return value;
    }// end of static method


    /**
     * Property statica di una classe
     * Controlla ricorsivamente in TUTTE le superclassi della entity
     *
     * @param entityClazz     classe su cui operare la riflessione
     * @param publicFieldName property statica e pubblica
     */
    public static Field getField(final Class<?> entityClazz, final String publicFieldName) {
        Class<?> clazz = entityClazz;

        while (clazz != Object.class) {
            try { // prova ad eseguire il codice
                return clazz.getDeclaredField(publicFieldName);
            } catch (Exception unErrore) { // intercetta l'errore
                log.warn(unErrore.toString() + " getField()");
            }// fine del blocco try-catch
            clazz = clazz.getSuperclass();
        }// end of while cycle

        return null;
    }// end of method


    /**
     * Properties di una entity. Tutte.
     * Compresa la entity
     * Compresa la superclasse AEntity
     *
     * @param entity da esaminare
     *
     * @return tutte le properties, anche della superclasse, elencate in ordine alfabetico
     */
    public static List<String> getAllPropertyNames(AEntity entity) {
        List<String> propertyNames = null;
        String[] stringArray;

        BeanPropertySqlParameterSource bean = new BeanPropertySqlParameterSource(entity);
        stringArray = bean.getReadablePropertyNames();
        propertyNames = LibArray.fromString(stringArray);

        return propertyNames;
    }// end of static method


    /**
     * Properties di una entity. Solo i campi dichiarati.
     * Compresa la entity
     * Compresa la superclasse AEntity
     * Escluso 'callbacks'
     * Escluso 'class'
     *
     * @param entity da esaminare
     *
     * @return properties, elencate in ordine alfabetico
     */
    public static List<String> getPropertyNames(AEntity entity) {
        List<String> propertyNames = null;
        List<String> propertyNamesAll = getAllPropertyNames(entity);

        if (propertyNamesAll != null && propertyNamesAll.size() > 0) {
            propertyNames = new ArrayList();
            for (String property : propertyNamesAll) {
                if (!property.equals("callbacks") && !property.equals("class")) {
                    propertyNames.add(property);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return propertyNames;
    }// end of static method


    /**
     * Table di una entity. Inserita (opzionale) come Annotation.
     *
     * @param entity su cui operare la riflessione
     *
     * @return tableName dichiarato nella Annotation @Table
     */
    public static String getTable(AEntity entity) {
        return getTable(entity.getClass());
    }// end of static method


    /**
     * Table di una classe entity. Inserita (opzionale) come Annotation.
     *
     * @param entityClazz su cui operare la riflessione
     *
     * @return tableName dichiarato nella Annotation @Table
     */
    public static String getTable(Class<? extends AEntity> entityClazz) {
        String tableName = "";
        Table table = entityClazz.getAnnotation(Table.class);

        if (table != null) {
            tableName = table.name();
        }// end of if cycle

        return tableName;
    }// end of static method


    /**
     * Metodo getter di una classe entity.
     *
     * @param entity       da esaminare
     * @param propertyName per estrarre il metodo
     *
     * @return method pubblico
     */
    public static Method getMethod(AEntity entity, String propertyName) {
        return getMethod(entity.getClass(), propertyName);
    }// end of static method


    /**
     * Metodo getter di una classe entity.
     *
     * @param entityClazz  su cui operare la riflessione
     * @param propertyName per estrarre il metodo
     *
     * @return method pubblico
     */
    public static Method getMethod(Class<? extends AEntity> entityClazz, String propertyName) {
        Method method = null;
        String methodNameGet = "get" + LibText.primaMaiuscola(propertyName);
        String methodNameIs = "is" + LibText.primaMaiuscola(propertyName);

        try { // prova ad eseguire il codice
            method = entityClazz.getMethod(methodNameGet);
        } catch (Exception unErrore) { // intercetta l'errore
            try { // prova ad eseguire il codice
                method = entityClazz.getMethod(methodNameIs);
            } catch (Exception unErrore2) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco try-catch

        return method;
    }// end of static method


    /**
     * Metodi getter di una classe entity.
     *
     * @param entity da esaminare
     *
     * @return tutti i metodi getter,  elencati in ordine di inserimento nella AEntity
     */
    public static List<Method> getMethods(AEntity entity) {
        return getMethods(entity.getClass());
    }// end of static method

    /**
     * Metodi getter di una classe entity.
     *
     * @param entityClazz su cui operare la riflessione
     *
     * @return tutti i metodi getter, elencati in ordine di inserimento nella AEntity
     */
    public static List<Method> getMethods(Class<? extends AEntity> entityClazz) {
        List<Method> methods = null;
        List<String> propertyNames = getListVisibleColumnNames(entityClazz, false, false);

        if (propertyNames != null && propertyNames.size() > 0) {
            methods = new ArrayList();
            for (String name : propertyNames) {
                methods.add(getMethod(entityClazz, name));
            }// end of for cycle
        }// end of if cycle

        return methods;
    }// end of static method


    /**
     * Mappatura dei valori di una entity.
     * Recupera le property
     * Aggiunge la key property
     * Recupera i valori
     *
     * @param entity da esaminare
     *
     * @return mappa dei valori attuali della entity, in ordine alfabetico
     */
    @Deprecated
    public static LinkedHashMap<String, Object> getBeanMap(AEntity entity) {
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        Method method = null;
        List<String> propertiesName = getAllPropertyNames(entity);
        propertiesName.add(Cost.PROPERTY_ID);
        Object value = null;

        for (String property : propertiesName) {
            method = getMethod(entity, property);
            if (method != null) {
                try { // prova ad eseguire il codice
                    value = method.invoke(entity);
                    map.put(property, value);
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }// fine del blocco try-catch
            }// end of if cycle
        }// end of for cycle

        return map;
    }// end of static method


    /**
     * Array dei valori validi di una entity
     *
     * @param entity da esaminare
     *
     * @return arry dei valori, in ordine alfabetico delle chiavi della mappa
     */
    @Deprecated
    public static Object[] getValues(AEntity entity) {
        Object[] args = null;
        LinkedHashMap<String, Object> mappa = LibReflection.getBeanMap(entity);

        if (mappa != null && mappa.size() > 0) {
            args = LibReflection.getValues(mappa);
        }// end of if cycle

        return args;
    }// end of static method


    /**
     * Array dei valori validi di una mappa
     *
     * @param mappa da esaminare
     *
     * @return arry dei valori, in ordine alfabetico delle chiavi della mappa
     */
    @Deprecated
    public static Object[] getValues(LinkedHashMap<String, Object> mappa) {
        ArrayList lista = new ArrayList();
        Object value;

        if (mappa != null && mappa.size() > 0) {
            for (String property : mappa.keySet()) {
                value = mappa.get(property);
                if (value != null) {
                    lista.add(value);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return lista.toArray();
    }// end of static method


    /**
     * Valore di una property della entity.
     *
     * @param entityBean      da esaminare
     * @param publicFieldName da esaminare
     *
     * @return valore della property
     */
    public static Object getValue(AEntity entityBean, String publicFieldName) {
        Object value = null;
        LinkedHashMap<String, Object> mappa = getBeanMap(entityBean);

        if (mappa != null && mappa.size() > 0) {
            if (mappa.containsKey(publicFieldName)) {
                value = mappa.get(publicFieldName);
            }// end of if cycle
        }// end of if cycle

        return value;
    }// end of static method

    /**
     * Valore di una property linkata della entity.
     *
     * @param entityBean      da esaminare
     * @param publicFieldName da esaminare
     *
     * @return valore della property
     */
    public static AEntity getValueLink(AEntity entityBean, String publicFieldName) {
        AEntity value = null;
        Object valueObj = getValue(entityBean, publicFieldName);

        if (valueObj != null && valueObj instanceof AEntity) {
            value = (AEntity) valueObj;
        }// end of if cycle


        return value;
    }// end of static method


//    /**
//     * Get all the annotation of the property.
//     *
//     * @param attr the metamodel Attribute
//     */
//    @SuppressWarnings("all")
//    public static AIField getFieldAnnotation(final Class<? extends AEntity> clazz, final String publicFieldName) {
//        AIField fieldAnnotation = null;
//        Annotation annotation = null;
//        Field javaField = null;
//        Object[] items = null;
//        javaField = LibReflection.getField(clazz, publicFieldName);
//
//        if (javaField != null) {
//            annotation = javaField.getAnnotation(AIField.class);
//        }// end of if cycle
//
//        if (annotation != null && annotation instanceof AIField) {
//            fieldAnnotation = (AIField) annotation;
//        }// end of if cycle
//
//        return fieldAnnotation;
//    }// end of static method
//
}// end of static class
