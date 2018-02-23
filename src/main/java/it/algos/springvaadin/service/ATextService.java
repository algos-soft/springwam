package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.enumeration.EAFirstChar;
import it.algos.springvaadin.enumeration.EAPrefType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:45
 * Classe di Libreria
 * Gestione e formattazione di stringhe di testo
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class ATextService {


    /**
     * Null-safe, short-circuit evaluation.
     *
     * @param stringa in ingresso da controllare
     *
     * @return vero se la stringa è vuota o nulla
     */
    public boolean isEmpty(final String stringa) {
        return stringa == null || stringa.trim().isEmpty();
    }// end of method


    /**
     * Null-safe, short-circuit evaluation.
     *
     * @param stringa in ingresso da controllare
     *
     * @return vero se la stringa esiste è non è vuota
     */
    public boolean isValid(final String stringa) {
        return !isEmpty(stringa);
    }// end of method


    /**
     * Controlla che sia una stringa e che sia valida.
     *
     * @param obj in ingresso da controllare
     *
     * @return vero se la stringa esiste è non è vuota
     */
    public boolean isValid(final Object obj) {
        if (obj instanceof String) {
            return !isEmpty((String) obj);
        } else {
            return false;
        }// end of if/else cycle
    }// end of method


    /**
     * Forza il primo carattere della stringa secondo il flag
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     * @param flag    maiuscolo o minuscolo
     *
     * @return uscita string in uscita
     */
    private String primoCarattere(final String testoIn, EAFirstChar flag) {
        String testoOut = testoIn.trim();
        String primo;
        String rimanente;

        if (this.isValid(testoOut)) {
            primo = testoOut.substring(0, 1);
            switch (flag) {
                case maiuscolo:
                    primo = primo.toUpperCase();
                    break;
                case minuscolo:
                    primo = primo.toLowerCase();
                    break;
                default: // caso non definito
                    break;
            } // fine del blocco switch
            rimanente = testoOut.substring(1);
            testoOut = primo + rimanente;
        }// fine del blocco if

        return testoOut.trim();
    }// end of method

    /**
     * Forza il primo carattere della stringa al carattere maiuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return test formattato in uscita
     */
    public String primaMaiuscola(final String testoIn) {
        return primoCarattere(testoIn, EAFirstChar.maiuscolo);
    }// end of method

    /**
     * Forza il primo carattere della stringa al carattere minuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return test formattato in uscita
     */
    public String primaMinuscola(final String testoIn) {
        return primoCarattere(testoIn, EAFirstChar.minuscolo);
    }// end of method


    /**
     * Elimina dal testo il tagIniziale, se esiste
     * <p>
     * Esegue solo se il testo è valido
     * Se tagIniziale è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn     ingresso
     * @param tagIniziale da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaTesta(final String testoIn, String tagIniziale) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagIniziale)) {
            tagIniziale = tagIniziale.trim();
            if (testoOut.startsWith(tagIniziale)) {
                testoOut = testoOut.substring(tagIniziale.length());
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of method

    /**
     * Elimina dal testo il tagFinale, se esiste
     * <p>
     * Esegue solo se il testo è valido
     * Se tagFinale è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn   ingresso
     * @param tagFinale da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaCoda(final String testoIn, String tagFinale) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagFinale)) {
            tagFinale = tagFinale.trim();
            if (testoOut.endsWith(tagFinale)) {
                testoOut = testoOut.substring(0, testoOut.length() - tagFinale.length());
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of method


    /**
     * Elimina il testo da tagFinale in poi
     * <p>
     * Esegue solo se il testo è valido
     * Se tagInterrompi è vuoto, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn   ingresso
     * @param tagInterrompi da dove inizia il testo da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaCodaDa(final String testoIn, String tagInterrompi) {
        String testoOut = testoIn.trim();

        if (this.isValid(testoOut) && this.isValid(tagInterrompi)) {
            tagInterrompi = tagInterrompi.trim();
            if (testoOut.contains(tagInterrompi)) {
                testoOut = testoOut.substring(0, testoOut.indexOf(tagInterrompi));
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of methodlevaCodaDa


    /**
     * Sostituisce nel testo tutte le occorrenze di oldTag con newTag.
     * Esegue solo se il testo è valido
     * Esegue solo se il oldTag è valido
     * newTag può essere vuoto (per cancellare le occorrenze di oldTag)
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso da elaborare
     * @param oldTag  da sostituire
     * @param newTag  da inserire
     *
     * @return testo modificato
     */
    public String sostituisce(final String testoIn, String oldTag, String newTag) {
        String testoOut = "";
        String prima = "";
        String rimane = testoIn;
        int pos = 0;

        if (this.isValid(testoIn) && this.isValid(oldTag)) {
            if (rimane.contains(oldTag)) {
                pos = rimane.indexOf(oldTag);

                while (pos != -1) {
                    pos = rimane.indexOf(oldTag);
                    if (pos != -1) {
                        prima += rimane.substring(0, pos);
                        prima += newTag;
                        pos += oldTag.length();
                        rimane = rimane.substring(pos);
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + rimane;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut.trim();
    }// end of  method


    public boolean isNumber(String value) {
        boolean status = true;
        char[] caratteri = value.toCharArray();

        for (char car : caratteri) {
            if (isNotNumber(car)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        return status;
    }// end of method


    private boolean isNotNumber(char ch) {
        return !isNumber(ch);
    }// end of method


    private static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }// end of method


    public String getModifiche(Object oldValue, Object newValue) {
        return getModifiche(oldValue, newValue, EAPrefType.string);
    } // fine del metodo


    public String getModifiche(Object oldValue, Object newValue, EAPrefType type) {
        String tatNew = "Aggiunto: ";
        String tatEdit = "Modificato: ";
        String tatDel = "Cancellato: ";
        String tagSep = " -> ";

        if (oldValue == null && newValue == null) {
            return "";
        }// end of if cycle

        if (oldValue instanceof String && newValue instanceof String) {
            if (this.isEmpty((String) oldValue) && isEmpty((String) newValue)) {
                return "";
            }// end of if cycle

            if (isValid((String) oldValue) && isEmpty((String) newValue)) {
                return tatDel + oldValue.toString();
            }// end of if cycle

            if (isEmpty((String) oldValue) && isValid((String) newValue)) {
                return tatNew + newValue.toString();
            }// end of if cycle

            if (!oldValue.equals(newValue)) {
                return tatEdit + oldValue.toString() + tagSep + newValue.toString();
            }// end of if cycle
        } else {
            if (oldValue != null && newValue != null && oldValue.getClass() == newValue.getClass()) {
                if (!oldValue.equals(newValue)) {
                    if (oldValue instanceof byte[]) {
                        try { // prova ad eseguire il codice
                            return tatEdit + type.bytesToObject((byte[]) oldValue) + tagSep + type.bytesToObject((byte[]) newValue);
                        } catch (Exception unErrore) { // intercetta l'errore
                            log.error(unErrore.toString() + " LibText.getDescrizione() - Sembra che il PrefType non sia del tipo corretto");
                            return "";
                        }// fine del blocco try-catch
                    } else {
                        return tatEdit + oldValue.toString() + tagSep + newValue.toString();
                    }// end of if/else cycle
                }// end of if cycle
            } else {
                if (oldValue != null && newValue == null) {
                    if (oldValue instanceof byte[]) {
                        return "";
                    } else {
                        return tatDel + oldValue.toString();
                    }// end of if/else cycle
                }// end of if cycle

                if (newValue != null && oldValue == null) {
                    if (newValue instanceof byte[]) {
                        return "";
                    } else {
                        return tatNew + newValue.toString();
                    }// end of if/else cycle
                }// end of if cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return "";
    }// end of method


}// end of class
