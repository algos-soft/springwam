package it.algos.springvaadin.renderer;

import com.vaadin.ui.renderers.AbstractRenderer;
import lombok.extern.slf4j.Slf4j;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 09-nov-2017
 * Time: 13:22
 */
@Slf4j
public class AlgosBooleanRenderer <T> extends AbstractRenderer<T, Boolean> {

        /**
         * simple boolean renderer that display true/false as icons
         */
        public AlgosBooleanRenderer() {
            super(Boolean.class);
        }

}// end of class
