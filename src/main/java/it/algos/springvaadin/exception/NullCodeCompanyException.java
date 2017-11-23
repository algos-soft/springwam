package it.algos.springvaadin.exception;

import it.algos.springvaadin.entity.AEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 16:50
 */
public class NullCodeCompanyException extends CompanyException {

    private final static String MESSAGE = "Manca il codeCompanyUnico";

    /**
     * Constructor for NullCompanyException.
     */
    public NullCodeCompanyException() {
        super(MESSAGE);
    }// fine del costruttore

}// end of class
