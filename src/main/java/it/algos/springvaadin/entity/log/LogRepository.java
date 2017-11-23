package it.algos.springvaadin.entity.log;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Created by gac on 30-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_LOG)
public interface LogRepository extends MongoRepository<Log, String> {


    public List<Log> findByOrderByEventoDesc();

    public List<Log> findByCompanyOrderByEventoDesc(Company company);

}// end of class
