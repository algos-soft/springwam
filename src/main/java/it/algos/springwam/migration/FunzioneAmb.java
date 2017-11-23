package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.SortProperty;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     *
     * @param sigla di riferimento interna (obbligatoria, unica all'interno della company)
     *
     * @return istanza della Entity, null se non trovata
     */
    public static FunzioneAmb findBySigla(String sigla, EntityManager manager) {
        return (FunzioneAmb) AQuery.getEntity(FunzioneAmb.class, FunzioneAmb_.sigla, sigla, manager);
    }// end of static method


    /**
     * Recupera una lista di tutti i records della Entity
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza
     *
     * @return lista delle istanze filtrate della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<FunzioneAmb> findAll(CroceAmb company, EntityManager manager) {
        List<FunzioneAmb> lista = null;

        if (manager != null) {
            lista = (List<FunzioneAmb>) getList(manager, company);
        }// end of if cycle

        return lista;
    }// end of method


    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public String getSigla() {
        return sigla;
    }// end of getter method

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method

    public int getOrdine() {
        return ordine;
    }// end of getter method

    public void setOrdine(int ordine) {
        this.ordine = ordine;
    }//end of setter method

    public String getSigla_visibile() {
        return sigla_visibile;
    }// end of getter method

    public void setSigla_visibile(String sigla_visibile) {
        this.sigla_visibile = sigla_visibile;
    }//end of setter method

    public String getFunzioni_dipendenti() {
        return funzioni_dipendenti;
    }// end of getter method

    public void setFunzioni_dipendenti(String funzioni_dipendenti) {
        this.funzioni_dipendenti = funzioni_dipendenti;
    }//end of setter method


    public static List getList(EntityManager manager, CroceAmb company) {
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<FunzioneAmb> from = criteriaQuery.from(FunzioneAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get(FunzioneAmb_.croce), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("ordine")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        return resultlist;
    }// end of method

}// end of entity class
