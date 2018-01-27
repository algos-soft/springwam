package it.algos.springwam.entity.utente;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.user.User;
import it.algos.springwam.entity.croce.Croce;
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
 * Date: 2018-01-16_10:27:41
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@SpringComponent
@Qualifier(AppCost.TAG_UTE)
@AIScript(sovrascrivibile = false)
public interface UtenteRepository extends MongoRepository<Utente, String> {

    public Utente findByNickname(String nickname);

    public Utente findByCompanyAndNickname(Company company, String nickname);

    public List<Utente> findByCompanyOrderByCognomeAsc(Company company);

    public List<Utente> findByOrderByCognomeAsc();

}// end of class
