package it.algos.springvaadin.entity.versione;

import com.vaadin.data.provider.Sort;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.Company;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import it.algos.springvaadin.lib.Cost;

import java.util.List;

/**
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@SpringComponent
@Qualifier(Cost.TAG_VERS)
public interface VersioneRepository extends MongoRepository<Versione, String> {

    public Versione findByProgettoAndOrdine(String progetto, int ordine);

    public List<Versione> findByOrderByProgetto();

    public List<Versione> findByCompanyOrderByOrdineAsc(Company company);

    public List<Versione> findTop1ByProgettoOrderByOrdineDesc(String progetto);

}// end of class