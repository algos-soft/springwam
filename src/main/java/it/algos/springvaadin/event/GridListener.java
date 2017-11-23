package it.algos.springvaadin.event;

import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.AEntity;

/**
 * Created by gac on 03/06/17.
 * Eventi possibili lanciati dalla Grid di una List
 */
@SpringComponent
public interface GridListener {

    public void attach();

    public void click();

    public void doppioClick(AEntity entityBean);

    public void selectionChanged();

    public void listener();

}// end of interface
