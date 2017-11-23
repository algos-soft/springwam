package it.algos.springvaadin.lib;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class LibFile {

    /**
     * Legge il contenuto di un file
     *
     * @param relativePath del file rispetto al project
     *
     * @return testo diviso per righe
     */
    public static List<String> read(String relativePath) {
        String tag = "./";
        return readAbsolute(tag + relativePath);
    }// end of static method


    /**
     * Legge il contenuto di un file
     *
     * @param nomeFile di testo
     *
     * @return testo diviso per righe
     */
    public static List<String> readResources(String nomeFile) {
        String tag = "./";
        String prefix = "src/main/resources/static/";
        String suffix = ".txt";

        return readAbsolute(tag + prefix + nomeFile + suffix);
    }// end of static method


    /**
     * Legge il contenuto di un file
     *
     * @param absolutePath del file nel computer
     *
     * @return testo diviso per righe
     */
    public static List<String> readAbsolute(String absolutePath) {
        List<String> righe = null;
        File file = new File(absolutePath);
        Path path = Paths.get(absolutePath);

        try { // prova ad eseguire il codice
            righe = Files.readAllLines(path);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return righe;
    }// end of static method


}// end of abstract static class
