package it.algos.springvaadin.event;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.entity.AEntity;
import org.springframework.context.ApplicationListener;

/**
 * Created by gac on 04/06/17.
 * Eventi possibili lanciati dai bottoni della ToolBar un Form
 * Eventi possibili lanciati dai Fields di un Form
 */
@SpringComponent
public interface FormListener {

    public void annulla();

    public void back();

    public void revert();

    public void registra();

    public void accetta();

    public void valoreCambiato();

    public void fieldModificato(ApplicationListener source, AEntity entityBean,AField sourceField) ;

}// end of interface
