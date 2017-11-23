package it.algos.springvaadin.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 17:33
 */
public class DuplicateCodeCompanyException extends CompanyException {

    private final static String MESSAGE_INI = "Esiste già un code=";
    private final static String MESSAGE_END = " per questa company (deve essere unico)";

    /**
     * Constructor for NullCompanyException.
     */
    public DuplicateCodeCompanyException(String code) {
        super(MESSAGE_INI + code + MESSAGE_END);
    }// fine del costruttore

}// end of class
