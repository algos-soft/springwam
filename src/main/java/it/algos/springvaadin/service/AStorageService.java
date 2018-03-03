package it.algos.springvaadin.service;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.login.IAUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.servlet.http.Cookie;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 15-feb-2018
 * Time: 15:47
 * Classe statica per la gestione (moderna) dei cookies
 */
@SpringComponent
@Scope("singleton")
@Slf4j
public class AStorageService {


    @Autowired
    protected ALogin login;


//    /**
//     * Legge i cookies dal browser (user, password, company)
//     * Regola il Login della sessione
//     */
//    public void checkCookies() {
//        IAUser user;
//        String nickname = "";
//        String password = "";
//        String company = "";
//
//            for (Cookie cookie : requestCookies) {
//                if (cookie.getName().equals(ACost.COOKIE_NAME_LOGIN)) {
//                    nickname = cookie.getValue();
//                }// end of if cycle
//
//                if (cookie.getName().equals(ACost.COOKIE_NAME_PASSWORD)) {
//                    password = cookie.getValue();
//                }// end of if cycle
//
//                if (cookie.getName().equals(ACost.COOKIE_NAME_COMPANY)) {
//                    company = cookie.getValue();
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        checkUtente(nickname, password);
//    }// end of static method


}// end of class
