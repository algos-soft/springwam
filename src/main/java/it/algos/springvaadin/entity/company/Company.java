package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.address.Address;
import it.algos.springvaadin.entity.address.AddressPresenter;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaPresenter;
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
@Document(collection = "company")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_COM)
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.nonUsata)
@AIList(fields = {"code", "descrizione", "contatto", "telefono", "email"}, dev = EAListButton.standard, admin = EAListButton.noSearch)
@AIForm()
@AIScript(sovrascrivibile = false)
public class Company extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * codice di riferimento interno (obbligatorio ed unico)
     */
    @NotEmpty(message = "La sigla interna è obbligatoria")
    @Size(min = 2, max = 20)
    @AIField(type = EAFieldType.text, widthEM = 8, focus = true, name = "Sigla", help = "Codice interno unico")
    @AIColumn(name = "Sigla", width = 100)
    private String code;


    /**
     * ragione sociale o descrizione della company (obbligatoria, non unica)
     */
    @NotEmpty(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @AIField(type = EAFieldType.text, firstCapital = true, widthEM = 24, help = "Descrizione della company")
    @AIColumn(width = 370)
    private String descrizione;


    /**
     * persona di riferimento (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = EAFieldType.link, clazz = PersonaPresenter.class, help = "Riferimento")
    @AIColumn(width = 220, name = "Riferimento")
    private Persona contatto;


    /**
     * telefono (facoltativo)
     */
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 170)
    private String telefono;


    /**
     * posta elettronica (facoltativo)
     */
    @Email
    @AIField(type = EAFieldType.email, widthEM = 24)
    @AIColumn(width = 350, name = "Mail")
    private String email;


    /**
     * indirizzo (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = EAFieldType.link, clazz = AddressPresenter.class, help = "Indirizzo")
    @AIColumn(width = 400, name = "Indirizzo")
    private Address indirizzo;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method


}// end of entity class
