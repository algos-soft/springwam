package it.algos.springwam.entity.funzione;

import it.algos.springvaadin.entity.company.Company;
import it.algos.springwam.application.AppCost;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springwam.entity.croce.Croce;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Created by gac on 24-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_FUN)
public interface FunzioneRepository extends MongoRepository<Funzione, String> {

    public Funzione findById(String idKey);

    public Funzione findByCompanyAndCode(Company company, String code);

    public List<?> findCodeByCompanyOrderByOrdineAsc(Company company);

    public List<Funzione> findByOrderByCompanyAscOrdineAsc();

    public List<Funzione> findByCompanyOrderByOrdineAsc(Company company);

    public List<Funzione> findTop1ByCompanyOrderByOrdineDesc(Company company);

}// end of class
