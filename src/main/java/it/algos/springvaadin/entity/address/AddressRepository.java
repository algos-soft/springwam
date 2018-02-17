package it.algos.springvaadin.entity.address;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.lib.ACost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-17_11:23:32
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(ACost.TAG_ADD)
@AIScript(sovrascrivibile = false)
public interface AddressRepository extends MongoRepository<Address, String> {
}// end of class
