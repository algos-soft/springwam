package it.algos.springwam.entity.croce;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.log.LogLevel;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaPresenter;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.list.ListButton;
import it.algos.springwam.application.AppCost;
import it.algos.webbase.web.field.AFType;
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
 * Created by gac on 31-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_CRO)
@AIEntity(company = ACompanyRequired.nonUsata)
@AIList(columns = {"code", "descrizione", "presidente", "contatto"}, dev = ListButton.standard, admin = ListButton.noSearch, user = ListButton.show)
//@AIForm(fields = {"code", "presidente", "note"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Croce extends Company {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * organizzazione (facoltativo)
     */
    @AIField(type = AFieldType.enumeration, clazz = Organizzazione.class, nullSelectionAllowed = false, widthEM = 30, admin = FieldAccessibility.showOnly)
    @AIColumn(width = 100)
    private Organizzazione organizzazione;


    /**
     * presidente (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = AFieldType.link, clazz = PersonaPresenter.class, admin = FieldAccessibility.allways, user = FieldAccessibility.showOnly)
    @AIColumn(width = 250)
    private Persona presidente;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method

}// end of entity class
