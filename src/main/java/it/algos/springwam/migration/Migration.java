package it.algos.springwam.migration;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.company.CompanyData;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.entity.indirizzo.Indirizzo;
import it.algos.springvaadin.entity.indirizzo.IndirizzoService;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.persona.PersonaService;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibAvviso;
import it.algos.springvaadin.lib.LibText;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.croce.Organizzazione;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.utente.Utente;
import it.algos.springwam.entity.utente.UtenteService;
import it.algos.webbase.web.entity.EM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: dom, 08-ott-2017
 * Time: 09:19
 * Importa i dati -on line- della vecchia versione del programma (webambulanze)
 */
@SpringComponent
@Slf4j
public class Migration {

    private final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";
    private EntityManager manager;
    private final static boolean SIGLA_MAIUSCOLA = false;

    private final static String[] escluseMatrice = {"ALGOS", "DEMO", "PAVT"};
    public final static List<String> ESCLUSE = Arrays.asList(escluseMatrice);


    private HashMap<Long, Funzione> mappaFunzioni = new HashMap<>();

    @Autowired
    private CroceService croceService;

    @Autowired
    private IndirizzoService indirizzoService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UserService userService;

    @Autowired
    protected CompanyData companyData;

    /**
     * Importa tutte le companies esistenti in webambulanze
     */
    public void importAllCroci() {
        creaManagers();
        List<CroceAmb> crociOld = CroceAmb.findAll(manager);

        for (CroceAmb croceOld : crociOld) {
            if (!ESCLUSE.contains(croceOld.getSigla())) {
                importCroce(croceOld.getSigla());
            }// end of if cycle
        }// end of for cycle

        chiudeManagers();
    }// end of constructor

    public void importCroce(String siglaCroce) {
        importCroce(siglaCroce, 2017);
    }// end of constructor


    public void importCroce(String siglaCroceOld, int anno) {
        importCroce(siglaCroceOld, siglaCroceOld, anno);
    }// end of constructor


    /**
     * Importa i dati di una singola company
     * Se l'anno è zero, importa i turni esistenti di tutti gli anni
     * <p>
     * Crea i manager specifici
     * Cerca una company con la sigla siglaCroceNew
     * La cancella, con tutti i dati
     * Cerca una company con la sigla siglaCroceOld
     * Importa i dati
     * Chiude i manager specifici
     *
     * @param siglaCroceOld nome della company usata in webambulanze
     * @param siglaCroceNew nome della company usata in springWam
     * @param anno          del quale importare i turni
     */
    public void importCroce(String siglaCroceOld, String siglaCroceNew, int anno) {
        Croce croceNew = null;
        CroceAmb croceOld = null;
        creaManagers();
        croceOld = CroceAmb.findBySigla(siglaCroceOld, manager);

        if (croceOld != null) {
            croceNew = importCroce(croceOld, siglaCroceNew, anno);
        } else {
            LibAvviso.warn("Non trovata la croce " + LibText.setRossoBold(siglaCroceOld) + " in webambulanze");
        }// end of if/else cycle

        chiudeManagers();

        companyData.updatePreferenze(croceNew);
    }// end of constructor


    /**
     * Importa i dati di una singola company
     * Se l'anno è zero, importa i turni esistenti di tutti gli anni
     * <p>
     * Cerca una company con la sigla siglaCroceNew
     * La cancella, con tutti i dati
     * Cerca una company con la sigla siglaCroceOld
     * Importa i dati
     *
     * @param croceOld      company usata in webambulanze
     * @param siglaCroceNew nome della company usata in springWam
     * @param anno          del quale importare i turni
     */
    private Croce importCroce(CroceAmb croceOld, String siglaCroceNew, int anno) {
        Croce croceNew = croceService.findByCode(siglaCroceNew);

        if (croceNew != null) {
            croceService.delete(croceNew);
        }// end of if cycle
        croceNew = importSoloCroce(croceOld, siglaCroceNew);


        if (croceNew != null) {
            importFunzioni(croceOld, croceNew);
            importServizi(croceOld, croceNew);
            importUtenti(croceOld, croceNew);
        }// end of if cycle

//        // regolazioni aggiuntive di una particolare croce/company
//        if (siglaCompanyNew.equals("gaps")) {
////            patchGaps();
//        }// end of if cycle
//
//        // regolazioni aggiuntive di una particolare croce/company
//        if (siglaCompanyNew.equals("crf")) {
////            patchCrf();
//        }// end of if cycle
//
//        // regolazioni aggiuntive di una particolare croce/company
//        if (siglaCompanyNew.equals("pap")) {
////            patchPap();
//        }// end of if
//
//        // regolazioni aggiuntive di una particolare croce/company
//        if (siglaCompanyNew.equals("crpt")) {
////            patchCrpt();
//        }// end of if cycle

        return croceNew;
    }// end of method


    /**
     * Importa la croce
     *
     * @param croceOld      company usata in webambulanze
     * @param siglaCroceNew nome della company da usare in springWam
     *
     * @return la nuova company
     */
    private Croce importSoloCroce(CroceAmb croceOld, String siglaCroceNew) {
        Croce croceNew = null;
        String descrizione;
        String email;
        String telefono;
        Indirizzo indirizzoNew;
        Persona presidenteNew = null;
        Persona contattoNew = null;
        Organizzazione org = null;

        try { // prova ad eseguire il codice
            descrizione = croceOld.getDescrizione();
            telefono = croceOld.getTelefono();
            email = croceOld.getEmail();
            indirizzoNew = indirizzoService.newEntity(croceOld.getIndirizzo());
            presidenteNew = fixPersona(croceOld.getPresidente());
            contattoNew = fixPersona(croceOld.getRiferimento());
            org = recuperaOrganizzazione(croceOld);

            if (SIGLA_MAIUSCOLA) {
                siglaCroceNew = siglaCroceNew.toUpperCase();
            } else {
                siglaCroceNew = siglaCroceNew.toLowerCase();
            }// end of if/else cycle

            croceNew = croceService.findOrCrea(org, presidenteNew, siglaCroceNew, descrizione, contattoNew, telefono, email, indirizzoNew);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return croceNew;
    }// end of method


    /**
     * Elaborazione per informazioni non suddivise nella vecchia croce
     */
    private Persona fixPersona(String personaOldStyle) {
        Persona persona = null;
        String[] parti = personaOldStyle.split(" ");

        if (parti != null && parti.length == 2) {
            persona = personaService.newEntity(parti[0], parti[1]);
        }// end of if cycle

        return persona;
    }// end of method


    /**
     * Importa le funzioni della croce
     *
     * @param croceOld company usata in webambulanze
     * @param croceNew company usata in springWam
     */
    private void importFunzioni(CroceAmb croceOld, Croce croceNew) {
        List<FunzioneAmb> listaFunzioniOld = FunzioneAmb.findAll(croceOld, manager);
        Funzione funzioneNew = null;

        try { // prova ad eseguire il codice
            if (listaFunzioniOld != null && listaFunzioniOld.size() > 0) {
                for (FunzioneAmb funzioneOld : listaFunzioniOld) {
                    funzioneNew = creaSingolaFunzione(funzioneOld, croceNew);
                    mappaFunzioni.put(funzioneOld.getId(), funzioneNew);
                }// end of for cycle
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
    }// end of method


    /**
     * Crea la singola funzione
     *
     * @param funzioneOld della companyOld
     * @param croceNew    company usata in springWam
     */
    private Funzione creaSingolaFunzione(FunzioneAmb funzioneOld, Croce croceNew) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        VaadinIcons icona = selezionaIcona(descrizione);

        return funzioneService.deleteAndCrea(ordine, croceNew, code, sigla, descrizione, icona, false);
    }// end of method

    /**
     * Elabora la vecchia descrizione2 per selezionare una icona adeguata
     *
     * @param descrizione usata in webambulanze
     *
     * @return la FontAwesome selezionata
     */
    private VaadinIcons selezionaIcona(String descrizione) {
        VaadinIcons icona = null;
        String autista = "utista";
        String soc = "Soccorritore";
        String soc2 = "Primo";
        String ser = "Centralino";
        String ser2 = "Pulizie";
        String ser3 = "Ufficio";

        if (descrizione.contains(autista)) {
//            glyph = FontAwesome.AMBULANCE;
            icona = VaadinIcons.ASTERISK;
        } else {
            if (descrizione.contains(soc) || descrizione.contains(soc2)) {
//                glyph = FontAwesome.HEART;
                icona = VaadinIcons.MONEY;
            } else {
                if (descrizione.contains(ser) || descrizione.contains(ser2) || descrizione.contains(ser3)) {
//                    glyph = FontAwesome.USER;
                    icona = VaadinIcons.FACEBOOK;
                } else {
//                    glyph = FontAwesome.STETHOSCOPE;
                    icona = VaadinIcons.RECYCLE;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return icona;
    }// end of method


    /**
     * Importa i servizi della croce
     *
     * @param croceOld company usata in webambulanze
     * @param croceNew company usata in springWam
     */
    private void importServizi(CroceAmb croceOld, Croce croceNew) {
        List<ServizioAmb> listaServiziOld = ServizioAmb.findAll(croceOld, manager);

        try { // prova ad eseguire il codice
            if (listaServiziOld != null && listaServiziOld.size() > 0) {
                for (ServizioAmb servizioOld : listaServiziOld) {
                    creaSingoloServizio(servizioOld, croceNew);
                }// end of for cycle
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

    }// end of method

    /**
     * Crea il singolo servizio
     *
     * @param servizioOld della companyOld
     * @param companyNew  company usata in springWam
     */
    private Servizio creaSingoloServizio(ServizioAmb servizioOld, Company companyNew) {
        Servizio servizio = null;
        String code = servizioOld.getSigla();
        int ordine = servizioOld.getOrdine();
        String descrizione = servizioOld.getDescrizione();
        VaadinIcons icona = selezionaIcona(descrizione);
        boolean orario = servizioOld.isOrario();
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();
        boolean visibile = servizioOld.isVisibile();
        List<Funzione> funzioni = new ArrayList<>();


        servizio = servizioService.deleteAndCrea(ordine, companyNew, code, descrizione, icona.getCodepoint(), orario, oraInizio, minutiInizio, oraFine, minutiFine, visibile);
        servizioAddFunzioni(servizioOld, servizio);
        return servizio;
    }// end of method


    /**
     * Aggiunge le funzioni al servizio
     *
     * @param servizioOld della companyOld
     * @param servizioNew appena creato in springWam
     */
    private void servizioAddFunzioni(ServizioAmb servizioOld, Servizio servizioNew) {
        List<Funzione> listaFunzioni = new ArrayList<>();
        Funzione funz = null;
        int numeroFunzioniObbligatorie = servizioOld.getFunzioni_obbligatorie();
        long idFunzione1 = servizioOld.getFunzione1_id();
        long idFunzione2 = servizioOld.getFunzione2_id();
        long idFunzione3 = servizioOld.getFunzione3_id();
        long idFunzione4 = servizioOld.getFunzione4_id();

        funz = mappaFunzioni.get(idFunzione1);
        if (funz != null) {
            funz.setObbligatoria(numeroFunzioniObbligatorie > 0);
            listaFunzioni.add(funz);
        }// end of if cycle

        funz = mappaFunzioni.get(idFunzione2);
        if (funz != null) {
            funz.setObbligatoria(numeroFunzioniObbligatorie > 1);
            listaFunzioni.add(funz);
        }// end of if cycle

        funz = mappaFunzioni.get(idFunzione3);
        if (funz != null) {
            funz.setObbligatoria(numeroFunzioniObbligatorie > 2);
            listaFunzioni.add(funz);
        }// end of if cycle

        funz = mappaFunzioni.get(idFunzione4);
        if (funz != null) {
            funz.setObbligatoria(numeroFunzioniObbligatorie > 3);
            listaFunzioni.add(funz);
        }// end of if cycle

        servizioNew.setFunzioni(listaFunzioni);
        try { // prova ad eseguire il codice
            servizioService.save(servizioNew);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method

    /**
     * Patch per carenza di informazioni nella vecchia croce
     */
    private Organizzazione recuperaOrganizzazione(CroceAmb companyOld) {
        Organizzazione organizzazioneNew = Organizzazione.get(companyOld.getOrganizzazione());

        if (companyOld.getSigla().equals("GAPS")) {
            organizzazioneNew = Organizzazione.csv;
        }// end of if cycle

        return organizzazioneNew;
    }// end of method


    /**
     * Importa gli utenti della croce
     *
     * @param croceOld company usata in webambulanze
     * @param croceNew company usata in springWam
     */
    private void importUtenti(CroceAmb croceOld, Croce croceNew) {
        List<UtenteAmb> listaUtentiOld = UtenteAmb.getList(manager, croceOld);
        Utente utenteNew = null;

        try { // prova ad eseguire il codice
            if (listaUtentiOld != null && listaUtentiOld.size() > 0) {
                for (UtenteAmb utenteOld : listaUtentiOld) {
                    utenteNew = creaSingoloUtente(croceOld, utenteOld, croceNew);
                }// end of for cycle
            }// end of if cycle
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
    }// end of method


    /**
     * Crea il singolo utente
     *
     * @param croceOld   company usata in webambulanze
     * @param utenteOld  della companyOld
     * @param companyNew company usata in springWam
     */
    private Utente creaSingoloUtente(CroceAmb croceOld, UtenteAmb utenteOld, Company companyNew) {
        Utente utenteNew = null;
        List<UserAmb> listaUserOld = UserAmb.getList(manager, croceOld);
        User userNew = creaSingoloUser(listaUserOld, utenteOld, companyNew);
        String nome = utenteOld.getNome();
        String cognome = utenteOld.getCognome();
        String telefono = utenteOld.getTelefono_cellulare();
        String telefono2 = utenteOld.getTelefono_fisso();
        String email = utenteOld.getEmail();
        List<Funzione> funzioni = creaFunzioniUtente(croceOld, utenteOld);

        if (nome.equals(".") || nome.equals(",")) {
            return null;
        }// end of if cycle

        utenteNew = utenteService.deleteAndCrea(companyNew, userNew, nome, cognome, telefono, email, funzioni);
        utenteNew = regolaRuoliAssociazione(utenteOld, utenteNew);

        return utenteNew;
    }// end of method


    /**
     * Crea il singolo user
     *
     * @param listaUserOld usata in webambulanze
     * @param utenteOld    della companyOld
     */
    private User creaSingoloUser(List<UserAmb> listaUserOld, UtenteAmb utenteOld, Company companyNew) {
        User user = null;
        UserAmb userOld = null;
        String nickname = "";
        String password = "";

        for (UserAmb userOldTmp : listaUserOld) {
            if (userOldTmp.getMilite() == utenteOld) {
                userOld = userOldTmp;
                break;
            }// end of if cycle
        }// end of for cycle

        if (userOld != null) {
            nickname = userOld.getNickname();
            password = userOld.getPass();
        }// end of if cycle

        if (LibText.isValid(nickname) && LibText.isValid(password)) {
            user = userService.findOrCrea(companyNew, nickname, password, true);
        }// end of if cycle

        return user;
    }// end of method


    private Utente regolaRuoliAssociazione(UtenteAmb militeOld, Utente utenteNew) {
        String tagIP = "I.P.";
        List<Long> listaRuoliUtente = UtenteRuoloAmb.getListAdmin(manager);
        UserAmb utente = UserAmb.getUtenteByMiliteID(manager, militeOld);
        long idKeyUtente = 0;
        if (utente != null) {
            idKeyUtente = utente.getId();
        }// end of if cycle
        boolean isAdmin = listaRuoliUtente.contains(idKeyUtente);
        boolean isDipendente = militeOld.isDipendente();
        boolean isInfermiere = militeOld.getCognome().startsWith(tagIP);

        utenteNew.setAdmin(isAdmin);
        utenteNew.setDipendente(isDipendente);
        utenteNew.setInfermiere(isInfermiere);

        try { // prova ad eseguire il codice
            utenteNew = (Utente) utenteService.save(utenteNew);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return utenteNew;
    }// end of method

    /**
     * Crea le funzioni utente
     *
     * @param croceOld company usata in webambulanze
     * @param milite   della companyOld
     */
    private List<Funzione> creaFunzioniUtente(CroceAmb croceOld, UtenteAmb milite) {
        List<Funzione> funzioni = new ArrayList<>();
        Funzione funzioneNew;
        List<MiliteFunzioneAmb> listaIncroci;
        long funzioneOldID;

        listaIncroci = MiliteFunzioneAmb.getList(manager, milite);
        if (listaIncroci != null && listaIncroci.size() > 0) {
            for (MiliteFunzioneAmb militeFunzione : listaIncroci) {
                funzioneOldID = militeFunzione.getFunzione_id();
                funzioneNew = mappaFunzioni.get(funzioneOldID);
                if (funzioneNew != null) {
                    funzioni.add(funzioneNew);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return funzioni;
    }// end of method

    /**
     * Creazione di un manager specifico
     * Manager per la sola lettura delle vecchie classi (Amb)
     */
    private void creaManagers() {
        if (manager == null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            if (factory != null) {
                manager = factory.createEntityManager();
            }// end of if cycle
        }// end of if cycle
    }// end of method


    /**
     * Chiusura dei due manager specifici
     * Uno per la sola lettura delle vecchie classi (Amb)
     * Uno per la cancellazione/scrittura delle nuove classi (WAM)
     */
    private void chiudeManagers() {

        if (manager != null) {
            manager.close();
            manager = null;
        }// end of if cycle

    }// end of method

}// end of class
