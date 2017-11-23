package it.algos.springwam.entity.iscrizione;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.utente.Utente;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_ISC)
public interface IscrizioneRepository extends MongoRepository<Iscrizione, String> {

    public Iscrizione findByTurnoAndUtente(Turno turno, Utente utente);

    public List<Iscrizione> findByOrderByTurnoAsc();

}// end of class
