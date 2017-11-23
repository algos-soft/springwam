package it.algos.springvaadin.validator;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.RangeValidator;
import com.vaadin.data.validator.StringLengthValidator;

/**
 * Accetta il valore vuoto
 * Controlla che il campo sia nell'intervallo di lunghezze previsto solo se NON vuoto
 */
public class AlgosStringLengthValidator extends StringLengthValidator {
    private final RangeValidator<Integer> validator;

    public AlgosStringLengthValidator(String errorMessage, Integer minLength, Integer maxLength) {
        super(errorMessage, minLength, maxLength);
        this.validator = RangeValidator.of(errorMessage, minLength, maxLength);
    }// end of constructor


    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.equals("")) {
            return this.toResult(value, true);
        } else {
            ValidationResult lengthCheck = this.validator.apply(value.length(), context);
            return this.toResult(value, !lengthCheck.isError());
        }
    }// end of method

}// end of class
