package it.algos.springvaadin.service;

import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.JavaScript;
import org.springframework.context.annotation.Scope;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by alex on 30-09-2015.
 * .
 */
@SpringComponent
@Scope("singleton")
public class ACookieService {


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);




    /**
     * Creates or updates a cookie in the browser.
     * Uses the default path and makes the cookie session-scoped (not persistent)
     *
     * @param key   the key
     * @param value the value
     */
    public void setCookie(String key, String value) {
        setCookie(key, value, getPath(), -1);
    }// end of method


    /**
     * Creates or updates a cookie in the browser.
     * Uses the default path
     *
     * @param key       the key
     * @param value     the value
     * @param expirySec the expiration time in seconds,
     *                  A positive value indicates that the cookie will expire after that many seconds have passed.
     *                  A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits.
     *                  A zero value causes the cookie to be deleted.
     */
    @Deprecated
    public void setCookie(String key, String value, int expirySec) {
        setCookie(key, value, getPath(), expirySec);
    }// end of method


    /**
     * Creates or updates a cookie in the browser.
     *
     * @param key       the key
     * @param value     the value
     * @param path      the path
     * @param expirySec the expiration time in seconds,
     *                  A positive value indicates that the cookie will expire after that many seconds have passed.
     *                  A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits.
     *                  A zero value causes the cookie to be deleted.
     */
    public void setCookie(String key, String value, String path, int expirySec) {

        JavaScript js = JavaScript.getCurrent();

        if (js != null) {

            // protects the value
            value = protect(value);

            if (path.endsWith("")) {
                path = "/";
            }// end of if cycle

            if (expirySec == 0) {
                String cmd = String.format("document.cookie = '%s=; path=%s; expires=Thu, 01 Jan 1970 00:00:01 GMT';", key, path);
                js.execute(cmd);
                return;
            }

            if (expirySec > 0) {
                Instant i = Instant.now().plusSeconds(expirySec);
                Date d = Date.from(i);
                String utc = getUTCString(d);
                String cmd = String.format("document.cookie = '%s=%s; path=%s; expires=%s';", key, value, path, utc);
                js.execute(cmd);
                return;
            }

            if (expirySec < 0) {
                String cmd = String.format("document.cookie = '%s=%s; path=%s';", key, value, path);
                js.execute(cmd);
                return;
            }
        }

    }// end of method


    /**
     * Deletes a cookie in the browser.
     * Uses the default path
     *
     * @param key the key
     */
    @Deprecated
    public void deleteCookie(String key) {
        setCookie(key, "", getPath(), 0);
    }// end of method

    /**
     * Deletes a cookie in the browser.
     * Uses the default path
     *
     * @param key the key
     */
    public void deleteCookie(String key, String path) {
        setCookie(key, "", path, 0);
    }// end of method


    /**
     * Find a cookie by name
     */
    public Cookie getCookie(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        if (name != null && cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    if (name.equals(cookie.getName())) {

                        // unprotect the value
                        String value = cookie.getValue();
                        cookie.setValue(unprotect(value));

                        return cookie;
                    }
                }
            }
        }

        return null;
    }// end of method

    /**
     * Return a cookie's value by name
     */
    public String getCookieValue(String name) {
        String value = "";
        Cookie cookie = getCookie(name);

        if (cookie != null) {
            value = cookie.getValue();
        }// fine del blocco if

        return value;
    }// end of method


    /**
     * Convert a date in JS cookie format
     */
    private String getUTCString(Date date) {
        DATE_FORMAT.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        String str = DATE_FORMAT.format(date);
        str += " UTC";
        return str;
    }// end of method

    /**
     * @return the path for the cookie
     */
    private String getPath() {
        String path = "/";
        return path;
    }// end of method

    /**
     * Protects some characters not allowed in cookie values.
     * As of RFC 6265, excluded characters are:
     * whitespace, double quote, comma, semicolon, and backslash
     * The equal character should be ok but some browsers mess with it,
     * so we protect this character too.
     *
     * @param in the original value
     *
     * @return the protected value (safe to write in cookie)
     */
    private String protect(String in) {
        String out = in;

        if (in != null) {
            out = out.replace(" ", "/xspc/");
            out = out.replace("\"", "/xquote/");
            out = out.replace(",", "/xcomma/");
            out = out.replace(";", "/xsemi/");
            out = out.replace("\\", "/xback/");
            out = out.replace("=", "/xeq/");
        }// end of if cycle

        return out;
    }// end of method

    /**
     * Unprotects some characters not allowed in cookie values.
     *
     * @param in the protected value
     *
     * @return the original value
     */
    private String unprotect(String in) {
        String out = in;

        if (in != null) {
            out = out.replace("/xspc/", " ");
            out = out.replace("/xquote/", "\"");
            out = out.replace("/xcomma/", ",");
            out = out.replace("/xsemi/", ";");
            out = out.replace("/xback/", "\\");
            out = out.replace("/xeq/", "=");
        }// end of if cycle

        return out;
    }// end of method


}// end of service class
