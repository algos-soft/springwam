package it.algos.springvaadin.service;

import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.LocalDateRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.renderer.ByteStringRenderer;
import it.algos.springvaadin.renderer.IconRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 09-dic-2017
 * Time: 11:42
 */
@SpringComponent
@Scope("singleton")
public class AColumnService {


    @Autowired
    public ATextService text;

    @Autowired
    public AAnnotationService annotation;

    @Autowired
    public AReflectionService reflection;

    public final static int WIDTH_CHECK_BOX = 65;
    public final static int WIDTH_ICON = 90;
    public final static int WIDTH_TEXT_SMALL = 80;
    public final static int WIDTH_TEXT_NORMAL = 100;
    public final static int WIDTH_TEXT_LARGE = 120;
    public final static int WIDTH_BYTE = 160;
    public final static int WIDTH_LOCAL_DATE = 160;
    public final static int WIDTH_LOCAL_DATE_TIME = 200;


//    public AIField getFieldAnnotation(final Class<? extends AEntity> clazz, String publicFieldName) {
//        return reflection.getField(clazz, publicFieldName).getAnnotation(AIField.class);
//    }// end of method


    /**
     * Aggiunge e regola una colonna
     * Nella Grid le colonne possono essere di due tipi: Text o Component
     * Caption, renderer e width
     * Regola la larghezza della Grid dopo ogni aggiunta di una nuova colonna
     *
     * @param grid                a cui aggiungere la colonna
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     *
     * @return la colonna appena creata
     */
    public Grid.Column add(final Grid grid, final Field reflectionJavaField) {
        Grid.Column colonna = null;
        EAFieldType type = annotation.getColumnType(reflectionJavaField);

        //--aggiunge una colonna di tipo Text per tutti i type, eccetto quelli indicati in questo switch statement
        //--le colonna 'saltate' qui, dovranno essere inserite come Component nella sotttoclasse xxxList.addColonna()
        switch (type) {
            case checkbox:
            case checkboxlabel:
                colonna = grid.addColumn(reflectionJavaField.getName());
                this.add(grid, reflectionJavaField, type, colonna);
                break;
            case noone:
                break;
            default:
                colonna = grid.addColumn(reflectionJavaField.getName());
                this.add(grid, reflectionJavaField, type, colonna);
                break;
        } // end of switch statement

        return colonna;
    }// end of method


    /**
     * Aggiunge e regola una colonna
     * Caption, renderer e width
     * Regola la larghezza della Grid dopo ogni aggiunta di una nuova colonna
     *
     * @param grid                a cui aggiungere la colonna
     * @param reflectionJavaField di riferimento per estrarre la Annotation
     * @param type                di field
     * @param colonna             di aggiungere
     */
    public void add(final Grid grid, final Field reflectionJavaField, EAFieldType type, Grid.Column colonna) {
        float larGrid = 0;
        int larCol = 0;
        String caption = annotation.getColumnName(reflectionJavaField);
        int width = annotation.getColumnWith(reflectionJavaField);

        DateRenderer render = new DateRenderer("%1$te-%1$tb-%1$tY", Locale.ITALIAN);
        LocalDateRenderer renderDate = new LocalDateRenderer("d-MMM-u", Locale.ITALIAN);
        LocalDateTimeRenderer renderTime = new LocalDateTimeRenderer("d-MMM-uu HH:mm", Locale.ITALIAN);
        ByteStringRenderer renderByte = new ByteStringRenderer();

        //--regola la  colonna appena creata
        switch (type) {
            case checkbox:
                width = width > 0 ? width : WIDTH_CHECK_BOX;
                break;
            case localdate:
                colonna.setRenderer(renderDate);
                width = width > 0 ? width : WIDTH_LOCAL_DATE;
                break;
            case localdatetime:
                colonna.setRenderer(renderTime);
                width = width > 0 ? width : WIDTH_LOCAL_DATE_TIME;
                break;
            case icon:
//            colonna.setRenderer(renderIcon);
//            colonna.setWidth(WIDTH_TEXT_NORMAL);
                break;
            case json:
                colonna.setRenderer(renderByte);
                width = width > 0 ? width : WIDTH_BYTE;
                break;
            default:
                width = width > 0 ? width : WIDTH_TEXT_NORMAL;
                break;
        } // end of switch statement
        colonna.setWidth(width);
        colonna.setCaption(caption);

        larGrid = grid.getWidth();
        larCol = ((Double) colonna.getWidth()).intValue();
        grid.setWidth(larGrid + larCol, Sizeable.Unit.PIXELS);
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
    public int regolaAnnotationAndGetLarghezza(Grid.Column colonna, Field reflectionField) {
        String caption = annotation.getColumnName(reflectionField);
        EAFieldType type = annotation.getColumnType(reflectionField);
        int width = annotation.getColumnWith(reflectionField);

        DateRenderer render = new DateRenderer("%1$te-%1$tb-%1$tY", Locale.ITALIAN);
        LocalDateRenderer renderDate = new LocalDateRenderer("d-MMM-u", Locale.ITALIAN);
        LocalDateTimeRenderer renderTime = new LocalDateTimeRenderer("d-MMM-uu HH:mm", Locale.ITALIAN);
        IconRenderer renderIcon = new IconRenderer();
        ByteStringRenderer renderByte = new ByteStringRenderer();

        colonna.setCaption(caption);
        colonna.setWidth(width > 0 ? width : WIDTH_TEXT_NORMAL);

        if (type == EAFieldType.localdate) {
            colonna.setRenderer(renderDate);
            colonna.setWidth(WIDTH_LOCAL_DATE);
        }// end of if cycle

        if (type == EAFieldType.localdatetime) {
            colonna.setRenderer(renderTime);
            colonna.setWidth(WIDTH_LOCAL_DATE_TIME);
        }// end of if cycle

        if (type == EAFieldType.icon) {
            colonna.setRenderer(renderIcon);
            colonna.setWidth(WIDTH_TEXT_NORMAL);
        }// end of if cycle

        if (type == EAFieldType.json) {
            colonna.setRenderer(renderByte);
            colonna.setWidth(WIDTH_BYTE);
        }// end of if cycle

        if (caption.equals(ACost.PROPERTY_ID)) {
            colonna.setWidth(290);
        }// end of if cycle

        return ((Double) colonna.getWidth()).intValue();
    }// end of method


}// end of class
