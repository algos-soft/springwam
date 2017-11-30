package it.algos.springwam.entity.riga;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 19-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_RIG)
@AIEntity(roleTypeVisibility = ARoleType.developer, company = ACompanyRequired.obbligatoria)
@AIList()
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Riga extends ACompanyEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * giorno di riferimento (obbligatorio)
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.date)
    @AIColumn()
    private LocalDate giorno;


    /**
     * servizio di riferimento (obbligatorio)
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.text)
    @AIColumn()
    private Servizio servizio;



    /**
     * turni effettuati (facoltativi, di norma 7)
     */
    @AIField(type = AFieldType.text)
    @AIColumn()
    private List<Turno> turni;



}// end of entity class
