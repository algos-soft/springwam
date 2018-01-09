package it.algos.springvaadin.button;

import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.enumeration.EAButtonType;
import it.algos.springvaadin.event.IAListener;
import it.algos.springvaadin.field.AField;
import org.springframework.context.ApplicationListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 08-set-2017
 * Time: 15:50
 * <p>
 * Factory interface per la nuovo dei bottoni
 * Crea ogni bottone del tpo richiesto e previsto nella Enumeration EAButtonType
 * Nella nuovo viene iniettato il parametro obbligatorio della 'sorgente' dell'evento generato dal bottone
 * Eventuali altri parametri facoltativi (target, entityBean), possono essere aggiunti. Da altre classi.
 */
public interface IAButtonFactory {

    /**
     * Creazione di un bottone
     *
     * @param type        del bottone, secondo la Enumeration EAButtonType
     * @param source      dell'evento generato dal bottone
     * @param target      a cui indirizzare l'evento generato dal bottone
     * @param sourceField che contiene il bottone
     *
     * @return il bottone creato
     */
    public AButton crea(EAButtonType type, IAListener source, IAListener target, AField sourceField);

    /**
     * Creazione di un bottone
     *
     * @param type        del bottone, secondo la Enumeration EAButtonType
     * @param source      dell'evento generato dal bottone
     * @param target      a cui indirizzare l'evento generato dal bottone
     * @param sourceField che contiene il bottone
     *
     * @return il bottone creato
     */
    public AButton crea(EAButtonType type, IAListener source, IAListener target, AField sourceField, AEntity entityBean);

}// end of interface
