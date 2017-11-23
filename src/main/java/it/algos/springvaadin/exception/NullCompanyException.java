package it.algos.springvaadin.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 15:51
 */
public class NullCompanyException extends CompanyException {

    private final static String MESSAGE = "Entity non creata perché manca la company che è obbligatoria";

    /**
     * Constructor for NullCompanyException.
     */
    public NullCompanyException() {
        super(MESSAGE);
    }// fine del costruttore


}// end of class
