package it.algos.springwam.entity.iscrizione;

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
import it.algos.springwam.entity.funzione.FunzionePresenter;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioPresenter;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.utente.Utente;
import it.algos.springwam.entity.utente.UtentePresenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by gac on 22-nov-17
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service
 * Estende la Entity astratta AEntity che contiene la key property ObjectId
 */
@SpringComponent
@Document(collection = AppCost.TAG_ISC)
@AIEntity(roleTypeVisibility = ARoleType.developer)
@AIList(columns = {"code", "descrizione"}, dev = ListButton.standard, admin = ListButton.noSearch, user = ListButton.show)
@AIForm(fields = {"code", "descrizione", "note"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Iscrizione extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * turno di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AFieldType.link, clazz = ServizioPresenter.class)
    private Turno turno ;


    /**
     * utente di riferimento (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AFieldType.link, clazz = UtentePresenter.class)
    private Utente utente ;

    /**
     * funzione per cui il milite/volontario/utente si iscrive (obbligatorio)
     * riferimento dinamico CON @DBRef
     */
    @NotNull
    @DBRef
    @AIField(type = AFieldType.link, clazz = FunzionePresenter.class)
    private Funzione funzione ;


    /**
     * timestamp di creazione (obbligatorio, inserito in automatico)
     * (usato per bloccare la cancIscrizione dopo un determinato intervallo di tempo)
     */
    @NotNull
    @AIField(type = AFieldType.localdatetime)
    private LocalDateTime timestamp ;


    /**
     * durata effettiva del turno del milite/volontario di questa iscrizione (obbligatorio, proposta come dal servizio)
     */
    @NotNull
    @AIField(type = AFieldType.integer)
    private int minutiEffettivi ;


    /**
     * eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
     * serve per evidenziare il problema nel tabellone
     */
    @AIField(type = AFieldType.checkbox)
    private boolean esisteProblema ;


    /**
     * eventuale nota associata al milite/volontario
     * serve per evidenziare il problema nel tabellone
     */
    @AIField(type = AFieldType.textarea)
    private String nota;


    /**
     * se è stata inviata la notifica di inizio turno dal sistema di notifiche automatiche
     */
    @AIField(type = AFieldType.checkbox)
    private boolean notificaInviata;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getUtente().getCognome() + getFunzione();
    }// end of method


}// end of entity class
