package it.algos.springvaadin.validator;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import it.algos.springvaadin.lib.LibText;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 21-ott-2017
 * Time: 15:10
 */
@Slf4j
public class AlgosEmailValidator extends RegexpValidator {

    //--accetta anche la stringa vuota
    private static final String PATTERN = "(^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$)?";

    public AlgosEmailValidator(String fieldName) {
        super(
                LibText.setRossoBold(LibText.primaMaiuscola(fieldName)) + " doesn't look like a valid email address",
                PATTERN,
                true);
    }// end of constructor

}// end of class
