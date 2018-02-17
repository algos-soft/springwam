package it.algos.springvaadin.entity;

import it.algos.springvaadin.annotation.AIColumn;
import it.algos.springvaadin.annotation.AIField;
import it.algos.springvaadin.enumeration.EAFieldAccessibility;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.enumeration.EARoleType;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 01-set-2017
 * Time: 18:30
 * Le sottoclassi concrete sono di tipo JavaBean
 * 1) la sottoclasse deve avere un costruttore senza argomenti (fornito in automatico da Lombok)
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolen al posto di get)
 * 3) la sottoclasse deve implementare l'annotation Serializable (lo fa in questa classe)
 * 4) la sottoclasse non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 * Annotated with @Getter (Lombok) for automatic use of Getter
 * Sottoclassi annotated (obbligatorio) with @SpringComponent
 * Sottoclassi annotated (facoltativo) with @Data (Lombok) for automatic use of Getter and Setter
 * Sottoclassi annotated (facoltativo) with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Sottoclassi annotated (facoltativo) with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * <p>
 * La classe NON può usare la Annotation @Setter (contrariamente alle altre classi di Entity),
 * perché 'oscurerebbe' la gestione automatica della key property ObjectId da parte di Mongo
 * Le properety sono tutte pubbliche (contrariamente alle altre classi di Entity),
 * per essere accessibili visto che mancano i 'setters'
 * <p>
 * La gestione delle property 'dataCreazione' e 'dataModifica' è automatica in AService.save()
 */
@Getter
public abstract class AEntity implements Serializable {


    /**
     * key property ObjectId
     * di default gestita direttamente da Mongo
     * può essere usata direttamente per identificare la entity con key 'leggibili'
     * NON va usato @NotEmpty, perchè altrimenti binder.validate().isOk() va in errore
     * Ci pensa Mongo a riempire il valore
     */
    @AIField(name = "Key", required = true, roleTypeVisibility = EARoleType.developer, dev = EAFieldAccessibility.showOnly)
    @AIColumn()
    public String id;


    /**
     * Eventuali note (facoltativo)
     */
    @AIField(type = EAFieldType.textarea, widthEM = 24, admin = EAFieldAccessibility.allways, user = EAFieldAccessibility.showOnly)
    @AIColumn()
    public String note = "";


    /**
     * Data di creazione del nuovo record (facoltativa, non modificabile)
     * Gestita in automatico.
     * Utilizzo obbligatorio o facoltativo.
     * Regolato uguale per tutta l'applicazione col flag KEY_USE_PROPERTY_CREAZIONE_AND_MODIFICA
     * Field visibile solo al developer
     */
    @AIField(type = EAFieldType.localdatetime, name = "Creazione della scheda", required = true, roleTypeVisibility = EARoleType.developer, dev = EAFieldAccessibility.showOnly)
    @AIColumn()
    public LocalDateTime creazione;


    /**
     * Data di edit del record (facoltativa, modificabile solo da codice, non da UI)
     * Utilizzo obbligatorio o facoltativo.
     * Regolato uguale per tutta l'applicazione col flag KEY_USE_PROPERTY_CREAZIONE_AND_MODIFICA
     * Field visibile solo al developer
     */
    @AIField(type = EAFieldType.localdatetime, name = "Ultima modifica della scheda", required = true, roleTypeVisibility = EARoleType.developer, dev = EAFieldAccessibility.showOnly)
    @AIColumn()
    public LocalDateTime modifica;

}// end of entity abstract class


