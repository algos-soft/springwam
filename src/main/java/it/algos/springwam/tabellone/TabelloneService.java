package it.algos.springwam.tabellone;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.LibReflection;
import it.algos.springvaadin.service.AlgosServiceImpl;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.riga.Riga;
import it.algos.springwam.entity.riga.RigaService;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.servizio.ServizioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: lun, 13-nov-2017
 * Time: 14:21
 */
@Service
@Qualifier(AppCost.TAG_TAB)
@Slf4j
public class TabelloneService extends AlgosServiceImpl {

    @Autowired
    private ServizioService servizioService;


    @Autowired
    private RigaService rigaService;

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
        Field field = LibReflection.getField(Riga.class,"servizio");
        listaField.add(field);
        return listaField;
    }// end of method


    /**
     * Costruisce le righe della grid (tabellone)
     * Per adesso 7 giorni @todo provvisorio
     */
    protected List<LinkedHashMap<String, String>> findMappaRowsColumns() {
        List<LinkedHashMap<String, String>> mappa = new ArrayList<>();
        List<String> column = findColumns();
        List<Servizio> servizi = servizioService.findAllByCompany();

        if (servizi != null) {
            for (int k = 0; k < servizi.size(); k++) {
                LinkedHashMap<String, String> fakeBean = new LinkedHashMap<>();

                fakeBean.put(column.get(0), servizi.get(k).getDescrizione());
                for (int y = 1; y < column.size(); y++) {
                    fakeBean.put(column.get(y), "first - " + y);
                }// end of for cycle

                mappa.add(fakeBean);
            }// end of for cycle
        }// end of if cycle


//        for (int i = 0; i < 5; i++) {
//            LinkedHashMap<String, String> fakeBean = new LinkedHashMap<>();
//            fakeBean.put(column.get(0), "first" + i);
//            fakeBean.put(column.get(1), "last" + i);
//            fakeBean.put(column.get(2), "last" + i);
//            fakeBean.put(column.get(3), "last" + i);
//            rows.add(fakeBean);
//        }// end of for cycle

        return mappa;
    }// end of method


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
     * Returns all instances of the current company.
     *
     * @return selected entities
     */
    public List findAllByCompany() {
        return rigaService.findAllByCompany();
    }// end of method

}// end of class