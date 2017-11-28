package it.algos.springwam.entity.servizio;

import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.funzione.Funzione;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.company.Company;

import java.util.List;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_SER)
public interface ServizioRepository extends MongoRepository<Servizio, String> {

    public Servizio findByCompanyAndCode(Company company, String code);

    public List<Servizio> findByOrderByCompanyAscOrdineAsc();

    public List<Servizio> findByCompanyOrderByOrdineAsc(Company company);

    public List<Servizio> findByCompanyAndVisibileOrderByOrdineAsc(Company company, boolean visible);

    public List<Servizio> findTop1ByCompanyOrderByOrdineDesc(Company company);

}// end of class
