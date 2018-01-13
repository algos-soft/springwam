package it.algos.springvaadin.entity.stato;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.context.annotation.Scope;
import lombok.*;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EACompanyRequired;
import it.algos.springvaadin.enumeration.EAFieldAccessibility;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.entity.AEntity;

/**
 * Created by gac on 11-nov-17
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Annotated with @Builder (Lombok) lets you automatically produce the code required to have your class
 * be instantiable with code such as: Person.builder().name("Adam Savage").city("San Francisco").build();
 * Annotated with @EqualsAndHashCode (facoltativo) per ???
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIEntity (facoltativo) per alcuni parametri generali del modulo
 * Annotated with @AIList (facoltativo) per le colonne della Lista e loro visibilità/accessibilità relativa all'utente
 * Annotated with @AIForm (facoltativo) per i fields del Form e loro visibilità/accessibilità relativa all'utente
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "stato")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_STA)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.nonUsata)
@AIList(dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm()
public class Stato extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di nuovo (obbligatorio, unico, con controllo automatico prima del save se è zero, non modificabile)
     * inserito automaticamente
     * se si cancella una entity, rimane il 'buco' del numero
     */
    @NotNull
    @Indexed(unique = true)
    @AIField(type = EAFieldType.integer, help = "Ordine di stato. Unico e normalmente progressivo")
    @AIColumn(name = "#", width = 55)
    private int ordine;


    /**
     * nome corrente completo, non ufficiale (obbligatorio ed unico). Codifica ISO 3166/MA
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 4)
    @AIField(type = EAFieldType.text, required = true, focus = true, firstCapital = true, help = "Codifica ISO 3166/MA")
    @AIColumn(width = 250)
    private String nome;


    /**
     * codice alfabetico di 2 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-2
     * 249 codici assegnati
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 2, max = 2)
    @AIField(type = EAFieldType.text, widthEM = 6, allUpper = true, onlyLetter = true, help = "Codifica ISO 3166-1 alpha-2")
    @AIColumn(width = 100)
    private String alfaDue;


    /**
     * codice alfabetico di 3 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-3
     * 249 codici assegnati
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 3, max = 3)
    @AIField(type = EAFieldType.text, widthEM = 6, allUpper = true, onlyLetter = true, help = "Codifica ISO 3166-1 alpha-3")
    @AIColumn(width = 100)
    private String alfaTre;


    /**
     * codice numerico di 3 cifre numeriche (facoltativo, vuoto oppure unico). Codifica ISO 3166-1 numerico
     * 249 codici assegnati
     */
    @Indexed(unique = false)
    @Size(min = 3, max = 3)
    @AIField(type = EAFieldType.text, widthEM = 6, onlyNumber = true, help = "Codifica ISO 3166-1 numerico")
    @AIColumn(width = 100, name = "Code")
    private String numerico;


    /**
     * immagine bandiera (facoltativo, unico).
     */
    @Indexed(unique = false)
    @AIField(type = EAFieldType.image, widthEM = 8)
    @AIColumn(width = 100)
    private byte[] bandiera;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }// end of method


}// end of entity class
