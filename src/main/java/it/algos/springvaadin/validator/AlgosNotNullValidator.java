package it.algos.springvaadin.validator;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import it.algos.springvaadin.lib.LibText;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: sab, 21-ott-2017
 * Time: 14:05
 * Controlla che il campo non sia nullo
 */
@Slf4j
public class AlgosNotNullValidator extends AbstractValidator<String> {

    private String fieldName;

    public AlgosNotNullValidator(String fieldName) {
        super("");
        this.fieldName = fieldName;
    }// end of constructor

    @Override
    public ValidationResult apply(String value, ValueContext valueContext) {
        boolean valido = true;
        if (value == null) {
            return this.toResult(value, false);
        }// end of if cycle

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
        return ch >= '0' && ch <= '9';
    }// end of method

    @Override
    protected String getMessage(String value) {
        String tagIni = " non può essere ";
        String name = LibText.primaMaiuscola(fieldName);
        name = LibText.setRossoBold(name);
        value = LibText.setRossoBold(value);

        return name + tagIni + value;
    }// end of method

}// end of validator class
