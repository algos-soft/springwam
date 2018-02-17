package it.algos.springvaadin.entity.persona;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.address.Address;
import it.algos.springvaadin.entity.address.AddressPresenter;
import it.algos.springvaadin.enumeration.EACompanyRequired;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.lib.ACost;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: 2018-01-14_06:46:57
 * Estende la Entity astratta ACEntity che contiene il riferimento alla property Company
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
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "persona")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_PER)
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.nonUsata)
@AIList(fields = {"nome", "cognome"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"nome", "cognome"})
@AIScript(sovrascrivibile = false)
public class Persona extends ACEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * nome (obbligatorio, non unica)
     */
    @NotEmpty
    @Size(min = 4, max = 40)
    @AIField(type = EAFieldType.text, required = true, focus = true, firstCapital = true)
    @AIColumn(width = 200)
    private String nome;


    /**
     * cognome (obbligatorio, non unica)
     */
//    @NotEmpty
    @AIField(type = EAFieldType.text, required = true, firstCapital = true)
    @AIColumn(width = 200)
    private String cognome;


    /**
     * telefono (facoltativo)
     */
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 160)
    private String telefono;


    /**
     * posta elettronica (facoltativo)
     */
    @Email
    @AIField(type = EAFieldType.email, widthEM = 24)
    @AIColumn(width = 350, name = "Mail")
    private String email;


    /**
     * indirizzo (facoltativo, non unica)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @AIField(type = EAFieldType.link, clazz = AddressPresenter.class, help = "Indirizzo")
    @AIColumn(width = 400, name = "Indirizzo")
    private Address indirizzo;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome() + " " + getCognome();
    }// end of method


}// end of entity class
