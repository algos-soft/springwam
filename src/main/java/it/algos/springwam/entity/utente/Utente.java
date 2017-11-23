package it.algos.springwam.entity.utente;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserPresenter;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springvaadin.list.ListButton;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.webbase.web.field.AFType;
import it.algos.webbase.web.search.SearchManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 16-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_UTE)
@AIEntity(company = ACompanyRequired.obbligatoria)
@AIList(columns = {"nome", "cognome", "telefono"},dev = ListButton.standard, admin = ListButton.standard, user = ListButton.show)
@AIForm()
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Utente extends Persona {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * indirizzo (facoltativo)
     * riferimento dinamico CON @DBRef
     */
    @DBRef
    @OneToOne
    @AIField(type = AFieldType.link, name = "Nickname e password", clazz = UserPresenter.class, admin = FieldAccessibility.allways)
    @AIColumn(roleTypeVisibility = ARoleType.nobody)
    private User user;


    /**
     * Dati dell'associazione
     */
    @AIField(type = AFieldType.checkbox)
    @AIColumn(name="adm")
    private boolean admin = false;

    @AIField(type = AFieldType.checkbox)
    @AIColumn(name="dip")
    private boolean dipendente = false;

    @AIField(type = AFieldType.checkbox)
    @AIColumn(name="inf")
    private boolean infermiere = false;

//    @it.algos.webbase.web.field.AIField(type = AFType.date, caption = "Data di nascita", help = "Data di nascita (facoltativa)")
//    private Date dataNascita = null;
//
//    @it.algos.webbase.web.field.AIField(type = AFType.checkbox, caption = "Invio Mail Turni")
//    private boolean invioMail = false;


    /**
     * Funzioni abilitate per questo milite/volontario/utente
     * Ozionali come programma, ma in pratica almeno una è indispensabile
     * Siccome sono 'embedded' in utente, non serve @OneToMany() o @ManyToOne()
     * Usando la caratteristica 'embedded', la funzione viene ricopiata dentro utente come si trova al momento.
     * Se modifico successivamente all'interno di utente la copia della funzione, le modifiche rimangono circostritte a questo singolo utente
     * Se modifico successivamente la funzione originaria, le modifiche NON si estendono alla funzione 'congelata' in utente
     */
    @AIField(type = AFieldType.noone, widthEM = 24, name = "Funzioni abilitate per questo utente")
    @AIColumn()
    private List<Funzione> funzioni;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getNome() + " " + getCognome();
    }// end of method


}// end of entity class
