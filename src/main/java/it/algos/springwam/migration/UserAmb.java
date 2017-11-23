package it.algos.springwam.migration;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     *
     * @return istanza della Entity, null se non trovata
     */
    public static UserAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (UserAmb) AQuery.find(UtenteAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method

//    /**
//     * Recupera una istanza della Entity usando la query per una property specifica
//     *
//     * @return istanza della Entity, null se non trovata
//     */
//    @SuppressWarnings("unchecked")
//    public static List<UserAmb> getList(CroceAmb company, EntityManager manager) {
//        return (List<UserAmb>) AQuery.getList(UserAmb.class, UserAmb_.croce, company, manager);
//    }// end of method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param volontario valore della property
     *
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static UtenteAmb getEntityByVolontario(List<UtenteAmb> entities, UtenteAmb volontario) {
        UtenteAmb instance = null;
        CroceAmb croce = volontario.getCroce();
        long keyID = volontario.getId();
        UtenteAmb milite = null;

        if (entities != null && entities.size() > 0) {
            for (UtenteAmb utente : entities) {
//                milite = utente.getMilite();
                if (milite != null) {
                    if (utente.getCroce() == croce && milite.getId() == keyID) {
                        instance = utente;
                        break;
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method

    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public UtenteAmb getMilite() {
        return milite;
    }// end of getter method

    public void setMilite(UtenteAmb milite) {
        this.milite = milite;
    }//end of setter method

    public String getUsername() {
        return username;
    }// end of getter method

    public void setUsername(String username) {
        this.username = username;
    }//end of setter method

    public String getNickname() {
        return nickname;
    }// end of getter method

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }//end of setter method

    public String getPassword() {
        return password;
    }// end of getter method

    public void setPassword(String password) {
        this.password = password;
    }//end of setter method

    public String getPass() {
        return pass;
    }// end of getter method

    public void setPass(String pass) {
        this.pass = pass;
    }//end of setter method

    public boolean isEnabled() {
        return enabled;
    }// end of getter method

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }//end of setter method

    public boolean isAccount_expired() {
        return account_expired;
    }// end of getter method

    public void setAccount_expired(boolean account_expired) {
        this.account_expired = account_expired;
    }//end of setter method

    public boolean isAccount_locked() {
        return account_locked;
    }// end of getter method

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }//end of setter method

    public boolean isPassword_expired() {
        return password_expired;
    }// end of getter method

    public void setPassword_expired(boolean password_expired) {
        this.password_expired = password_expired;
    }//end of setter method


    public static List getList(EntityManager manager, CroceAmb company) {
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UserAmb> from = criteriaQuery.from(UserAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get(UserAmb_.croce), company));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("username")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        return resultlist;
    }// end of method

    public static UserAmb getUtenteByMiliteID(EntityManager manager, UtenteAmb milite) {
        UserAmb userAmb = null;
        List<Object> resultlist = null;
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<UserAmb> from = criteriaQuery.from(UserAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get(UserAmb_.milite), milite));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        if (resultlist != null && resultlist.size() == 1) {
            userAmb = (UserAmb) resultlist.get(0);
        }// end of if cycle

        return userAmb;
    }// end of method

}// end of entity class
