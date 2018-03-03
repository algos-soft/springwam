package it.algos.springwam;

import com.vaadin.spring.annotation.SpringView;
import it.algos.springvaadin.service.ASessionService;
import it.algos.springwam.entity.funzione.Funzione;
import it.algos.springwam.entity.funzione.FunzioneService;
import it.algos.springwam.entity.iscrizione.Iscrizione;
import it.algos.springwam.entity.servizio.Servizio;
import it.algos.springwam.entity.turno.Turno;
import it.algos.springwam.tabellone.TabelloneService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Project springwam
 * Created by Algos
 * User: gac
 * Date: sab, 03-mar-2018
 * Time: 10:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class TabelloneServiceTest extends ATest {


    @InjectMocks
    public TabelloneService service;

    private List<Iscrizione> iscrizioniPreviste;
    private List<Iscrizione> iscrizioniOttenute;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        service.array = array;

        super.setUp();
    }// end of method


    @SuppressWarnings("javadoc")
    /**
     * Lista di iscrizioni, lunga quanto le funzioni del servizio del turno
     * Se una funzione non ha iscrizione, ne metto una vuota
     *
     * @param turno di riferimento
     *
     * @return lista (Iscrizione) di iscrizioni del turno
     */
    @Test
    public void getIscrizioni() {
        iscrizioniPreviste = iscrizioniAll;
        iscrizioniOttenute = service.getIscrizioni(turnoUno);
        assertEquals(iscrizioniPreviste.size(), iscrizioniOttenute.size());

        iscrizioniPreviste = iscrizioniAll;
        iscrizioniOttenute = service.getIscrizioni(turnoDue);
        assertEquals(iscrizioniPreviste.size(), iscrizioniOttenute.size());
        iscrizionePrevista = iscrizioneCinque;
        iscrizioneOttenuta = iscrizioniOttenute.get(0);
        assertEquals(iscrizionePrevista, iscrizioneOttenuta);
        iscrizionePrevista = iscrizioneSei;
        iscrizioneOttenuta = iscrizioniOttenute.get(1);
        assertEquals(iscrizionePrevista, iscrizioneOttenuta);

        iscrizioniPreviste = iscrizioniDue;
        iscrizioniOttenute = service.getIscrizioni(turnoTre);
        assertEquals(iscrizioniPreviste.size(), iscrizioniOttenute.size());
        iscrizionePrevista = iscrizioneSette;
        iscrizioneOttenuta = iscrizioniOttenute.get(1);
        assertEquals(iscrizionePrevista, iscrizioneOttenuta);
    }// end of single test


}// end of class
