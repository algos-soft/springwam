package it.algos.springvaadin.entity.preferenza;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springvaadin.login.ARoleType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Created by gac on 16-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(Cost.TAG_PRE)
public interface PreferenzaRepository extends MongoRepository<Preferenza, String> {


    public Preferenza findByCode(String code);

    public Preferenza findByCompanyAndCode(Company company, String code);

    public List<Preferenza> findByOrderByOrdineAsc();

    public List<Preferenza> findByCompanyOrderByOrdineAsc(Company company);

    public List<Preferenza> findByCompanyAndLivelloOrderByOrdineAsc(Company company, ARoleType livello);

    public List<Preferenza> findTop1ByOrderByOrdineDesc();


}// end of class

