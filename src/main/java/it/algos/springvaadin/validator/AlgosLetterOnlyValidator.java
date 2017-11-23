package it.algos.springvaadin.validator;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import it.algos.springvaadin.lib.LibText;

/**
 * Controlla che il campo (TextField) contenga solo caratteri alfabetici e non cifre
 */
public class AlgosLetterOnlyValidator extends AbstractValidator<String> {


    private String fieldName;

    public AlgosLetterOnlyValidator(String fieldName) {
        super("");
        this.fieldName = fieldName;
    }// end of constructor


    @Override
    public ValidationResult apply(String value, ValueContext valueContext) {
        boolean valido = true;
        char[] caratteri = value.toCharArray();

        for (char car : caratteri) {
            if (isNotNumber(car)) {
                valido = false;
            }// end of if cycle
        }// end of for cycle

        return this.toResult(value, valido);
    }// end of method


    private boolean isNotNumber(char ch) {
        return !isNumber(ch);
    }// end of method


    private boolean isNumber(char ch) {
        return ch >= 'A' && ch <= 'z';
    }// end of method

    @Override
    protected String getMessage(String value) {
        String tagIni = " accetta solo lettere ed il valore ";
        String tagEnd = " non va bene";
        String name = LibText.primaMaiuscola(fieldName);
        name = LibText.setRossoBold(name);
        value = LibText.setRossoBold(value);
        return name + tagIni + value + tagEnd;
    }// end of method

}// end of class
