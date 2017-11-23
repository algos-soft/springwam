package it.algos.springvaadin.entity.indirizzo;

import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyRepository;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.stato.Stato;
import it.algos.springvaadin.entity.stato.StatoService;
import it.algos.springvaadin.entity.versione.Versione;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAnnotation;
import it.algos.springvaadin.lib.LibSession;
import it.algos.springvaadin.lib.LibText;
import it.algos.springvaadin.service.AlgosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by gac on 07-ago-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come annotation
 */
@Service
@Qualifier(Cost.TAG_IND)
@Slf4j
public class IndirizzoService extends AlgosServiceImpl {

    private IndirizzoRepository repository;
    public StatoService statoService;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public IndirizzoService(@Qualifier(Cost.TAG_IND) MongoRepository repository, StatoService statoService) {
        super(repository);
        this.repository = (IndirizzoRepository) repository; //casting per uso locale
        this.statoService = statoService;
    }// end of Spring constructor


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * Properties obbligatorie
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Indirizzo findOrCrea(String indirizzo, String localita) {
        return findOrCrea(indirizzo, localita, "", (Stato) null);
    }// end of method


    /**
     * Ricerca e nuovo di una entity (la crea se non la trova)
     * All properties
     * Le entites di questa collezione sono 'embedded', quindi non ha senso controllare se esiste già nella collezione
     * Metodo tenuto per 'omogeneità' e per poter 'switchare' a @DBRef in qualunque momento la collezione che usa questa property
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     * @param stato:     stato (obbligatoria, non unica)
     *
     * @return la entity trovata o appena creata
     */
    public Indirizzo findOrCrea(String indirizzo, String localita, String cap, Stato stato) {
        return newEntity(indirizzo, localita, cap, stato);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    @Override
    public Indirizzo newEntity() {
        return newEntity("", "", "", (Stato) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param indirizzo: via, nome e numero (obbligatoria, non unica)
     * @param localita:  località (obbligatoria, non unica)
     * @param cap:       codice di avviamento postale (obbligatoria, non unica)
     * @param stato:     stato (obbligatoria, non unica)
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    public Indirizzo newEntity(String indirizzo, String localita, String cap, Stato stato) {
        if (statoService != null && statoService.repository != null) {
            return new Indirizzo(indirizzo, localita, cap, stato != null ? stato : statoService.findDefault());
        } else {
            if (statoService != null) {
                return new Indirizzo(indirizzo, localita, cap, statoService.newEntity());
            } else {
                return new Indirizzo(indirizzo, localita, cap, (Stato) null);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Usato da Migration per convertire la vecchia versione dell'applicazione WAM
     *
     * @param indirizzoOld: via, nome e numero - località - cap
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    public Indirizzo newEntity(String indirizzoOld) {
        Indirizzo indirizzo = null;
        String sep = "-";
        String spazio = " ";
        String secondaParte = "";
        String ind = "";
        String loc = "";
        String cap = "";
        String[] parti = indirizzoOld.split(sep);
        int posCap = 0;
        int posLoc = 0;

        if (parti != null) {
            if (parti.length > 0) {
                ind = parti[0].trim();
            }// end of if cycle

            if (parti.length > 1) {
                secondaParte = parti[1].trim();
                if (secondaParte != null) {

                    if (LibText.isLeadingDigit(secondaParte)) {
                        posCap = secondaParte.indexOf(spazio);
                        cap = secondaParte.substring(0, posCap).trim();
                        posLoc = secondaParte.lastIndexOf(spazio, secondaParte.length());
                        if (posLoc == posCap) {
                            loc = secondaParte.substring(posCap).trim();
                        } else {
                            loc = secondaParte.substring(posCap, posLoc).trim();
                        }// end of if/else cycle
                    }// end of if cycle
                }// end of if cycle

            }// end of if cycle
        }// end of if cycle

        indirizzo = newEntity(ind, loc, cap, null);

        return indirizzo;
    }// end of method


}// end of class
