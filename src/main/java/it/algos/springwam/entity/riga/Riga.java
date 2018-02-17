package it.algos.springwam.entity.riga;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EACompanyRequired;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
@AIEntity(roleTypeVisibility = EARoleType.developer, company = EACompanyRequired.nonUsata)
@AIList(dev = EAListButton.standard)
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Riga extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * giorno di riferimento (obbligatorio)
     */
    @NotNull
    @Indexed()
    @AIField(type = EAFieldType.date)
    @AIColumn()
    private LocalDate giornoIniziale;


    /**
     * servizio di riferimento (obbligatorio)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @NotNull
    @Indexed()
    @AIField(type = EAFieldType.text)
    @AIColumn(width = 200)
    private Servizio servizio;


    /**
     * turni effettuati (facoltativi, di norma 7)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @AIField(type = EAFieldType.text)
    @AIColumn()
    private List<Turno> turni;


}// end of entity class
