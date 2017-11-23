package it.algos.springvaadin.validator;

import com.mongodb.MongoClient;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.stato.StatoService;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Controlla che il valore del campo unico non esista già
 */
public class AlgosUniqueValidator extends AbstractValidator<String> {

    private Class<? extends AEntity> entityClazz;
    private String fieldName;
    private String dbName;
    private Object oldValue;

    private MongoClient mongo;
    private MongoOperations mongoOps;

    private String tagIni = " deve essere unico ed il valore ";
    private String tagEnd = " esiste già ";


    public AlgosUniqueValidator(final Class<? extends AEntity> entityClazz, String fieldName, Object oldValue) {
        super("");
        this.entityClazz = entityClazz;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.dbName = entityClazz.getSimpleName().toLowerCase();

        String dataBase="springvaadin";
        mongo = new MongoClient("localhost", 27017);
        mongoOps = new MongoTemplate(mongo, dataBase);
    }// end of constructor


    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.equals("") || value.equals(oldValue)) {
            return this.toResult(value, true);
        } else {
            if (esiste(value)) {
                return this.toResult(value, false);
            } else {
                return this.toResult(value, true);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    private boolean esiste(String value) {
        AEntity entity = null;

        try { // prova ad eseguire il codice
            entity = mongoOps.findOne(
                    new Query(Criteria.where(fieldName).is(value)),
                    entityClazz, dbName);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return entity != null;
    }// end of method


    @Override
    protected String getMessage(String value) {
        String name = LibText.primaMaiuscola(fieldName);
        name = LibText.setRossoBold(name);
        value = LibText.setRossoBold(value);
        return name + tagIni + value + tagEnd;
    }// end of method

}// end of class
