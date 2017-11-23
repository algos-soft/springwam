package it.algos.springvaadin.lib;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.entity.company.Company;
import it.algos.springvaadin.login.Login;

import javax.servlet.ServletContext;

/**
 * Created by gac on 26/05/15.
 * .
 */
public abstract class LibSession {

    /**
     * Recupera dalla sessione l'attributo company
     */
    public static boolean isCompanyValida() {
        return getCompany() != null;
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo company
     */
    public static Company getCompany() {
        return (Company) getAttribute(Attribute.company);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo company
     */
    public static void setCompany(Company company) {
        setAttribute(Attribute.company, company);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo debug
     */
    public static boolean isDebug() {
        return isBool(Attribute.debug);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo debug
     */
    public static void setDebug(boolean status) {
        setBool(Attribute.debug, status);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo login
     */
    public static Login getLogin() {
        return (Login) getAttribute(Attribute.login);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo login
     */
    public static void setLogin(Login login) {
        setAttribute(Attribute.login, login);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo loggato
     */
    public static boolean isLoggato() {
        return isBool(Attribute.loggato);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo loggato
     */
    public static void setLoggato(boolean status) {
        setBool(Attribute.loggato, status);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo developer
     */
    public static boolean isDeveloper() {
        return isBool(Attribute.developer);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo developer
     */
    public static void setDeveloper(boolean status) {
        setBool(Attribute.developer, status);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo admin
     */
    public static boolean isAdmin() {
        return isBool(Attribute.admin) || isBool(Attribute.developer);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo admin
     */
    public static void setAdmin(boolean status) {
        setBool(Attribute.admin, status);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo servletContext
     */
    public static ServletContext getServletContext() {
        return (ServletContext) getAttribute(Attribute.servletContext);
    }// end of static method

    /**
     * Regola per la sessione corrente l'attributo servletContext
     */
    public static void setAdmin(ServletContext servletContext) {
        setAttribute(Attribute.servletContext, servletContext);
    }// end of static method


    /**
     * Recupera dalla request l'attributo company (se esiste)
     * Regola la variabile statica
     */
    public static String checkCompany(VaadinRequest request) {
        return LibSession.getStr(request, Attribute.company);
    }// end of static method

    /**
     * Recupera dalla request l'attributo developer (se esiste)
     * Regola la variabile statica
     */
    public static void checkDeveloper(VaadinRequest request) {
        LibSession.checkBool(request, Attribute.developer);
    }// end of static method

    /**
     * Recupera dalla request l'attributo debug (se esiste)
     * Regola la variabile statica
     */
    public static void checkDebug(VaadinRequest request) {
        LibSession.checkBool(request, Attribute.debug);
    }// end of static method


    /**
     * Recupera dalla sessione l'attributo specifico
     */
    public static boolean isBool(Attribute attributo) {
        boolean status = false;
        Object devObj = null;
        VaadinSession sessione = VaadinSession.getCurrent();

        if (attributo != null && sessione != null) {
            devObj = sessione.getAttribute(attributo.toString());
            if (devObj != null) {
                if (devObj instanceof Boolean) {
                    status = (Boolean) devObj;
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of static method

    /**
     * Regola lo specifico attributo
     */
    public static void setBool(Attribute attributo, boolean status) {
        VaadinSession sessione = VaadinSession.getCurrent();

        if (attributo != null && sessione != null) {
            sessione.setAttribute(attributo.toString(), status);
        }// fine del blocco if
    }// end of static method

    /**
     * Regola lo specifico attributo della sessione.
     * Qualsiasi applicazione può registrare dei propri attributi nella session.
     */
    public static void setAttribute(String name, Object value) {
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            session.setAttribute(name, value);
        }// fine del blocco if
    }// end of static method

    /**
     * Regola lo specifico attributo della sessione.
     * Qualsiasi applicazione può registrare dei propri attributi nella session.
     */
    public static void setAttribute(Attribute attributo, Object value) {
        setAttribute(attributo.toString(), value);
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static Object getAttribute(Attribute attributo) {
        return getAttribute(attributo.toString());
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static Object getAttribute(String name) {
        Object value = null;
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            value = session.getAttribute(name);
        }// fine del blocco if

        return value;
    }// end of static method


    /**
     * Verifica se un attributo esiste nella sessione.
     */
    public static boolean isAttribute(String name) {
        Object value = null;
        VaadinSession session = VaadinSession.getCurrent();

        if (session != null) {
            value = session.getAttribute(name);
        }// fine del blocco if

        return (value != null);
    }// end of static method


    /**
     * Recupera un attributo dalla sessione.
     */
    public static String getAttributeStr(String name) {
        String value = "";
        Object obj = getAttribute(name);

        if (obj instanceof String) {
            value = (String) obj;
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static String getAttributeStr(Attribute attributo) {
        return getAttributeStr(attributo.toString());
    }// end of static method

    /**
     * Recupera un attributo dalla sessione.
     */
    public static boolean getAttributeBool(String name) {
        boolean value = false;
        Object obj = getAttribute(name);

        if (obj instanceof Boolean) {
            value = (Boolean) obj;
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla request l'attributo stringa indicato (se esiste)
     * Regola la variabile statica
     */
    public static void checkStr(VaadinRequest request, Attribute attribute) {
        String value = LibSession.getStr(request, attribute);
        LibSession.setAttribute(attribute.toString(), value);
    }// end of static method

    /**
     * Recupera dalla request l'attributo stringa indicato (se esiste)
     * Restituisce il valore booleano
     */
    public static String getStr(VaadinRequest request, Attribute attribute) {
        String value = "";
        Object obj = null;
        String attributeTxt = attribute.toString();

        if (request != null) {
            obj = request.getParameter(attributeTxt);
        }// fine del blocco if

        if (obj != null && obj instanceof String) {
            value = (String) obj;
        }// fine del blocco if

        return value;
    }// end of static method

    /**
     * Recupera dalla request l'attributo booleano indicato (se esiste)
     * Regola la variabile statica
     */
    public static void checkBool(VaadinRequest request, Attribute attribute) {
        boolean status = LibSession.getBool(request, attribute);
        LibSession.setBool(attribute, status);
    }// end of static method

    /**
     * Recupera dalla request l'attributo booleano indicato (se esiste)
     * Restituisce il valore booleano
     */
    public static boolean getBool(VaadinRequest request, Attribute attribute) {
        boolean status = false;
        Object obj = null;
        String objValue = "";
        String attributeTxt = attribute.toString();

        if (request != null) {
            obj = request.getParameter(attributeTxt);
        }// fine del blocco if

        if (obj != null) {
            objValue = (String) obj;
            if (objValue.equals("False") || objValue.equals("Falso") || objValue.equals("false") || objValue.equals("falso)")) {
                status = false;
            } else {
                status = true;
            }// fine del blocco if-else
        }// fine del blocco if

        return status;
    }// end of static method


    public enum Attribute {
        company, debug, login, loggato, developer, admin, servletContext
    }// end of inner enumeration

}// end of abstract static class
