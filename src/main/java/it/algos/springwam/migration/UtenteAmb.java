package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import lombok.Data;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by gac on 08 ott 2016.
 * Entity per una volontario
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Milite")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class UtenteAmb extends BaseEntity {

    //--dati associazione
    boolean dipendente = false;
    boolean attivo = true;

    //--croce di riferimento
    @ManyToOne
    private CroceAmb croce;

    //--dati anagrafici
    private String nome = "";
    private String cognome = "";
    private String telefono_cellulare = "";
    private String telefono_fisso = "";
    private String email = "";
    //    private Date data_nascita;
    private String note = "";
    private int ore_anno;
    private int turni_anno;
    private int ore_extra;

    //--non usate
//    private   Date scadenzaBLSD = null;
//    private  Date scadenzaTrauma = null;
//    private   Date scadenzaNonTrauma = null;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UtenteAmb() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     *
     * @return istanza della Entity, null se non trovata
     */
    public static UtenteAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (UtenteAmb) AQuery.find(UtenteAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method




    /**
     * Recupera una lista di records della Entity
     *
     * @return lista di alcune istanze della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<UtenteAmb> findAll(CroceAmb company, EntityManager manager) {
        List<UtenteAmb> lista = null;

        if (manager != null) {
            lista = (List<UtenteAmb>) getList(company, manager);
        }// end of if cycle

        return lista;
    }// end of method


    public static List getList(CroceAmb company, EntityManager manager) {
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UtenteAmb> from = criteriaQuery.from(UtenteAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("cognome")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        return resultlist;
    }// end of method

}// end of entity class
