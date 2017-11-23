package it.algos.springvaadin.entity.versione;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by gac on 01/06/17
 * <p>
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 * <p>
 * Tipicamente usata dal developer per gestire le versioni, patch e release dell'applicazione
 * Non prevede la differenziazione per Company
 */
@SpringComponent
@Document()
@AIEntity(company = ACompanyRequired.facoltativa)
@AIList(columns = {"progetto", "ordine", "gruppo", "descrizione", "evento"}, admin = ListButton.edit)
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Versione extends ACompanyEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * progetto (obbligatorio, non unica)
     */
    @NotEmpty()
    @Indexed()
    @AIField(type = AFieldType.text, dev = FieldAccessibility.newOnly, admin = FieldAccessibility.never)
    @AIColumn(width = 160)
    private String progetto;


    /**
     * ordine di versione (obbligatorio, unico per progetto, con controllo automatico prima del save se è zero, non modificabile)
     * inserito automaticamente
     * se si cancella una entity, rimane il 'buco' del numero
     * unico indipendentemente dalla company
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.integer, widthEM = 3, help = "Ordine di versione. Unico e normalmente progressivo", dev = FieldAccessibility.newOnly, admin = FieldAccessibility.showOnly)
    @AIColumn(name = "#", width = 55)
    private int ordine;


    /**
     * codifica di gruppo per identificare la tipologia della versione (obbligatoria, non unica)
     */
    @NotEmpty(message = "Il titolo del gruppo è obbligatorio")
    @Indexed()
    @Size(min = 2, max = 20)
    @AIField(type = AFieldType.text, focus = true, help = "Tipologia della versione", admin = FieldAccessibility.showOnly)
    @AIColumn(width = 140)
    private String gruppo;


    /**
     * descrizione (obbligatoria, non unica)
     * non va inizializzato con una stringa vuota, perché da Vaadin 8 in poi lo fa automaticamente
     */
    @NotEmpty(message = "La descrizione è obbligatoria")
    @Size(min = 4, max = 200)
    @AIField(type = AFieldType.text, firstCapital = true, widthEM = 32, help = "Descrizione della versione", admin = FieldAccessibility.showOnly)
    @AIColumn(width = 600)
    private String descrizione;


    /**
     * momento in cui si effettua la edit della versione (obbligatoria, non unica, non modificabile)
     * inserita automaticamente
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.localdate, help = "Data di inserimento della versione", dev = FieldAccessibility.newOnly, admin = FieldAccessibility.showOnly)
    @AIColumn(name = "Data")
    private LocalDate evento;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getGruppo() + " - " + getDescrizione();
    }// end of method


}// end of entity class
