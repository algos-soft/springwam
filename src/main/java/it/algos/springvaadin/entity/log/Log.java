package it.algos.springvaadin.entity.log;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.entity.logtype.Logtype;
import it.algos.springvaadin.enumeration.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.context.annotation.Scope;
import lombok.*;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.entity.ACEntity;

import java.time.LocalDateTime;

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
@Document(collection = "log")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_LOG)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"code", "descrizione"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"code", "descrizione"})
@AIScript(sovrascrivibile = false)
public class Log extends ACEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * rilevanza del log (obbligatorio)
     */
    @NotEmpty(message = "Livello del log è obbligatorio")
    @Indexed()
    @AIField(type = EAFieldType.enumeration, clazz = EALogLevel.class, nullSelectionAllowed = false, widthEM = 10, admin = EAFieldAccessibility.showOnly)
    @AIColumn(width = 100)
    private EALogLevel livello;



    /**
     * raggruppamento logico dei log per type di eventi (obbligatorio)
     */
    @NotEmpty(message = "La tipologia del log è obbligatoria")
    @Indexed()
    @AIField(type = EAFieldType.combo, clazz = Logtype.class, nullSelectionAllowed = false, widthEM = 10, admin = EAFieldAccessibility.showOnly)
    @AIColumn(width = 140)
    private Logtype type;



    /**
     * descrizione (facoltativa)
     */
    @AIField(
            type = EAFieldType.text,
            required = true,
            name = "Descrizione completa",
            widthEM = 24,
            admin = EAFieldAccessibility.allways,
            user = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Descrizione", width = 500)
    private String descrizione;


    /**
     * Data dell'evento (obbligatoria, non modificabile)
     * Gestita in automatico
     * Field visibile solo al buttonAdmin
     */
    @NotNull
    @Indexed()
    @AIField(type = EAFieldType.localdatetime, name = "Data dell'evento di log", admin = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Data")
    public LocalDateTime evento;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getType() + " - " + getDescrizione();
    }// end of method


}// end of entity class
