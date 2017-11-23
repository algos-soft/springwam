package it.algos.springvaadin.app;

import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.field.AField;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-set-2017
 * Time: 14:45
 */
@Configuration
public class AlgosConfiguration {


    @Bean
    public Function<Class<? extends AButton>, AButton> BottoneFactory() {
        return obj-> getBottone(obj);
    }// end of method


    @Bean
    @Scope(value = "prototype")
    AButton getBottone(Class<? extends AButton> clazz) {
        AButton obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }// end of method


    @Bean
    public Function<Class<? extends AField>, AField> FieldFactory() {
        return obj -> getField(obj);
    }// end of method

    @Bean
    @Scope(value = "prototype")
    AField getField(Class<? extends AField> clazz) {
        AField obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }// end of method

}// end of class

