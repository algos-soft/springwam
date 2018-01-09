package it.algos.springvaadin.listener;

import it.algos.springvaadin.event.ALogoutEvent;

/**
 * Listener invoked on logout.
 */
public interface ALogoutListener {
    void onUserLogout(ALogoutEvent event);
}// end of interface
