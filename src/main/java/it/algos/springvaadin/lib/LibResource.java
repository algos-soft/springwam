package it.algos.springvaadin.lib;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Image;
import it.algos.springvaadin.app.AlgosApp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe statica di libreria per il recupero delle risorse dal server
 */
public abstract class LibResource {

    /**
     * Restituisce una immagine dalla directory di default come byte array.
     * <p>
     *
     * @param name nome dell'immagine
     *
     * @return il corrispondente byte array.
     */
    public static byte[] getImgBytes(String name) {
        return getImgBytes(AlgosApp.IMG_FOLDER_NAME, name);
    }// end of static method

    /**
     * Restituisce una immagine dalla directory di default come resource.
     * <p>
     *
     * @param name nome dell'immagine
     *
     * @return la Resource corrispondente.
     */
    public static Resource getImgResource(String name) {
        return getImgResource(AlgosApp.IMG_FOLDER_NAME, name);
    }// end of static method

    /**
     * Restituisce una immagine come byte array.
     * <p>
     *
     * @param localPath percorso relativo fino alla directory dove si trova l'immagine
     * @param name      nome dell'immagine
     *
     * @return il corrispondente byte array.
     */
    public static byte[] getImgBytes(String localPath, String name) {
        byte[] bytes = new byte[0];
        String realPath = "";
        String fullPath;
        File filePath;

        try { // prova ad eseguire il codice
            realPath = LibVaadin.getPath();
            fullPath = realPath + "/" + localPath + "/" + name;
            filePath = new File(fullPath);
            if (filePath.exists() && !filePath.isDirectory()) {
                bytes = Files.readAllBytes(Paths.get(fullPath));
            }// end of if cycle

        } catch (Exception unErrore) { // intercetta l'errore
            unErrore.printStackTrace();
        }// fine del blocco try-catch

        return bytes;
    }// end of static method


    /**
     * Restituisce una immagine come resource.
     * <p>
     *
     * @param path percorso relativo fino alla directory dove si trova l'immagine
     * @param name nome dell'immagine
     *
     * @return la Resource corrispondente.
     */
    public static Resource getImgResource(String path, String name) {
        Resource res = null;
        byte[] bytes = getImgBytes(path, name);
        if (bytes != null) {
            if (bytes.length > 0) {
                res = getStreamResource(bytes);
            }// end of if cycle
        }// end of if cycle

        return res;
    }// end of static method


    /**
     * Create a StreamResource from a byte array
     * <p>
     *
     * @param bytes the byte array
     *
     * @return the StreamResource
     */
    @SuppressWarnings("serial")
    public static StreamResource getStreamResource(final byte[] bytes) {
        StreamResource resource = null;

        // Create a stream source returning the data from the byte array
        StreamResource.StreamSource streamSource = new ByteStreamResource(bytes);

        // create a StreamResource as data source for the image
        // the name of the stream must be changed or the browser cache will not update the image
        String resName = "" + System.currentTimeMillis();
        resource = new StreamResource(streamSource, resName);

        return resource;
    }// end of static method


    /**
     * Create an Image from a resource
     * <p>
     *
     * @param resource the resource
     *
     * @return the image
     */
    public static Image getImage(final Resource resource) {
        Image image = new Image(null, resource);
        return image;
    }// end of static method

    /**
     * Create an Image from a byte array
     * <p>
     *
     * @param bytes the byte array
     *
     * @return the image
     */
    public static Image getImage(final byte[] bytes) {
        Image image = new Image();
        StreamResource resource = LibResource.getStreamResource(bytes);
        image.setSource(resource);
        return image;
    }// end of static method


    /**
     * Create an Image from a name
     * <p>
     *
     * @param name nome dell'immagine
     *
     * @return the image
     */
    public static Image getImage(String name) {
        Image image = null;
        Resource res = LibResource.getImgResource(name);

        if (res != null) {
            image = new Image();
            image.setSource(res);
        }// end of if cycle

        return image;
    }// end of static method

    public static VaadinIcons getVaadinIcon(int codePoint) {
        VaadinIcons icona = null;
        VaadinIcons[] icons = VaadinIcons.values();

        for (VaadinIcons icon : icons) {
            if (icon.getCodepoint() == codePoint) {
                icona = icon;
                break;
            }// end of if cycle
        }// end of for cycle

        return icona;
    }// end of static method

}// end of abstract class
