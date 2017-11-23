package it.algos.springvaadin.lib;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.*;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.AIColumn;
import it.algos.springvaadin.annotation.AIField;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.label.LabelRosso;
import it.algos.springvaadin.label.LabelVerde;
import it.algos.springvaadin.renderer.IconRenderer;
import it.algos.springvaadin.renderer.ByteStringRenderer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * Created by gac on 14/06/17
 * .
 */
public abstract class LibColumn {


    public final static int WIDTH_CHECK_BOX = 65;
    public final static int WIDTH_ICON = 90;
    public final static int WIDTH_TEXT_SMALL = 80;
    public final static int WIDTH_TEXT_NORMAL = 100;
    public final static int WIDTH_TEXT_LARGE = 120;
    public final static int WIDTH_BYTE = 160;
    public final static int WIDTH_LOCAL_DATE = 160;
    public final static int WIDTH_LOCAL_DATE_TIME = 200;


//    public static Annotation getAnnotation(Field field) {
//        return field.getAnnotation(AIColumn.class);
//    }// end of method


    @Deprecated
    public static AIColumn getColumnAnnotation(final Class<? extends AEntity> clazz, String publicFieldName) {
        Field field = LibReflection.getField(clazz, publicFieldName);

        if (publicFieldName.equals(Cost.PROPERTY_ID)) {
            return null;
        } else {
            if (field != null) {
                return field.getAnnotation(AIColumn.class);
            } else {
                return null;
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    public static AIField getFieldAnnotation(final Class<? extends AEntity> clazz, String publicFieldName) {
        return LibReflection.getField(clazz, publicFieldName).getAnnotation(AIField.class);
    }// end of method


    /**
     * Aggiunge una colonna
     *
     * @param grid           a cui aggiungere la colonna
     * @param reflectedField di riferimento per estrarre le Annotation
     *
     * @return la colonna appena creata
     */
    public static Grid.Column add(Grid grid, Field reflectedField) {
        Grid.Column colonna = null;
        AFieldType type = LibAnnotation.getColumnType(reflectedField);

        switch (type) {
            case checkbox:
//                 colonna = grid.addComponentColumn(servizio -> {
//                     Component comp = null;
//                     boolean orario = ((Servizio) servizio).isOrario();
//                     if (orario) {
//                         comp = new LabelVerde(VaadinIcons.CHECK);
//                     } else {
//                         comp = new LabelRosso(VaadinIcons.CLOSE);
//                     }// end of if/else cycle
//                     return comp;
//                 });//end of lambda expressions
                colonna = grid.addColumn(reflectedField.getName());
                break;
            default:
                colonna = grid.addColumn(reflectedField.getName());
                break;
        } // end of switch statement

        return colonna;
    }// end of method

    /**
     * Regola una colonna
     * Caption, renderer e width
     * Restituisce la larghezza dopo le regolazioni
     *
     * @param colonna         appena costruita, da regolare se ci sono Annoattion diverse dallo standard
     * @param reflectionField di riferimento per estrarre le Annotation
     *
     * @return la larghezza della colonna come regolata
     */
    public static int regolaAnnotationAndGetLarghezza(Grid.Column colonna, Field reflectionField) {
        String caption = LibAnnotation.getColumnName(reflectionField);
        AFieldType type = LibAnnotation.getColumnType(reflectionField);
        int width = LibAnnotation.getColumnWith(reflectionField);

        DateRenderer render = new DateRenderer("%1$te-%1$tb-%1$tY", Locale.ITALIAN);
        LocalDateRenderer renderDate = new LocalDateRenderer("d-MMM-u", Locale.ITALIAN);
        LocalDateTimeRenderer renderTime = new LocalDateTimeRenderer("d-MMM-uu HH:mm", Locale.ITALIAN);
        IconRenderer renderIcon = new IconRenderer();
        ByteStringRenderer renderByte = new ByteStringRenderer();

        colonna.setCaption(caption);
        colonna.setWidth(width > 0 ? width : WIDTH_TEXT_NORMAL);

        if (type == AFieldType.localdate) {
            colonna.setRenderer(renderDate);
            colonna.setWidth(WIDTH_LOCAL_DATE);
        }// end of if cycle

        if (type == AFieldType.localdatetime) {
            colonna.setRenderer(renderTime);
            colonna.setWidth(WIDTH_LOCAL_DATE_TIME);
        }// end of if cycle

        if (type == AFieldType.icon) {
            colonna.setRenderer(renderIcon);
            colonna.setWidth(WIDTH_TEXT_NORMAL);
        }// end of if cycle

        if (type == AFieldType.json) {
            colonna.setRenderer(renderByte);
            colonna.setWidth(WIDTH_BYTE);
        }// end of if cycle

        if (caption.equals(Cost.PROPERTY_ID)) {
            colonna.setWidth(290);
        }// end of if cycle

        return ((Double) colonna.getWidth()).intValue();
    }// end of method


//    /**
//     * Aggiunge una colonna
//     * Se ci sono Annotazioni, la regola
//     */
//    public static int addColumn(final Class<? extends AEntity> clazz, Grid grid, String publicFieldName) {
//        AIColumn columnAnnotation = getColumnAnnotation(clazz, publicFieldName);
//        Grid.Column colonna = null;
//        AFieldType type = LibAnnotation.getTypeColumn(clazz, publicFieldName);
//        String name = LibAnnotation.getNameColumn(clazz, publicFieldName);
//        int width = LibAnnotation.getColumnWith(clazz, publicFieldName);
//
//        DateRenderer render = new DateRenderer("%1$te-%1$tb-%1$tY", Locale.ITALIAN);
//        LocalDateRenderer renderDate = new LocalDateRenderer("d-MMM-u", Locale.ITALIAN);
//        LocalDateTimeRenderer renderTime = new LocalDateTimeRenderer("d-MMM-uu HH:mm", Locale.ITALIAN);
//        IconRenderer renderIcon = new IconRenderer();
//        ByteStringRenderer renderByte = new ByteStringRenderer();
//
//        if (grid == null || LibText.isEmpty(publicFieldName)) {
//            return 0;
//        }// end of if cycle
//
//        colonna = grid.addColumn(publicFieldName);
//        colonna.setCaption(name);
//        colonna.setWidth(width > 0 ? width : WIDTH_TEXT_NORMAL);
//
//        if (type == AFieldType.localdate) {
//            colonna.setRenderer(renderDate);
//            colonna.setWidth(WIDTH_LOCAL_DATE);
//        }// end of if cycle
//
//        if (type == AFieldType.localdatetime) {
//            colonna.setRenderer(renderTime);
//            colonna.setWidth(WIDTH_LOCAL_DATE_TIME);
//        }// end of if cycle
//
//        if (type == AFieldType.icon) {
//            colonna.setRenderer(renderIcon);
//            colonna.setWidth(WIDTH_TEXT_NORMAL);
//        }// end of if cycle
//
//        if (type == AFieldType.json) {
//            colonna.setRenderer(renderByte);
//            colonna.setWidth(WIDTH_BYTE);
//        }// end of if cycle
//
//        if (columnAnnotation == null && publicFieldName.equals(Cost.PROPERTY_ID)) {
//            colonna.setWidth(LibAnnotation.getListWidthID(clazz));
//        }// end of if cycle
//
//        return ((Double) colonna.getWidth()).intValue();
//    }// end of method


//    /**
//     * Aggiunge le colonne
//     * Se ci sono Annotazioni, le regola
//     */
//    public static int addColumns(final Class<? extends AEntity> clazz, Grid grid, List<String> columns) {
//        int lar = 0;
//        grid.removeAllColumns();
//
//        if (columns != null) {
//            for (String titolo : columns) {
//                if (LibAnnotation.isColumnVisibile(clazz, titolo)) {
//                    lar += addColumn(clazz, grid, titolo);
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return lar;
//    }// end of method

//    /**
//     * Aggiunge tutte le colonne
//     * Se ci sono Annotazioni, le regola
//     */
//    public static int addColumns(final Class<? extends AEntity> beanType, Grid grid) {
//        return addColumns(beanType, grid, LibReflection.getListVisibleColumnNames(beanType, false, false));
//    }// end of method

//    /**
//     * Aggiunge le colonne
//     * Se ci sono Annotazioni, le regola
//     */
//    public static int addColumns(Grid grid, List<String> colonneVisibili) {
//
//        for (String titolo : colonneVisibili) {
//            grid.addColumn(titolo);
//        }// end of for cycle
//
//        return 400;
////        return addColumns(grid.getBeanType(), grid, LibReflection.getAllPropertyName(grid.getBeanType()));
//    }// end of method

//    /**
//     * Aggiunge tutte le colonne
//     * Se ci sono Annotazioni, le regola
//     */
//    public static int addColumns(Grid grid) {
//        return addColumns(grid, LibReflection.getListVisibleColumnNames(grid.getBeanType()));
//    }// end of method

}// end of static class
