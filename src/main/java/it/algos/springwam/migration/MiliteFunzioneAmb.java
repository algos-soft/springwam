package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: mar, 27-feb-2018
 * Time: 20:39
 */
@Entity
@Table(name = "MiliteFunzione")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class MiliteFunzioneAmb extends BaseEntity {

    private long croce_id = 0;
    private long funzione_id = 0;
    private long milite_id = 0;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public MiliteFunzioneAmb() {
    }// end of constructor


//    public static List<MiliteFunzioneAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
//        List<MiliteFunzioneAmb> lista = new ArrayList<>();
//        List<Object> resultlist = null;
//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
//        Root<MiliteFunzioneAmb> from = criteriaQuery.from(MiliteFunzioneAmb.class);
//        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
//        CriteriaQuery<Object> select = criteriaQuery.select(from);
//        TypedQuery<Object> typedQuery = manager.createQuery(select);
//        resultlist = typedQuery.getResultList();
//
//        for (Object entity : resultlist) {
//            lista.add((MiliteFunzioneAmb) entity);
//        }// end of for cycle
//
//        return lista;
//    }// end of method


    public static List<MiliteFunzioneAmb> findAllByMilite(UtenteAmb milite, EntityManager manager) {
        List<MiliteFunzioneAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<MiliteFunzioneAmb> from = criteriaQuery.from(MiliteFunzioneAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("milite_id"), milite.getId()));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((MiliteFunzioneAmb) entity);
        }// end of for cycle

        return lista;
    }// end of method


    public static List<FunzioneAmb> findAllFunzioniByMilite(UtenteAmb milite, EntityManager manager) {
        List<FunzioneAmb> lista = new ArrayList<>();
        List<MiliteFunzioneAmb> listaAll = findAllByMilite(milite, manager);

        for (MiliteFunzioneAmb milFunz : findAllByMilite(milite, manager)) {
            lista.add(FunzioneAmb.find(milFunz.getFunzione_id(),manager));
        }// end of for cycle

        return lista;
    }// end of method

}// end of entity class
