package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import lombok.Data;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 30 ago 2016.
 * <p>
 * Entity per un servizio
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Tipo_turno")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class ServizioAmb extends BaseEntity {

    @ManyToOne
    private CroceAmb croce;

    private String sigla;
    private String descrizione;
    private int ordine;
    private int ora_inizio;
    private int minuti_inizio;
    private int ora_fine;
    private int minuti_fine;
    private boolean orario;
    private boolean primo;
    private boolean visibile;

    private long funzione1_id;
    private long funzione2_id;
    private long funzione3_id;
    private long funzione4_id;

    private int funzioni_obbligatorie;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public ServizioAmb() {
    }// end of constructor


    public static List<ServizioAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
        List<ServizioAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<ServizioAmb> from = criteriaQuery.from(ServizioAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("ordine")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((ServizioAmb) entity);
        }// end of for cycle

        return lista;
    }// end of method

}// end of entity class
