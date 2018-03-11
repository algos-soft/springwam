package it.algos.springwam.entity.iscrizione;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import it.algos.springwam.entity.servizio.ServizioPresenter;
import it.algos.springwam.entity.turno.Turno;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

import java.time.LocalDateTime;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-02-04_17:23:11
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
@Document(collection = "iscrizione")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_ISC)
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"turno", "milite","funzione"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"turno", "utente", "funzione"})
@AIScript(sovrascrivibile = false)
public class Iscrizione extends ACEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @NotNull
    @AIField(type = EAFieldType.combo, clazz = FunzioneService.class)
    @AIColumn(width = 140)
    private Funzione funzione;


    /**
     * milite di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = EAFieldType.combo, clazz = MiliteService.class)
    @AIColumn(width = 140)
    private Milite milite;


    /**
     * timestamp di creazione (obbligatorio, inserito in automatico)
     * (usato per bloccare la cancIscrizione dopo un determinato intervallo di tempo)
     */
    @NotNull
    @AIField(type = EAFieldType.localdatetime)
    @AIColumn(width = 140)
    private LocalDateTime timestamp;


    /**
     * durata effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     */
    @NotNull
    @AIField(type = EAFieldType.integer)
    @AIColumn(width = 140)
    private int durata;


    /**
     * eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
     * serve per evidenziare il problema nel tabellone
     */
    @AIField(type = EAFieldType.checkbox)
    @AIColumn(width = 140)
    private boolean esisteProblema;


    /**
     * se è stata inviata la notifica di inizio turno dal sistema di notifiche automatiche
     */
    @AIField(type = EAFieldType.checkbox)
    private boolean notificaInviata;


//    /**
//     * @return a string representation of the object.
//     */
//    @Override
//    public String toString() {
//        return getCode();
//    }// end of method


}// end of entity class
