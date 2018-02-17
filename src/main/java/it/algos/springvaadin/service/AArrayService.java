package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:46
 */
@SpringComponent
@Scope("singleton")
public class AArrayService {


    /**
     * Libreria di servizio. Inietta da Spring come 'singleton'
     */
    @Autowired
    public ATextService text;


    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (size > 0)
     * Il primo elemento deve essere valido
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final List array) {
        boolean status = false;

        if (array != null && array.size() > 0) {
            if (array.get(0) != null) {
                if (array.get(0) instanceof String) {
                    status = text.isValid(array.get(0));
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (length > 0)
     * Il primo elemento deve essere una stringa valida
     *
     * @param array (String[]) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final String[] array) {
        boolean status = false;

        if (array != null && array.length > 0) {
            if (array[0] != null) {
                if (array[0] instanceof String) {
                    status = text.isValid((String) array[0]);
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method


    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final List array) {
        return !isValid(array);
    }// end of method


    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final String[] array) {
        return !isValid(array);
    }// end of method


    /**
     * Aggiunge un elemento ad una List (di per se immutabile)
     * Deve esistere (not null)
     *
     * @param arrayIn (List) ingresso da incrementare
     *
     * @return la lista aumentata di un elemento
     */
    public List add(final List arrayIn, Object obj) {
        List arrayOut = null;
        ArrayList lista = null;

        if (this.isValid(arrayIn)) {
            lista = new ArrayList(arrayIn);
            lista.add(obj);
            arrayOut = lista;
        }// end of if cycle

        return arrayOut;
    }// end of method

}// end of class
