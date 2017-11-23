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
 * Time: 14:33
 * Controlla che il campo (IntegerField) non contenga il valore zero
 * Un valore vuoto viene controllato da un altro validator
 */
@Slf4j
public class AlgosNumberNotZeroValidator extends AbstractValidator<Integer> {


    public AlgosNumberNotZeroValidator(String fieldName) {
        super(LibText.setRossoBold(LibText.primaMaiuscola(fieldName)) + " non può essere uguala a " + LibText.setRossoBold("zero"));
    }// end of constructor


    @Override
    public ValidationResult apply(Integer value, ValueContext valueContext) {
        return this.toResult(value, value > 0);
    }// end of method


}// end of validator class
