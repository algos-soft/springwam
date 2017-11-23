package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.annotation.*;
import it.algos.springvaadin.entity.ACompanyEntity;
import it.algos.springvaadin.entity.ACompanyRequired;
import it.algos.springvaadin.field.AFieldType;
import it.algos.springvaadin.field.FieldAccessibility;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.list.ListButton;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.servizio.Servizio;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.List;

/**
 * Created by gac on 24-set-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_FUN)
@AIEntity(company = ACompanyRequired.obbligatoria)
@AIList(columns = {"ordine", "icona", "code", "sigla", "descrizione"}, dev = ListButton.standard, admin = ListButton.noSearch, user = ListButton.show)
@AIForm(fields = {"ordine", "icona", "code", "sigla", "descrizione", "note"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Funzione extends ACompanyEntity {


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
            type = AFieldType.integer,
            name = "Ordine di presentazione nelle liste",
            widthEM = 3,
            help = "Ordine della funzione. Unico e progressivo nella company",
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
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
            type = AFieldType.text,
            required = true,
            name = "Codifica interna unica",
            focus = true,
            widthEM = 9,
            admin = FieldAccessibility.newOnly)
    @AIColumn(name = "Code", width = 120, roleTypeVisibility = ARoleType.developer)
    private String code;


    /**
     * sigla di codifica visibile (obbligatoria, non unica)
     */
    @NotEmpty
    @Size(min = 2, max = 20)
    @Indexed()
    @AIField(
            type = AFieldType.text,
            required = true,
            name = "Sigla visibile nel tabellone",
            widthEM = 9,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Sigla", width = 120)
    private String sigla;


    /**
     * descrizione (obbligatoria, non unica)
     */
    @NotEmpty
    @Size(min = 4)
    @AIField(
            type = AFieldType.text,
            required = true,
            name = "Descrizione completa",
            widthEM = 26,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Descrizione", width = 500)
    private String descrizione;


    /**
     * icona (facoltativa)
     */
    @AIField(
            type = AFieldType.icon,
            name = "Icona grafica rappresentativa della funzione",
            widthEM = 8,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.never)
    @AIColumn(roleTypeVisibility = ARoleType.nobody, width = 85)
    private int icona;


    /**
     * funzione obbligatoria o facoltativa per uno specifico servizio (ha senso solo per il servizio in cui è 'embedded')
     * Il flag NON viene usato direttamente da questa funzione.
     * Quando un servizio usa una funzione, ne effettua una 'copia' e la mantiene al suo interno per uso esclusivo.
     * Se modifico successivamente all'interno del servizio la copia della funzione, le modifiche rimangono circostritte a quello specifico servizio
     * Se modifico successivamente questa funzione, le modifiche NON si estendono alle funzioni 'congelata' nei singoli servizi
     * Possono esserci decine di copie di questa funzione, 'embedded' nei servizi ed ognuna avere property diverse tra di loro, se sono state modifcate all'interno del singolo servizio
     */
    @AIField(type = AFieldType.checkbox, roleTypeVisibility = ARoleType.nobody)
    @AIColumn(roleTypeVisibility = ARoleType.nobody)
    private boolean obbligatoria;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getSigla();
    }// end of method

//@todo da sviluppare
//    //--funzioni che vengono automaticamente abilitate se questa funzione è abilitata
//    @OneToMany
//    public List<Funzione> funzioniDipendenti = new ArrayList<>();


}// end of entity class
