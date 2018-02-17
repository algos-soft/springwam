package it.algos.springwam.entity.servizio;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.springwam.entity.funzione.Funzione;
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

import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: 2018-01-16_08:50:45
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
@Document(collection = "servizio")
@Scope("session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Qualifier(AppCost.TAG_SER)
@AIEntity(roleTypeVisibility = EARoleType.user, company = EACompanyRequired.obbligatoria)
@AIList(fields = {"ordine", "code", "descrizione", "oraInizio", "oraFine", "visibile", "colore"}, dev = EAListButton.standard, admin = EAListButton.noSearch, user = EAListButton.show)
@AIForm(fields = {"code", "descrizione", "colore", "orario", "oraInizio", "minutiInizio", "oraFine", "minutiFine", "visibile", "note"})
@AIScript(sovrascrivibile = false)
public class Servizio extends ACEntity {

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
            name = "Sigla interna unica",
            focus = true,
            widthEM = 9)
    @AIColumn(name = "Sigla", width = 140)
    private String code;


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
    @AIColumn(name = "Descrizione", width = 400)
    private String descrizione;


    /**
     * colore del servizio (facoltativo)
     */
    @AIField(
            type = EAFieldType.integer,
            name = "Colore distintivo per il tabellone",
            widthEM = 3)
    @AIColumn(name = "Colore", width = 120)
    private int colore = new Color(128, 128, 128).getRGB();


    /**
     * orario predefinito (obbligatorio, avis, centralino ed extra non ce l'hanno)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @AIField(
            type = EAFieldType.checkbox,
            name = "Turno con orario predefinito",
            widthEM = 3)
    @AIColumn(name = "Orario", width = 90)
    private boolean orario;


    /**
     * ora prevista di inizio turno (obbligatoria, se orario è true)
     */
    @AIField(
            type = EAFieldType.integer,
            name = "Ora inizio turno",
            widthEM = 3)
    @AIColumn(name = "Inizio", width = 120)
    private int oraInizio;

    /**
     * minuti previsti di inizio turno (facoltativo, standard è zero)
     * nella GUI la scelta viene bloccata ai quarti d'ora
     */
    @AIField(
            type = EAFieldType.integer,
            name = "Minuti inizio turno",
            widthEM = 3)
    @AIColumn(name = "->M", width = 65)
    private int minutiInizio = 0;

    /**
     * ora prevista di fine turno (obbligatoria, se orario è true)
     */
    @AIField(
            type = EAFieldType.integer,
            name = "Ora (prevista) fine turno",
            widthEM = 3)
    @AIColumn(name = "Fine", width = 120)
    private int oraFine;

    /**
     * minuti previsti di fine turno (facoltativo, standard è zero)
     * nella GUI la scelta viene bloccata ai quarti d'ora
     */
    @AIField(
            type = EAFieldType.integer,
            name = "Minuti (previsti) fine turno",
            widthEM = 3)
    @AIColumn(name = "M->", width = 65)
    private int minutiFine = 0;


    /**
     * visibile nel tabellone
     * al contrario è disabilitato dal tabellone, perchè non più utilizzato
     * sempre visibile nello storico per i periodi in cui è stato eventualmente utilizzato
     * (di default true)
     * colonna non visibile nella grid, perché la costruisce specifica in ServizioList
     */
    @AIField(
            type = EAFieldType.checkbox,
            name = "Attivo, visibile nel tabellone",
            widthEM = 3)
    @AIColumn(name = "Tab.", width = 100)
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
    @AIField(type = EAFieldType.noone, widthEM = 20, name = "Funzioni previste per espletare il servizio")
    @AIColumn()
    private List<Funzione> funzioni;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCode();
    }// end of method

    /**
     * Ritorna una stringa che rappresenta l'orario dalle... alle...
     */
    public String getStrOrario() {
        if (isOrario()) {
            return strHM(oraInizio) + ":" + strHM(minutiInizio) + " - " + strHM(oraFine) + ":" + strHM(minutiFine);
        } else {
            return "Variabile";
        }// end of if/else cycle
    }// end of method

    /**
     * @return il numero di ore o minuti formattato su 2 caratteri fissi
     */
    private String strHM(int num) {
        String s = "" + num;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

}// end of entity class
