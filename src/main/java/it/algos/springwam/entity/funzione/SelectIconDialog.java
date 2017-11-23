package it.algos.springwam.entity.funzione;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import it.algos.springvaadin.dialog.BaseDialog;
import it.algos.springvaadin.lib.LibResource;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Dialogo di selezione di una icona
 */
//@SpringComponent
class SelectIconDialog extends BaseDialog {


    public SelectIconDialog() {
        super();
        setTitle("Scegli una icona");
        IconGrid grid = new IconGrid();
        addComponent(grid);

        Component dc = getDetailComponent();
        if (dc instanceof VerticalLayout) {
            VerticalLayout vl = (VerticalLayout) dc;
            vl.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
        }

        Button bRemove = new Button("Rimuovi icona");
        bRemove.setIcon(VaadinIcons.TRASH);
        getToolbar().addComponent(bRemove);
        bRemove.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DialogEvent e = new DialogEvent(2);
                fireListeners(e);
                close();
            }
        });


        Button bChiudi = new Button("Chiudi");
        bChiudi.setIcon(VaadinIcons.CLOSE);
        getToolbar().addComponent(bChiudi);
        bChiudi.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DialogEvent e = new DialogEvent(0);
                fireListeners(e);
                close();
            }
        });
    }


    class IconGrid extends GridLayout {

        public IconGrid() {
            super();
            setColumns(5);
            setSpacing(true);
            populate();
        }

        private void populate() {
            int[] codepoints = {VaadinIcons.AMBULANCE.getCodepoint(),
                    VaadinIcons.CROSS_CUTLERY.getCodepoint(),
                    VaadinIcons.HEART.getCodepoint(),
                    VaadinIcons.MEDAL.getCodepoint(),
                    VaadinIcons.STETHOSCOPE.getCodepoint(),
                    VaadinIcons.USER.getCodepoint(),
                    VaadinIcons.USER_STAR.getCodepoint(),
                    VaadinIcons.MALE.getCodepoint(),
                    VaadinIcons.FEMALE.getCodepoint(),
                    VaadinIcons.PHONE.getCodepoint(),
                    VaadinIcons.BRIEFCASE.getCodepoint(),
                    VaadinIcons.STAR.getCodepoint(),
                    VaadinIcons.TABLET.getCodepoint(),
            };
            for (int codepoint : codepoints) {
                VaadinIcons icona = LibResource.getVaadinIcon(codepoint);

                Button b = new Button();
                b.setIcon(icona);
                b.setWidth("3em");
                b.addStyleName("verde");
                addComponent(b);

                b.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        iconClicked(codepoint);
                    }
                });
            }

        }

        private void iconClicked(int codepoint) {
            DialogEvent e = new DialogEvent(1, codepoint);
            fireListeners(e);
            close();
        }


    }


    // listener list
    private List<CloseListener> closeListenerList = new ArrayList();

    public void addCloseListener(CloseListener l) {
        closeListenerList.add(l);
    }

    public interface CloseListener {
        void dialogClosed(DialogEvent event);
    }

    public class DialogEvent extends EventObject {

        private int exitcode;
        private int codepoint;

        /**
         * @param exitcode: 0=cancel, 1=confirm, 2=remove icon.
         *                  If exitcode==1, selectedCodepoint contains the codepoint of the selected icon
         */
        public DialogEvent(int exitcode, int selectedCodepoint) {
            super(SelectIconDialog.this);
            this.exitcode = exitcode;
            this.codepoint = selectedCodepoint;
        }

        public DialogEvent(int exitcode) {
            this(exitcode, 0);
        }

        public int getExitcode() {
            return exitcode;
        }

        public int getCodepoint() {
            return codepoint;
        }
    }

    private void fireListeners(DialogEvent e) {
        for (CloseListener l : closeListenerList) {
            l.dialogClosed(e);
        }
    }

}
