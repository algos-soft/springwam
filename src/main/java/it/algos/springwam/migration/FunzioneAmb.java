package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import lombok.Data;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gac on 27 ago 2016.
 * <p>
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
@Table(name = "Funzione")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class FunzioneAmb extends BaseEntity {

    //--croce di riferimento
    @ManyToOne
    private CroceAmb croce;

    private String sigla;
    private String descrizione;
    private int ordine;
    private String sigla_visibile;
    private String funzioni_dipendenti;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public FunzioneAmb() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     *
     * @return istanza della Entity, null se non trovata
     */
    public static FunzioneAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (FunzioneAmb) AQuery.find(FunzioneAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method


    public static List<FunzioneAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
        List<FunzioneAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<FunzioneAmb> from = criteriaQuery.from(FunzioneAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("ordine")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((FunzioneAmb) entity);
        }// end of for cycle

        return lista;
    }// end of method

}// end of entity class
