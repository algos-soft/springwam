package it.algos.springvaadin.field;

import com.vaadin.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
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
        LocalDate localDate = null;
        String valueTxt = "";
        String sepW = ",  ";
        String sepD = "-";
        String sepDT = "  ";
        String sepT = ":";

        if (value instanceof LocalDateTime) {
            localDateTime = (LocalDateTime) value;

            valueTxt += localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
            valueTxt += sepW;
            valueTxt += localDateTime.getDayOfMonth();
            valueTxt += sepD;
//        valueTxt += MeseEnum.getShort(localDateTime.getMonthValue());//nome breve (3 caratteri) in italiano
//        valueTxt += sepD;
//        valueTxt += localDateTime.getYear();//le ultime due cifre
//        valueTxt += sepDT;
//        valueTxt += LibNum.addZeri(localDateTime.getHour());//aggiunge eventuale zero iniziale per avere sempre 2 cifre
//        valueTxt += sepT;
//        valueTxt += LibNum.addZeri(localDateTime.getMinute());//aggiunge eventuale zero iniziale per avere sempre 2 cifre
//        valueTxt += sepT;
//        valueTxt += LibNum.addZeri(localDateTime.getSecond());//aggiunge eventuale zero iniziale per avere sempre 2 cifre

        } else {
            if (value instanceof LocalDate) {
                localDate = (LocalDate) value;

                valueTxt += localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
                valueTxt += sepW;
                valueTxt += localDate.getDayOfMonth();
                valueTxt += sepD;
                valueTxt += localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
                valueTxt += sepD;
                valueTxt += localDate.getYear();
            } else {
                return;
            }// end of if/else cycle
        }// end of if/else cycle


        textField.setValue(valueTxt);
        textField.setEnabled(false);
        textField.setWidth("15em");
    }// end of method

}// end of class

