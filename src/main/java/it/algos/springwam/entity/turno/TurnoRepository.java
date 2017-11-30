package it.algos.springwam.entity.turno;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springwam.entity.servizio.Servizio;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_TUR)
public interface TurnoRepository extends MongoRepository<Turno, String> {

    public Turno findByCompanyAndGiornoAndServizio(Company company, LocalDate giorno, Servizio servizio);

    public Turno findByCompanyAndServizioAndInizio(Company company, Servizio servizio, LocalDateTime inizio);

    public List<Turno> findByOrderByInizioAsc();

    public List<Turno> findByCompanyOrderByServizioAsc(Company company);

}// end of class
