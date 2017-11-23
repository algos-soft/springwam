package it.algos.springvaadin.annotation;

import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.login.ARoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gac on 05 ott 2016.
 * AlgosInterfaceColumn (AIColumn)
 * Annotation to add some property for a single column of the Grid.
 * Alcune property sono in comune con AIField
 * Se nell'annotation AIColumn manca una property ,
 * si prende il valore della corrispondente property di AIField
 * (se esiste, altrimenti il valore di default)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in field only.
public @interface AIColumn {


    /**
     * (Required) The type of the field.
     * Se manca (valore di default), prende quello indicato in AIField
     * Se manca anche in AIField, prende il valore di default di AIField
     */
    AFieldType type() default AFieldType.ugualeAlField;

    /**
     * (Optional) The name of the field.
     * Defaults to the property or field name.
     */
    String name() default "";

    /**
     * (Optional) The width of the field.
     * Defaults to 150
     */
    int width() default 150;


    /**
     * (Optional) Visibilità a secondo del ruolo dell'User collegato
     * Defaults to guest.
     */
    ARoleType roleTypeVisibility() default ARoleType.guest;

}// end of interface annotation