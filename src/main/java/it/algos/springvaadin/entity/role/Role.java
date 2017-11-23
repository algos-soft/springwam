package it.algos.springvaadin.entity.role;

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
 * Created by gac on 11-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document()
@AIEntity( company = ACompanyRequired.nonUsata)
@AIList()
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Role extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di rilevanza (obbligatorio, unico)
     * il più importante per primo
     */
    @NotNull
    @Indexed()
    @AIField(type = AFieldType.integer, widthEM = 3, dev = FieldAccessibility.showOnly)
    @AIColumn(name = "#", width = 55)
    private int ordine;

    /**
     * codice di riferimento (obbligatorio, unico)
     */
    @NotEmpty
    @Size()
    @Indexed()
    @AIField(type = AFieldType.text, required = true, widthEM = 12, dev = FieldAccessibility.showOnly)
    @AIColumn(width = 210)
    private String code;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method


}// end of entity class
