package it.algos.springvaadin.entity.log;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.event.AEvent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.entity.company.Company;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(ACost.TAG_LOG)
public interface LogRepository extends MongoRepository<Log, String> {

    public Log findByCompanyAndEvento(Company company, LocalDateTime evento);

    public List<Log> findByOrderByEventoAsc();

    public List<Log> findByCompanyOrderByEventoAsc(Company company);

}// end of class
