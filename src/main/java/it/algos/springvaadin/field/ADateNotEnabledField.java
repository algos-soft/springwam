package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import it.algos.springvaadin.lib.Cost;
import it.algos.springvaadin.lib.LibNum;
import it.algos.springvaadin.lib.MeseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: mar, 12-set-2017
 * Time: 06:47
 */
@Slf4j
@SpringComponent
public class ADateNotEnabledField extends AField {


    /**
     * Visualizza graficamente nella UI i componenti grafici (uno o più)
     * Riceve il valore dal DB Mongo, già col casting al typo previsto
     */
    @Override
    public void doSetValue(Object value) {
        LocalDateTime localDateTime = null;
        String valueTxt = "";
        String sepW = ", ";
        String sepD = "-";
        String sepDT = "  ";
        String sepT = ":";

        if (value instanceof LocalDateTime) {
            localDateTime = (LocalDateTime) value;
        } else {
            return;
        }// end of if/else cycle
        valueTxt+=  localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN);
        valueTxt += sepW;
        valueTxt += localDateTime.getDayOfMonth();
        valueTxt += sepD;
        valueTxt += MeseEnum.getShort(localDateTime.getMonthValue());//nome breve (3 caratteri) in italiano
        valueTxt += sepD;
        valueTxt += localDateTime.getYear();//le ultime due cifre
        valueTxt += sepDT;
        valueTxt += LibNum.addZeri(localDateTime.getHour());//aggiunge eventuale zero iniziale per avere sempre 2 cifre
        valueTxt += sepT;
        valueTxt += LibNum.addZeri(localDateTime.getMinute());//aggiunge eventuale zero iniziale per avere sempre 2 cifre
        valueTxt += sepT;
        valueTxt += LibNum.addZeri(localDateTime.getSecond());//aggiunge eventuale zero iniziale per avere sempre 2 cifre


        textField.setValue(valueTxt);
    }// end of method

}// end of class

