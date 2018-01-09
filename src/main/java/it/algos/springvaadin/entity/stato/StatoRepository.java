package it.algos.springvaadin.entity.stato;
import com.vaadin.spring.annotation.SpringComponent;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.ACost;

import java.util.List;

/**
 * Created by gac on TIMESTAMP
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 */
@SpringComponent
@Qualifier(ACost.TAG_STA)
public interface StatoRepository extends MongoRepository<Stato, String> {

    public Stato findByNome(String nome);

    public List<Stato> findByOrderByOrdineAsc();

    public List<Stato> findTop1ByOrderByOrdineDesc();

}// end of class
