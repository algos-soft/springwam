package it.algos.springvaadin.field;

import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.dialog.ImageDialog;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 29-ago-2017
 * Time: 08:11
 * Annotated with @SpringComponent (obbligatorio)
 * Annotated with 'prototype', in modo da poterne utilizzare più di uno
 * Annotated with @Qualifier, per individuare la classe specifica da iniettare in AFieldFactory
 */
@SpringComponent
@Scope("prototype")
@Qualifier(Cost.FIELD_IMAGE)
public class AImageField extends AField {

    private HorizontalLayout placeholderImage = new HorizontalLayout();

    @Autowired
    private ImageDialog targetAutowired;

    private byte[] imgByte;


    public void setWidth(String width) {
        if (placeholderImage != null) {
            placeholderImage.setWidth(width);
            placeholderImage.setHeight("4em");
        }// end of if cycle
    }// end of method

    /**
     * Regolazioni varie DOPO aver creato l'istanza
     * L'istanza può essere creata da Spring o con clone(), ma necessita comunque di questi due parametri
     */
    @Override
    public void inizializza(String publicFieldName, ApplicationListener source) {
        super.inizializza(publicFieldName, source);
        if (button != null) {
            button.setTarget(targetAutowired);
        }// end of if cycle
    }// end of method


    @Override
    public Component initContent() {
        if (placeholderImage != null && button != null) {
            return new HorizontalLayout(button, placeholderImage);
        } else {
            return null;
        }// end of if/else cycle
    }// end of method


    /**
     * Recupera dalla UI il valore (eventualmente) selezionato
     * Alcuni fields (ad esempio quelli non enabled, ed altri) non modificano il valore
     * Elabora le (eventuali) modifiche effettuate dalla UI e restituisce un valore del typo previsto per il DB mongo
     */
    @Override
    public byte[] getValue() {
        return imgByte;
    }// end of method


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        Image image = null;
        placeholderImage.removeAllComponents();

        if (value != null && value instanceof byte[]) {
            imgByte = (byte[]) value;
        }// end of if cycle

        if (imgByte != null && imgByte.length > 0) {
            image = LibResource.getImage(imgByte);
        }// end of if cycle

        if (image != null) {
            image.setWidth("8em");
            image.setHeight("4em");
            placeholderImage.addComponent(image);
        }// end of if cycle
    }// end of method

    public void setButton(AButton button) {
        this.button = button;
        this.button.setTarget(target);
    }// end of method

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }// end of method

    public byte[] getImgByte() {
        return imgByte;
    }// end of method

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }// end of method

}// end of class

