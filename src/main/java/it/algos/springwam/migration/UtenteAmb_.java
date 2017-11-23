package it.algos.springwam.migration;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

/**
 * Created by gac on 08 ott 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class UtenteAmb_ {
    public static volatile SingularAttribute<UtenteAmb, CroceAmb> croce;
    public static volatile SingularAttribute<UtenteAmb, String> nome;
    public static volatile SingularAttribute<UtenteAmb, String> cognome;
    public static volatile SingularAttribute<UtenteAmb, String> telefonoCellulare;
    public static volatile SingularAttribute<UtenteAmb, String> telefonoFisso;
    public static volatile SingularAttribute<UtenteAmb, String> email;
//    public static volatile SingularAttribute<UtenteAmb, Date> dataNascita;
    public static volatile SingularAttribute<UtenteAmb, String> note;
    public static volatile SingularAttribute<UtenteAmb, Boolean> admin;
    public static volatile SingularAttribute<UtenteAmb, Boolean> dipendente;
    public static volatile SingularAttribute<UtenteAmb, Boolean> attivo;
    public static volatile SingularAttribute<UtenteAmb, Integer> oreAnno;
    public static volatile SingularAttribute<UtenteAmb, Integer> turniAnno;
    public static volatile SingularAttribute<UtenteAmb, Integer> oreExtra;
}// end of entity class
