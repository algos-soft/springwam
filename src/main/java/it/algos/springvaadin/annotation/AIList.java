package it.algos.springvaadin.annotation;

import it.algos.springvaadin.list.ListButton;
import it.algos.springvaadin.login.ARoleType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceList (AIList)
 * Annotation to add some property for a single collection or list.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIList {


    /**
     * (Optional) Status (shows the ID property as column) .
     * Defaults to false.
     */
    boolean showsID() default false;


    /**
     * (Optional) The width of the ID column.
     * Defaults to 270
     */
    int widthID() default 290;


    /**
     * (Optional) List of visible columns on Grid
     * Defaults to all.
     */
    String[] columns() default "";


    /**
     * (Optional) List of buttom on bottom
     * Specific for developer role
     * Defaults to standard.
     */
    ListButton dev() default ListButton.standard;

    /**
     * (Optional) List of buttom on bottom
     * Specific for buttonAdmin role
     * Defaults to standard.
     */
    ListButton admin() default ListButton.noSearch;

    /**
     * (Optional) List of buttom on bottom
     * Specific for buttonUser role
     * Defaults to standard.
     */
    ListButton user() default ListButton.edit;


}// end of interface annotation
