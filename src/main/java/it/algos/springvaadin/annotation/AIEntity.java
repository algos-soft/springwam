package it.algos.springvaadin.annotation;

import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.login.ARoleType;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 14:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIEntity {

    /**
     * (Optional) Visibilità a secondo del ruolo dell'User collegato
     * Defaults to guest.
     */
    ARoleType roleTypeVisibility() default ARoleType.guest;

    ACompanyRequired company() default ACompanyRequired.nonUsata;
}// end of interface annotation
