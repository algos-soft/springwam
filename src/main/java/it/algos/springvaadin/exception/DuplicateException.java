package it.algos.springvaadin.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mer, 17-gen-2018
 * Time: 11:41
 */
@Slf4j
public class DuplicateException extends RuntimeException {

    public final static String MESSAGE = "Entity non creata perché ne esiste già un'altra ed alcune property devono essere uniche";

    /**
     * Constructor for NullCompanyException.
     */
    public DuplicateException() {
        super(MESSAGE);
    }// fine del costruttore


}// end of class
