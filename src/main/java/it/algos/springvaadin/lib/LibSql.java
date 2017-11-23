package it.algos.springvaadin.lib;

import it.algos.springvaadin.entity.AEntity;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gac on 04/07/17
 */
public class LibSql {

    private static String SPACE = " ";
    private static String VIRGOLA = ",";
    private static String UGUALE = "=";
    private static String INTERROGATIVO = "?";

    /**
     * Get the query string for INSERT
     */
    @Deprecated
    public static String getQueryInsert(String tableName, AEntity entity) {
        String query = "";

//        LinkedHashMap<String, Object> map = this.getBeanMap(entityBean);
//        String campi = StringUtils.join(map.keySet(), ",");
//        String valori = LibText.repeat("?", ",", map.size());
//        campi = LibText.setTonde(campi);
//        valori = "values" + LibText.setTonde(valori);

//        query += "INSERT INTO";
//        query += space;
//        query += tableName;
//        query += space;
//        query += campi;
//        query += space;
//        query += valori;

        return query;
    }// end of static method


    /**
     * Get the query string for INSERT
     */
    @Deprecated
    public static String getQueryInsert(String tableName, Collection keySet) {
        String query = "";

        String campi = StringUtils.join(keySet);
        String valori = LibText.repeat("?", ",", keySet.size());
        campi = LibText.setTonde(campi);
        valori = "values" + LibText.setTonde(valori);

        query += "INSERT INTO";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += campi;
        query += SPACE;
        query += valori;

        return query;
    }// end of static method


    /**
     * Get the query string for COUNT
     *
     * @param tableName da conteggiare
     *
     * @return stringa della query
     */
    public static String getQueryCount(String tableName) {
        String query = "";

        query += "SELECT COUNT(*) FROM";
        query += SPACE;
        query += tableName;

        return query;
    }// end of static method


    /**
     * Get the query string for SELECT ALL entities
     *
     * @param tableName da cui estrarre le entities
     *
     * @return stringa della query
     */
    public static String getQueryFindAll(String tableName) {
        String query = "";

        query += "SELECT * FROM";
        query += SPACE;
        query += tableName;

        return query;
    }// end of static method


    /**
     * Get the query string for SELECT one entity
     *
     * @param tableName da cui estrarre la entity
     *
     * @return stringa della query
     */
    public static String getQueryFindOne(String tableName) {
        String query = "";

        query += "SELECT * FROM";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += "WHERE id=?";

        return query;
    }// end of static method


    /**
     * Get the query string for INSERT
     *
     * @param tableName   in cui inserire la nuova entity
     * @param columnsName delle property
     *
     * @return stringa della query
     */
    public static String getQueryInsert(String tableName, List<String> columnsName) {
        return getQueryInsert(tableName, columnsName.toArray(new String[columnsName.size()]));
    }// end of static method


    /**
     * Get the query string for INSERT
     *
     * @param tableName   in cui inserire la nuova entity
     * @param columnsName delle property
     *
     * @return stringa della query
     */
    public static String getQueryInsert(String tableName, String[] columnsName) {
        String query = "";

        String campi = "";
        for (String column : columnsName) {
            campi += column;
            campi += VIRGOLA;
            campi += SPACE;
        }// end of for cycle
        campi = LibText.levaCoda(campi, VIRGOLA);
        campi = LibText.setTonde(campi);

        query += "INSERT INTO";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += campi;
        query += SPACE;
        query += repeatValue(columnsName.length);

        return query;
    }// end of static method


    /**
     * Get the query string for UPDATE
     *
     * @param entityBean da modificare
     *
     * @return stringa della query
     */
    @Deprecated
    public static String getQueryUpdate(AEntity entityBean) {
        return getQueryUpdate(LibReflection.getTable(entityBean), entityBean);
    }// end of static method

    /**
     * Get the query string for UPDATE
     *
     * @param tableName  in cui modificare la entity
     * @param entityBean da modificare
     *
     * @return stringa della query
     */
    @Deprecated
    public static String getQueryUpdate(String tableName, AEntity entityBean) {
        return getQueryUpdate(tableName, getAllFieldNameWithValidValue(entityBean));
    }// end of static method


    /**
     * All field names di una Entity, with a valid value (not null)
     *
     * @param entityBean su cui operare la riflessione
     *
     * @return tutte i fieldNames validi, elencati in ordine alfabetico
     */
    private static List<String> getAllFieldNameWithValidValue(final AEntity entityBean) {
        ArrayList lista = new ArrayList();
        List<String> allEsistenti = LibReflection.getListVisibleColumnNames(entityBean.getClass());
        Object value;

        for (String name : allEsistenti) {
            value = LibReflection.getValue(entityBean, name);

            if (value != null) {
                lista.add(name);
            }// end of if cycle
        }// end of for cycle

        return lista;
    }// end of static method

    /**
     * Get the query string for UPDATE
     *
     * @param tableName   in cui modificare la entity
     * @param columnsName delle property
     *
     * @return stringa della query
     */
    public static String getQueryUpdate(String tableName, String[] columnsName) {
        return getQueryUpdate(tableName, LibArray.fromString(columnsName));
    }// end of static method


    /**
     * Get the query string for UPDATE
     *
     * @param tableName   in cui modificare la entity
     * @param columnsName delle property
     *
     * @return stringa della query
     */
    public static String getQueryUpdate(String tableName, List<String> columnsName) {
        String query = "";

        String campi = "";
        for (String column : columnsName) {
            campi += column;
            campi += UGUALE;
            campi += INTERROGATIVO;
            campi += VIRGOLA;
            campi += SPACE;
        }// end of for cycle
        campi = LibText.levaCoda(campi, VIRGOLA);

        query += "UPDATE";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += "SET";
        query += SPACE;
        query += campi;
        query += SPACE;
        query += "WHERE id=?";

        return query;
    }// end of static method


    /**
     * Get the query string for DELETE
     *
     * @param tableName in cui cancellare la entity
     *
     * @return stringa della query
     */
    public static String getQueryDelete(String tableName) {
        String query = "";

        query += "DELETE FROM";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += "WHERE id=?";

        return query;
    }// end of static method


    /**
     * Get the max value for propertyName (numeric)
     *
     * @param tableName    da esaminare
     * @param propertyName da esaminare
     *
     * @return stringa della query
     */
    public static String getQueryMax(String tableName, String propertyName) {
        String query = "";

        query += "SELECT * FROM";
        query += SPACE;
        query += tableName;
        query += SPACE;
        query += "ORDER BY";
        query += SPACE;
        query += propertyName;
        query += SPACE;
        query += "DESC LIMIT 1";

        return query;
    }// end of static method


    /**
     * Ripete n volte una stringa, con un separatore
     *
     * @param stringaBase    da ripetere
     * @param separatore     tra le ripetizioni
     * @param numRipetizioni della stringa base
     *
     * @return stringaBase ripetuta n volte, con n-1 separatori
     */
    public static String repeat(String stringaBase, String separatore, int numRipetizioni) {
        String stringaOut = "";

        for (int k = 0; k < numRipetizioni - 1; k++) {
            stringaOut += stringaBase;
            stringaOut += separatore;
        }// end of for cycle
        stringaOut += stringaBase;

        return stringaOut;
    }// end of static method


    /**
     * Ripete n volte una stringaBase='?', con un separatore=','
     * Aggiunge il tag VALUES
     * Inserisce il risultato dentro due parentesi tonde
     *
     * @param numRipetizioni del tag base
     *
     * @return stringaBase ripetuta n volte, con n-1 separatori; inserita dentro due parentesi tonde
     */
    public static String repeatValue(int numRipetizioni) {
        String stringaBase = INTERROGATIVO;
        String separatore = VIRGOLA;

        String stringaOut = repeat(stringaBase, separatore, numRipetizioni);

        return "VALUES " + LibText.setTonde(stringaOut);
    }// end of static method

}// end of static class
