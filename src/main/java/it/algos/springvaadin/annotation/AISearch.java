package it.algos.springvaadin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceSearch (AISearch)
 * Annotation to add some property for a search dialog.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AISearch {


    /**
     * (Optional) Status (shows the ID property field) .
     * Defaults to false.
     */
    boolean showsID() default false;


    /**
     * (Optional) The width of the ID field.
     * Expressed in int, to be converted in String ending with "em"
     * Defaults to "16em".
     */
    int widthIDEM() default 16;


    /**
     * (Optional) List of visible fields on Form
     * Defaults to all.
     */
    String[] fields() default "";

}// end of interface annotation
