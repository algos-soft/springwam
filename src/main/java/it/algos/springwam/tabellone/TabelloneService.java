package it.algos.springwam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.springvaadin.annotation.AIScript;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAListButton;
import it.algos.springvaadin.enumeration.EATypeButton;
import it.algos.springvaadin.service.ADateService;
import it.algos.springvaadin.service.ALoginService;
import it.algos.springvaadin.service.AReflectionService;
import it.algos.springvaadin.service.AService;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.riga.RigaService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.entity.turno.TurnoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 14:21
 * Estende la Entity astratta AService. Layer di collegamento tra il Presenter e la Repository.
 * Annotated with @@Slf4j (facoltativo) per i logs automatici
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with @Service (ridondante)
 * Annotated with @Scope (obbligatorio = 'singleton')
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script del framework
 */
@Slf4j
@SpringComponent
@Service
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Qualifier(AppCost.TAG_TAB)
@AIScript(sovrascrivibile = false)
public class TabelloneService extends AService {

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private AReflectionService reflection;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private RigaService rigaService;

    @Autowired
    private ADateService dateService;

    /**
     * Costruttore @Autowired (nella superclasse)
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation
     * Si usa un @Qualifier(), per avere la sottoclasse specifica
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti
     */
    public TabelloneService() {
        super(null);
    }// end of Spring constructor


    /**
     * Colonne visibili (e ordinate) nella Grid
     * Sovrascrivibile
     * La colonna ID normalmente non si visualizza
     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
     * 2) Se la classe AEntity->@AIList(columns = ...) prevede una lista specifica, usa quella lista (con o senza ID)
     * 3) Se non trova AEntity->@AIList, usa tutti i campi della AEntity (senza ID)
     * 4) Se trova AEntity->@AIList(showsID = true), questo viene aggiunto, indipendentemente dalla lista
     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
     * Ad esempio: company della classe ACompanyEntity
     *
     * @return lista di fields visibili nella Grid
     */
    @Override
    public List<Field> getListFields() {
        List<Field> listaField = new ArrayList<>();
        Field field = reflection.getField(Riga.class, "servizio");
        listaField.add(field);
        return listaField;
    }// end of method


    public List creaColonne(LocalDate giornoInizio, int giorni) {
        List colonne = new ArrayList<>();


        return colonne;
    }// end of method


//    /**
//     * Costruisce le righe della grid (tabellone)
//     * Per adesso 7 giorni @todo provvisorio
//     */
//    protected List<LinkedHashMap<String, String>> findMappaRowsColumns() {
//        List<LinkedHashMap<String, String>> mappa = new ArrayList<>();
//        List<String> column = findColumns();
//        List<Servizio> servizi = servizioService.findAllByCompany();
//
//        if (servizi != null) {
//            for (int k = 0; k < servizi.size(); k++) {
//                LinkedHashMap<String, String> fakeBean = new LinkedHashMap<>();
//
//                fakeBean.put(column.get(0), servizi.get(k).getDescrizione());
//                for (int y = 1; y < column.size(); y++) {
//                    fakeBean.put(column.get(y), "first - " + y);
//                }// end of for cycle
//
//                mappa.add(fakeBean);
//            }// end of for cycle
//        }// end of if cycle
//
//
////        for (int i = 0; i < 5; i++) {
////            LinkedHashMap<String, String> fakeBean = new LinkedHashMap<>();
////            fakeBean.put(column.get(0), "first" + i);
////            fakeBean.put(column.get(1), "last" + i);
////            fakeBean.put(column.get(2), "last" + i);
////            fakeBean.put(column.get(3), "last" + i);
////            rows.add(fakeBean);
////        }// end of for cycle
//
//        return mappa;
//    }// end of method


    /**
     * Costruisce le colonne della grid (tabellone)
     * Per adesso 7 giorni @todo provvisorio
     */
    protected List<String> findColumns() {
        return findColumns(LocalDate.now(), 7);
    }// end of method


    /**
     * Costruisce le colonne della grid (tabellone)
     * Per adesso 7 giorni @todo provvisorio
     */
    protected List<String> findColumnsShort() {
        return findColumnsShort(LocalDate.now(), 7);
    }// end of method


    /**
     * Costruisce le colonne della grid (tabellone)
     */
    protected List<String> findColumns(LocalDate inizio, int numGiorni) {
        List<String> columns = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d");

        columns.add("");
        for (int k = 0; k < numGiorni; k++) {
            columns.add(inizio.plusDays(k).format(formatter));
        }// end of for cycle

        return columns;
    }// end of method


    /**
     * Costruisce le colonne della grid (tabellone)
     */
    protected List<String> findColumnsShort(LocalDate inizio, int numGiorni) {
        List<String> columns = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M");

        columns.add("x");
        for (int k = 0; k < numGiorni; k++) {
            columns.add(inizio.plusDays(k).format(formatter));
        }// end of for cycle

        return columns;
    }// end of method

//    /**
//     * Returns all entities of the type.
//     * <p>
//     * Senza filtri
//     * Ordinati per ID
//     * <p>
//     * Methods of this library return Iterable<T>, while the rest of my code expects Collection<T>
//     * L'annotation standard di JPA prevede un ritorno di tipo Iterable, mentre noi usiamo List
//     * Eseguo qui la conversione, che rimane trasparente al resto del programma
//     *
//     * @return all entities
//     */
//    @Override
//    public List<? extends AEntity> findAll() {
//        return creaRighe(LocalDate.now(), 7);
//    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (vuota e non salvata)
     */
    @Override
    public AEntity newEntity() {
        return null;
    }// end of method


    /**
     * Crea in memoria le righe di questa company per i turni dei giorni indicati
     *
     * @return selected entities
     */
    public List<Riga> creaRighe(LocalDate giornoInizio, int giorni) {
        List<Riga> righe = new ArrayList<>();
        List<Turno> listaTurni;
        Turno turno;
        List<Servizio> listaServiziVisibili = servizioService.findAllByCompanyVisibili();
        LocalDate giorno = null;
        Riga riga;

        if (listaServiziVisibili != null && listaServiziVisibili.size() > 0) {
            for (int k = 0; k < listaServiziVisibili.size(); k++) {
                Servizio servizio = listaServiziVisibili.get(k);
                listaTurni = new ArrayList<>();

                for (int y = 0; y < giorni; y++) {
                    giorno = giornoInizio.plusDays(y);
                    turno = turnoService.findByGiornoAndServizio(giorno, servizio);
                    listaTurni.add(turno);
                }// end of for cycle

                riga = rigaService.newEntity(giornoInizio, servizio, listaTurni);
                righe.add(riga);
            }// end of for cycle
        }// end of if cycle

        return righe;
    }// end of method


    /**
     * Lista di bottoni presenti nella toolbar (footer) della view AList
     * Legge la enumeration indicata nella @Annotation della AEntity
     *
     * @return lista (type) di bottoni visibili nella toolbar della view AList
     */
    public List<EATypeButton> getListTypeButtons() {
        return null;
    }// end of method


    /**
     * Lista di iscrizioni, lunga quanto le funzioni del servizio del turno
     * Se una funzione non ha iscrizione, ne metto una vuota
     *
     * @param turno di riferimento
     *
     * @return lista (Iscrizione) di iscrizioni del turno
     */
    public List<Iscrizione> getIscrizioni(Turno turno) {
        List<Iscrizione> items = new ArrayList<>();
        List<Iscrizione> iscrizioniEmbeddeTurno = turno.getIscrizioni();
        Servizio servizio = null;
        servizio = turno.getServizio();
        List<Funzione> funzioni = servizio.getFunzioni();
        boolean trovata;

        for (Funzione funz : funzioni) {
            trovata = false;

            if (array.isValid(iscrizioniEmbeddeTurno)) {
                for (Iscrizione iscr : iscrizioniEmbeddeTurno) {
                    if (iscr.getFunzione().getCode().equals(funz.getCode())) {
                        items.add(iscr);
                        trovata = true;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle

            if (!trovata) {
                items.add(null);
            }// end of if cycle
        }// end of for cycle

        return items;
    }// end of method

}// end of class