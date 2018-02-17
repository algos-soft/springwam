package it.algos.springvaadin.renderer;

import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.Json;
import elemental.json.JsonValue;
import it.algos.springvaadin.enumeration.EAPrefType;


/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: ven, 20-ott-2017
 * Time: 09:50
 * Vale SOLO per bytes che rappresentano una stringa
 */
public class ByteStringRenderer extends TextRenderer {


    public JsonValue encode(Object value) {
        if (value != null && value instanceof byte[]) {
            String stringa = (String) EAPrefType.string.bytesToObject((byte[]) value);
            return Json.create(stringa);
        } else {
            return super.encode((Object) null);
        }// end of if/else cycle
    }// end of method


}// end of class
