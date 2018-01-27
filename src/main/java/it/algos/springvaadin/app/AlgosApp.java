package it.algos.springvaadin.app;


import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Repository di costanti della applicazione
 * <p>
 * Alcune costanti sono 'final static', uguali per tutte le applicazioni e non modificabili
 * <p>
 * Altre costanti, pur esendo utilizzate da tutti i progetti, sono solo 'static' e quindi modificabili;
 * qui viene impostato solo il valore 'standard' iniziale
 * i progetti specifici possono modificare, normalmente nella classe xxxBootStrap,
 * queste costanti per l'utilizzo successivo
 * <p>
 * I singoli progetti hanno/possono avere una classe specifica xxxApp
 * (tecnicamente non è una sottoclasse perché sono classi astratte di metodi statici)
 * per gestire delle costanti specifiche del progetto stesso che non avrebbe senso generalizzare
 * <p>
 * Altre costanti 'final static' sono raggruppate nella classe it.algos.webbase.web.lib.Cost
 */
public abstract class AlgosApp {

    /**
     * Costante globale dell'applicazione. Non modificabile.
     * Name of the folder for temporary uploaded files<br>
     * The folder is located in the context folder of the container
     */
    public final static String UPLOAD_FOLDER_NAME = "uploads";

    /**
     * Costante globale dell'applicazione. Non modificabile.
     * Name of the folder for the files to download<br>
     * The folder is located in the context folder of the container
     */
    public final static String DOWNLOAD_FOLDER_NAME = "downloads";

    /**
     * Costante globale dell'applicazione. Non modificabile.
     * Name of the base folder for demo data.<br>
     * Demo data is loaded at bootstrap to populate empty tables.<br>
     */
    public final static String DEMODATA_FOLDER_NAME = "demo/";


    /**
     * Costante globale dell'applicazione. Non modificabile.
     * Usando SpringBoot, la cartella deve (DEVE) essere contenuta nella directory 'webapp'
     * Name of the base folder for images.<br>
     */
    public final static String TEXT_FOLDER_NAME = "./src/main/resources/txt/";


    /**
     * Costante globale dell'applicazione. Non modificabile.
     * Usando SpringBoot, la cartella deve (DEVE) essere contenuta nella directory 'webapp'
     * Name of the base folder for images.<br>
     */
    public final static String IMG_FOLDER_NAME = "./src/main/webapp/VAADIN/img/";

//    /**
//     * Costante globale dell'applicazione. Non modificabile.
//     * Usando SpringBoot, la cartella deve (DEVE) essere contenuta nella directory 'webapp'
//     * Name of the base folder for images.<br>
//     */
//    public final static String RESOURCES_FOLDER_NAME = "./src/main/resources/static/";


//    /**
//     * Costante globale dell'applicazione. Non modificabile.
//     * Usando SpringBoot, la cartella deve (DEVE) essere contenuta nella directory 'webapp'
//     * Name of the base folder for images.<br>
//     */
////    public final static String IMG_FOLDER_NAME = "./src/main/webapp/VAADIN/img/";
//    public final static String IMG_FOLDER_NAME = "./src/main/resources/static/img/";


    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Will be overwritten at the end of BootStrap
     */
    public static boolean SETUP_TIME;


    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_DEBUG;

    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Use company.<br>
     * Not final<br>
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_MULTI_COMPANY;


    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Use security access.<br>
     * Not final flag<br>
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_SECURITY;


    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Flag per mostrare il Login obbligatorio alla partenza.
     * Ha senso solo se il flag usaSecurity è attivato
     * Se è true, parte inizialmente col Login (a tutto schermo?)
     * Se è false, permette di visualizzare uno screenShot o un menu iniziale
     * o comunque una parte dell'applicazione che non necessita di essere protetta
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_LOGIN_OBBLIGATORIO;


    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Flag controllo parametri nella web request.
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_CHECK_PARAMS;

    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Flag controllo cookies.
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_CHECK_COOKIES;

    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Use versione entity.<br>
     * Not final flag<br>
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_VERS;

    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Use preferences entity.<br>
     * Not final flag<br>
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_LOG;

    /**
     * Costante globale dell'applicazione. Business logic. Modificabile.
     * Use logo entity.<br>
     * Not final flag<br>
     * Fixed in AlgosSpringBoot.afterPropertiesSet()
     * Can be overwritten on local xxxSpringBoot.afterPropertiesSet() method
     */
    public static boolean USE_PREF;


    /**
     * Servlet context<br>
     * registered as soon as possible to make it available to every component
     */
    private static ServletContext servletContext;


    /**
     * Returns the path to the Uploads folder.
     *
     * @return the path to the Uploads folder
     */
    public static Path getUploadPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.UPLOAD_FOLDER_NAME));
    }// end of static method

    public static Path getDownloadPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.DOWNLOAD_FOLDER_NAME));
    }// end of static method

    public static Path getDemoDataFolderPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/" + AlgosApp.DEMODATA_FOLDER_NAME));
    }// end of static method

    public static String getStrProjectPath() {
        ServletContext sc = AlgosApp.getServletContext();
        return Paths.get(sc.getRealPath("/")).toString();
    }// end of static method

    public static ServletContext getServletContext() {
        return servletContext;
    }// end of static method

    public static void setServletContext(ServletContext servletContext) {
        AlgosApp.servletContext = servletContext;
    }// end of static method

}// end of class