package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: lun, 05-feb-2018
 * Time: 14:58
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class ADateService {


    /**
     * Convert java.util.Date to java.time.LocalDate
     *
     * @param data da convertire
     *
     * @return data locale
     */
    public LocalDate dateToLocalDate(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }// end of method


    /**
     * Convert java.util.Date to java.time.LocalDateTime
     *
     * @param data da convertire
     *
     * @return data e ora locale
     */
    public LocalDateTime dateToLocalDateTime(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }// end of method


    /**
     * Convert java.time.LocalDate to java.util.Date
     *
     * @param localDate da convertire
     *
     * @return data (deprecated)
     */
    public Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Convert java.time.LocalDate to java.time.LocalDateTime
     *
     * @param localDate da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    public LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        Instant istante = date.toInstant();
        return istante.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }// end of method


    /**
     * Convert java.time.LocalDateTime to java.time.LocalDate
     *
     * @param localDateTime da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    public LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }// end of method


    /**
     * Convert java.time.LocalDateTime to java.util.Date
     *
     * @param localDateTime da convertire
     *
     * @return data (deprecated)
     */
    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Ritorna il numero della settimana dell'anno di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero della settimana dell'anno
     */
    public int getWeekYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_YEAR);
    }// end of method


    /**
     * Ritorna il numero della settimana del mese di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero della settimana del mese
     */
    public int getWeekMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_MONTH);
    }// end of method


    /**
     * Ritorna il numero del giorno dell'anno di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno dell'anno
     */
    public int getDayYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_YEAR);
    }// end of method


    /**
     * Ritorna il numero del giorno del mese di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno del mese
     */
    public int getDayMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_MONTH);
    }// end of method


    /**
     * Ritorna il numero del giorno della settimana di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    public int getDayWeek(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_WEEK);
    }// end of method


    /**
     * Ritorna il numero delle ore di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero delle ore
     */
    public int getOre(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.HOUR_OF_DAY);
    }// end of method


    /**
     * Ritorna il numero dei minuti di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero dei minuti
     */
    public int getMinuti(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.MINUTE);
    }// end of method


    /**
     * Ritorna il numero dei secondi di una data fornita.
     * <p>
     *
     * @param data fornita
     *
     * @return il numero dei secondi
     */
    public int getSecondi(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.SECOND);
    }// end of method


    /**
     * Ritorna il numero dell'anno di una data fornita.
     * <p>
     *
     * @return il numero dell'anno
     */
    public int getYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.YEAR);
    }// end of method


    /**
     * Ritorna il numero del giorno dell'anno della data corrente.
     * <p>
     *
     * @return il numero del giorno dell'anno
     */
    public int getDayYear() {
        return getDayYear(new Date());
    }// end of method


    /**
     * Ritorna il numero del giorno del mese della data corrente.
     * <p>
     *
     * @return il numero del giorno del mese
     */
    public int getDayMonth() {
        return getDayMonth(new Date());
    }// end of method


    /**
     * Ritorna il numero del giorno della settimana della data corrente.
     * <p>
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    public int getDayWeek() {
        return getDayWeek(new Date());
    }// end of method


    /**
     * Ritorna il numero delle ore della data corrente.
     *
     * @return il numero delle ore
     */
    public int getOre() {
        return getOre(new Date());
    }// end of method


    /**
     * Ritorna il numero dei minuti della data corrente.
     *
     * @return il numero dei minuti
     */
    public int getMinuti() {
        return getMinuti(new Date());
    }// end of method


    /**
     * Ritorna il numero dei secondi della data corrente.
     *
     * @return il numero dei secondi
     */
    public int getSecondi() {
        return getSecondi(new Date());
    }// end of method


    /**
     * Ritorna il numero dell'anno della data corrente.
     *
     * @return il numero dei secondi
     */
    public int getYear() {
        return getYear(new Date());
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno corrente.
     * <p>
     *
     * @return primo gennaio dell'anno
     */
    public Date getPrimoGennaio() {
        return creaData(1, 1, this.getYear());
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno indicato.
     * <p>
     *
     * @param anno di riferimento
     *
     * @return primo gennaio dell'anno
     */
    public Date getPrimoGennaio(int anno) {
        return creaData(1, 1, anno);
    }// end of method


    /**
     * Costruisce la data per il 31° dicembre dell'anno indicato.
     * <p>
     *
     * @param anno di riferimento
     *
     * @return ultimo dell'anno
     */
    public Date getTrentunoDicembre(int anno) {
        Date data = creaData(31, 12, anno);
        return lastTime(data);
    }// end of method


    /**
     * Forza la data all'ultimo millisecondo.
     * <p>
     *
     * @param dateIn la data da forzare
     *
     * @return la data con ore/minuti/secondi/millisecondi al valore massimo
     */
    public Date lastTime(Date dateIn) {
        Calendar calendario = getCal(dateIn);

        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        calendario.set(Calendar.MILLISECOND, 999);

        return calendario.getTime();
    }// end of method


    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     *
     * @return la data creata
     */
    public Date creaData(int giorno, int numMeseDellAnno, int anno) {
        return creaData(giorno, numMeseDellAnno, anno, 0, 0, 0);
    }// end of method

    /**
     * Crea una data.
     * <p>
     *
     * @param giorno          il giorno del mese (1 per il primo)
     * @param numMeseDellAnno il mese dell'anno (1 per gennaio)
     * @param anno            l'anno
     * @param ora             ora (24H)
     * @param minuto          il minuto
     * @param secondo         il secondo
     *
     * @return la data creata
     */
    public Date creaData(int giorno, int numMeseDellAnno, int anno, int ora, int minuto, int secondo) {
        Calendar calendario;

        if (numMeseDellAnno > 0) {
            numMeseDellAnno--;
        }// fine del blocco if

        calendario = new GregorianCalendar(anno, numMeseDellAnno, giorno, ora, minuto, secondo);
        return calendario.getTime();
    }// end of method


    private Calendar getCal() {
        /* crea il calendario */
        Calendar calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0);

        /**
         * regola il calendario come non-lenient (se la data non è valida non effettua la rotazione automatica dei
         * valori dei campi, es. 32-12-2004 non diventa 01-01-2005)
         */
        calendario.setLenient(false);

        return calendario;
    }// end of method

    /**
     * Calendario con regolata la data
     *
     * @param data da inserire nel calendario
     *
     * @return calendario regolato
     */
    private Calendar getCal(Date data) {
        Calendar calendario = getCal();
        calendario.setTime(data);
        return calendario;
    }// end of  method

}// end of class
