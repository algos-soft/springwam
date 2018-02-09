package it.algos.springwam.entity.iscrizione;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springwam.entity.turno.Turno;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.entity.company.Company;
import java.util.List;
import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:23:11
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(AppCost.TAG_ISC)
@AIScript(sovrascrivibile = false)
public interface IscrizioneRepository extends MongoRepository<Iscrizione, String> {

}// end of class
