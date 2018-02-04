package it.algos.springwam.entity.servizio;

import com.vaadin.spring.annotation.SpringComponent;
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
 * Date: 2018-01-16_08:50:45
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_SER)
@AIScript(sovrascrivibile = false)
public interface ServizioRepository extends MongoRepository<Servizio, String> {

    public Servizio findByCompanyAndCode(Company company, String code);

    public List<Servizio> findByOrderByCodeAsc();

    public List<Servizio> findByCompanyOrderByOrdineAsc(Company company);

    public List<Servizio> findTop1ByCompanyOrderByOrdineDesc(Company company);

}// end of class
