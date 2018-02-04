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
import it.algos.springwam.entity.croce.Croce;
import it.algos.springwam.entity.croce.CroceService;
import it.algos.springwam.entity.croce.EAOrganizzazione;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.milite.Milite;
import it.algos.springwam.entity.milite.MiliteService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.utente.Utente;
import it.algos.springwam.entity.utente.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Arrays;
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
    private CroceService croceService;

    @Autowired
    private FunzioneService funzioneService;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private MiliteService militeService;

    @Autowired
    private RoleService roleService;


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

//    /**
//     * Aggiunge le funzioni al servizio
//     *
//     * @param servizioOld della companyOld
//     * @param servizioNew appena creato in springWam
//     */
//    private void servizioAddFunzioni(ServizioAmb servizioOld, Servizio servizioNew) {
//        List<Funzione> listaFunzioni = new ArrayList<>();
//        Funzione funz = null;
//        int numeroFunzioniObbligatorie = servizioOld.getFunzioni_obbligatorie();
//        long idFunzione1 = servizioOld.getFunzione1_id();
//        long idFunzione2 = servizioOld.getFunzione2_id();
//        long idFunzione3 = servizioOld.getFunzione3_id();
//        long idFunzione4 = servizioOld.getFunzione4_id();
//
//        funz = mappaFunzioni.get(idFunzione1);
//        if (funz != null) {
//            funz.setObbligatoria(numeroFunzioniObbligatorie > 0);
//            listaFunzioni.add(funz);
//        }// end of if cycle
//
//        funz = mappaFunzioni.get(idFunzione2);
//        if (funz != null) {
//            funz.setObbligatoria(numeroFunzioniObbligatorie > 1);
//            listaFunzioni.add(funz);
//        }// end of if cycle
//
//        funz = mappaFunzioni.get(idFunzione3);
//        if (funz != null) {
//            funz.setObbligatoria(numeroFunzioniObbligatorie > 2);
//            listaFunzioni.add(funz);
//        }// end of if cycle
//
//        funz = mappaFunzioni.get(idFunzione4);
//        if (funz != null) {
//            funz.setObbligatoria(numeroFunzioniObbligatorie > 3);
//            listaFunzioni.add(funz);
//        }// end of if cycle
//
//        servizioNew.setFunzioni(listaFunzioni);
//        try { // prova ad eseguire il codice
//            servizioService.save(servizioNew);
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//        }// fine del blocco try-catch
//    }// end of method


    /**
     * Importa tutti i militi esistenti in webambulanze, in fase iniziale di setup
     */
    public void importAllMilitiSetup() {
//        if (militeService.count() == 0) {
        importAllMiliti();
//        }// end of if cycle
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
        List<UtenteAmb> utenti = UtenteAmb.findAll(croceOld, manager);

        for (UserAmb userOld : users) {
            creaSingoloMilite(croceNew, utenti, userOld);
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
    private void creaSingoloMilite(Croce croceNew, List<UtenteAmb> utenti, UserAmb userOld) {
        Milite entity;
        String nickname = userOld.getNickname();
        String password = userOld.getPass();
        Role role = getRuolo(userOld);
        boolean enabled = userOld.isEnabled();

        if (militeService.findByKeyUnica(croceNew, nickname) == null) {

            entity = new Milite();
            entity.setNickname(nickname);
            entity.setPassword(password);
            entity.setRole(role);
            entity.setEnabled(true);
            entity.setNome(nickname);
            entity.setCognome(nickname);
            entity.setEnabled(enabled);
            entity.setDipendente(false);
            entity.setInfermiere(false);
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


    //    private Utente regolaRuoliAssociazione(UtenteAmb militeOld, Utente utenteNew) {
//        String tagIP = "I.P.";
//        List<Long> listaRuoliUtente = UtenteRuoloAmb.getListAdmin(manager);
//        UserAmb utente = UserAmb.getUtenteByMiliteID(manager, militeOld);
//        long idKeyUtente = 0;
//        if (utente != null) {
//            idKeyUtente = utente.getId();
//        }// end of if cycle
//        boolean isAdmin = listaRuoliUtente.contains(idKeyUtente);
//        boolean isDipendente = militeOld.isDipendente();
//        boolean isInfermiere = militeOld.getCognome().startsWith(tagIP);
//
//        utenteNew.setAdmin(isAdmin);
//        utenteNew.setDipendente(isDipendente);
//        utenteNew.setInfermiere(isInfermiere);
//
//        try { // prova ad eseguire il codice
//            utenteNew = (Utente) utenteService.save(utenteNew);
//        } catch (Exception unErrore) { // intercetta l'errore
//            log.error(unErrore.toString());
//        }// fine del blocco try-catch
//
//        return utenteNew;
//    }// end of method


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

}// end of class
