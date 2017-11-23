package it.algos.springvaadin.entity.persona;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoPresenter;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.login.ARoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

/**
 * Created by gac on 11-ott-17
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
@AIEntity(company = ACompanyRequired.facoltativa)
@AIList()
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Persona extends ACompanyEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * nome (obbligatorio, non unica)
     */
    @NotEmpty
    @Size(min = 4, max = 40)
    @AIField(type = AFieldType.text, required = true, focus = true, firstCapital = true)
    @AIColumn(width = 200)
    private String nome;


    /**
     * cognome (obbligatorio, non unica)
     */
    @NotEmpty
    @AIField(type = AFieldType.text, required = true, firstCapital = true)
    @AIColumn(width = 200)
    private String cognome;


    /**
     * telefono (facoltativo)
     */
    @AIField(type = AFieldType.text)
    @AIColumn(width = 160)
    private String telefono;


    /**
     * posta elettronica (facoltativo)
     */
    @Email
    @AIField(type = AFieldType.email, widthEM = 24)
    @AIColumn(width = 350, name = "Mail")
    private String email;


    /**
     * indirizzo (facoltativo, non unica)
     * riferimento statico SENZA @DBRef (embedded)
     */
    @AIField(type = AFieldType.link, clazz = IndirizzoPresenter.class, help = "Indirizzo")
    @AIColumn(width = 400, name = "Indirizzo")
    private Indirizzo indirizzo;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome() + " " + getCognome();
    }// end of method

}// end of entity class
