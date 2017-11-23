package it.algos.springwam.entity.utente;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_UTE)
public interface UtenteRepository extends MongoRepository<Utente, String> {

    public Utente findByCompanyAndNomeAndCognome(Company company, String nome, String cognome);

    public List<Utente> findByOrderByCognomeAsc();

    public List<Utente> findByCompanyOrderByCognomeAsc(Company company);

}// end of class
