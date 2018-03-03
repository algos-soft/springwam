package it.algos.springwam;

import it.algos.springvaadin.service.AArrayService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: sab, 03-mar-2018
 * Time: 12:13
 */
@Slf4j
public class ATest {


    @InjectMocks
    public AArrayService array;

    @InjectMocks
    protected Funzione funzioneUno;
    @InjectMocks
    protected Funzione funzioneDue;
    @InjectMocks
    protected Funzione funzioneTre;
    @InjectMocks
    protected Funzione funzioneQuattro;

    @InjectMocks
    public Servizio servizioUno;
    @InjectMocks
    public Servizio servizioDue;

    @InjectMocks
    public Milite militeUno;
    @InjectMocks
    public Milite militeDue;
    @InjectMocks
    public Milite militeTre;
    @InjectMocks
    public Milite militeQuattro;

    @InjectMocks
    public Iscrizione iscrizioneUno;
    @InjectMocks
    public Iscrizione iscrizioneDue;
    @InjectMocks
    public Iscrizione iscrizioneTre;
    @InjectMocks
    public Iscrizione iscrizioneQuattro;
    @InjectMocks
    public Iscrizione iscrizioneCinque;
    @InjectMocks
    public Iscrizione iscrizioneSei;
    @InjectMocks
    public Iscrizione iscrizioneSette;

    @InjectMocks
    public Turno turnoUno;
    @InjectMocks
    public Turno turnoDue;
    @InjectMocks
    public Turno turnoTre;


    protected final static String FUNZ_CODE_UNO = "alfaCode";
    protected final static String FUNZ_CODE_DUE = "betaCode";
    protected final static String FUNZ_CODE_TRE = "gammaCode";
    protected final static String FUNZ_CODE_QUATTRO = "deltaCode";

    protected final static String FUNZ_SIGLA_UNO = "alfaSigla";
    protected final static String FUNZ_SIGLA_DUE = "betaSigla";
    protected final static String FUNZ_SIGLA_TRE = "gammaSigla";
    protected final static String FUNZ_SIGLA_QUATTRO = "deltaSigla";

    protected final static String FUNZ_DESC_UNO = "Descrizione alfa";
    protected final static String FUNZ_DESC_DUE = "Descrizione beta";
    protected final static String FUNZ_DESC_TRE = "Descrizione gamma";
    protected final static String FUNZ_DESC_QUATTRO = "Descrizione delta";

    protected final static String SERV_CODE_UNO = "alfaCode";
    protected final static String SERV_CODE_DUE = "betaCode";
    protected final static String SERV_DESC_UNO = "Descrizione alfa";
    protected final static String SERV_DESC_DUE = "Descrizione beta";

    protected final static String MIL_NICK_UNO = "gac";
    protected final static String MIL_PASS_UNO = "nessuna";
    protected final static String MIL_NOME_UNO = "Marco";
    protected final static String MIL_COGNOME_UNO = "Beretta";

    protected final static String MIL_NICK_DUE = "alex";
    protected final static String MIL_PASS_DUE = "lasua";
    protected final static String MIL_NOME_DUE = "Giovanni";
    protected final static String MIL_COGNOME_DUE = "Marconi";

    protected final static String MIL_NICK_TRE = "lucy";
    protected final static String MIL_PASS_TRE = "briciola";
    protected final static String MIL_NOME_TRE = "Rosa";
    protected final static String MIL_COGNOME_TRE = "Luxemburg";

    protected final static String MIL_NICK_QUATTRO = "quercia";
    protected final static String MIL_PASS_QUATTRO = "mare";
    protected final static String MIL_NOME_QUATTRO = "Lucia";
    protected final static String MIL_COGNOME_QUATTRO = "Nonna";

    protected final static String TITOLO_EXTRA = "Assistenza manifestazione";
    protected final static String LOCALITA_EXTRA = "Stadio San Leuca";

    protected List<Funzione> funzioniAll;
    protected List<Funzione> funzioniRidotte;

    protected List<Iscrizione> iscrizioniAll;
    protected List<Iscrizione> iscrizioniDue;
    protected List<Iscrizione> iscrizioniSingola;

    protected Iscrizione iscrizionePrevista;
    protected Iscrizione iscrizioneOttenuta;



    protected void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(array);
        setUpFunzioni();
        setUpServizio();
        setUpMiliti();
        setUpIscrizioni();
        setUpTurni();
    }// end of method


    protected void setUpFunzioni() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(funzioneUno);
        MockitoAnnotations.initMocks(funzioneDue);
        MockitoAnnotations.initMocks(funzioneTre);
        MockitoAnnotations.initMocks(funzioneQuattro);

        funzioneUno = Funzione.builder()
                .ordine(1).code(FUNZ_CODE_UNO).sigla(FUNZ_SIGLA_UNO)
                .descrizione(FUNZ_DESC_UNO).obbligatoria(false).build();
        funzioneDue = Funzione.builder()
                .ordine(1).code(FUNZ_CODE_DUE).sigla(FUNZ_SIGLA_DUE)
                .descrizione(FUNZ_DESC_DUE).obbligatoria(false).build();
        funzioneTre = Funzione.builder()
                .ordine(1).code(FUNZ_CODE_TRE).sigla(FUNZ_SIGLA_TRE)
                .descrizione(FUNZ_DESC_TRE).obbligatoria(false).build();
        funzioneQuattro = Funzione.builder()
                .ordine(1).code(FUNZ_CODE_QUATTRO).sigla(FUNZ_SIGLA_QUATTRO)
                .descrizione(FUNZ_DESC_QUATTRO).obbligatoria(false).build();

        funzioniAll = new ArrayList<>();
        funzioniAll.add(funzioneUno);
        funzioniAll.add(funzioneDue);
        funzioniAll.add(funzioneTre);
        funzioniAll.add(funzioneQuattro);

        funzioniRidotte = new ArrayList<>();
        funzioniRidotte.add(funzioneUno);
        funzioniRidotte.add(funzioneTre);
    }// end of method


    protected void setUpServizio() {
        MockitoAnnotations.initMocks(servizioUno);
        servizioUno = Servizio.builder()
                .ordine(1).code(SERV_CODE_UNO).descrizione(SERV_DESC_UNO).orario(true)
                .oraInizio(7).oraFine(13).visibile(true).funzioni(funzioniAll).build();
        servizioDue = Servizio.builder()
                .ordine(1).code(SERV_CODE_DUE).descrizione(SERV_DESC_DUE).orario(true)
                .oraInizio(7).oraFine(13).visibile(true).funzioni(funzioniRidotte).build();
    }// end of method


    protected void setUpMiliti() {
        MockitoAnnotations.initMocks(militeUno);

        militeUno = new Milite();
        militeUno.setNickname(MIL_NICK_UNO);
        militeUno.setPassword(MIL_PASS_UNO);
        militeUno.setEnabled(true);
        militeUno.setNome(MIL_NOME_UNO);
        militeUno.setCognome(MIL_COGNOME_UNO);
        militeUno.setFunzioni(funzioniAll);

        militeDue = new Milite();
        militeDue.setNickname(MIL_NICK_DUE);
        militeDue.setPassword(MIL_PASS_DUE);
        militeDue.setEnabled(true);
        militeDue.setNome(MIL_NOME_DUE);
        militeDue.setCognome(MIL_COGNOME_DUE);
        militeDue.setFunzioni(funzioniAll);

        militeTre = new Milite();
        militeTre.setNickname(MIL_NICK_TRE);
        militeTre.setPassword(MIL_PASS_TRE);
        militeTre.setEnabled(true);
        militeTre.setNome(MIL_NOME_TRE);
        militeTre.setCognome(MIL_COGNOME_TRE);
        militeTre.setFunzioni(funzioniAll);

        militeQuattro = new Milite();
        militeQuattro.setNickname(MIL_NICK_QUATTRO);
        militeQuattro.setPassword(MIL_PASS_QUATTRO);
        militeQuattro.setEnabled(true);
        militeQuattro.setNome(MIL_NOME_QUATTRO);
        militeQuattro.setCognome(MIL_COGNOME_QUATTRO);
        militeQuattro.setFunzioni(funzioniAll);
    }// end of method


    protected void setUpIscrizioni() {
        MockitoAnnotations.initMocks(iscrizioneUno);

        iscrizioneUno = Iscrizione.builder()
                .funzione(funzioneUno)
                .milite(militeUno)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneDue = Iscrizione.builder()
                .funzione(funzioneDue)
                .milite(militeDue)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneTre = Iscrizione.builder()
                .funzione(funzioneTre)
                .milite(militeTre)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneQuattro = Iscrizione.builder()
                .funzione(funzioneQuattro)
                .milite(militeQuattro)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneCinque = Iscrizione.builder()
                .funzione(funzioneUno)
                .milite(militeDue)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneSei = Iscrizione.builder()
                .funzione(funzioneDue)
                .milite(militeQuattro)
                .timestamp(LocalDateTime.now())
                .durata(servizioUno.getDurata())
                .build();

        iscrizioneSette = Iscrizione.builder()
                .funzione(funzioneTre)
                .milite(militeTre)
                .timestamp(LocalDateTime.now())
                .durata(servizioDue.getDurata())
                .build();

        iscrizioniAll = new ArrayList<>();
        iscrizioniAll.add(iscrizioneUno);
        iscrizioniAll.add(iscrizioneDue);
        iscrizioniAll.add(iscrizioneTre);
        iscrizioniAll.add(iscrizioneQuattro);

        iscrizioniDue = new ArrayList<>();
        iscrizioniDue.add(iscrizioneCinque);
        iscrizioniDue.add(iscrizioneSei);

        iscrizioniSingola = new ArrayList<>();
        iscrizioniSingola.add(iscrizioneSette);
    }// end of method


    protected void setUpTurni() {
        MockitoAnnotations.initMocks(turnoUno);

        turnoUno = Turno.builder()
                .giorno(LocalDate.now())
                .servizio(servizioUno)
                .inizio(LocalDateTime.now().plusHours(servizioUno.getOraInizio()))
                .fine(LocalDateTime.now().plusHours(servizioUno.getOraFine()))
                .iscrizioni(iscrizioniAll)
                .titoloExtra(TITOLO_EXTRA)
                .localitaExtra(LOCALITA_EXTRA)
                .build();

        turnoDue = Turno.builder()
                .giorno(LocalDate.now().plusDays(1))
                .servizio(servizioUno)
                .inizio(LocalDateTime.now().plusHours(servizioUno.getOraInizio()))
                .fine(LocalDateTime.now().plusHours(servizioUno.getOraFine()))
                .iscrizioni(iscrizioniDue)
                .build();

        turnoTre = Turno.builder()
                .giorno(LocalDate.now().plusDays(2))
                .servizio(servizioDue)
                .inizio(LocalDateTime.now().plusHours(servizioDue.getOraInizio()))
                .fine(LocalDateTime.now().plusHours(servizioDue.getOraFine()))
                .iscrizioni(iscrizioniSingola)
                .build();
    }// end of method


}// end of class