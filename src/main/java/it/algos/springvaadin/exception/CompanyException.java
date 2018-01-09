package it.algos.springvaadin.exception;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 15:52
 */
public abstract class CompanyException extends RuntimeException {

    /**
     * Constructor for CompanyException.
     *
     * @param msg the detail message
     */
    public CompanyException(String msg) {
        super(msg);
    }// fine del costruttore

    /**
     * Constructor for NullCompanyException.
     *
     * @param msg   the detail message
     * @param cause the root cause from the data access API in use
     */
    public CompanyException(String msg, Throwable cause) {
        super(msg, cause);
    }// fine del costruttore

}// end of abstract class
