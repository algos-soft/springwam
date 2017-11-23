package it.algos.springvaadin.entity.company;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoPresenter;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaPresenter;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.ListButton;
import it.algos.springvaadin.login.ARoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
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
 */
@SpringComponent
@Document()
@AIEntity(roleTypeVisibility = ARoleType.admin, company = ACompanyRequired.nonUsata)
@AIList(columns = {"code", "descrizione", "contatto", "telefono", "email"}, admin = ListButton.edit)
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Company extends AEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * codice di riferimento interno (obbligatorio ed unico)
     */
    @NotEmpty(message = "La sigla interna è obbligatoria")
    @Size(min = 2, max = 20)
    @AIField(type = AFieldType.text, widthEM = 8, focus = true, name = "Sigla", help = "Codice interno unico", admin = FieldAccessibility.showOnly)
    @AIColumn(name = "Sigla", width = 100)
    private String code;


    /**
     * ragione sociale o descrizione della company (obbligatoria, non unica)
     */
    @NotEmpty(message = "La descrizione è obbligatoria")
    @Size(min = 2, max = 50)
    @AIField(type = AFieldType.text, firstCapital = true, widthEM = 24, help = "Descrizione della company", admin = FieldAccessibility.allways)
    @AIColumn(width = 370)
    private String descrizione;


    /**
     * persona di riferimento (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = AFieldType.link, clazz = PersonaPresenter.class, help = "Riferimento", admin = FieldAccessibility.allways)
    @AIColumn(width = 220, name = "Riferimento")
    private Persona contatto;


    /**
     * telefono (facoltativo)
     */
    @AIField(type = AFieldType.text, admin = FieldAccessibility.allways)
    @AIColumn(width = 170)
    private String telefono;


    /**
     * posta elettronica (facoltativo)
     */
    @Email
    @AIField(type = AFieldType.email, widthEM = 24, admin = FieldAccessibility.allways)
    @AIColumn(width = 350, name = "Mail")
    private String email;


    /**
     * indirizzo (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = AFieldType.link, clazz = IndirizzoPresenter.class, help = "Indirizzo", admin = FieldAccessibility.allways)
    @AIColumn(width = 400, name = "Indirizzo", roleTypeVisibility = ARoleType.nobody)
    private Indirizzo indirizzo;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method

}// end of class
