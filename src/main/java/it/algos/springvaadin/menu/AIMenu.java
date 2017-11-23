package it.algos.springvaadin.menu;

import com.vaadin.icons.VaadinIcons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gac on 28/05/17.
 * Annotation to add some property for a menu item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in field only.
public @interface AIMenu {

    String label() default "menu";
    VaadinIcons icon() ;

}// end of interface annotation
