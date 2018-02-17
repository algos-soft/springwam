package it.algos.springvaadin.azione;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.ItemClickListener;
import it.algos.springvaadin.enumeration.EATypeAction;
import it.algos.springvaadin.grid.IAGrid;
import it.algos.springvaadin.lib.ACost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 12-dic-2017
 * Time: 12:53
 */
@Slf4j
@SpringComponent
@Scope("prototype")
@Qualifier(ACost.TAG_AZ_CLICK)
public class AActionClick extends AAction {

    private final static long defaultTimeInterval = 50000;

    /**
     * Costruttore @Autowired
     *
     * @param applicationEventPublisher iniettato da Spring
     */
    public AActionClick(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }// end of @Autowired constructor


    /**
     * Metodo invocato (automaticamente dalla annotation) DOPO il costruttore
     */
    @PostConstruct
    protected void inizia() {
        super.type = EATypeAction.click;
    }// end of method


    /**
     * Aggiunge alla Grid il listener per la singola azione specifica
     *
     * @param algosGrid di riferimento
     */
    @Override
    public void addListener(IAGrid algosGrid) {


        algosGrid.getGrid().addItemClickListener(new ItemClickListener() {
            boolean isAlreadyOneClick;

            Pippo pippo=new Pippo(false);

            @Override
            public void itemClick(Grid.ItemClick itemClick) {
                log.warn("click: "+System.currentTimeMillis());
                prova();
//                if (isAlreadyOneClick) {
////                    System.out.println("double click");
//                    fire(EATypeAction.doppioClick, (AEntity) itemClick.getItem());
//                    isAlreadyOneClick = false;
//                } else {
//                    isAlreadyOneClick = true;
////                    Timer timer = new Timer("doubleClickTimer", false);
////                    timer.schedule(new TimerTask() {
////                        /**
////                         * The action to be performed by this timer task.
////                         */
////                        @Override
////                        public void run() {
////                        }// end of method
////                    }, defaultTimeInterval);
//                }// end of if cycle
                log.warn("ritorno dal ciclo: "+System.currentTimeMillis());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

//    @Async
    public void prova() {
        long inizioCiclo = System.currentTimeMillis();
        log.warn("inizio ciclo: "+inizioCiclo);
        Timer timer = new Timer("doubleClickTimer", false);
        timer.schedule(new TimerTask() {
            /**
             * The action to be performed by this timer task.
             */
            @Override
            public void run() {
            }// end of method
        }, defaultTimeInterval);
        long fineCiclo = System.currentTimeMillis();
        long intervalloDiTempo = fineCiclo - inizioCiclo;
        log.warn("fine ciclo: "+fineCiclo);
        log.warn("intervalloDiTempo: "+intervalloDiTempo);
    }// end of method

    //    long defaultTimeInterval = 500;
//    long lastClickTime = 0;
//    long currentClickTime;
//    long intervalloDiTempo = 0;
//
    protected void pippoz() {
        try { // prova ad eseguire il codice
            Thread.sleep(defaultTimeInterval);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch
    }// end of method

    private class Pippo {
        boolean isAlreadyOneClick;

        public Pippo(boolean isAlreadyOneClick) {
            this.isAlreadyOneClick = isAlreadyOneClick;
        }

        public boolean isAlreadyOneClick() {
            return isAlreadyOneClick;
        }

        public void setAlreadyOneClick(boolean alreadyOneClick) {
            isAlreadyOneClick = alreadyOneClick;
        }
    }// end of class
}// end of class
