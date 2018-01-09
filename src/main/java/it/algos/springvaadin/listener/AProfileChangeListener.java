package it.algos.springvaadin.listener;

import it.algos.springvaadin.event.AProfileChangeEvent;

/**
 * Created by alex on 04/08/16.
 */
public interface AProfileChangeListener {
    void profileChanged(AProfileChangeEvent event);
}// end of interface
