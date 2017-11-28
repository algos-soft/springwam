package it.algos.springvaadin.lib;

import java.util.Arrays;
import java.util.List;

/**
 * Repository di costanti della applicazione
 * <p>
 * Costanti per leggere/scrivere sempre uguale nelle mappe, negli attributi, nei cookies, nelle property
 * Altre costanti 'static' sono raggruppate nella classe it.algos.webbase.web.AlgosApp
 */
public abstract class Cost {

    public final static String LOGIN_INFO = "loginInfo";

    public final static String COOKIE_LOGIN_NICK = "cookieLoginNick";
    public final static String COOKIE_LOGIN_PASS = "cookieLoginPass";
    public final static String COOKIE_LOGIN_ROLE = "cookieLoginRole";

    public final static String KEY_MAP_COMPANY = "company";
    public final static String SIGLA_COMPANY_DEMO = "demo";

    public final static String PROPERTY_ID = "id";
    public final static String PROPERTY_COMPANY = "company";
    public final static String PROPERTY_SERIAL = "serialVersionUID";
    public final static String PROPERTY_ORDINE = "ordine";
    public final static String PROPERTY_CREAZIONE = "creazione";
    public final static String PROPERTY_MODIFICA = "modifica";
    public final static String COMPANY_CODE = "code";
    public final static String COMPANY_UNICO = "codeCompanyUnico";

    private final static String[] esclusiMatrice = {Cost.PROPERTY_SERIAL, Cost.PROPERTY_ID, Cost.PROPERTY_COMPANY};
    public final static List<String> ESCLUSI = Arrays.asList(esclusiMatrice);
    private final static String[] companyMatrice = {Cost.COMPANY_CODE, Cost.COMPANY_UNICO};
    public final static List<String> COMPANY_OPTIONAL = Arrays.asList(companyMatrice);

    public final static String TAG_VERS = "vers";
    public final static String TAG_COMP = "comp";
    public final static String TAG_LOG = "log";
    public final static String TAG_HELP = "help";
    public final static String TAG_HOME = "home";
    public final static String TAG_ROL = "role";
    public final static String TAG_USE = "user";
    public final static String TAG_IND = "ind";
    public final static String TAG_STA = "stato";
    public final static String TAG_PER = "per";
    public final static String TAG_PRE = "pref";

    public final static String BOT_ACCETTA = "accetta";
    public final static String BOT_ANNULLA = "annulla";
    public final static String BOT_BACK = "back";
    public final static String BOT_CREATE = "nuovo";
    public final static String BOT_DELETE = "elimina";
    public final static String BOT_EDIT = "edit";
    public final static String BOT_SHOW = "show";
    public final static String BOT_IMAGE = "immagine";
    public final static String BOT_IMPORT = "import";
    public final static String BOT_LINK_ACCETTA = "linkaccetta";
    public final static String BOT_LINK_REGISTRA = "linkregistra";
    public final static String BOT_REGISTRA = "registra";
    public final static String BOT_REVERT = "revert";
    public final static String BOT_SEARCH = "ricerca";
    public final static String BOT_SHOW_ALL = "tutto";
    public final static String BOT_LINK = "botlink";
    public final static String BOT_CHOOSER = "apri";

    public final static String FIELD_ID = "KeyID";
    public final static String FIELD_TEXT = "text";
    public final static String FIELD_TEXT_AREA = "textarea";
    public final static String FIELD_INTEGER = "integer";
    public final static String FIELD_DATE = "date";
    public final static String FIELD_DATE_TIME = "datetime";
    public final static String FIELD_IMAGE = "image";
    public final static String FIELD_COMBO = "combobox";
    public final static String FIELD_LINK = "fieldlink";
    public final static String FIELD_CHEK_BOX = "fieldcheckbox";
    public final static String FIELD_JSON = "fieldjson";
    public final static String FIELD_RADIO = "fieldradio";

    public final static String VIEW_IMAGE = "viewimage";

    public final static String BAR_LIST = "toolbarlist";
    public final static String BAR_FORM = "toolbarform";
    public final static String BAR_LINK = "toolbarlink";

    public final static String STYLE_GREEN = "buttonGreen";
    public final static String STYLE_BLUE = "buttonBlue";
    public final static String STYLE_RED = "buttonRed";

    public final static String TAG_AZ_ATTACH = "attach";
    public final static String TAG_AZ_CLICK = "click";
    public final static String TAG_AZ_DOPPIO_CLICK = "doppioClick";
    public final static String TAG_AZ_SINGLE_SELECTION = "singleSelectionChanged";
    public final static String TAG_AZ_MULTI_SELECTION = "multiSelectionChanged";
    public final static String TAG_AZ_VALUE_CHANGED = "valueChange";
    public final static String TAG_AZ_LISTENER = "listener";

    //--chiavi per usare le preferenze della applicazione
    public final static String KEY_USE_DEBUG = "useDebug";
    public final static String KEY_USE_MULTI_COMPANY = "useMultiCompany";
    public final static String KEY_DISPLAY_NEW_RECORD_ONLY = "newRecordOnly";
    public final static String KEY_DISPLAY_FOOTER_INFO = "displayFooterInfo";
    public final static String KEY_DISPLAY_TOOLTIPS = "displayTooltips";
    public final static String KEY_USE_SELEZIONE_MULTIPLA_GRID = "useSelezioneMultiplaGrid";
    public final static String KEY_USE_PROPERTY_CREAZIONE_AND_MODIFICA = "usePropertyCreazioneAndModifica";
    public final static String DISPLAY_FIELD_ORDINE = "displayFieldOrdine";
    public final static String DISPLAY_LISTE_COLLEGATE = "displayListeCollegate";
    public final static String USA_FORM_LAYOUT = "usaFormLayout";
    public final static String USA_REFRESH_DEMO = "usaRefreshDemo";
    public final static String ATTIVA_MIGRATION = "attivaMigration";
    public final static String USA_MIGRATION_COMPLETA = "usaMigrationCompleta";
    public final static String CLICK_BOTTONI_IN_LISTA = "clickBottoniInLista";
    public final static String INFO_APP = "Algos® WAM-2.0 del 14.5.17";

    //--chiavi per i bottoni di List e Form
    public final static String TAG_BOT_NEW = "usaBottoneNew";
    public final static String TAG_BOT_EDIT = "usaBottoneEdit";
    public final static String TAG_BOT_DELETE = "usaBottoneDelete";
    public final static String TAG_BOT_SEARCH = "usaBottoneRicerca";
    public final static String TAG_BOT_SHOW = "usaBottoneShow";
    public final static String TAG_BOT_ANNULLA = "usaBottoneAnnulla";
    public final static String TAG_BOT_REVERT = "usaBottoneRevert";
    public final static String TAG_BOT_SAVE = "usaBottoneRegistra";
}// end of static class;
