package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import lombok.Data;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Entity per una funzione
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Croce")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class CroceAmb extends BaseEntity {

    private String sigla;
    private String descrizione;
    private String presidente;
    private String indirizzo;
    private String organizzazione;
    private String riferimento;
    private String email;
    private String telefono;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public CroceAmb() {
    }// end of constructor


    /**
     * Recupera una lista di tutti i records della Entity
     *
     * @return lista di tutte le istanze della Entity
     */
    public static List<CroceAmb> findAll(EntityManager manager) {
        List resultlist = null;

        if (manager == null) {
            return null;
        }// end of if cycle

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<CroceAmb> from = criteriaQuery.from(CroceAmb.class);
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        return resultlist;
    }// end of method

}// end of entity class
