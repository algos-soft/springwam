package it.algos.springvaadin.service;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import it.algos.springvaadin.entity.company.CompanyService;
import it.algos.springvaadin.lib.ACost;
import it.algos.springvaadin.login.ALogin;
import it.algos.springvaadin.login.ALoginButton;
import it.algos.springvaadin.login.IAUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by gac on 01/06/17.
 * Classe statica per la Business Logic iniziale dell'applicazione
 */
@SpringComponent
@Scope("singleton")
public class AStartService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    protected ALogin login;

    @Autowired
    protected ALoginButton loginButton;

    @Autowired
    public ALoginService userService;

    /**
     * Legge eventuali parametri passati nella request
     * <p>
     */
    //@todo da sviluppare
    public boolean checkParams(VaadinRequest request) {
        boolean continua = false;

        Object alfa = request.getContextPath();
        Object beta = request.getParameterMap();
        Object delta = request.getPathInfo();
        Object gamma = request.getCookies();

        return continua;
    }// end of static method


    /**
     * Legge i cookies dalla request (user, password, company)
     * Regola il Login della sessione
     */
    public void checkCookies(VaadinRequest request) {
        Cookie[] requestCookies = request.getCookies();
        IAUser user;
        String nickname = "";
        String password = "";
        String company = "";

        if (requestCookies != null && requestCookies.length > 1) {
            for (Cookie cookie : requestCookies) {
                if (cookie.getName().equals(ACost.COOKIE_NAME_LOGIN)) {
                    nickname = cookie.getValue();
                }// end of if cycle

                if (cookie.getName().equals(ACost.COOKIE_NAME_PASSWORD)) {
                    password = cookie.getValue();
                }// end of if cycle

                if (cookie.getName().equals(ACost.COOKIE_NAME_COMPANY)) {
                    company = cookie.getValue();
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        checkUtente(nickname, password);
    }// end of static method


    public boolean checkUtente(String nickname, String password) {
        IAUser user;
        boolean valido = false;

        nickname = nickname.replaceAll("/xspc/", " ");
        user = userService.findByNickname(nickname);
        if (user != null && userService.passwordValida(nickname, password)) {
            login.esegueLogin(nickname, password);
            loginButton.updateUI();
            valido = true;
        }// end of if cycle

        return valido;
    }// end of  method


//    /**
//     * Controlla il login della security
//     * <p>
//     * Creazione del wrapper di informazioni mantenuto nella sessione <br>
//     */
//    //@todo da sviluppare
//    public boolean checkSecurity(VaadinRequest request) {
//        boolean continua = false;
//        LibSession.setLogin(null);
//        LibSession.setAdmin(false);
//        LibSession.setDeveloper(false);
//        String siglaUser = getSiglaUtente(request);
//
////        if (LibText.isValid(siglaUser)) {
////            if (siglaUser.equals("gac")) {
////                LibSession.setDeveloper(true);
////            }// end of if cycle
////        }// end of if cycle
//
//        if (getRoleUtente(request) == ARoleType.user) {
////            LibSession.setAdmin(true);
//        }// end of if cycle
//
//        if (getRoleUtente(request) == ARoleType.admin) {
//            LibSession.setAdmin(true);
//        }// end of if cycle
//
//        if (getRoleUtente(request) == ARoleType.developer) {
//            LibSession.setDeveloper(true);
//        }// end of if cycle
//
//        return continua;
//    }// end of static method


    public String getSiglaUtente(VaadinRequest request) {
        String siglaUser = "";
        HttpServletRequest httpRequest;
        String queryString;

        if (request instanceof HttpServletRequest) {
            httpRequest = (HttpServletRequest) request;
            queryString = httpRequest.getQueryString();
//            siglaUser = getSiglaUtente(queryString);
        }// end of if cycle

        return siglaUser;
    }// end of  method


//    /**
//     * Controlla la company selezionata (sigla nell'url del browser come company=xxx)
//     * È stata intercettata nella @RequestMapping di AlgosController
//     * e inserita nel modello dati della request che arriva ad AlgosUIParams
//     * Se esiste una company con la sigla indicata, recupara la Company e la memorizzata nella sessione
//     * <p>
//     * //     * Nella sessione corrente, dovrebbe già esserci l'attributo 'company,
//     * //     * iniettato dal modello dati del reindirizzo effettuato in AlgosController
//     * //     * Ma forse si potrebbe arrivare alla Ui di partenza in altro modo
//     * //     * ed è meglio 'forzare' il valore anche se è già presente
//     *
//     * @todo da perfezionare il valore di ritorno
//     * Restituisce vero se esiste una company valida
//     */
//    public boolean checkCompany(VaadinRequest request) {
//        boolean continua = false;
//        LibSession.setCompany((Company) null);
//        String siglaCompany = getSiglaCompany(request);
//        Company company = null;
//
//        if (LibText.isValid(siglaCompany)) {
//            company = companyService.findByCode(siglaCompany);
//        }// end of if cycle
//
//        if (company != null) {
//            LibSession.setCompany(company);
//            continua = true;
//        }// end of if cycle
//
//        return continua;
//    }// end of  method


//    public String getSiglaCompany(VaadinRequest request) {
//        String siglaCompany = "";
//        HttpServletRequest httpRequest;
//        String queryString;
//
//        if (request instanceof HttpServletRequest) {
//            httpRequest = (HttpServletRequest) request;
//            queryString = httpRequest.getQueryString();
//            siglaCompany = getSiglaCompany(queryString);
//        }// end of if cycle
//
//        return siglaCompany;
//    }// end of  method


//    /**
//     * Controlla che la company ricevuta come parametro in ingresso, sia valida
//     */
//    public boolean isCompanyValida(HttpServletRequest request) {
//        return getCompany(request) != null;
//    }// end of static method


//    /**
//     * Recupera la company come parametro in ingresso.
//     * Il nome della company, contenuto nell'URI, è recuperato se presente nella forma:
//     * 1) /demo
//     * 2) /demo=
//     * 3) /company=demo
//     * 4) /?company=demo
//     * 5) /?company=demo&buttonUser=gac
//     * 6) http://localhost:8090?demo
//     * Deve poi essere una company valida prevista nella collezione 'company'
//     */
//    public Company getCompany(HttpServletRequest request) {
//        Company company = null;
//        String siglaCompany = getSiglaCompany(request.getRequestURI());
//
//        if (!siglaCompany.equals("")) {
//            company = companyService.findByCode(siglaCompany);
//        }// end of if cycle
//
//        return company;
//    }// end of method


//    /**
//     * Recupera la sigla della company come parametro in ingresso.
//     * Il nome della company, contenuto nell'URI, è recuperato se presente nella forma:
//     * 1) /demo
//     * 2) /demo=
//     * 3) /company=demo
//     * 4) /?company=demo
//     * 5) /?company=demo&buttonUser=gac
//     * 6) http://localhost:8090?demo
//     */
//    public String getSiglaCompany(String url) {
//        String companyName = "";
//        Map<String, String> mappaParams = getParams(url);
//        String tagCompany = "company";
//        String tagVuoto = "";
//        String tagIniPatch = "v-";
//
//        if (mappaParams.size() > 0) {
//            if (mappaParams.containsKey(tagCompany)) {
//                companyName = mappaParams.get(tagCompany);
//                return companyName;
//            } else {
//                Object[] chiavi = mappaParams.keySet().toArray();
//                Object[] valori = mappaParams.values().toArray();
//
//                if (mappaParams.size() == 1 && ((String) chiavi[0]).startsWith(tagIniPatch)) {
//                    return "";
//                }// end of if cycle
//
//                if (valori[0] == null || valori[0].equals(tagVuoto)) {
//                    return (String) chiavi[0];
//                }// end of if cycle
//            }// end of if/else cycle
//        }// end of if cycle
//
//        return companyName;
//    }// end of method


//    /**
//     * Recupera i parametri in ingresso.
//     * 1) /demo
//     * 2) /demo=
//     * 3) /company=demo
//     * 4) /?company=demo
//     * 5) /?company=demo&buttonUser=gac
//     * 6) http://localhost:8090?demo
//     */
//    public Map getParams(String url) {
//        MultiValueMap<String, String> queryMultiParams = null;
//        String tagMettere = "?";
//        String tagLevare = "/";
//
//        if (url.startsWith(tagLevare)) {
//            url = LibText.levaTesta(url, tagLevare);
//        }// end of if cycle
//        queryMultiParams = UriComponentsBuilder.fromUriString(url).build().getQueryParams();
//
//        if (queryMultiParams.size() == 0) {
//            url = tagMettere + url;
//            queryMultiParams = UriComponentsBuilder.fromUriString(url).build().getQueryParams();
//        }// end of if cycle
//
//        return queryMultiParams.toSingleValueMap();
//    }// end of method


//    /**
//     * Recupera la sigla dell'Utente come parametro in ingresso.
//     * Il nickname del User, contenuto nell'URI, è recuperato se presente nella forma:
//     * 1) /buttonUser=gac
//     * 2) /buttonUser=gac
//     * 3) /admin=gac
//     * 4) /developer=gac
//     * 5) /?buttonUser=gac
//     * 6) /?buttonUser=gac
//     * 7) /?admin=gac
//     * 8) /?developer=gac
//     * 9) /company=demo&buttonUser=gac
//     * 10) /?company=demo&buttonUser=gac
//     * 11) http://localhost:8090?demo&user=gac
//     * 12) /demo&buttonUser=gac
//     * 13) /?demo&buttonUser=gac
//     * 14) /buttonUser=gac&demo
//     * 15) /?buttonUser=gac&demo
//     */
//    public String getSiglaUtente(String url) {
//        String siglaUtente = "";
//        Map<String, String> mappaParams = getParams(url);
//        String[] tagUtenti = {"buttonUser", "ute", "buttonUser", "admin", "dev", "developer"};
//        String tagVuoto = "";
//        String tagIniPatch = "v-";
//
//        if (mappaParams.size() > 0) {
//            for (String tag : tagUtenti) {
//                if (mappaParams.containsKey(tag)) {
//                    return mappaParams.get(tag);
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return siglaUtente;
//    }// end of method


//    /**
//     * Recupera il ruolo dell'buttonUser come parametro in ingresso.
//     * Il ruolo dell'buttonUser, contenuto nell'URI, è recuperato se presente nella forma:
//     * 1) /buttonUser=gac
//     * 2) /buttonUser=gac
//     * 3) /admin=gac
//     * 4) /developer=gac
//     * 5) /?buttonUser=gac
//     * 6) /?buttonUser=gac
//     * 7) /?admin=gac
//     * 8) /?developer=gac
//     * 9) /company=demo&buttonUser=gac
//     * 10) /?company=demo&buttonUser=gac
//     * 11) http://localhost:8090?demo&user=gac
//     * 12) /demo&buttonUser=gac
//     * 13) /?demo&buttonUser=gac
//     * 14) /buttonUser=gac&demo
//     * 15) /?buttonUser=gac&demo
//     */
//    public ARoleType getRoleUtente(VaadinRequest request) {
//        ARoleType ruoloUtente = null;
//        HttpServletRequest httpRequest;
//        String queryString;
//
//        if (request instanceof HttpServletRequest) {
//            httpRequest = (HttpServletRequest) request;
//            queryString = httpRequest.getQueryString();
//            ruoloUtente = getRoleUtente(queryString);
//        }// end of if cycle
//
//        return ruoloUtente;
//    }// end of  method


//    /**
//     * Recupera il ruolo dell'buttonUser come parametro in ingresso.
//     * Il ruolo dell'buttonUser, contenuto nell'URI, è recuperato se presente nella forma:
//     * 1) /buttonUser=gac
//     * 2) /buttonUser=gac
//     * 3) /admin=gac
//     * 4) /developer=gac
//     * 5) /?buttonUser=gac
//     * 6) /?buttonUser=gac
//     * 7) /?admin=gac
//     * 8) /?developer=gac
//     * 9) /company=demo&buttonUser=gac
//     * 10) /?company=demo&buttonUser=gac
//     * 11) http://localhost:8090?demo&user=gac
//     * 12) /demo&buttonUser=gac
//     * 13) /?demo&buttonUser=gac
//     * 14) /buttonUser=gac&demo
//     * 15) /?buttonUser=gac&demo
//     */
//    public ARoleType getRoleUtente(String url) {
//        ARoleType ruoloUtente = null;
//        String[] tagUtente = {"buttonUser", "ute", "buttonUser"};
//        String[] tagAdmin = {"admin"};
//        String[] tagDeveloper = {"dev", "developer"};
//        Map<String, String> mappaParams = getParams(url);
//
//        if (mappaParams.size() > 0) {
//            for (String tag : tagUtente) {
//                if (mappaParams.containsKey(tag)) {
//                    return ARoleType.user;
//                }// end of if cycle
//            }// end of for cycle
//            for (String tag : tagAdmin) {
//                if (mappaParams.containsKey(tag)) {
//                    return ARoleType.admin;
//                }// end of if cycle
//            }// end of for cycle
//            for (String tag : tagDeveloper) {
//                if (mappaParams.containsKey(tag)) {
//                    return ARoleType.developer;
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return ruoloUtente;
//    }// end of method

}// end of static class
