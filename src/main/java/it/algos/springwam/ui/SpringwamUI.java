package it.algos.springwam.ui;

import it.algos.springwam.entity.croce.CroceList;
import it.algos.springwam.entity.funzione.FunzioneList;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.entity.address.AddressList;
import it.algos.springvaadin.entity.company.CompanyList;
import it.algos.springvaadin.entity.log.LogList;
import it.algos.springvaadin.entity.logtype.LogtypeList;
import it.algos.springvaadin.entity.stato.StatoList;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.ui.AUI;
import com.vaadin.server.VaadinRequest;
import it.algos.springwam.application.AppCost;
import com.vaadin.annotations.Theme;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Grid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created by gac on @TODAY@
 * <p>
 * UI di partenza dell'applicazione, selezionata da SpringBoot con @SpringUI()
 * Questa classe DEVE estendere AlgosUIParams
 * Questa classe DEVE prevedere l'Annotation @SpringUI()
 * All'interno dell'applicazione, @SpringUI() deve essere utilizzata in una sola classe che estende UI
 */
@Slf4j
@Theme("wam")
@SpringUI()
@SpringViewDisplay()
@Scope("session")
public class SpringwamUI extends AUI {

    /**
     * Creazione delle viste (moduli) specifiche dell'applicazione.
     * La superclasse AlgosUIParams crea (flag true/false) le viste (moduli) usate da tutte le applicazioni
     * I flag si regolano in @PostConstruct:init()
     * <p>
     * Aggiunge al menu generale, le viste (moduli) disponibili alla partenza dell'applicazione
     * Ogni modulo può eventualmente modificare il proprio menu
     * <p>
     * Deve (DEVE) essere sovrascritto dalla sottoclasse
     * Chiama il metodo  addView(...) della superclasse per ogni vista (modulo)
     * La vista viene aggiunta alla barra di menu principale (di partenza)
     * La vista viene aggiunta allo SpringViewProvider usato da SpringNavigator
     */
    protected void addVisteSpecifiche() {
		menuLayout.addView(CroceList.class);
		menuLayout.addView(FunzioneList.class);
    }// end of method

    /**
     * Lancio della vista iniziale
     * Chiamato DOPO aver finito di costruire il MenuLayout e la AlgosUI
     * Deve (DEVE) essere sovrascritto dalla sottoclasse
     */
    @Override
    protected void startVistaIniziale() {
        getNavigator().navigateTo(ACost.VIEW_HOME);
    }// end of method


}// end of class
