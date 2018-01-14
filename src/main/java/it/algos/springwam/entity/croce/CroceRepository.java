package it.algos.springwam.entity.croce;

import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import it.algos.springvaadin.annotation.*;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_22:57:46
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 */
@SpringComponent
@Qualifier(AppCost.TAG_CRO)
@AIScript(sovrascrivibile = false)
public interface CroceRepository extends MongoRepository<Croce, String> {

    public Croce findByCode(String code);

    public List<Croce> findByOrderByCodeAsc();

}// end of class
