package it.algos.springvaadin.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class AlgosConverter implements Converter<String, String> {

    @Override
    public Result<String> convertToModel(String s, ValueContext valueContext) {
        return null;
    }// end of method

    @Override
    public String convertToPresentation(String value, ValueContext valueContext) {
        return value;
    }// end of method

}// end of class
