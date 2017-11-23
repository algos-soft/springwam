package it.algos.springvaadin.bottone;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.springvaadin.bottone.AButton;
import it.algos.springvaadin.field.AField;
import it.algos.springvaadin.lib.Cost;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: dom, 20-ago-2017
 * Time: 19:12
 * <p>
 * Bottoni della toolbar di una Grid
 * Bottoni della toolbar di un Form
 * <p>
 * Enumeration utilizzata per 'marcare' un evento, in fase di generazione
 * Enumeration utilizzata per 'riconoscerlo' nel metodo onApplicationEvent()
 */
public enum AButtonType {


    accetta(Cost.BOT_ACCETTA, VaadinIcons.CHECK, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, ShortcutAction.KeyCode.ENTER, 0),
    annulla(Cost.BOT_ANNULLA, VaadinIcons.ARROW_BACKWARD, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, ShortcutAction.KeyCode.BACKSPACE, ShortcutAction.ModifierKey.ALT),
    back(Cost.BOT_ANNULLA, VaadinIcons.ARROW_BACKWARD, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, 0, 0),
    chooser(Cost.BOT_CHOOSER, VaadinIcons.SEARCH, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, 0, 0),
    create(Cost.BOT_CREATE, VaadinIcons.PLUS, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, ShortcutAction.KeyCode.N, ShortcutAction.ModifierKey.ALT),
    delete(Cost.BOT_DELETE, VaadinIcons.CLOSE, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_DANGER, 0, 0),
    deleteLink("", VaadinIcons.CLOSE, true, AButton.ICON_WIDTH, ValoTheme.BUTTON_DANGER, 0, 0),
    edit(Cost.BOT_EDIT, VaadinIcons.EDIT, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_PRIMARY, ShortcutAction.KeyCode.SPACEBAR, 0),
    show(Cost.BOT_SHOW, VaadinIcons.ALIGN_JUSTIFY, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_PRIMARY, ShortcutAction.KeyCode.SPACEBAR, 0),
    editLinkDBRef("", VaadinIcons.EDIT, true, AButton.ICON_WIDTH, ValoTheme.BUTTON_PRIMARY, 0, 0),
    editLinkNoDBRef("", VaadinIcons.EDIT, true, AButton.ICON_WIDTH, ValoTheme.BUTTON_PRIMARY, 0, 0),
    image("", VaadinIcons.EDIT, true, AButton.ICON_WIDTH, "", 0, 0),
    importa(Cost.BOT_IMPORT, VaadinIcons.EXTERNAL_BROWSER, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_DANGER, 0, 0),
    linkAccetta(Cost.BOT_ACCETTA, VaadinIcons.EDIT, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_PRIMARY, ShortcutAction.KeyCode.ENTER, 0),
    linkRegistra(Cost.BOT_REGISTRA, VaadinIcons.EDIT, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_PRIMARY, ShortcutAction.KeyCode.ENTER, 0),
    registra(Cost.BOT_REGISTRA, VaadinIcons.DATABASE, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_PRIMARY, ShortcutAction.KeyCode.ENTER, 0),
    revert(Cost.BOT_REVERT, VaadinIcons.REFRESH, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, ShortcutAction.KeyCode.R, ShortcutAction.ModifierKey.ALT),
    search(Cost.BOT_SEARCH, VaadinIcons.SEARCH, true, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, ShortcutAction.KeyCode.F, ShortcutAction.ModifierKey.ALT),
    showAll(Cost.BOT_SHOW_ALL, VaadinIcons.ALIGN_JUSTIFY, false, AButton.NORMAL_WIDTH, ValoTheme.BUTTON_FRIENDLY, 0, 0);


    private String caption;
    private Resource icon;
    private boolean enabled;
    private String width;
    private String style;
    private int keyCode;
    private int modifier;


    /**
     * Costruttore interno dell'Enumeration
     */
    AButtonType(String caption, Resource icon, boolean enabled, String width, String style, int keyCode, int modifier) {
        this.setCaption(caption);
        this.setIcon(icon);
        this.setEnabled(enabled);
        this.setWidth(width);
        this.setStyle(style);
        this.setKeyCode(keyCode);
        this.setModifier(modifier);
    }// fine del costruttore interno


    public String getCaption() {
        return caption;
    }// end of method

    public void setCaption(String caption) {
        this.caption = caption;
    }// end of method

    public Resource getIcon() {
        return icon;
    }// end of method

    public void setIcon(Resource icon) {
        this.icon = icon;
    }// end of method

    public boolean isEnabled() {
        return enabled;
    }// end of method

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }// end of method

    public String getWidth() {
        return width;
    }// end of method

    public void setWidth(String width) {
        this.width = width;
    }// end of method

    public String getStyle() {
        return style;
    }// end of method

    public void setStyle(String style) {
        this.style = style;
    }// end of method

    public int getKeyCode() {
        return keyCode;
    }// end of method

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }// end of method

    public int getModifier() {
        return modifier;
    }// end of method

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }// end of method

}// end of Enumeration
