package it.algos.springwam.migration;


import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.SortProperty;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 09 ott 2016.
 * Entity per il ruolo di un utente
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Utente_ruolo")
@Access(AccessType.PROPERTY)
@ReadOnly
public class UtenteRuoloAmb {

    private long ruolo_id = 0;
    private long utente_id = 0;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UtenteRuoloAmb() {
    }// end of constructor


    @Id
    public long getRuolo_id() {
        return ruolo_id;
    }// end of getter method

    public void setRuolo_id(long ruolo_id) {
        this.ruolo_id = ruolo_id;
    }//end of setter method

    @Id
    public long getUtente_id() {
        return utente_id;
    }// end of getter method

    public void setUtente_id(long utente_id) {
        this.utente_id = utente_id;
    }//end of setter method


    public static List<Long> getListAdmin(EntityManager manager) {
        List<Long> listaUtenti = null;
        long idAdmin = 3;
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UtenteRuoloAmb> from = criteriaQuery.from(UtenteRuoloAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get(UtenteRuoloAmb_.ruolo_id), idAdmin));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        if (resultlist != null && resultlist.size() > 0) {
            listaUtenti = new ArrayList<>();
            for (Object obj : resultlist) {
                if (obj instanceof UtenteRuoloAmb) {
                    listaUtenti.add(((UtenteRuoloAmb) obj).utente_id);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaUtenti;
    }// end of method

}// end of entity class

