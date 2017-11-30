package it.algos.springwam.entity.turno;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.list.ListButton;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioPresenter;
import it.algos.webbase.web.field.AFType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_TUR)
@AIEntity(roleTypeVisibility = ARoleType.developer, company = ACompanyRequired.obbligatoria)
@AIList(columns = {"giorno", "servizio"}, dev = ListButton.standard)
@AIForm(fields = {"giorno", "servizio", "inizio", "fine", "iscrizioni", "titoloExtra", "localitaExtra"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Turno extends ACompanyEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * giorno di inizio turno (obbligatorio, calcolato da inizio - serve per le query)
     */
    @NotNull
    @AIField(type = AFieldType.localdate)
    @AIColumn()
    private LocalDate giorno;


    /**
     * servizio di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AFieldType.link, clazz = ServizioPresenter.class)
    @AIColumn()
    private Servizio servizio;


    /**
     * giorno, ora e minuto di inizio turno (obbligatorio)
     */
    @NotNull
    @AIField(type = AFieldType.localdatetime)
    @AIColumn()
    private LocalDateTime inizio;


    /**
     * giorno, ora e minuto di fine turno (obbligatorio, suggerito da servizio)
     */
    @NotNull
    @AIField(type = AFieldType.localdatetime)
    @AIColumn()
    private LocalDateTime fine;


    /**
     * iscrizioni dei volontari a questo turno (obbligatorio per un turno valido)
     */
    private List<Iscrizione> iscrizioni;


    /**
     * motivazione del turno extra (facoltativo)
     */
    @AIField(type = AFieldType.text)
    @AIColumn()
    private String titoloExtra;


    /**
     * nome evidenziato della località per turni extra (facoltativo)
     */
    @AIField(type = AFieldType.text)
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
