package it.algos.springwam.entity.utente;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserPresenter;
import it.algos.springvaadin.login.IAUser;
import it.algos.springwam.entity.funzione.Funzione;
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
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-16_10:27:41
 * Estende la Entity astratta ACEntity che contiene il riferimento alla property Company
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
@Document(collection = "utente")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_UTE)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"nome", "cognome"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"nome", "cognome"})
@AIScript(sovrascrivibile = false)
public class Utente extends Persona implements IAUser {

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


    @AIField(type = EAFieldType.checkbox)
    @AIColumn(name = "dip")
    private boolean dipendente = false;

    @AIField(type = EAFieldType.checkbox)
    @AIColumn(name = "inf")
    private boolean infermiere = false;


    /**
     * Funzioni abilitate per questo milite/volontario/utente
     * Ozionali come programma, ma in pratica almeno una è indispensabile
     * Siccome sono 'embedded' in utente, non serve @OneToMany() o @ManyToOne()
     * Usando la caratteristica 'embedded', la funzione viene ricopiata dentro utente come si trova al momento.
     * Se modifico successivamente all'interno di utente la copia della funzione, le modifiche rimangono circostritte a questo singolo utente
     * Se modifico successivamente la funzione originaria, le modifiche NON si estendono alla funzione 'congelata' in utente
     */
    @AIField(type = EAFieldType.noone, widthEM = 24, name = "Funzioni abilitate per questo utente")
    @AIColumn()
    private List<Funzione> funzioni;



    /**
     * @return the password (encrypted)
     */
    @Override
    public String getEncryptedPassword() {
        return null;
    }

    /**
     * Validate a password for this current user.
     *
     * @param password the password
     *
     * @return true if valid
     */
    @Override
    public boolean validatePassword(String password) {
        return false;
    }

    /**
     * @return true if this user is developer
     */
    @Override
    public boolean isDeveloper() {
        return false;
    }

    /**
     * @return true if this user is admin
     */
    @Override
    public boolean isAdmin() {
        return false;
    }


}// end of entity class
