package it.algos.springvaadin.entity.log;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 15-ott-2017
 * Time: 06:26
 */
@Slf4j
public enum LogLevel {
    debug, info, warn, error, mail;

    public static String[] getValues() {
        String[] valori;
        LogLevel[] types = values();
        valori = new String[values().length];

        for (int k = 0; k < types.length; k++) {
            valori[k] = types[k].toString();
        }// end of for cycle

        return valori;
    }// end of static method

}// end of enum class
