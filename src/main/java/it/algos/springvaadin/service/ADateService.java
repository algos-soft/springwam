package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
     * Date HA ore, minuti e secondi
     * LocalDate NON ha ore, minuti e secondi
     * Si perdono quindi le ore i minuti ed i secondi di Date
     *
     * @param data da convertire
     *
     * @return data locale (deprecated)
     */
    @Deprecated
    public LocalDate dateToLocalDate(Date data) {
        Instant instant = Instant.ofEpochMilli(data.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }// end of method


    /**
     * Convert java.time.LocalDate to java.util.Date
     * LocalDate NON ha ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * La Date ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Convert java.util.Date to java.time.LocalDateTime
     * Date HA ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * Non si perde nulla
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
     * Convert java.time.LocalDateTime to java.util.Date
     * LocalDateTime HA ore, minuti e secondi
     * Date HA ore, minuti e secondi
     * Non si perde nulla
     *
     * @param localDateTime da convertire
     *
     * @return data (deprecated)
     */
    @Deprecated
    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }// end of method


    /**
     * Convert java.time.LocalDate to java.time.LocalDateTime
     * LocalDate NON ha ore, minuti e secondi
     * LocalDateTime HA ore, minuti e secondi
     * La LocalDateTime ottenuta ha il tempo regolato a mezzanotte
     *
     * @param localDate da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    @Deprecated
    public LocalDateTime localDateToLocalDateTime(LocalDate localDate) {
        Date date = localDateToDate(localDate);
        Instant istante = date.toInstant();
        return istante.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }// end of method


    /**
     * Convert java.time.LocalDateTime to java.time.LocalDate
     * LocalDateTime HA ore, minuti e secondi
     * LocalDate NON ha ore, minuti e secondi
     * Si perdono quindi le ore i minuti ed i secondi di Date
     *
     * @param localDateTime da convertire
     *
     * @return data con ore e minuti alla mezzanotte
     */
    public LocalDate localDateTimeToLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }// end of method


    /**
     * Restituisce una stringa nel formato d-M-yy
     * <p>
     * Returns a string representation of the date <br>
     * Not using leading zeroes in day <br>
     * Two numbers for year <b>
     *
     * @param localDate da rappresentare
     *
     * @return la data sotto forma di stringa
     */
    public String getWeekLong(LocalDate localDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("EEEE d");
        return format.format(localDateToDate(localDate));
    }// end of method


    /**
     * Ritorna il numero della settimana dell'anno di una data fornita.
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero della settimana dell'anno
     */
    @Deprecated
    public int getWeekYear(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_YEAR);
    }// end of method


    /**
     * Ritorna il numero della settimana del mese di una data fornita.
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero della settimana del mese
     */
    @Deprecated
    public int getWeekMonth(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.WEEK_OF_MONTH);
    }// end of method


    /**
     * Ritorna il numero del giorno dell'anno di una data fornita.
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero del giorno dell'anno
     */
    @Deprecated
    public int getDayYear(Date data) {
        return dateToLocalDate(data).getDayOfYear();
    }// end of method


    /**
     * Ritorna il numero del giorno del mese di una data fornita.
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero del giorno del mese
     */
    @Deprecated
    public int getDayOfMonth(Date data) {
        return dateToLocalDate(data).getDayOfMonth();
    }// end of method


    /**
     * Ritorna il numero del giorno della settimana di una data fornita.
     * Usa Calendar
     *
     * @param data fornita
     *
     * @return il numero del giorno della settimana (1=dom, 7=sab)
     */
    @Deprecated
    public int getDayWeek(Date data) {
        Calendar calendario = getCal(data);
        return calendario.get(Calendar.DAY_OF_WEEK);
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma breve
     */
    public String getDayWeekShort(LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il giorno della settimana in forma breve
     */
    @Deprecated
    public String getDayWeekShort(Date data) {
        return getDayWeekShort(dateToLocalDate(data));
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     *
     * @param localDate fornita
     *
     * @return il giorno della settimana in forma estesa
     */
    public String getDayWeekFull(LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }// end of method


    /**
     * Ritorna il giorno (testo) della settimana di una data fornita.
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il giorno della settimana in forma estesa
     */
    @Deprecated
    public String getDayWeekFull(Date data) {
        return getDayWeekFull(dateToLocalDate(data));
    }// end of method


    /**
     * Ritorna il numero delle ore di una data fornita.
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero delle ore
     */
    @Deprecated
    public int getOre(Date data) {
        return dateToLocalDateTime(data).getHour();
    }// end of method


    /**
     * Ritorna il numero dei minuti di una data fornita.
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero dei minuti
     */
    @Deprecated
    public int getMinuti(Date data) {
        return dateToLocalDateTime(data).getMinute();
    }// end of method


    /**
     * Ritorna il numero dei secondi di una data fornita.
     * Usa LocalDateTime internamente, perché Date è deprecato
     *
     * @param data fornita
     *
     * @return il numero dei secondi
     */
    @Deprecated
    public int getSecondi(Date data) {
        return dateToLocalDateTime(data).getSecond();
    }// end of method


    /**
     * Ritorna il numero dell'anno di una data fornita.
     * Usa LocalDate internamente, perché Date è deprecato
     *
     * @return il numero dell'anno
     */
    @Deprecated
    public int getYear(Date data) {
        return dateToLocalDate(data).getYear();
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno corrente.
     *
     * @return primo gennaio dell'anno
     */
    public LocalDate getPrimoGennaio() {
        return getPrimoGennaio(LocalDate.now().getYear());
    }// end of method


    /**
     * Costruisce la data per il 1° gennaio dell'anno indicato.
     *
     * @param anno di riferimento
     *
     * @return primo gennaio dell'anno
     */
    public LocalDate getPrimoGennaio(int anno) {
        return LocalDate.of(anno, 1, 1);
    }// end of method


    /**
     * Costruisce la localData per il giorno dell'anno indicato.
     *
     * @param giorno di riferimento (numero progressivo dell'anno)
     *
     * @return localData
     */
    public LocalDate getLocalDateByDay(int giorno) {
        return LocalDate.ofYearDay(LocalDate.now().getYear(), giorno);
    }// end of single test


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
