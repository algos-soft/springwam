package it.algos.springwam.entity.croce;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaPresenter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.index.Indexed;
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
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_22:57:46
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
 * Inserisce SEMPRE la versione di serializzazione che viene poi filtrata per non mostrarla in List e Form
 * Le singole property sono annotate con @AIField (obbligatorio per il tipo di Field) e @AIColumn (facoltativo)
 */
@SpringComponent
@Document(collection = "croce")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_CRO)
@AIEntity(roleTypeVisibility = EARoleType.user, company = EACompanyRequired.nonUsata)
@AIList(fields = {"code", "descrizione", "presidente", "contatto"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"code", "descrizione", "presidente", "contatto"})
@AIScript(sovrascrivibile = false)
public class Croce extends Company {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;



    /**
     * organizzazione (facoltativo)
     */
    @AIField(type = EAFieldType.enumeration, clazz = EAOrganizzazione.class, nullSelectionAllowed = false, widthEM = 30)
    @AIColumn(width = 100)
    private EAOrganizzazione organizzazione;


    /**
     * presidente (facoltativo)
     * riferimento statico SENZA @DBRef
     */
    @AIField(type = EAFieldType.link, clazz = PersonaPresenter.class)
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
