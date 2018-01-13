package it.algos.springwam.entity.funzione;

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
 * Date: 2018-01-13_17:06:34
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_FUN)
@AIScript(sovrascrivibile = false)
public interface FunzioneRepository extends MongoRepository<Funzione, String> {

    public Funzione findByCompanyAndCode(Company company, String code);

    public List<Funzione> findByOrderByCodeAsc();

    public List<Funzione> findByCompanyOrderByCodeAsc(Company company);

    public List<Funzione> findTop1ByCompanyOrderByOrdineDesc(Company company);

}// end of class
