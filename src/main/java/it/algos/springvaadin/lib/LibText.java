package it.algos.springvaadin.lib;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import it.algos.springvaadin.entity.preferenza.PrefType;
import lombok.extern.slf4j.Slf4j;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class LibText {

    public static final String REF = "<ref";
    public static final String NOTE = "<!--";
    public static final String GRAFFE = "{{";
    public static final String VUOTA = "";
    public static final int INT_NULLO = -1;
    public static final String PUNTO = ".";
    public static final String VIRGOLA = ",";
    public static final String PARENTESI = "(";
    public static final String INTERROGATIVO = "?";

    /**
     * tag per la singola parentesi di apertura
     */
    public static final String PARENTESI_INI = "(";

    /**
     * tag per la singola parentesi di chiusura
     */
    public static final String PARENTESI_END = ")";

    /**
     * Converts multiple spaces to single spaces.
     *
     * @param string in entrata
     *
     * @return string in uscita
     */
    public static String zapMultiSpaces(String string) {
        return string.replaceAll("\\s+", " ");
    }// end of static method

    /**
     * Removes heading and trailing spaces and converts multiple spaces to single spaces.
     *
     * @param string in entrata
     *
     * @return string in uscita
     */
    public static String fixSpaces(String string) {
        string = string.trim();
        string = zapMultiSpaces(string);
        return string;
    }// end of static method


    /**
     * Forza il primo carattere della stringa secondo il flag
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     *
     * @param entrata stringa in ingresso
     * @param flag    maiuscolo o minuscolo
     *
     * @return uscita string in uscita
     */
    private static String primoCarattere(String entrata, PrimoCarattere flag) {
        String uscita = entrata;
        String primo;
        String rimanente;

        if (entrata != null && !entrata.equals("")) {
            primo = entrata.substring(0, 1);
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
            rimanente = entrata.substring(1);
            uscita = primo + rimanente;
        }// fine del blocco if

        return uscita;
    }// end of static method

    /**
     * Forza il primo carattere della stringa a maiuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita string in uscita
     */
    public static String primaMaiuscola(String entrata) {
        return primoCarattere(entrata, PrimoCarattere.maiuscolo);
    }// end of static method

    /**
     * Forza il primo carattere della stringa a minuscolo
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita string in uscita
     */
    public static String primaMinuscola(String entrata) {
        return primoCarattere(entrata, PrimoCarattere.minuscolo);
    }// end of static method


    /**
     * Elimina la testa iniziale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la testa, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param testa   da eliminare
     *
     * @return uscita stringa convertita
     */
    public static String levaTesta(String entrata, String testa) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (testa != null) {
                testa = testa.trim();
                if (uscita.startsWith(testa)) {
                    uscita = uscita.substring(testa.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    }// end of static method

    /**
     * Elimina la coda terminale della stringa, se esiste. <br>
     * <p>
     * Esegue solo se la stringa è valida. <br>
     * Se manca la coda, restituisce la stringa. <br>
     * Elimina spazi vuoti iniziali e finali. <br>
     *
     * @param entrata stringa in ingresso
     * @param coda    da eliminare
     *
     * @return uscita stringa convertita
     */
    public static String levaCoda(String entrata, String coda) {
        String uscita = entrata;

        if (entrata != null) {
            uscita = entrata.trim();
            if (coda != null) {
                coda = coda.trim();
                if (uscita.endsWith(coda)) {
                    uscita = uscita.substring(0, uscita.length() - coda.length());
                    uscita = uscita.trim();
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    }// end of static method


    /**
     * Elimina la parte di stringa successiva al tag, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata   stringa in ingresso
     * @param tagFinale dopo il quale eliminare
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopo(String entrata, String tagFinale) {
        String uscita = entrata;
        int pos;

        if (uscita != null && tagFinale != null) {
            uscita = entrata.trim();
            if (uscita.contains(tagFinale)) {
                pos = uscita.indexOf(tagFinale);
                uscita = uscita.substring(0, pos);
                uscita = uscita.trim();
            }// fine del blocco if
        }// fine del blocco if

        return uscita;
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag <ref>, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoRef(String entrata) {
        return levaDopo(entrata, REF);
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag <!--, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoNote(String entrata) {
        return levaDopo(entrata, NOTE);
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag {{, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoGraffe(String entrata) {
        return levaDopo(entrata, GRAFFE);
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag -virgola-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoVirgola(String entrata) {
        return levaDopo(entrata, VIRGOLA);
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag -aperta parentesi-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoParentesi(String entrata) {
        return levaDopo(entrata, PARENTESI);
    }// end of static method

    /**
     * Elimina la parte di stringa successiva al tag -punto interrogativo-, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public static String levaDopoInterrogativo(String entrata) {
        return levaDopo(entrata, INTERROGATIVO);
    }// end of static method

    /**
     * Trova nel testo, la prima occorrenza di un tag compreso nella lista di tag
     *
     * @param testo    da controllare
     * @param listaTag tag da trovare (uno o più)
     *
     * @return posizione nel testo del primo tag trovato, zero se non ce ne sono
     */
    public static int trovaPrimo(String testo, String... listaTag) {
        int pos = 0;
        int max = testo.length();
        int singlePos;

        if (listaTag != null) {
            pos = max;
            for (String singleTag : listaTag) {
                singlePos = testo.indexOf(singleTag);
                if (singlePos > 0) {
                    pos = Math.min(pos, singlePos);
                }// fine del blocco if
            }// end of for cycle

            if (pos == max) {
                pos = 0;
            }// fine del blocco if
        }// fine del blocco if

        return pos;
    }// end of static method


    /**
     * Elimina tutti i punti contenuti nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa convertita
     */
    public static String levaPunti(String entrata) {
        return levaTesto(entrata, PUNTO);
    }// end of static method

    /**
     * Elimina tutte le virgole contenute nella stringa.
     * Esegue solo se la stringa è valida
     * Se arriva un oggetto non stringa, restituisce l'oggetto
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa convertita
     */
    public static String levaVirgole(String entrata) {
        return levaTesto(entrata, VIRGOLA);
    }// end of static method

    /**
     * Elimina tutti i caratteri contenuti nella stringa.
     * Esegue solo se il testo è valido
     *
     * @param testoIn    in ingresso
     * @param subStringa da eliminare
     *
     * @return testoOut stringa convertita
     */
    public static String levaTesto(String testoIn, String subStringa) {
        String testoOut = testoIn;

        if (testoIn != null && subStringa != null) {
            testoOut = testoIn.trim();
            if (testoOut.contains(subStringa)) {
                testoOut = sostituisce(testoOut, subStringa, VUOTA);
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of static method

    /**
     * Sostituisce tutte le occorrenze.
     * Esegue solo se il testo è valido
     *
     * @param testoIn    in ingresso
     * @param oldStringa da eliminare
     * @param newStringa da sostituire
     *
     * @return testoOut convertito
     */
    public static String sostituisce(String testoIn, String oldStringa, String newStringa) {
        String testoOut = testoIn;
        int pos = 0;
        String prima = VUOTA;

        if (testoIn != null && oldStringa != null && newStringa != null) {
            testoOut = testoIn.trim();
            if (testoIn.contains(oldStringa)) {

                while (pos != INT_NULLO) {
                    pos = testoIn.indexOf(oldStringa);
                    if (pos != INT_NULLO) {
                        prima += testoIn.substring(0, pos);
                        prima += newStringa;
                        pos += oldStringa.length();
                        testoIn = testoIn.substring(pos);
                    }// fine del blocco if
                }// fine di while

                testoOut = prima + testoIn;
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of static method


    /**
     * Sostituisce il testo nella posizione indicata.
     * Esegue solo se il testo è valido
     *
     * @param testoIn    in ingresso
     * @param posIni     inizio del testo da eliminare
     * @param posEnd     fine del testo da eliminare
     * @param newStringa da sostituire
     *
     * @return testoOut convertito
     */
    public static String sostituisce(String testoIn, int posIni, int posEnd, String newStringa) {
        String testoOut = testoIn;
        String prima;
        String dopo;
        int length = 0;

        if (testoIn != null && newStringa != null) {
            length = testoIn.length();
            if (posIni > 0 && posEnd > 0 && posEnd > posIni && posEnd < length) {

                prima = testoIn.substring(0, posIni);
                dopo = testoIn.substring(posEnd);
                testoOut = prima + newStringa + dopo;

            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of static method


    /**
     * Restituisce la posizione di un tag in un testo
     * Riceve una lista di tag da provare
     * Restituisce la prima posizione tra tutti i tag trovati
     *
     * @param testo in ingresso
     * @param lista di stringhe, oppure singola stringa
     *
     * @return posizione della prima stringa trovata
     * -1 se non ne ha trovato nessuna
     * -1 se il primo parametro è nullo o vuoto
     * -1 se il secondo parametro è nullo
     * -1 se il secondo parametro non è ne una lista di stringhe, ne una stringa
     */

    public static int getPos(String testo, ArrayList<String> lista) {
        int pos = testo.length();
        int posTmp;
        ArrayList<Integer> posizioni = new ArrayList<Integer>();

        if (!testo.equals("") && lista != null) {

            for (String stringa : lista) {
                posTmp = testo.indexOf(stringa);
                if (posTmp != INT_NULLO) {
                    posizioni.add(posTmp);
                }// fine del blocco if
            } // fine del ciclo for-each

            if (posizioni.size() > 0) {
                for (Integer num : posizioni) {
                    pos = Math.min(pos, num);
                } // fine del ciclo for-each
            } else {
                pos = 0;
            }// fine del blocco if-else
        }// fine del blocco if

        return pos;
    } // fine del metodo


    /**
     * Restituisce la posizione di un tag in un testo
     * Riceve una lista di tag da provare
     * Restituisce la prima posizione tra tutti i tag trovati
     *
     * @param testo in ingresso
     * @param lista di stringhe, oppure singola stringa
     *
     * @return posizione della prima stringa trovata
     * -1 se non ne ha trovato nessuna
     * -1 se il primo parametro è nullo o vuoto
     * -1 se il secondo parametro è nullo
     * -1 se il secondo parametro non è ne una lista di stringhe, ne una stringa
     */
    public static int getPos(String testo, String... lista) {
        return getPos(testo, (ArrayList) LibArray.fromString(lista));
    } // fine del metodo

//    /**
//     * Costruisce una codifica unica col codice della company seguito da uno o più campi chiave
//     */
//    public static String creaChiave(BaseCompany company, String... chiavi) {
//        String chiaveUnica = "";
//        String companyCode = "";
//
//        if (company != null) {
//            companyCode = company.getCompanyCode();
//            companyCode = companyCode.toLowerCase();
//            companyCode = LibText.primaMaiuscola(companyCode);
//        }// end of if cycle
//
//        chiaveUnica += companyCode;
//        for (String chiave : chiavi) {
//            chiave = chiave.replaceAll("\\s", "");
//            if (chiaveUnica.equals("")) {
//                chiaveUnica += chiave;
//            } else {
//                chiaveUnica += LibText.primaMaiuscola(chiave);
//            }// end of if/else cycle
//        }// end of for cycle
//
//        return chiaveUnica;
//    }// end of static method

    public static boolean isValida(String stringa) {
        boolean valida = false;

        if (stringa != null && stringa.length() > 0) {
            valida = true;
        }// end of if cycle

        return valida;
    }// end of method

    public static String getValida(String stringa) {
        String valida = null;

        if (stringa != null && stringa.length() > 0) {
            valida = stringa;
        }// end of if cycle

        return valida;
    }// end of static method

    public static boolean isEmpty(final String string) {
        // Null-safe, short-circuit evaluation.
        return string == null || string.trim().isEmpty();
    }// end of static method

    public static boolean isValid(final String string) {
        return !isEmpty(string);
    }// end of static method

    /**
     * Carattere rosso e bold
     */
    public static String setRossoBold(String testoIn) {
        return "<strong style=\"color: red;\">" + testoIn + "</strong>";
    }// end of inner enumeration


    /**
     * Carattere verde e bold
     */
    public static String setVerdeBold(String testoIn) {
        return "<strong style=\"color: green;\">" + testoIn + "</strong>";
    }// end of inner enumeration

    /**
     * Carattere blu e bold
     */
    public static String setBluBold(String testoIn) {
        return "<strong style=\"color: blue;\">" + testoIn + "</strong>";
    }// end of inner enumeration

    /**
     * Carattere  bold
     */
    public static String setBold(String testoIn) {
        if (LibText.isValid(testoIn)) {
            return "<strong>" + testoIn + "</strong>";
        } else {
            return "";
        }// end of if/else cycle
    }// end of inner enumeration


    /**
     * Ripete le due stringhe
     * uno per numVolte
     * due per numVolte + 1
     */
    public static String repeat(String uno, String due, int numVolte) {
        String stringa = "";


        for (int k = 0; k < numVolte - 1; k++) {
            stringa += uno;
            stringa += due;
        }// end of for cycle
        stringa += uno;

        return stringa;
    }// end of static method

    /**
     * Aggiunge parentesi all'inizio ed alla fine della stringa.
     * Aggiunge SOLO se gia non esiste
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con tag aggiunti
     */
    public static String setTonde(String stringaIn) {
        String stringaOut = stringaIn.trim();

        if (!stringaOut.startsWith(PARENTESI_INI)) {
            stringaOut = PARENTESI_INI + stringaOut;
        }// end of if cycle

        if (!stringaOut.endsWith(PARENTESI_END)) {
            stringaOut = stringaOut + PARENTESI_END;
        }// end of if cycle

        return stringaOut;
    } // fine del metodo

    /**
     * Controlla la validità di un indirizzo email
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        boolean status = matcher.matches();
        return matcher.matches();
    }// end of static method

    /**
     * Controlla la validità di un indirizzo email
     */
    public static boolean isWrongEmail(String email) {
        return !isValidEmail(email);
    }// end of static method


    public static boolean isNumber(String value) {
        boolean status = true;
        char[] caratteri = value.toCharArray();

        for (char car : caratteri) {
            if (isNotNumber(car)) {
                status = false;
            }// end of if cycle
        }// end of for cycle

        return status;
    }// end of method

    private static boolean isNotNumber(char ch) {
        return !isNumber(ch);
    }// end of method


    private static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }// end of method

    public static boolean isNumberNotNull(String value) {
        int intValue = 0;

        if (isNumber(value)) {
            intValue = Integer.decode(value);
        }// end of if cycle

        return intValue > 0;
    }// end of method


    public static boolean isLeadingDigit(final String value) {
        final char c = value.charAt(0);
        return (c >= '0' && c <= '9');
    } // fine del metodo

    public static String getDescrizione(Object oldValue, Object newValue) {
        return getDescrizione(oldValue, newValue, PrefType.string);
    } // fine del metodo


    public static String getDescrizione(Object oldValue, Object newValue, PrefType type) {
        String tatNew = "Aggiunto: ";
        String tatEdit = "Modificato: ";
        String tatDel = "Cancellato: ";
        String tagSep = " -> ";

        if (oldValue == null && newValue == null) {
            return "";
        }// end of if cycle

        if (oldValue instanceof String && newValue instanceof String) {
            if (isEmpty((String) oldValue) && isEmpty((String) newValue)) {
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

    /**
     * Enumeration locale per il flag del primo carattere
     */
    private enum PrimoCarattere {
        maiuscolo, minuscolo
    }// end of inner enumeration


}// end of static class
