package it.algos.springvaadin.exception;

import it.algos.springvaadin.entity.AEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 16:05
 */
public class NotCompanyEntityException extends CompanyException {

    private final static String MESSAGE_INI = "La classe ";
    private final static String MESSAGE_END = " non estende ACompanyEntity";

    /**
     * Constructor for NullCompanyException.
     */
    public NotCompanyEntityException(Class<? extends AEntity> clazz) {
        super(MESSAGE_INI + clazz.getSimpleName() + MESSAGE_END);
    }// fine del costruttore

}// end of class
