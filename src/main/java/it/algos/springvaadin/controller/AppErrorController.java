package it.algos.springvaadin.controller;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by gac on 07/06/17.
 * Basic Controller which is called for unhandled errors
 */
@RestController
public class AppErrorController implements ErrorController {

    /**
     * Error Attributes in the Application
     */
    private ErrorAttributes errorAttributes;

    private final static String ERROR_PATH = "/error";

    /**
     * Controller for the Error Controller
     * @param errorAttributes
     */
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }// end of method

    /**
     * Supports the HTML Error View
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        return new ModelAndView("/errors/error", getErrorAttributes(request, false));
    }// end of method

    /**
     * Supports other formats like JSON, XML
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<Map<String, Object>>(body, status);
    }// end of method

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }// end of method


    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");

        if (parameter == null) {
            return false;
        }// end of if cycle

        return !"false".equals(parameter.toLowerCase());
    }// end of method

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode != null) {
            try { // prova ad eseguire il codice
                return HttpStatus.valueOf(statusCode);
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// end of if cycle

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }// end of method

}// end of class