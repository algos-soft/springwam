package it.algos.springwam.entity.croce;

import it.algos.springvaadin.entity.company.CompanyRepository;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 31-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_CRO)
public interface CroceRepository extends MongoRepository<Croce, String> {

    public Croce findByCode(String code);

    public List<Croce> findByOrderByCodeAsc();


}// end of class
