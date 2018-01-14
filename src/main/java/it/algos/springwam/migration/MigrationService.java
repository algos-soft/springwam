package it.algos.springwam.migration;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
public class MigrationService {


    private EntityManager manager;
    private final static String PERSISTENCE_UNIT_NAME = "Webambulanzelocal";

    private final static String[] escluseMatrice = {"ALGOS", "DEMO", "PAVT"};
    public final static List<String> ESCLUSE = Arrays.asList(escluseMatrice);


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
     */
    public void importCroce(String siglaCroceOld) {
//        Croce croceNew = null;
//        CroceAmb croceOld = null;
//        creaManagers();
//        croceOld = CroceAmb.findBySigla(siglaCroceOld, manager);
//
//        if (croceOld != null) {
//            croceNew = importCroce(croceOld, siglaCroceNew, anno);
//        } else {
//            LibAvviso.warn("Non trovata la croce " + LibText.setRossoBold(siglaCroceOld) + " in webambulanze");
//        }// end of if/else cycle
//
//        chiudeManagers();
//
//        companyData.updatePreferenze(croceNew);
    }// end of constructor


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
