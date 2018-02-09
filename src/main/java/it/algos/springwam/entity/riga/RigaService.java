package it.algos.springwam.entity.riga;

import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.service.AService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by gac on 19-nov-17
 * Annotated with @Service (obbligatorio)
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare come interfaccia
 */
@Slf4j
@Service
@Qualifier(AppCost.TAG_SER)
@AIScript(sovrascrivibile = false)
public class RigaService extends AService {


    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public RigaService() {
        super(null);
    }// end of Spring constructor


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Riga newEntity() {
        return newEntity(null, null, null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * All properties
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok)
     *
     * @param giorno   di riferimento (obbligatorio)
     * @param servizio di riferimento (obbligatorio)
     * @param turni    effettuati (facoltativi, di norma 7)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Riga newEntity(LocalDate giorno, Servizio servizio, List<Turno> turni) {
        Riga entity = Riga.builder()
                .giorno(giorno)
                .servizio(servizio)
                .turni(turni)
                .build();

        return entity;
    }// end of method


}// end of class
