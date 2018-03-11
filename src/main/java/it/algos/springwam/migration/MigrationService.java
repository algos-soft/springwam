package it.algos.springwam.migration;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.address.Address;
import it.algos.springvaadin.entity.address.AddressService;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.entity.persona.Persona;
import it.algos.springvaadin.entity.role.Role;
import it.algos.springvaadin.entity.role.RoleService;
import it.algos.springvaadin.entity.user.User;
import it.algos.springvaadin.entity.user.UserService;
import it.algos.springvaadin.service.ADateService;
import it.algos.springvaadin.service.ATextService;
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.croce.EAOrganizzazione;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.iscrizione.IscrizioneService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: sab, 13-gen-2018
 * Time: 22:53
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class MigrationService {


    @Autowired
    public ATextService text;

    @Autowired
    private CroceService croceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private IscrizioneService iscrizioneService;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private ADateService dateService;


    private EntityManager manager;
    private final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";

    private final static String[] escluseMatrice = {"ALGOS", "DEMO", "PAVT"};
    public final static List<String> ESCLUSE = Arrays.asList(escluseMatrice);


    /**
     * Importa tutte le companies esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllCrociSetup() {
        if (croceService.count() == 0) {
            importAllCroci();
        }// end of if cycle
    }// end of constructor


    /**
     * Importa tutte le companies esistenti in webambulanze
     */
    public void importAllCroci() {
        creaManagers();

        for (CroceAmb croceOld : getAllCrociValide()) {
            importCroce(croceOld);
        }// end of for cycle

        chiudeManagers();
    }// end of constructor


    /**
     * Importa i dati di una singola company
     * <p>
     * Crea i manager specifici
     * Cerca una croce siglaCroce
     * La cancella, con tutti i dati
     * Crea una croce siglaCroce
     * Importa i dati
     * Chiude i manager specifici
     *
     * @param croceOld usata in webambulanze
     */
    public Croce importCroce(CroceAmb croceOld) {
        Croce croceNew = null;
        String siglaCroceNew;
        String descrizione;
        String email;
        String telefono;
        Address indirizzoNew = null;
        Persona presidenteNew = null;
        Persona contattoNew = null;
        EAOrganizzazione org = null;

        croceNew = croceService.findByKeyUnica(croceOld.getSigla());
        if (croceNew != null) {
            croceService.delete(croceNew);
        }// end of if cycle

        try { // prova ad eseguire il codice
            descrizione = croceOld.getDescrizione();
            telefono = croceOld.getTelefono();
            email = croceOld.getEmail();
//            indirizzoNew = addressService.newEntity(croceOld.getIndirizzo());
//            presidenteNew = fixPersona(croceOld.getPresidente());
//            contattoNew = fixPersona(croceOld.getRiferimento());
//            org = recuperaOrganizzazione(croceOld);

            siglaCroceNew = croceOld.getSigla().toLowerCase();
            croceNew = croceService.findOrCrea(org, presidenteNew, siglaCroceNew, descrizione, contattoNew, telefono, email, indirizzoNew);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return croceNew;
//        companyData.updatePreferenze(croceNew);
    }// end of constructor


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
        Address indirizzoNew = null;
        Persona presidenteNew = null;
        Persona contattoNew = null;
        EAOrganizzazione org = null;

        try { // prova ad eseguire il codice
            descrizione = croceOld.getDescrizione();
            telefono = croceOld.getTelefono();
            email = croceOld.getEmail();
//            indirizzoNew = addressService.newEntity(croceOld.getIndirizzo());
//            presidenteNew = fixPersona(croceOld.getPresidente());
//            contattoNew = fixPersona(croceOld.getRiferimento());
//            org = recuperaOrganizzazione(croceOld);

            if (false) {
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
     * Importa tutti le funzioni esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllFunzioniSetup() {
        if (funzioneService.count() == 0) {
            importAllFunzioni();
        }// end of if cycle
    }// end of constructor


    /**
     * Importa tutti le funzioni esistenti in webambulanze
     */
    public void importAllFunzioni() {
        Croce croceNew;
        creaManagers();

        for (CroceAmb croceOld : getAllCrociValide()) {
            croceNew = getCroce(croceOld);
            importFunzioni(croceOld, croceNew);
        }// end of for cycle

        chiudeManagers();
    }// end of constructor


    /**
     * Importa le funzioni di una croce
     */
    public void importFunzioni(CroceAmb croceOld, Croce croceNew) {
        for (FunzioneAmb funzOld : FunzioneAmb.findAllByCroce(croceOld, manager)) {
            creaSingolaFunzione(funzOld, croceNew);
        }// end of for cycle
    }// end of constructor


    /**
     * Crea la singola funzione
     *
     * @param funzioneOld della companyOld
     * @param croceNew    company usata in springWam
     */
    private void creaSingolaFunzione(FunzioneAmb funzioneOld, Croce croceNew) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        VaadinIcons icona = selezionaIcona(descrizione);

        //--non posso usare il metodo funzioneService.findOrCrea(), perchè manca la sessione  e manca il login da cui
        //--prendere automaticamente la company
        if (funzioneService.findByKeyUnica(croceNew, code) == null) {
            Funzione entity = Funzione.builder()
                    .ordine(ordine != 0 ? ordine : funzioneService.getNewOrdine())
                    .code(code)
                    .sigla(sigla)
                    .descrizione(descrizione)
                    .icona(icona != null ? icona.getCodepoint() : 0)
                    .obbligatoria(false)
                    .build();
            entity.company = croceNew;
            funzioneService.save(entity);
        }// end of if cycle
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
        String medica = "edica";
        String soc = "Primo";
        String soc2 = "Soccorritore";
        String cen = "Centralino";
        String pul = "Pulizie";
        String uff = "Ufficio";

        if (descrizione.contains(autista)) {
            if (descrizione.contains(medica)) {
                icona = VaadinIcons.AMBULANCE;
            } else {
                icona = VaadinIcons.TRUCK;
            }// end of if/else cycle
        } else {
            if (descrizione.contains(soc) || descrizione.contains(soc2)) {
                if (descrizione.contains(soc)) {
                    icona = VaadinIcons.DOCTOR;
                }// end of if cycle
                if (descrizione.contains(soc2)) {
                    icona = VaadinIcons.SPECIALIST;
                }// end of if cycle
            } else {
                if (descrizione.contains(cen) || descrizione.contains(pul) || descrizione.contains(uff)) {
                    if (descrizione.contains(cen)) {
                        icona = VaadinIcons.PHONE;
                    }// end of if cycle
                    if (descrizione.contains(pul)) {
                        icona = VaadinIcons.BED;
                    }// end of if cycle
                    if (descrizione.contains(uff)) {
                        icona = VaadinIcons.OFFICE;
                    }// end of if cycle
                } else {
                    icona = VaadinIcons.USER;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return icona;
    }// end of method


    /**
     * Importa tutti i servizi esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllServiziSetup() {
        if (servizioService.count() == 0) {
            importAllServizi();
        }// end of if cycle
    }// end of constructor


    /**
     * Importa tutti i servizi esistenti in webambulanze
     */
    public void importAllServizi() {
        Croce croceNew;
        creaManagers();

        for (CroceAmb croceOld : getAllCrociValide()) {
            croceNew = getCroce(croceOld);
            importServizi(croceOld, croceNew);
        }// end of for cycle

        chiudeManagers();
    }// end of constructor


    /**
     * Importa i servizi di una croce
     */
    public void importServizi(CroceAmb croceOld, Croce croceNew) {
        for (ServizioAmb servOld : ServizioAmb.findAllByCroce(croceOld, manager)) {
            creaSingoloServizio(servOld, croceNew);
        }// end of for cycle
    }// end of constructor


    /**
     * Crea il singolo servizio
     *
     * @param servizioOld della companyOld
     * @param croceNew    company usata in springWam
     */
    private void creaSingoloServizio(ServizioAmb servizioOld, Croce croceNew) {
        String code = servizioOld.getSigla();
        int ordine = servizioOld.getOrdine();
        String descrizione = servizioOld.getDescrizione();
//        VaadinIcons icona = selezionaIcona(descrizione);
        boolean orario = servizioOld.isOrario();
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();
        boolean visibile = servizioOld.isVisibile();
        List<Funzione> funzioni = selezionaFunzioni(servizioOld, croceNew);


        //--non posso usare il metodo servizioService.findOrCrea(), perchè manca la sessione  e manca il login da cui
        //--prendere automaticamente la company
        if (servizioService.findByKeyUnica(croceNew, code) == null) {
            Servizio entity = Servizio.builder()
                    .ordine(ordine != 0 ? ordine : servizioService.getNewOrdine())
                    .code(code)
                    .descrizione(descrizione)
                    .colore(0)
                    .orario(orario)
                    .oraInizio(oraInizio)
                    .minutiInizio(minutiInizio)
                    .oraFine(oraFine)
                    .minutiFine(minutiFine)
                    .visibile(visibile)
                    .funzioni(funzioni)
                    .build();
            entity.company = croceNew;
            servizioService.save(entity);
        }// end of if cycle
    }// end of method


    /**
     * Recupera le funzioni del servizio
     *
     * @param servizioOld della companyOld
     */
    private List<Funzione> selezionaFunzioni(ServizioAmb servizioOld, Croce croceNew) {
        List<Funzione> listaFunzioni = new ArrayList<>();
        FunzioneAmb funzAmb = null;
        String code;
        Funzione funz = null;
        int numeroFunzioniObbligatorie = servizioOld.getFunzioni_obbligatorie();
        long idFunzione1 = servizioOld.getFunzione1_id();
        long idFunzione2 = servizioOld.getFunzione2_id();
        long idFunzione3 = servizioOld.getFunzione3_id();
        long idFunzione4 = servizioOld.getFunzione4_id();

        funzAmb = FunzioneAmb.find(idFunzione1, manager);
        if (funzAmb != null) {
            funz = funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla());
            funz.setObbligatoria(numeroFunzioniObbligatorie > 0);
            listaFunzioni.add(funz);
        }// end of if cycle

        funzAmb = FunzioneAmb.find(idFunzione2, manager);
        if (funzAmb != null) {
            funz = funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla());
            funz.setObbligatoria(numeroFunzioniObbligatorie > 1);
            listaFunzioni.add(funz);
        }// end of if cycle

        funzAmb = FunzioneAmb.find(idFunzione3, manager);
        if (funzAmb != null) {
            funz = funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla());
            funz.setObbligatoria(numeroFunzioniObbligatorie > 2);
            listaFunzioni.add(funz);
        }// end of if cycle

        funzAmb = FunzioneAmb.find(idFunzione4, manager);
        if (funzAmb != null) {
            funz = funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla());
            funz.setObbligatoria(numeroFunzioniObbligatorie > 3);
            listaFunzioni.add(funz);
        }// end of if cycle

        return listaFunzioni;
    }// end of method


    /**
     * Importa tutti i militi esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllMilitiSetup() {
        if (militeService.count() == 0) {
            importAllMiliti();
        }// end of if cycle
    }// end of constructor


    /**
     * Importa tutti i militi esistenti in webambulanze
     */
    public void importAllMiliti() {
        Croce croceNew;
        creaManagers();

        for (CroceAmb croceOld : getAllCrociValide()) {
            croceNew = getCroce(croceOld);
            importMiliti(croceOld, croceNew);
        }// end of for cycle

        chiudeManagers();
    }// end of constructor


    /**
     * Importa i militi esistenti in una croce di webambulanze
     */
    public void importMiliti(CroceAmb croceOld, Croce croceNew) {
        List<UserAmb> users = UserAmb.findAllByCroce(croceOld, manager);

        for (UserAmb userOld : users) {
            creaSingoloMilite(croceNew, userOld);
        }// end of for cycle
    }// end of constructor


    /**
     * Crea il singolo milite
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     *
     * @param userOld  della companyOld
     * @param croceNew company usata in springWam
     */
    private void creaSingoloMilite(Croce croceNew, UserAmb userOld) {
        Milite entity;
        Role role = getRuolo(userOld);
        String nickname = userOld.getNickname();
        String password = userOld.getPass();
        boolean enabled = userOld.isEnabled();
        UtenteAmb utenteOld = userOld.getMilite();
        String nome = "";
        String cognome = "";
        boolean dipendente = false;
        List<Funzione> funzioni = getFunzioni(croceNew, userOld);

        if (utenteOld != null) {
            nome = utenteOld.getNome();
            cognome = utenteOld.getCognome();
            dipendente = utenteOld.isDipendente();
        } else {
            int a = 87;
        }// end of if/else cycle


        if (text.isEmpty(nome) && text.isEmpty(cognome)) {
            return;
        }// end of if cycle


        if (militeService.findByKeyUnica(croceNew, nickname) == null) {
            entity = new Milite();
            entity.setRole(role);
            entity.setNickname(nickname);
            entity.setPassword(password);
            entity.setEnabled(true);
            entity.setNome(nome);
            entity.setCognome(cognome);
            entity.setEnabled(enabled);
            entity.setDipendente(dipendente);
            entity.setInfermiere(false);
            entity.setFunzioni(funzioni);
            entity.company = croceNew;
            militeService.save(entity);
        }// end of if cycle

    }// end of method


    private Role getRuolo(UserAmb userOld) {
        Role role = roleService.getUser();
        UtenteAmb utenteOld = userOld.getMilite();
        long utenteID = 0;
        long userID = userOld.getId();
        UtenteRuoloAmb utenteRuoloOld;
        int roleID = 0;

        if (utenteOld != null) {
            utenteID = utenteOld.getId();
            if (utenteID == 15) {
                int a = 87;
            }// end of if cycle

            roleID = UtenteRuoloAmb.findRoleIDByUserID(userID, manager);
            switch (roleID) {
                case 0:
                    role = roleService.getUser();
                    break;
                case 1:
                    role = roleService.getDev();
                    break;
                case 2:
                    role = roleService.getAdmin();
                    break;
                case 3:
                    role = roleService.getAdmin();
                    break;
                case 4:
                    role = roleService.getUser();
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

        }// end of if cycle

        return role;
    }// end of method


    private List<Funzione> getFunzioni(Croce croceNew, UserAmb userOld) {
        List<Funzione> funzioni = null;
        List<FunzioneAmb> funzioniOld = null;
        UtenteAmb utenteOld = userOld.getMilite();

        if (utenteOld != null) {
            funzioniOld = MiliteFunzioneAmb.findAllFunzioniByMilite(utenteOld, manager);
        }// end of if cycle

        if (funzioniOld != null) {
            funzioni = new ArrayList<>();
            for (FunzioneAmb funzAmb : funzioniOld) {
                funzioni.add(funzioneService.findByKeyUnica(croceNew, funzAmb.getSigla()));
            }// end of for cycle
        }// end of if cycle

        return funzioni;
    }// end of method


    /**
     * Importa tutti i turni esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllTurniSetup() {
        if (turnoService.count() == 0) {
            importAllTurni();
        }// end of if cycle
    }// end of constructor


    /**
     * Importa tutti i turni esistenti in webambulanze
     */
    public void importAllTurni() {
        Croce croceNew;
        creaManagers();

        for (CroceAmb croceOld : getAllCrociValide()) {
            croceNew = getCroce(croceOld);
            importTurni(croceOld, croceNew);
        }// end of for cycle

        chiudeManagers();
    }// end of constructor


    /**
     * Importa i turni esistenti in una croce di webambulanze
     */
    public void importTurni(CroceAmb croceOld, Croce croceNew) {
        Date primoGennaioDate = dateService.localDateToDate(dateService.getPrimoGennaio(2018));
        Timestamp inizio = new Timestamp(primoGennaioDate.getTime());
        Date trentunoDicembreDate = dateService.getTrentunoDicembre(2018);
        Timestamp fine = new Timestamp(trentunoDicembreDate.getTime());
        List<TurnoAmb> turni = TurnoAmb.findAllByCroceAndYear(croceOld, inizio, fine, manager);

        for (TurnoAmb turnoOld : turni) {
            creaSingoloTurno(croceNew, turnoOld);
        }// end of for cycle
    }// end of constructor


    /**
     * Crea il singolo turno
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     *
     * @param turnoOld della companyOld
     * @param croceNew company usata in springWam
     */
    private void creaSingoloTurno(Croce croceNew, TurnoAmb turnoOld) {
        Turno entity = null;
        Servizio servizio = recuperaServizio(croceNew, turnoOld);
        Date inizioOld = turnoOld.getInizio();
        Date fineOld = turnoOld.getFine();
        LocalDate giornoNew = dateService.dateToLocalDate(inizioOld);
        LocalDateTime inizioNew = dateService.dateToLocalDateTime(inizioOld);
        LocalDateTime fineNew = dateService.dateToLocalDateTime(fineOld);
        List<Iscrizione> iscrizioni;
        String titoloExtra = turnoOld.getTitolo_extra();
        String localitaExtra = turnoOld.getLocalità_extra();
        String note = turnoOld.getNote();

        entity = new Turno();
        entity.setGiorno(giornoNew);
        entity.setServizio(servizio);
        entity.setInizio(inizioNew);
        entity.setFine(fineNew);
        entity.setTitoloExtra(titoloExtra);
        entity.setLocalitaExtra(localitaExtra);
        entity.company = croceNew;
        turnoService.save(entity);

        iscrizioni = recuperaIscrizioni(turnoOld, entity);
        entity.setIscrizioni(iscrizioni);
        turnoService.save(entity);
    }// end of method


    private Servizio recuperaServizio(Croce croceNew, TurnoAmb turnoOld) {
        Servizio servizioNew = null;
        ServizioAmb servizioOld = null;
        String sigla;

        if (turnoOld != null) {
            servizioOld = turnoOld.getTipo_turno();
            if (servizioOld != null) {
                sigla = servizioOld.getSigla();

                if (!sigla.equals("")) {
                    servizioNew = servizioService.findByKeyUnica(croceNew, sigla);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle

        return servizioNew;
    }// end of method


    private List<Iscrizione> recuperaIscrizioni(TurnoAmb turnoOld, Turno turnoNew) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        Iscrizione iscrizione;

        iscrizione = recuperaIscrizione(turnoOld, turnoOld.getMilite_funzione1(), turnoOld.getFunzione1(), turnoOld.getOre_milite1(), turnoOld.isProblemi_funzione1(), turnoNew);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld, turnoOld.getMilite_funzione2(), turnoOld.getFunzione2(), turnoOld.getOre_milite2(), turnoOld.isProblemi_funzione2(), turnoNew);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld, turnoOld.getMilite_funzione3(), turnoOld.getFunzione3(), turnoOld.getOre_milite3(), turnoOld.isProblemi_funzione3(), turnoNew);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        iscrizione = recuperaIscrizione(turnoOld, turnoOld.getMilite_funzione4(), turnoOld.getFunzione4(), turnoOld.getOre_milite4(), turnoOld.isProblemi_funzione4(), turnoNew);
        if (iscrizione != null) {
            iscrizioni.add(iscrizione);
        }// end of if cycle

        if (iscrizioni.size() == 0) {
            iscrizioni = null;
        }// end of if cycle

        return iscrizioni;
    }// end of method


    /**
     * Crea la singola iscrizione (embedded)
     * Non è detto che ci sia il login corretto per la company
     * Quindi non posso usare il metodo userService.findOrCrea() che usa la company del login
     * Quindi inserisco la company direttamente
     */
    private Iscrizione recuperaIscrizione(TurnoAmb turnoOld, UtenteAmb utenteOld, FunzioneAmb funzioneOld, int durata, boolean esisteProblema, Turno turnoNew) {
        Iscrizione entity = null;
        String siglaOld;
        String codeNew;
        Croce croceNew = (Croce) turnoNew.getCompany();
        Funzione funzioneNew = null;
        Milite militeNew;
        LocalDateTime timestamp = LocalDateTime.now();
        String nota = "";

        if (utenteOld != null) {
            militeNew = recuperaMilite(croceNew, utenteOld);
        } else {
            return null;
        }// end of if/else cycle

        if (funzioneOld != null) {
            funzioneNew = recuperaFunzione(funzioneOld, croceNew, turnoNew);
        }// end of if cycle

        entity = new Iscrizione();
        entity.setMilite(militeNew);
        entity.setFunzione(funzioneNew);
        entity.setTimestamp(timestamp);
        entity.setDurata(durata);
        entity.setEsisteProblema(esisteProblema);
        entity.setNotificaInviata(false);
        entity.note = nota;

        return entity;
    }// end of method


    private Milite recuperaMilite(Croce croceNew, UtenteAmb utenteOld) {
        Milite milite = null;
        String nome = utenteOld.getNome();
        String cognome = utenteOld.getCognome();

        milite = militeService.findByNomeCognome(croceNew, nome, cognome);

        return milite;
    }// end of method


    private Funzione recuperaFunzione(FunzioneAmb funzioneOld, Croce croceNew, Turno turnoNew) {
        Funzione funzione = null;
        List<Funzione> funzioniEmbeddeNelServizio = null;
        String siglaOld;
        String codeNew;
        Servizio servizio = turnoNew.getServizio();
        siglaOld = funzioneOld.getSigla();
        codeNew = siglaOld;
        funzioniEmbeddeNelServizio = servizio.getFunzioni();

        for (Funzione funz : funzioniEmbeddeNelServizio) {
            if (funz.getCode().equals(codeNew)) {
                funzione=funz;
            }// end of if cycle
        }// end of for cycle

        return funzione;
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


    /**
     * Tutte le vecchie croci (valide)
     */
    public List<CroceAmb> getAllCrociValide() {
        List<CroceAmb> croci = new ArrayList<>();
        List<CroceAmb> crociOld = CroceAmb.findAll(manager);

        for (CroceAmb croceOld : crociOld) {
            if (!ESCLUSE.contains(croceOld.getSigla())) {
                croci.add(croceOld);
            }// end of if cycle
        }// end of for cycle

        return croci;
    }// end of constructor


    /**
     * Recupera la nuova croce
     */
    public Croce getCroce(CroceAmb croceOld) {
        Croce croceNew = croceService.findByKeyUnica(croceOld.getSigla().toLowerCase());

        return croceNew;
    }// end of constructor


    public List<TurnoAmb> findAllByCroceAndYear(CroceAmb company, int anno, EntityManager manager) {
        List<TurnoAmb> lista = new ArrayList<>();
        List<Object> resultlist = null;
        Date primoGennaioDate = dateService.localDateToDate(dateService.getPrimoGennaio(anno));
        Timestamp primoGennaio = new Timestamp(primoGennaioDate.getTime());
        Date trentunoDicembreDate = dateService.getTrentunoDicembre(anno);
        Timestamp trentunoDicembre = new Timestamp(trentunoDicembreDate.getTime());
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<TurnoAmb> from = criteriaQuery.from(TurnoAmb.class);
        criteriaQuery.where(criteriaBuilder.equal(from.get("croce"), company));
        criteriaQuery.where(criteriaBuilder.greaterThan(from.get("inizio"), primoGennaio));
        criteriaQuery.where(criteriaBuilder.lessThan(from.get("fine"), trentunoDicembre));
        CriteriaQuery<Object> select = criteriaQuery.select(from);
        select.orderBy(criteriaBuilder.asc(from.get("giorno")));
        TypedQuery<Object> typedQuery = manager.createQuery(select);
        resultlist = typedQuery.getResultList();

        for (Object entity : resultlist) {
            lista.add((TurnoAmb) entity);
        }// end of for cycle

        return lista;
    }// end of method

}// end of class
