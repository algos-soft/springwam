package it.algos.springvaadin.enumeration;

/**
 * Created by gac on 03/06/17.
 * <p>
 * Azioni generate dalla Grid
 * Azioni generate dai Fields di un Form
 * <p>
 * Enumeration utilizzata per 'marcare' una azione, in fase di generazione
 * Enumeration utilizzata per 'riconoscerla' nel metodo onApplicationEvent()
 */
public enum EATypeAction {

    attach,
    click,
    doppioClick,
    singleSelectionChanged,
    multiSelectionChanged,
    valueChange,
    daemonMail,
    listener;

}// end of enumeration
