package it.algos.springvaadin.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:46
 * <p>
 * Classe di Libreria
 * Gestione e formattazione utile per Html
 */
@SpringComponent
@Scope("singleton")
public class AHtmlService {

    @Autowired
    public ATextService text;

    public String ref = "<ref";
    public String note = "<!--";
    public String graffe = "{{";
    public String vuota = "";
    public String punto = ".";
    public String virgola = ",";
    public String parentesi = "(";
    public String interrogativo = "?";


    /**
     * Carattere rosso e bold
     *
     * @param testoIn ingresso da 'incapsulare' nei tags
     *
     * @return testo composto; null se testoIn è vuoto/null
     */
    public String setRossoBold(final String testoIn) {
        String testoOut = "";

        if (text.isValid(testoIn)) {
            testoOut = "<strong style=\"color: red;\">" + testoIn + "</strong>";
        }// end of if cycle

        return testoOut;
    }// end of method


    /**
     * Carattere verde e bold
     *
     * @param testoIn ingresso da 'incapsulare' nei tags
     *
     * @return testo composto; null se testoIn è vuoto/null
     */
    public String setVerdeBold(final String testoIn) {
        String testoOut = "";

        if (text.isValid(testoIn)) {
            testoOut = "<strong style=\"color: green;\">" + testoIn + "</strong>";
        }// end of if cycle

        return testoOut;
    }// end of method

    /**
     * Carattere blu e bold
     *
     * @param testoIn ingresso da 'incapsulare' nei tags
     *
     * @return testo composto; null se testoIn è vuoto/null
     */
    public String setBluBold(final String testoIn) {
        String testoOut = "";

        if (text.isValid(testoIn)) {
            testoOut = "<strong style=\"color: blue;\">" + testoIn + "</strong>";
        }// end of if cycle

        return testoOut;
    }// end of method

    /**
     * Carattere  bold
     *
     * @param testoIn ingresso da 'incapsulare' nei tags
     *
     * @return testo composto; null se testoIn è vuoto/null
     */
    public String setBold(final String testoIn) {
        String testoOut = "";

        if (text.isValid(testoIn)) {
            testoOut = "<strong>" + testoIn + "</strong>";
        }// end of if cycle

        return testoOut;
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag indicato (se esiste).
     */
    private String levaDopo(final String testoIn, String tag, boolean escluso) {
        String testoOut = testoIn;
        int pos;
        int delta = escluso ? tag.length() : 0;

        if (text.isValid(testoIn) && text.isValid(tag)) {
            testoOut = testoIn.trim();
            if (testoOut.contains(tag)) {
                pos = testoOut.indexOf(tag) + delta;
                testoOut = testoOut.substring(0, pos);
                testoOut = testoOut.trim();
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag indicato (se esiste).
     * Elimina a partire dalla fine del tag
     */
    private String levaDopoTagEscluso(final String testoIn, String tag) {
        return levaDopo(testoIn, tag, true);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag indicato (se esiste).
     * Elimina a partire dall'inizio del tag
     */
    private String levaDopoTagCompreso(final String testoIn, String tag) {
        return levaDopo(testoIn, tag, false);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag indicato (se esiste).
     * <p>
     * Elimina a partire dalla fine del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     * @param tag     finale dopo il quale eliminare
     *
     * @return testo troncato
     */
    public String levaDopo(final String testoIn, String tag) {
        return levaDopoTagEscluso(testoIn, tag);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag <ref> (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoRef(final String testoIn) {
        return levaDopoTagCompreso(testoIn, ref);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag <!-- (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoNote(final String testoIn) {
        return levaDopoTagCompreso(testoIn, note);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag {{ (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoGraffe(final String testoIn) {
        return levaDopoTagCompreso(testoIn, graffe);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag -virgola- (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoVirgola(final String testoIn) {
        return levaDopoTagCompreso(testoIn, virgola);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag -aperta parentesi- (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoParentesi(final String testoIn) {
        return levaDopoTagCompreso(testoIn, parentesi);
    }// end of method


    /**
     * Elimina la parte di testo successiva al tag -punto interrogativo- (se esiste).
     * <p>
     * Elimina a partire dall'inizio del tag
     * Esegue solo se il testo è valido
     * Se manca il tag, restituisce il testo
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso
     *
     * @return testo troncato
     */
    public String levaDopoInterrogativo(final String testoIn) {
        return levaDopoTagCompreso(testoIn, interrogativo);
    }// end of method


    /**
     * Elimina dal testo la stringa indicata.
     * <p>
     * Esegue solo se il testo è valido
     * Se la stringa non è valida, restituisce il testo
     *
     * @param testoIn    in ingresso
     * @param subStringa da eliminare
     *
     * @return testo ridotto
     */
    public String levaTesto(final String testoIn, String subStringa) {
        String testoOut = testoIn;

        if (text.isValid(testoIn) && text.isValid(subStringa)) {
            testoOut = testoIn.trim();
            if (testoOut.contains(subStringa)) {
                testoOut = text.sostituisce(testoOut, subStringa, vuota);
            }// fine del blocco if
        }// fine del blocco if

        return testoOut;
    }// end of  method

    /**
     * Elimina dal testo tutti i punti esistenti (eventuali).
     * <p>
     * Esegue solo se il testo è valido
     *
     * @param testoIn in ingresso
     *
     * @return testo ridotto
     */
    public String levaPunti(final String testoIn) {
        return levaTesto(testoIn, punto);
    }// end of method

    /**
     * Elimina dal testo tutte le virgole esistenti (eventuali).
     * <p>
     * Esegue solo se il testo è valido
     *
     * @param testoIn in ingresso
     *
     * @return testo ridotto
     */
    public String levaVirgole(final String testoIn) {
        return levaTesto(testoIn, virgola);
    }// end of method

}// end of class
