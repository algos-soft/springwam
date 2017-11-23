package it.algos.springvaadin.renderer;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.ClientConnector;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.Json;
import elemental.json.JsonValue;
import it.algos.springvaadin.entity.preferenza.PrefType;
import it.algos.springvaadin.form.AlgosForm;
import it.algos.springvaadin.nav.AlgosNavView;
import it.algos.springvaadin.view.AlgosView;
import lombok.extern.slf4j.Slf4j;


/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 20-ott-2017
 * Time: 09:50
 * Vale SOLO per bytes che rappresentano una stringa
 */
@Slf4j
public class ByteStringRenderer extends TextRenderer {


    public JsonValue encode(Object value) {
//        Grid.Column<Object, Object> colonna = this.getParent();
//        ClientConnector connector = colonna.getParent();
//        UI ui = connector.getUI();
//        Navigator nav = ui.getNavigator();
//        AlgosNavView navView = (AlgosNavView) nav.getCurrentView();
//        AlgosView vista = navView.getLinkedView();
//        AlgosForm form = vista.getForm();

        if (value != null && value instanceof byte[]) {
            String stringa = (String) PrefType.string.bytesToObject((byte[]) value);
            return Json.create(stringa);
        } else {
            return super.encode((Object) null);
        }// end of if/else cycle
    }// end of method


}// end of class
