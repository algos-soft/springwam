package it.algos.springvaadin.entity.role;

import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 11-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_ROL)
public interface RoleRepository extends MongoRepository<Role, String> {

    public Role findByCode( String code);

    public List<Role> findByOrderByOrdineAsc();

}// end of class
