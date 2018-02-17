package it.algos.springvaadin.entity.persona;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-14_06:46:57
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(ACost.TAG_PER)
@AIScript(sovrascrivibile = false)
public interface PersonaRepository extends MongoRepository<Persona, String> {

    public Persona findByNome(String nickname);
    public Persona findByCompanyAndNome(Company company, String nickname);

}// end of class
