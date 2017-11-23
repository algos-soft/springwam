package it.algos.springvaadin.entity.indirizzo;


import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.login.ARoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

/**
 * Created by gac on 01/06/17
 * <p>
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 * <p>
 * Usato da altre 'collection' (ex moduli)
 * Questa Entity viene usata 'embedded'- Si può 'switchare' a @DBRef, implementando Service e Repository
 */
@SpringComponent
@Document()
@AIEntity(company = ACompanyRequired.nonUsata)
@AIList()
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Indirizzo extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * indirizzo: via, nome e numero (obbligatoria, non unica)
     */
    @NotEmpty(message = "L'indirizzo è obbligatorio")
    @Size(min = 2, max = 50)
    @AIField(type = AFieldType.text, widthEM = 20, focus = true, help = "Via, nome e numero")
    @AIColumn(width = 300)
    private String indirizzo;


    /**
     * località (obbligatoria, non unica)
     */
    @NotEmpty()
    @Size(min = 4, max = 40)
    @AIField(type = AFieldType.text, firstCapital = true, widthEM = 20, help = "Città, comune, paese")
    @AIColumn(width = 300)
    private String localita;


    /**
     * codice di avviamento postale (facoltativo, non unica)
     */
    @Size(min = 5, max = 5, message = "Il codice postale deve essere di 5 cifre")
    @AIField(type = AFieldType.text, widthEM = 5, help = "Codice di avviamento postale")
    @AIColumn(width = 90)
    private String cap;


    /**
     * stato (obbligatoria, non unica)
     * riferimento dinamico con @DBRef (obbligatorio per il ComboBox)
     */
    @DBRef
    @NotEmpty()
    @AIField(type = AFieldType.combo, clazz = Stato.class)
    @AIColumn(width = 140)
    private Stato stato;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String value = "";
        String spazio = " ";
        String sep = " - ";

        value += indirizzo;
        value += (LibText.isValid(cap) ? sep + cap : sep);
        value += spazio + localita;
        value += spazio + (stato != null ? stato : "");

        return value;
    }// end of method


}// end of entity class
