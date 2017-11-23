package it.algos.springwam.migration;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
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
     * @return istanza della Entity, null se non trovata
     */
    public static UtenteAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (UtenteAmb) AQuery.find(UtenteAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method


    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static UtenteAmb getEntityByCompanyAndNomeAndCognome(CroceAmb company, String nome, String cognome, EntityManager manager) {
        Container.Filter filterCompany = new Compare.Equal(UtenteAmb_.croce.getName(), company);
        Container.Filter filterNome = new Compare.Equal(UtenteAmb_.nome.getName(), nome);
        Container.Filter filterCognome = new Compare.Equal(UtenteAmb_.cognome.getName(), cognome);
        return (UtenteAmb) AQuery.getEntity(UtenteAmb.class, manager, filterCompany, filterNome, filterCognome);
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query di una property specifica
     * Filtrato sulla company passata come parametro.
     *
     * @param company di appartenenza (property della superclasse)
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @param manager the EntityManager to use
     * @return istanza della Entity, null se non trovata
     */
    public static List<UtenteAmb> getListByCompanyAndNomeAndCognome(CroceAmb company, String nome, String cognome, EntityManager manager) {
        Container.Filter filterCompany = new Compare.Equal(UtenteAmb_.croce.getName(), company);
        Container.Filter filterNome = new Compare.Equal(UtenteAmb_.nome.getName(), nome);
        Container.Filter filterCognome = new Compare.Equal(UtenteAmb_.cognome.getName(), cognome);
        return (List<UtenteAmb>) AQuery.getList(UtenteAmb.class, manager, filterCompany, filterNome, filterCognome);
    }// end of static method

    /**
//     * Recupera una lista di tutti i records della Entity
//     * Filtrato sulla company passata come parametro.
//     *
//     * @param company di appartenenza
//     * @return lista delle istanze filtrate della Entity
//     */
//    @SuppressWarnings("unchecked")
//    public static List<UtenteAmb> getList(CroceAmb company, EntityManager manager) {
//        return (List<UtenteAmb>) AQuery.getList(UtenteAmb.class, UtenteAmb_.croce, company, manager);
//    }// end of method

    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public String getNome() {
        return nome;
    }// end of getter method

    public void setNome(String nome) {
        this.nome = nome;
    }//end of setter method

    public String getCognome() {
        return cognome;
    }// end of getter method

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }//end of setter method

    public String getTelefono_cellulare() {
        return telefono_cellulare;
    }// end of getter method

    public void setTelefono_cellulare(String telefono_cellulare) {
        this.telefono_cellulare = telefono_cellulare;
    }//end of setter method

    public String getTelefono_fisso() {
        return telefono_fisso;
    }// end of getter method

    public void setTelefono_fisso(String telefono_fisso) {
        this.telefono_fisso = telefono_fisso;
    }//end of setter method

    public String getEmail() {
        return email;
    }// end of getter method

    public void setEmail(String email) {
        this.email = email;
    }//end of setter method

//    public Date getData_nascita() {
//        return data_nascita;
//    }// end of getter method
//
//    public void setData_nascita(Date data_nascita) {
//        this.data_nascita = data_nascita;
//    }//end of setter method

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
    }//end of setter method

    public boolean isDipendente() {
        return dipendente;
    }// end of getter method

    public void setDipendente(boolean dipendente) {
        this.dipendente = dipendente;
    }//end of setter method

    public boolean isAttivo() {
        return attivo;
    }// end of getter method

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }//end of setter method

    public int getOre_anno() {
        return ore_anno;
    }// end of getter method

    public void setOre_anno(int ore_anno) {
        this.ore_anno = ore_anno;
    }//end of setter method

    public int getTurni_anno() {
        return turni_anno;
    }// end of getter method

    public void setTurni_anno(int turni_anno) {
        this.turni_anno = turni_anno;
    }//end of setter method

    public int getOre_extra() {
        return ore_extra;
    }// end of getter method

    public void setOre_extra(int ore_extra) {
        this.ore_extra = ore_extra;
    }//end of setter method

    public static List getList(EntityManager manager, CroceAmb company) {
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UtenteAmb> from = criteriaQuery.from(UtenteAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get(UtenteAmb_.croce), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("cognome")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        return resultlist;
    }// end of method

}// end of entity class
