package it.algos.springvaadin.entity.logtype;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.*;
import it.algos.springvaadin.lib.ACost;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.index.Indexed;
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
@Document(collection = "logtype")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_LOGTYPE)
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.nonUsata)
@AIList(fields = {"code"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"code"})
@AIScript(sovrascrivibile = false)
public class Logtype extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione (obbligatorio, unico)
     * il più importante per primo
     */
    @NotNull
    @Indexed()
    @AIField(type = EAFieldType.integer, widthEM = 3, dev = EAFieldAccessibility.showOnly, admin = EAFieldAccessibility.never)
    @AIColumn(name = "#", width = 55)
    private int ordine;


    /**
     * codice di riferimento (obbligatorio)
     */
    @NotEmpty
    @Size(min = 2, max = 20)
    @Indexed()
    @AIField(
            type = EAFieldType.text,
            required = true,
            focus = true,
            name = "Codice",
            widthEM = 9,
            admin = EAFieldAccessibility.allways,
            user = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Code", width = 120)
    private String code;



    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method


}// end of entity class
