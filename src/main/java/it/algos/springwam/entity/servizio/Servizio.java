package it.algos.springwam.entity.servizio;

import com.vaadin.shared.ui.colorpicker.Color;
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
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.webbase.web.field.AFType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by gac on 30-ott-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_SER)
@AIEntity(company = ACompanyRequired.obbligatoria)
@AIList(columns = {"ordine", "code", "descrizione"}, dev = ListButton.standard, admin = ListButton.noSearch, user = ListButton.show)
//@AIForm(fields = {"ordine","code", "descrizione", "note"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Servizio extends ACompanyEntity {


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
            name = "Ordine di presentazione nel tabellone",
            widthEM = 3,
            help = "Ordine di apparizione nei PopUp. Modificabile nella lista con i bottoni Sposta su e giu",
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "#", width = 55)
    private int ordine;

    /**
     * codice di riferimento (obbligatorio)
     */
    @NotEmpty
    @Size(min = 2, max = 20)
    @Indexed()
    @AIField(
            type = AFieldType.text,
            required = true,
            focus = true,
            name = "Sigla visibile nel tabellone",
            widthEM = 9,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Sigla", width = 170)
    private String code;


    /**
     * descrizione2 (obbligatoria)
     */
    @NotEmpty
    @AIField(
            type = AFieldType.text,
            required = true,
            name = "Descrizione completa",
            widthEM = 24,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Descrizione", width = 320)
    private String descrizione;


    /**
     * colore del servizio (facoltativo)
     */
    @AIField(
            type = AFieldType.integer,
            name = "Colore distintivo per il tabellone",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(width = 50, roleTypeVisibility = ARoleType.nobody)
    private int colore = new Color(128, 128, 128).getRGB();


    /**
     * orario predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @AIField(
            type = AFieldType.checkbox,
            name = "Orario predefinito del turno",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Orario", width = 90, roleTypeVisibility = ARoleType.user)
    private boolean orario;


    /**
     * ora prevista di inizio turno (obbligatoria, se orario è true)
     */
    @AIField(
            type = AFieldType.integer,
            name = "Ora inizio turno",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "In", width = 65, roleTypeVisibility = ARoleType.admin)
    private int oraInizio;

    /**
     * minuti previsti di inizio turno (facoltativo, standard è zero)
     * nella GUI la scelta viene bloccata ai quarti d'ora
     */
    @AIField(
            type = AFieldType.integer,
            name = "Minuti inizio turno",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(width = 60, roleTypeVisibility = ARoleType.nobody)
    private int minutiInizio = 0;

    /**
     * ora prevista di fine turno (obbligatoria, se orario è true)
     */
    @AIField(
            type = AFieldType.integer,
            name = "Ora (prevista) fine turno",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Out", width = 65, roleTypeVisibility = ARoleType.admin)
    private int oraFine;

    /**
     * minuti previsti di fine turno (facoltativo, standard è zero)
     * nella GUI la scelta viene bloccata ai quarti d'ora
     */
    @AIField(
            type = AFieldType.integer,
            name = "Minuti (previsti) fine turno",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(width = 50, roleTypeVisibility = ARoleType.nobody)
    private int minutiFine = 0;


    /**
     * visibile nel tabellone
     * al contrario è disabilitato dal tabellone, perchè non più utilizzato
     * sempre visibile nello storico per i periodi in cui è stato eventualmente utilizzato
     * (di default true)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @AIField(
            type = AFieldType.checkbox,
            name = "Attivo, visibile nel tabellone",
            widthEM = 3,
            admin = FieldAccessibility.allways,
            user = FieldAccessibility.showOnly)
    @AIColumn(name = "Tab.", width = 60, roleTypeVisibility = ARoleType.user)
    private boolean visibile;


    /**
     * Funzioni previste per espletare il servizio
     * Ozionali come programma, ma in pratica almeno una è indispensabile
     * Siccome sono 'embedded' in servizio, non serve @OneToMany() o @ManyToOne()
     * Possono essere 'obbligatorie' o meno (flag booleano).
     * Il flag appartiene alla singola funzione di questo servizio ma NON viene usato dalla funzione originaria.
     * Usando la caratteristica 'embedded', la funzione viene ricopiata dentro servizio come si trova al momento.
     * Se modifico successivamente all'interno del servizio la copia della funzione, le modifiche rimangono circostritte a questo singolo servizio
     * Se modifico successivamente la funzione originaria, le modifiche NON si estendono alla funzione 'congelata' nel servizio
     */
    @AIField(type = AFieldType.noone, widthEM = 20, name = "Funzioni previste per espletare il servizio")
    @AIColumn()
    private List<Funzione> funzioni;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method


}// end of entity class
