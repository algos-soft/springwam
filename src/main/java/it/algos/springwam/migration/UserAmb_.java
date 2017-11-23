package it.algos.springwam.migration;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 08 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class UserAmb_ {
    public static volatile SingularAttribute<UserAmb, CroceAmb> croce;
    public static volatile SingularAttribute<UserAmb, UtenteAmb> milite;
    public static volatile SingularAttribute<UserAmb, String> username;
    public static volatile SingularAttribute<UserAmb, String> nickname;
    public static volatile SingularAttribute<UserAmb, String> password;
    public static volatile SingularAttribute<UserAmb, String> pass;
    public static volatile SingularAttribute<UserAmb, Boolean> enabled;
    public static volatile SingularAttribute<UserAmb, Boolean> accountExpired;
    public static volatile SingularAttribute<UserAmb, Boolean> accountLocked;
    public static volatile SingularAttribute<UserAmb, Boolean> passwordExpired;
}// end of entity class
