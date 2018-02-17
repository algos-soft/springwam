package it.algos.springvaadin.event;

import org.springframework.context.ApplicationListener;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 12-dic-2017
 * Time: 10:10
 */
public interface IAListener extends ApplicationListener<AEvent> {


    /**
     * Handle an application event.
     *
     * @param event to respond to
     */
    @Override
    public void onApplicationEvent(AEvent event);


}// end of interface
