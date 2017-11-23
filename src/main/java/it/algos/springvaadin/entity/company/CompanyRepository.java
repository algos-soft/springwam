package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 * La classe concreta, che implementa tutti i metodi, viene creata da Spring a Runtime
 */
@SpringComponent
@Qualifier(Cost.TAG_COMP)
public interface CompanyRepository extends MongoRepository<Company, String> {

    public Company findByCode(String code);

    public List<Company> findByOrderByCodeAsc();

}// end of class
