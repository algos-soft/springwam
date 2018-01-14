package it.algos.springvaadin.service;

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
     * Legge il contenuto di un file di testo
     *
     * @param nomeFile di testo
     *
     * @return testo diviso per righe
     */
    public List<String> readText(String nomeFile) {
        List<String> righe = null;
        String suffix = ".txt";
        String pathTxt = AlgosApp.RESOURCES_FOLDER_NAME + nomeFile + suffix;
        Path filePath = Paths.get(pathTxt);

        try { // prova ad eseguire il codice
            righe = Files.readAllLines(filePath);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return righe;
    }// end of method


    /**
     * Legge una risorsa come byte array.
     *
     * @param nomeRisorsaConSuffisso
     *
     * @return il corrispondente byte array.
     */
    public byte[] getImgBytes(String nomeRisorsaConSuffisso) {
        byte[] bytes = new byte[0];
        String pathTxt = AlgosApp.IMG_FOLDER_NAME + nomeRisorsaConSuffisso;

        try { // prova ad eseguire il codice
            bytes = Files.readAllBytes(Paths.get(pathTxt));
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return bytes;
    }// end of method


    /**
     * Create an Image from a file
     * <p>
     *
     * @param nomeRisorsaConSuffisso
     *
     * @return the image
     */
    public Image getImage(String nomeRisorsaConSuffisso) {
        return getImage(this.getImgBytes(nomeRisorsaConSuffisso));
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
        Image image = new Image();
        StreamResource resource = this.getStream(bytes);
        image.setSource(resource);
        return image;
    }// end of method


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
    }// end of method


    /**
     * Legge il contenuto di un file di testo
     *
     * @param filename di testo
     *
     * @return testo diviso per righe
     */
    @Deprecated
    public List<String> readAllLinesDeprecated(String filename) {
        byte[] bytes = getTextBytesDeprecated(filename);
        String string = new String(bytes);
        String[] rows = string.split("\n");
        List<String> lines = Arrays.asList(rows);
        return lines;
    }


    /**
     * Read a resource as a byte array.
     *
     * @param resPath the resource path inside the resources folder
     * @param resName the resource name
     *
     * @return the corresponding byte array.
     */
    @Deprecated
    private byte[] getResourceBytesDeprecated(String resPath, String resName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resPath + "/" + resName);
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }// end of method


//    /**
//     * Read an image as a byte array.
//     * <p>
//     *
//     * @param resName the image name
//     *
//     * @return the byte array.
//     */
//    @Deprecated
//    public byte[] getImageBytesDeprecated(String resName) {
//        return getTextBytesDeprecated("img", resName);
//    }
//
//
//    /**
//     * Read an image as Image.
//     * <p>
//     *
//     * @param resName the image name
//     *
//     * @return the Image.
//     */
//    @Deprecated
//    public Image getImageDeprecated(String resName) {
//        byte[] bytes = getTextBytesDeprecated("img", resName);
//        return getImageDeprecated(bytes);
//    }


    /**
     * Create an Image from a resource
     * <p>
     *
     * @param resource the resource
     *
     * @return the image
     */
    @Deprecated
    public Image getImageDeprecated(final Resource resource) {
        Image image = new Image(null, resource);
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
    @Deprecated
    public Image getImageDeprecated(final byte[] bytes) {
        Image image = new Image();
        StreamResource resource = this.getStream(bytes);
        image.setSource(resource);
        return image;
    }// end of method


    /**
     * Read a text file as a byte array.
     * <p>
     *
     * @param resName the file name
     *
     * @return the byte array.
     */
    @Deprecated
    public byte[] getTextBytesDeprecated(String resName) {
        return getResourceBytesDeprecated("txt", resName);
    }


    /**
     * Create a StreamResource from a byte array
     *
     * @param bytes the byte array
     *
     * @return the StreamResource
     */
    @SuppressWarnings("serial")
    @Deprecated
    public StreamResource getStreamDeprecated(final byte[] bytes) {
        StreamResource resource = null;

        // Create a stream source returning the data from the byte array
        StreamResource.StreamSource streamSource = new ByteStreamResource(bytes);

        // create a StreamResource as data source for the image
        // the name of the stream must be changed or the browser cache will not update the image
        String resName = "" + System.currentTimeMillis();
        resource = new StreamResource(streamSource, resName);

        return resource;
    }

}// end of singleton class
