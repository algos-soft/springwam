package it.algos.springvaadin.entity.user;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.login.ARoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document()
@AIEntity( company = ACompanyRequired.obbligatoria)
@AIList()
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends ACompanyEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * nickname di riferimento (obbligatorio, unico per company)
     */
    @NotEmpty
    @Size(min = 3, max = 20)
    @Indexed()
    @AIField(
            type = AFieldType.text,
            required = true,
            focus = true,
            name = "NickName",
            widthEM = 12)
    @AIColumn(name = "Nick", width = 300)
    private String nickname;


    /**
     * password (obbligatoria o facoltativa, non unica)
     */
    @Size(min = 3, max = 20)
    @AIField(
            type = AFieldType.text,
            required = true,
            name = "Password",
            widthEM = 12,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Password", width = 200)
    private String password;


    /**
     * buttonUser abilitato (facoltativo, di default true)
     */
    @AIField(type = AFieldType.checkbox, required = true, widthEM = 4, admin = FieldAccessibility.allways)
    @AIColumn(name = "OK")
    private boolean enabled = true;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNickname();
    }// end of method


}// end of entity class
