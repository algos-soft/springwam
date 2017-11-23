package it.algos.springvaadin.entity.stato;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
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

/**
 * Created by gac on 10-ago-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 * <p>
 * Gli stati vengono classificati secondo la norma ISO 3166
 *
 * @https://it.wikipedia.org/wiki/ISO_3166
 */
@SpringComponent
@Document()
@AIEntity(company = ACompanyRequired.nonUsata)
@AIList(showsID = true, widthID = 80, columns = {"ordine", "nome", "prova", "alfaDue", "alfaTre", "numerico"})
@AIForm(showsID = true, widthIDEM = 4)
@AISearch(fields = {"nome", "alfaDue"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Stato extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di nuovo (obbligatorio, unico, con controllo automatico prima del save se è zero, non modificabile)
     * inserito automaticamente
     * se si cancella una entity, rimane il 'buco' del numero
     */
    @NotNull
    @Indexed(unique = true)
    @AIField(type = AFieldType.integer, help = "Ordine di stato. Unico e normalmente progressivo", dev = FieldAccessibility.showOnly)
    @AIColumn(name = "#", width = 55)
    private int ordine;


    /**
     * nome corrente completo, non ufficiale (obbligatorio ed unico). Codifica ISO 3166/MA
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 4)
    @AIField(type = AFieldType.text, required = true, focus = true, firstCapital = true, help = "Codifica ISO 3166/MA")
    @AIColumn(width = 250)
    private String nome;


    /**
     * codice alfabetico di 2 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-2
     * 249 codici assegnati
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 2, max = 2)
    @AIField(type = AFieldType.text, widthEM = 6, allUpper = true, onlyLetter = true, help = "Codifica ISO 3166-1 alpha-2")
    @AIColumn(width = 100)
    private String alfaDue;


    /**
     * codice alfabetico di 3 cifre (obbligatorio, unico). Codifica ISO 3166-1 alpha-3
     * 249 codici assegnati
     */
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 3, max = 3)
    @AIField(type = AFieldType.text, widthEM = 6, allUpper = true, onlyLetter = true, help = "Codifica ISO 3166-1 alpha-3")
    @AIColumn(width = 100)
    private String alfaTre;


    /**
     * codice numerico di 3 cifre numeriche (facoltativo, vuoto oppure unico). Codifica ISO 3166-1 numerico
     * 249 codici assegnati
     */
    @Indexed(unique = false)
    @Size(min = 3, max = 3)
    @AIField(type = AFieldType.text, widthEM = 6, onlyNumber = true, help = "Codifica ISO 3166-1 numerico")
    @AIColumn(width = 100, name = "Code")
    private String numerico;


    /**
     * immagine bandiera (facoltativo, unico).
     */
    @Indexed(unique = false)
    @AIField(type = AFieldType.image, widthEM = 8)
    @AIColumn(width = 100)
    private byte[] bandiera;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome();
    }// end of method


}// end of entity class
