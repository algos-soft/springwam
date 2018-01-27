package it.algos.springvaadin.entity.log;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
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
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(ACost.TAG_LOG)
@AIScript(sovrascrivibile = false)
public interface LogRepository extends MongoRepository<Log, String> {

    public Log findByCompanyAndEvento(Company company, LocalDateTime evento);

    public List<Log> findByOrderByEventoDesc();

    public List<Log> findByCompanyOrderByEventoDesc(Company company);

    public List<Log> findByCompanyOrderByEventoAsc(Company company);

}// end of class
