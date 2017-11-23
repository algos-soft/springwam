package it.algos.springwam.migration;

import it.algos.springwam.entity.croce.Organizzazione;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 01 ago 2016.
 * Entity della vecchia versione di webambulanze da cui migrare i dati
 */
public class CroceAmb_ {
    public static volatile SingularAttribute<CroceAmb, String> sigla;
    public static volatile SingularAttribute<CroceAmb, String> descrizione;
    public static volatile SingularAttribute<CroceAmb, String> presidente;
    public static volatile SingularAttribute<CroceAmb, String> indirizzo;
    public static volatile SingularAttribute<CroceAmb, String> riferimento;
    public static volatile SingularAttribute<CroceAmb, String> email;
    public static volatile SingularAttribute<CroceAmb, String> telefono;
    public static volatile SingularAttribute<CroceAmb, Organizzazione> organizzazione;
}// end of entity class
