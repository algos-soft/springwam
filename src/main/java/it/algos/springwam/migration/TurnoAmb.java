package it.algos.springwam.migration;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.service.ADateService;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.query.AQuery;
import lombok.Data;
import org.eclipse.persistence.annotations.ReadOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 11 ott 2016.
 * <p>
 * Entity per un turno
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@SpringComponent
@Scope("singleton")
@Table(name = "Turno")
@Access(AccessType.PROPERTY)
@Data
@ReadOnly
public class TurnoAmb extends BaseEntity {


    @ManyToOne
    private CroceAmb croce;


    //--tipologia del turno
    @ManyToOne
    private ServizioAmb tipo_turno;

    //--giorno di svolgimento del turno (giorno iniziale se termina il mattino dopo)
    //--ore e minuti sono sempre a zero
    private Timestamp giorno;

    //--giorno, ora e minuto di inizio turno
    private Timestamp inizio;

    //--giorno, ora e minuto di fine turno
    private Timestamp fine;

    //--numero variabile di funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    @ManyToOne
    private FunzioneAmb funzione1;
    @ManyToOne
    private FunzioneAmb funzione2;
    @ManyToOne
    private FunzioneAmb funzione3;
    @ManyToOne
    private FunzioneAmb funzione4;
    //--numero variabile di militi assegnati alle funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    @ManyToOne
    private UtenteAmb milite_funzione1 = null;
    @ManyToOne
    private UtenteAmb milite_funzione2 = null;
    @ManyToOne
    private UtenteAmb milite_funzione3 = null;
    @ManyToOne
    private UtenteAmb milite_funzione4 = null;
    //--ultima modifica effettuata per le funzioni previste per il tipo di turno
    //--massimo hardcoded di 4
    //--serve per bloccare le modifiche dopo un determinatpo intervallo di tempo
    private Timestamp modifica_funzione1 = null;
    private Timestamp modifica_funzione2 = null;
    private Timestamp modifica_funzione3 = null;
    private Timestamp modifica_funzione4 = null;
    //--durata del turno per ogni milite
    //--massimo hardcoded di 4
    private int ore_milite1 = 0;
    private int ore_milite2 = 0;
    private int ore_milite3 = 0;
    private int ore_milite4 = 0;
    //--eventuali problemi di presenza del milite nel turno
    //--serve per evidenziare il problema nel tabellone
    //--massimo hardcoded di 4
    private boolean problemi_funzione1 = false;
    private boolean problemi_funzione2 = false;
    private boolean problemi_funzione3 = false;
    private boolean problemi_funzione4 = false;
    //--motivazione del turno
    private String titolo_extra = "";
    //--nome evidenziato della località
    private String località_extra = "";
    //--descrizione dei viaggi extra
    private String note = "";
    //--turno previsto (vuoto) oppure assegnato (militi inseriti)
    private boolean assegnato = false;


    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public TurnoAmb() {
    }// end of constructor


    public static List<TurnoAmb> findAllByCroce(CroceAmb company, EntityManager manager) {
        List<TurnoAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<TurnoAmb> from = criteriaQuery.from(TurnoAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
//        select.orderBy(criteriaBuilder.asc(from.get("ordine")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((TurnoAmb) entity);
        }// end of for cycle

        return lista;
    }// end of static method


    public static List<TurnoAmb> findAllByCroceAndYear(CroceAmb company, Timestamp inizio, Timestamp fine, EntityManager manager) {
        List<TurnoAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<TurnoAmb> from = criteriaQuery.from(TurnoAmb.class);
        criteriaQuery.where(
                criteriaBuilder.equal(from.get("croce"), company),
                criteriaBuilder.greaterThan(from.get("inizio"), inizio),
                criteriaBuilder.lessThan(from.get("fine"), fine));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("giorno")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((TurnoAmb) entity);
        }// end of for cycle

        return lista;
    }// end of static method

}// end of entity class
