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
 * Time: 14:52
 * Controlla che il campo (IntegerField) non sia nullo
 * Un valore zero viene controllato da un altro validator
 */
@Slf4j
public class AlgosNumberNotNullValidator extends AbstractValidator<Integer> {


    public AlgosNumberNotNullValidator(String fieldName) {
        super(LibText.setRossoBold(LibText.primaMaiuscola(fieldName)) + " non può essere " + LibText.setRossoBold("null"));
    }// end of constructor


    @Override
    public ValidationResult apply(Integer value, ValueContext valueContext) {
        return this.toResult(value, value != null);
    }// end of method


}// end of validator class
