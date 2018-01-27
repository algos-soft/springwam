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
 * Created by gac on 08 ott 2016.
 * Entity per un utente
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Utente")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class UserAmb extends BaseEntity {

    //--croce di riferimento
    @ManyToOne
    private CroceAmb croce;

    //--milite di riferimento
    @OneToOne
    private UtenteAmb milite;

    private String username;
    private String nickname;
    private String password;
    private String pass;
    private boolean enabled = true;
    private boolean account_expired = false;
    private boolean account_locked = false;
    private boolean password_expired = false;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UserAmb() {
    }// end of constructor


    /**
     * Recupera una lista di records della Entity
     *
     * @return lista di alcune istanze della Entity
     */
    public static List<UserAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
        List<UserAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UserAmb> from = criteriaQuery.from(UserAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("username")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((UserAmb) entity);
        }// end of for cycle

        return lista;
    }// end of method


}// end of entity class
