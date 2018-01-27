package it.algos.springvaadin.entity.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.IAUser;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.context.annotation.Scope;
import lombok.*;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.enumeration.EARoleType;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EACompanyRequired;
import it.algos.springvaadin.enumeration.EAFieldAccessibility;
import it.algos.springvaadin.enumeration.EAFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.AEntity;

/**
 * Created by gac on 11-nov-17
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity
 * Annotated with @Scope (obbligatorio = 'session')
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Annotated with @Builder (Lombok) lets you automatically produce the code required to have your class
 * be instantiable with code such as: Person.builder().name("Adam Savage").city("San Francisco").build();
 * Annotated with @EqualsAndHashCode (facoltativo) per ???
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIEntity (facoltativo) per alcuni parametri generali del modulo
 * Annotated with @AIList (facoltativo) per le colonne della Lista e loro visibilità/accessibilità relativa all'utente
 * Annotated with @AIForm (facoltativo) per i fields del Form e loro visibilità/accessibilità relativa all'utente
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "user")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(ACost.TAG_USE)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.obbligatoria)
@AIList(dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIScript(sovrascrivibile = false)
public class User extends ACEntity implements IAUser {

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
            type = EAFieldType.text,
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
            type = EAFieldType.text,
            required = true,
            widthEM = 12,
            admin = EAFieldAccessibility.allways,
            user = EAFieldAccessibility.showOnly)
    @AIColumn(name = "Password", width = 200)
    private String password;


    /**
     * ruolo (obbligatorio, non unico)
     * riferimento dinamico con @DBRef (obbligatorio per il ComboBox)
     */
    @DBRef
    @AIField(type = EAFieldType.combo, required = true, clazz = Role.class)
    @AIColumn(name = "Ruolo", width = 200)
    public Role role;


    /**
     * buttonUser abilitato (facoltativo, di default true)
     */
    @AIField(type = EAFieldType.checkboxlabel, required = true, admin = EAFieldAccessibility.allways)
    @AIColumn(name = "OK")
    private boolean enabled;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return nickname;
    }// end of method

    /**
     * @return the password (encrypted)
     */
    @Override
    public String getEncryptedPassword() {
        return null;
    }// end of method


    /**
     * Validate a password for this current user.
     *
     * @param password the password
     *
     * @return true if valid
     */
    @Override
    public boolean validatePassword(String password) {
        boolean valid = false;

        if (isEnabled()) {
            if (this.password.equals(password)) {
                valid = true;
            }// end of if cycle
        }// end of if cycle

        return valid;
    }// end of method


    /**
     * @return true if this user is admin
     */
    @Override
    public boolean isAdmin() {
        return false;
    }// end of method

    /**
     * @return true if this user is developer
     */
    @Override
    public boolean isDeveloper() {
        return false;
    }// end of method

}// end of entity class
