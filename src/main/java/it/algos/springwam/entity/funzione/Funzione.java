package it.algos.springwam.entity.funzione;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
import it.algos.springvaadin.entity.ACEntity;
import it.algos.springvaadin.lib.ACost;
import it.algos.springwam.application.AppCost;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-13_16:21:12
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
@Document(collection = "funzione")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_FUN)
@AIEntity(roleTypeVisibility = EARoleType.admin, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"ordine", "sigla", "descrizione"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"icona", "sigla", "descrizione", "note"})
@AIScript(sovrascrivibile = false)
public class Funzione extends ACEntity {

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * ordine di presentazione nelle liste (obbligatorio, unico nella company,
     * con controllo automatico prima del save se è zero)
     * modificabile da developer ed admin non dall'utente
     * unico all'interno della company
     */
    @NotNull
    @Indexed()
    @AIField(
            type = EAFieldType.integer,
            name = "Ordine di presentazione nelle liste",
            widthEM = 3,
            help = "Ordine della funzione. Unico e progressivo nella company")
    @AIColumn(name = "#", width = 55)
    private int ordine;


    /**
     * sigla di codifica interna specifica per ogni company (obbligatorio, unico nella company)
     * visibile solo per admin e developer
     */
    @NotEmpty
    @Size(min = 2, max = 20)
    @Indexed()
    @AIField(
            type = EAFieldType.text,
            required = true,
            name = "Codifica interna unica",
            focus = true,
            widthEM = 9)
    @AIColumn(name = "Code", width = 120)
    private String code;


    /**
     * sigla di codifica visibile (obbligatoria, non unica)
     */
    @NotEmpty
    @Size(min = 2, max = 20)
    @Indexed()
    @AIField(
            type = EAFieldType.text,
            required = true,
            name = "Sigla visibile nel tabellone",
            widthEM = 9)
    @AIColumn(name = "Sigla", width = 120)
    private String sigla;


    /**
     * descrizione (obbligatoria, non unica)
     */
    @NotEmpty
    @Size(min = 4)
    @AIField(
            type = EAFieldType.text,
            required = true,
            name = "Descrizione completa",
            widthEM = 26)
    @AIColumn(name = "Descrizione", width = 500)
    private String descrizione;


    /**
     * icona (facoltativa)
     */
    @AIField(
            type = EAFieldType.icon,
            name = "Icona grafica rappresentativa della funzione",
            widthEM = 8)
    @AIColumn(name = "Icona", width = 90)
    private int icona;


    /**
     * funzione obbligatoria o facoltativa per uno specifico servizio (ha senso solo per il servizio in cui è 'embedded')
     * Il flag NON viene usato direttamente da questa funzione.
     * Quando un servizio usa una funzione, ne effettua una 'copia' e la mantiene al suo interno per uso esclusivo.
     * Se modifico successivamente all'interno del servizio la copia della funzione, le modifiche rimangono circostritte a quello specifico servizio
     * Se modifico successivamente questa funzione, le modifiche NON si estendono alle funzioni 'congelata' nei singoli servizi
     * Possono esserci decine di copie di questa funzione, 'embedded' nei servizi ed ognuna avere property diverse tra di loro, se sono state modifcate all'interno del singolo servizio
     */
    @AIField(type = EAFieldType.checkbox, roleTypeVisibility = EARoleType.nobody)
    @AIColumn()
    private boolean obbligatoria;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method


}// end of entity class
