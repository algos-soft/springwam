package it.algos.springvaadin.entity.log;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.ListButton;
import it.algos.springvaadin.login.ARoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by gac on 30-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document()
@AIEntity(company = ACompanyRequired.obbligatoria)
@AIList(dev = ListButton.standard, admin = ListButton.edit)
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Log extends ACompanyEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * rilevanza del log (obbligatorio)
     */
    @NotEmpty(message = "Livello del log è obbligatorio")
    @Indexed()
    @AIField(type = AFieldType.enumeration, clazz = LogLevel.class, nullSelectionAllowed = false, widthEM = 10, admin = FieldAccessibility.showOnly)
    @AIColumn(width = 100)
    private LogLevel livello;


    /**
     * raggruppamento logico dei log per categorie di eventi (obbligatorio)
     */
    @NotEmpty(message = "La tipologia del log è obbligatoria")
    @Indexed()
    @AIField(type = AFieldType.enumeration, clazz = LogType.class, nullSelectionAllowed = false, widthEM = 10, admin = FieldAccessibility.showOnly)
    @AIColumn(width = 140)
    private String gruppo;


    /**
     * descrizione: completa in forma testo (obbligatoria)
     */
    @NotEmpty(message = "La descrizione è obbligatoria")
    @Indexed()
    @Size(min = 2, max = 100)
    @AIField(type = AFieldType.textarea, focus = true, firstCapital = true, numRowsTextArea = 2, widthEM = 24, help = "Messaggio del log", admin = FieldAccessibility.showOnly)
    @AIColumn(width = 350)
    private String descrizione;


    /**
     * Data dell'evento (obbligatoria, non modificabile)
     * Gestita in automatico
     * Field visibile solo al buttonAdmin
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.localdatetime, name = "Data dell'evento di log", admin = FieldAccessibility.showOnly)
    @AIColumn(name = "Data", roleTypeVisibility = ARoleType.admin)
    public LocalDateTime evento;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getGruppo() + " - " + getDescrizione();
    }// end of method


}// end of entity class
