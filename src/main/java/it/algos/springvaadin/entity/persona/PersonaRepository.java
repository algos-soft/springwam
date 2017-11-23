package it.algos.springvaadin.entity.persona;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Created by gac on 11-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PER)
public interface PersonaRepository extends MongoRepository<Persona, String> {

    public List<Persona> findByOrderByCognome();

    public List<Persona> findByCompanyOrderByCognomeAsc(Company company);

}// end of class
