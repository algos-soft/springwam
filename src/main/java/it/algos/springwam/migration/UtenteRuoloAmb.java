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
//@Data
@ReadOnly
public class UtenteRuoloAmb {


    private long ruolo_id = 0;
    private long utente_id = 0;

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
    }

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UtenteRuoloAmb() {
    }// end of constructor


    public static int findRoleIDByUserID(long userID, EntityManager manager) {
        int roleID = 99;
        int roleTmp = 0;
        UtenteRuoloAmb utenteRuolo = null;
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UtenteRuoloAmb> from = criteriaQuery.from(UtenteRuoloAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("utente_id"), userID));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        if (resultlist != null) {
            if (resultlist.size() == 1) {
                utenteRuolo = (UtenteRuoloAmb) resultlist.get(0);
                roleID = (int) utenteRuolo.getRuolo_id();
            } else {
                for (Object obj : resultlist) {
                    utenteRuolo = (UtenteRuoloAmb) obj;
                    roleTmp = (int) utenteRuolo.getRuolo_id();
                    if (roleTmp < roleID) {
                        roleID = roleTmp;
                    }// end of if cycle
                }// end of for cycle
            }// end of if/else cycle
        }// end of if cycle

        return roleID;
    }// end of method


    /**
     * Recupera una lista di records della Entity
     *
     * @return lista di alcune istanze della Entity
     */
    public static List<UtenteAmb> findAllAdmin(EntityManager manager) {
        List<UtenteAmb> lista = new ArrayList<>();
        UtenteRuoloAmb utenteRuolo;
        long utenteID;
        UtenteAmb utente;
        List<Object> resultlist = null;
        long idAdmin = 3;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UtenteRuoloAmb> from = criteriaQuery.from(UtenteRuoloAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("ruolo_id"), idAdmin));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            utenteRuolo = (UtenteRuoloAmb) entity;
            utenteID = utenteRuolo.getUtente_id();
            utente = UtenteAmb.find(utenteID, manager);
            lista.add(utente);
        }// end of for cycle

        return lista;
    }// end of method


}// end of entity class

