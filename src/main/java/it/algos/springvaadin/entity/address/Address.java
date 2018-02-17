package it.algos.springvaadin.entity.address;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.enumeration.EACompanyRequired;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.lib.ACost;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "address")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_ADD)
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.nonUsata)
@AIList(fields = {"indirizzo", "localita", "cap", "stato"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"indirizzo", "localita", "cap", "stato"})
@AIScript(sovrascrivibile = false)
public class Address extends ACEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * indirizzo: via, nome e numero (obbligatoria, non unica)
     */
    @NotEmpty(message = "L'indirizzo è obbligatorio")
    @Size(min = 2, max = 50)
    @AIField(type = EAFieldType.text, widthEM = 20, focus = true, help = "Via, nome e numero")
    @AIColumn(width = 300)
    private String indirizzo;


    /**
     * località (obbligatoria, non unica)
     */
    @NotEmpty()
    @Size(min = 4, max = 40)
    @AIField(type = EAFieldType.text, firstCapital = true, widthEM = 20, help = "Città, comune, paese")
    @AIColumn(width = 300)
    private String localita;


    /**
     * codice di avviamento postale (facoltativo, non unica)
     */
    @Size(min = 5, max = 5, message = "Il codice postale deve essere di 5 cifre")
    @AIField(type = EAFieldType.text, widthEM = 5, help = "Codice di avviamento postale")
    @AIColumn(width = 90)
    private String cap;


    /**
     * stato (obbligatoria, non unica)
     * riferimento dinamico con @DBRef (obbligatorio per il ComboBox)
     */
    @DBRef
    @NotNull
    @AIField(type = EAFieldType.combo, clazz = Stato.class)
    @AIColumn(width = 140)
    private Stato stato;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String value = "";
        String spazio = " ";
        String sep = " - ";

        value += indirizzo;
        value += (cap != null && !cap.equals("")) ? sep + cap : sep;
        value += spazio + localita;
        value += spazio + (stato != null ? stato : "");

        return value;
    }// end of method

}// end of entity class
