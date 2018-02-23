package it.algos.springwam.entity.turno;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.enumeration.*;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.servizio.Servizio;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.context.annotation.Scope;
import lombok.*;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:19:25
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
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "turno")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_TUR)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"giorno", "servizio"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"giorno", "servizio", "inizio", "fine", "iscrizioni", "titoloExtra", "localitaExtra"}, buttonsAdmin = EAFormButton.conferma, buttonsUser = EAFormButton.conferma)
@AIScript(sovrascrivibile = false)
public class Turno extends ACEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * giorno di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     */
    @NotNull
    @AIField(type = EAFieldType.localdate)
    @AIColumn(width = 150)
    private LocalDate giorno;


    /**
     * servizio di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = EAFieldType.combo, clazz = Servizio.class)
    @AIColumn(width = 140)
    private Servizio servizio;


    /**
     * giorno, ora e minuto di inizio turno (obbligatorio)
     */
    @NotNull
    @AIField(type = EAFieldType.localdatetime)
    @AIColumn()
    private LocalDateTime inizio;


    /**
     * giorno, ora e minuto di fine turno (obbligatorio, suggerito da servizio)
     */
    @NotNull
    @AIField(type = EAFieldType.localdatetime)
    @AIColumn()
    private LocalDateTime fine;


    /**
     * iscrizioni dei volontari a questo turno (obbligatorio per un turno valido)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @NotNull
    private List<Iscrizione> iscrizioni;


    /**
     * motivazione del turno extra (facoltativo)
     */
    @AIField(type = EAFieldType.text)
    @AIColumn()
    private String titoloExtra;


    /**
     * nome evidenziato della località per turni extra (facoltativo)
     */
    @AIField(type = EAFieldType.text)
    @AIColumn()
    private String localitaExtra;


//    /**
//     * @return a string representation of the object.
//     */
//    @Override
//    public String toString() {
//        return getCode();
//    }// end of method


}// end of entity class
