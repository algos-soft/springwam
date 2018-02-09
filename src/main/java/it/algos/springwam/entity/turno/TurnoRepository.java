package it.algos.springwam.entity.turno;

import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.entity.company.Company;

import java.time.LocalDate;
import java.util.List;

import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(AppCost.TAG_TUR)
@AIScript(sovrascrivibile = false)
public interface TurnoRepository extends MongoRepository<Turno, String> {

    public List<Turno> findByCompanyAndGiorno(Company company, LocalDate giorno);

    public List<Turno> findByCompanyAndGiornoIsGreaterThanEqual(Company company, LocalDate giorno);

    public List<Turno> findTop42ByCompanyAndGiornoIsGreaterThanEqual(Company company, LocalDate giorno);

}// end of class
