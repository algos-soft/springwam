package it.algos.springwam.entity.milite;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-31_15:21:40
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@SpringComponent
@Qualifier(AppCost.TAG_MIL)
@AIScript(sovrascrivibile = false)
public interface MiliteRepository extends MongoRepository<Milite, String> {

    public Milite findByNickname(String nickname);

    public Milite findByCompanyAndNickname(Company company, String nickname);

    public Milite findByCompanyAndNomeAndCognome(Company company, String nome, String cognome);

    public List<Milite> findByCompanyOrderByCognomeAsc(Company company);

    public List<Milite> findByOrderByCognomeAsc();

}// end of class
