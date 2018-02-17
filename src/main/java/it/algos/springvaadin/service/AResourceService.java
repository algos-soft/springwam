package it.algos.springvaadin.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Image;
import it.algos.springvaadin.app.AlgosApp;
import it.algos.springvaadin.lib.ByteStreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 04-gen-2018
 * Time: 16:56
 */
@Slf4j
@SpringComponent
@Scope("singleton")
public class AResourceService {


    /**
     * Read an image as Image.
     *
     * @param resImageName the image name
     *
     * @return the Image.
     */
    public Image getImage(String resImageName) {
        Image image = null;
        byte[] bytes = getImageBytes(resImageName);

        if (bytes != null && bytes.length > 0) {
            image = getImage(bytes);
        }// end of if cycle

        return image;
    }// end of method


    /**
     * Create an Image from a byte array
     * <p>
     *
     * @param bytes the byte array
     *
     * @return the image
     */
    public Image getImage(final byte[] bytes) {
        Image image = null;
        StreamResource resource = this.getStream(bytes);

        if (resource != null) {
            image = new Image();
            image.setSource(resource);
        }// end of if cycle

        return image;
    }// end of method


    /**
     * Legge il contenuto di un file di testo
     *
     * @param fileName di testo
     *
     * @return testo diviso per righe
     */
    public List<String> readAllLines(String fileName) {
        List<String> lines = null;
        String stringa;
        String[] righe=null;
        byte[] bytes = getTextBytes(fileName);

        if (bytes != null && bytes.length > 0) {
            stringa = new String(bytes);
            righe = stringa.split("\n");
        }// end of if cycle

        if (righe != null && righe.length > 0) {
            lines = Arrays.asList(righe);
        }// end of if cycle

        return lines;
    }// end of method


    /**
     * Read a resource as a byte array.
     *
     * @param resLocalPath the resource local path inside the resources folder
     * @param resName      the resource name
     *
     * @return the corresponding byte array.
     */
    private byte[] getResourceBytes(String resLocalPath, String resName) {
        byte[] bytes = new byte[0];
        InputStream inputStream = null;
        ClassLoader classLoader = getClass().getClassLoader();

        if (classLoader != null) {
            inputStream = classLoader.getResourceAsStream(resLocalPath + "/" + resName);
        }// end of if cycle

        if (inputStream!=null) {
            try { // prova ad eseguire il codice
                bytes = IOUtils.toByteArray(inputStream);
            } catch (IOException unErrore) { // intercetta l'errore
                unErrore.printStackTrace();
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return bytes;
    }// end of method


    /**
     * Read an image as a byte array.
     *
     * @param resImageName the image file name
     *
     * @return the byte array.
     */
    public byte[] getImageBytes(String resImageName) {
        return getResourceBytes(AlgosApp.IMG_FOLDER_NAME, resImageName);
    }// end of method


    /**
     * Read a text file as a byte array.
     *
     * @param resTextName the text file name
     *
     * @return the byte array.
     */
    public byte[] getTextBytes(String resTextName) {
        return getResourceBytes(AlgosApp.TEXT_FOLDER_NAME, resTextName);
    }// end of method


    /**
     * Create an Image from a resource
     * <p>
     *
     * @param resource the resource
     *
     * @return the image
     */
    public Image getImage(final Resource resource) {
        Image image = new Image(null, resource);
        return image;
    }


    /**
     * Create a StreamResource from a byte array
     *
     * @param bytes the byte array
     *
     * @return the StreamResource
     */
    @SuppressWarnings("serial")
    public StreamResource getStream(final byte[] bytes) {
        StreamResource resource = null;

        // Create a stream source returning the data from the byte array
        StreamResource.StreamSource streamSource = new ByteStreamResource(bytes);

        // create a StreamResource as data source for the image
        // the name of the stream must be changed or the browser cache will not update the image
        String resName = "" + System.currentTimeMillis();
        resource = new StreamResource(streamSource, resName);

        return resource;
    }



    public VaadinIcons getVaadinIcon(int codePoint) {
        VaadinIcons icona = null;
        VaadinIcons[] icons = VaadinIcons.values();

        for (VaadinIcons icon : icons) {
            if (icon.getCodepoint() == codePoint) {
                icona = icon;
                break;
            }// end of if cycle
        }// end of for cycle

        return icona;
    }// end of method

}// end of service class
