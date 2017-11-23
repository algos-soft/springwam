package it.algos.springvaadin.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import it.algos.springvaadin.lib.LibText;

public class UpperConverter extends AlgosConverter{

    @Override
    public Result<String> convertToModel(String value, ValueContext valueContext) {
        return Result.ok(value.toUpperCase());
    }// end of method

}// end of class

