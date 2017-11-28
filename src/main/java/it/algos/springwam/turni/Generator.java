package it.algos.springwam.turni;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.springvaadin.annotation.AIEntity;
import it.algos.springvaadin.field.ADateField;
import it.algos.springvaadin.lib.DateConvertUtils;
import it.algos.springvaadin.login.ARoleType;
import it.algos.springwam.application.AppCost;
import it.algos.springwam.entity.servizio.Servizio;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Componente per preparare il generatore/eliminatore di turni vuoti
 */
@SpringView(name = AppCost.TAG_GEN)
@AIEntity(roleTypeVisibility = ARoleType.admin)
public class Generator extends VerticalLayout implements View {

    private ADateField dateField1=new ADateField();
    private ADateField dateField2=new ADateField();;
    private ProgressBar progressBar;
    private boolean generatorRunning;
    private ArrayList<Servizio> servizi;
    private CheckBox[][] matrix;
    private boolean genera; //true=genera, false=cancella
    private Button bEsegui;
    private MenuBar.MenuItem itemGenera;

    public Generator() {
        this.genera = true;

        addComponent(creaCompTitolo());
        addComponent(creaCompDettaglio());

        Component panComandi = creaPanComandi();
        addComponent(panComandi);

        setComponentAlignment(panComandi, Alignment.BOTTOM_CENTER);

        // seleziona l'item Genera
        itemGenera.getCommand().menuSelected(itemGenera);
    }// end of Spring constructor


    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<strong>" + "Generatore turni" + "</strong>", ContentMode.HTML));
        return layout;
    }// end of method


    /**
     * Crea il componente che visualizza i dettagli del turno
     * (nome ecc..)
     *
     * @return il componente di dettaglio
     */
    private Component creaCompDettaglio() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        HorizontalLayout layoutDate = new HorizontalLayout();
        layoutDate.setSpacing(true);
        layoutDate.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

//        dateField1 = new ADateField("Dal giorno");
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        dateField1.setValue(DateConvertUtils.asUtilDate(tomorrow));
//        dateField1.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                Object value = valueChangeEvent.getProperty().getValue();
//                if (value != null && value instanceof Date) {
//                    Date date = (Date) value;
//                    LocalDate d2 = DateConvertUtils.asLocalDate(date).plusWeeks(1);
//                    dateField2.setValue(DateConvertUtils.asUtilDate(d2));
//                }
//            }
//        });


//        dateField2 = new DateField("Al giorno");
//        LocalDate date = DateConvertUtils.asLocalDate(dateField1.getValue());
//        Date duWeek = DateConvertUtils.asUtilDate(date.plusWeeks(1));
//        dateField2.setValue(duWeek);

        // menubar genera / cancella
        MenuBar actionBar = new MenuBar();
        itemGenera = actionBar.addItem("Genera", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                for (MenuBar.MenuItem item : actionBar.getItems()) {
                    item.setStyleName(null);
                }
                menuItem.setStyleName("highlight");
                bEsegui.setCaption("Genera turni");
                genera = true;
            }
        });
        itemGenera.setDescription("Genera i turni vuoti per i giorni e i servizi specificati. I turni già esistenti non vengono ricreati.");

        MenuBar.MenuItem itemCancella = actionBar.addItem("Cancella", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                for (MenuBar.MenuItem item : actionBar.getItems()) {
                    item.setStyleName(null);
                }
                menuItem.setStyleName("highlight");
                bEsegui.setCaption("Cancella turni");
                genera = false;
            }
        });
        itemCancella.setDescription("Cancella i turni per i giorni e i servizi specificati. I turni vengono cancellati solo se nessuno è iscritto.");

        layoutDate.addComponent(dateField1);
        layoutDate.addComponent(dateField2);
        layoutDate.addComponent(actionBar);

        servizi = new ArrayList<>();
//        servizi.addAll(WamQuery.queryServizi(entityManager, true));//@todo completare
//        servizi.addAll(WamQuery.queryServizi(entityManager, false));

        GridLayout grid = new GridLayout(9, servizi.size() + 2);
        grid.setSpacing(true);

        grid.addComponent(new Label("Lun"), 1, 0);
        grid.addComponent(new Label("Mar"), 2, 0);
        grid.addComponent(new Label("Mer"), 3, 0);
        grid.addComponent(new Label("Gio"), 4, 0);
        grid.addComponent(new Label("Ven"), 5, 0);
        grid.addComponent(new Label("Sab"), 6, 0);
        grid.addComponent(new Label("Dom"), 7, 0);

        int rows = servizi.size();
        int cols = 7;
        matrix = new CheckBox[rows][cols];

        int row = 0;
        for (Servizio serv : servizi) {

            // nome servizio
            Label label = new Label(serv.getDescrizione());
            grid.addComponent(label, 0, row + 1);

            // riga orizzontale di checkboxes
            for (int col = 0; col < 7; col++) {
                CheckBox cb = new CheckBox();
                grid.addComponent(cb, col + 1, row + 1);
                matrix[row][col] = cb;
            }

            // componente multiselettore per riga
            int row1 = row;
            SwitchOnOffH onOff = new SwitchOnOffH(new SwitchListener() {

                @Override
                public void clickedOn() {
                    turnRow(matrix, row1, true);
                }

                @Override
                public void clickedOff() {
                    turnRow(matrix, row1, false);
                }
            });
            grid.addComponent(onOff, 8, row + 1);

            row++;

        }

        // componenti multiselettore per colonna
        for (int col = 1; col <= 7; col++) {
            int col1 = col - 1;
            SwitchOnOffV onOff = new SwitchOnOffV(new SwitchListener() {
                @Override
                public void clickedOn() {
                    turnColumn(matrix, col1, true);
                }

                @Override
                public void clickedOff() {
                    turnColumn(matrix, col1, false);
                }
            });
            grid.addComponent(onOff, col, row + 1);
        }

        // componente multiselettore globale
        SwitchOnOffH onOffGen = new SwitchOnOffH(new SwitchListener() {
            @Override
            public void clickedOn() {
                for (int row = 0; row < matrix.length; row++) {
                    turnRow(matrix, row, true);
                }
            }

            @Override
            public void clickedOff() {
                for (int row = 0; row < matrix.length; row++) {
                    turnRow(matrix, row, false);
                }
            }
        }, FontAwesome.CHECK_SQUARE, FontAwesome.TIMES);
        // nell' ultima cella in basso a dx
//        onOffGen.setCaption("tutto");
        grid.addComponent(onOffGen, grid.getColumns() - 1, grid.getRows() - 1);
        grid.setComponentAlignment(onOffGen, Alignment.BOTTOM_CENTER);


        layout.addComponent(layoutDate);
        layout.addComponent(grid);
        return layout;
    }// end of method

    private void turnRow(CheckBox[][] matrix, int row, boolean state) {
        for (int i = 0; i < matrix[row].length; i++) {
            matrix[row][i].setValue(state);
        }
    }// end of method

    private void turnColumn(CheckBox[][] matrix, int col, boolean state) {
        CheckBox[] boxes = getColumn(matrix, col);
        for (CheckBox cb : boxes) {
            cb.setValue(state);
        }
    }// end of method


    private static CheckBox[] getColumn(CheckBox[][] array, int index) {
        CheckBox[] column = new CheckBox[array.length];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }// end of method


    /**
     * Crea e ritorna il pannello comandi.
     *
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        progressBar = new ProgressBar();
        progressBar.setCaption("");
        progressBar.setWidth("12em");
//        progressBar.addStyleName("invisible");

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                fireDismissListeners(new DismissEvent(bAnnulla, false, false));@todo rimettere
            }
        });

        bEsegui = new Button("Genera turni");
        bEsegui.setWidth("10em");
        bEsegui.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        bEsegui.addStyleName(ValoTheme.BUTTON_PRIMARY);    // "primary" not "default"
//        bEsegui.addClickListener(new Button.ClickListener() {
//
//            TurniGenEngine generator;
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//
//                if (!generatorRunning) {
//
//                    // resetta la progress bar
//                    // Attenzione! la UI deve essere annotata con @Push per usare UI.access()
//                    getUI().access(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setCaption("");
//                            progressBar.setValue(0f);
//                        }
//                    });
//
//                    // crea i dati e controlla se sono validi
//                    GeneratorData data = createGeneratorData();
//                    String err = data.checkValid();
//                    if (!err.equals("")) {
//                        Notification.show("Dati non validi", err, Notification.Type.ERROR_MESSAGE);
//                        return;
//                    }
//
//                    generator = new TurniGenEngine(data, null);
//
//
//                    // aggiunge un listener per essere informato ogni volta che un giorno è fatto
//                    // e aggiornare la progressbar
//                    generator.addGiornoProgressListener(new TurniGenEngine.GiornoProgressListener() {
//
//                        int quanti = 0;
//
//                        @Override
//                        public void progressUpdate(int progress) {
//
//                            quanti = progress;
//
//                            getUI().access(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int tot = generator.getQuantiGiorni();
//                                    float progress = (float) quanti / (tot);
//                                    progressBar.setCaption(quanti + "/" + tot);
//                                    progressBar.setValue(progress);
//                                }
//                            });
//
//
//                        }
//                    });
//
//                    // aggiunge un listener per essere informato quando il lavoro è finito
//                    generator.addEngineDoneListener(new TurniGenEngine.EngineDoneListener() {
//                        @Override
//                        public void engineDone(boolean success) {
//                            getUI().access(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    // aspetta un attimo che il thread finisca di aggiornare la barra
//                                    // prima di lanciare l'evento di fine
//                                    try {
//                                        Thread.sleep(200);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    fireDismissListeners(new DismissEvent(bAnnulla, success, false));
//                                    generatorRunning = false;
//
//                                }
//                            });
//                        }
//                    });
//
//                    // avvia il generatore in un thread separato
//                    GeneratorThread thread = new GeneratorThread(generator);
//                    thread.start();
//                    generatorRunning = true;
//
//                    // disabilita i bottoni
//                    bAnnulla.setEnabled(false);
//                    bEsegui.setCaption("Interrompi");
//
//                } else {
//
//                    new ConfirmDialog("", "Sei sicuro?", new ConfirmDialog.Listener() {
//                        @Override
//                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
//                            if (confirmed) {
//                                generator.abort();
//                                bEsegui.setCaption("Esegui");
//                            }
//                        }
//                    }).show();
//
//                }
//
//            }
//
//        });

        layout.addComponent(progressBar);
        layout.addComponent(bAnnulla);
        layout.addComponent(bEsegui);

        return layout;
    }// end of method

//    /**
//     * Crea il wrapper dei dati per il motore di generazione
//     *
//     * @return il wrapper dei dati
//     */
//    private GeneratorData createGeneratorData() {
//        int action;
//        if (genera) {
//            action = GeneratorData.ACTION_CREATE;
//        } else {
//            action = GeneratorData.ACTION_DELETE;
//        }
//
//        GeneratorData data = new GeneratorData(getData1(), getData2(), action);
//
//        for (int giorno = 0; giorno < 7; giorno++) {
//            ArrayList<Servizio> servChecked = new ArrayList<>();
//            int row = 0;
//            for (Servizio serv : servizi) {
//                CheckBox check = matrix[row][giorno];
//                if (check.getValue()) {
//                    servChecked.add(serv);
//                }
//                row++;
//            }
//
//            DayOfWeek dow = DayOfWeek.of(giorno + 1);
//            data.putGiorno(dow, servChecked.toArray(new Servizio[0]));
//        }
//        return data;
//    }// end of method


//    private LocalDate getData1() {
//        return DateConvertUtils.asLocalDate(dateField1.getValue());
//    }// end of method
//
//    private LocalDate getData2() {
//        return DateConvertUtils.asLocalDate(dateField2.getValue());
//    }// end of method


    private class SwitchOnOffV extends VerticalLayout {
        public SwitchOnOffV(SwitchListener swListener) {

            setSizeUndefined();
            setSpacing(false);

            HorizontalLayout layOn = new HorizontalLayout();
            layOn.addStyleName("icon-green");
            layOn.addComponent(new Label(FontAwesome.CHECK_SQUARE_O.getHtml(), ContentMode.HTML));
            layOn.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOn();
            });

            HorizontalLayout layOff = new HorizontalLayout();
            layOff.addStyleName("icon-red");
            layOff.addComponent(new Label(FontAwesome.TIMES.getHtml(), ContentMode.HTML));
            layOff.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOff();
            });


            addComponent(layOn);
            addComponent(layOff);

        }// end of method
    }// end of inner class


    private class SwitchOnOffH extends HorizontalLayout {

        public SwitchOnOffH(SwitchListener swListener, FontAwesome iconOn, FontAwesome iconOff) {

            setSizeUndefined();
            setSpacing(true);

            HorizontalLayout layOn = new HorizontalLayout();
            layOn.addStyleName("icon-green");
            layOn.addComponent(new Label(iconOn.getHtml(), ContentMode.HTML));
            layOn.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOn();
            });

            HorizontalLayout layOff = new HorizontalLayout();
            layOff.addStyleName("icon-red");
            layOff.addComponent(new Label(iconOff.getHtml(), ContentMode.HTML));
            layOff.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOff();
            });

            addComponent(layOn);
            addComponent(layOff);

        }

        public SwitchOnOffH(SwitchListener swListener) {
            this(swListener, FontAwesome.CHECK_SQUARE_O, FontAwesome.TIMES);
        }// end of method
    }// end of inner class


    interface SwitchListener {
        void clickedOn();

        void clickedOff();
    }// end of inner interface

}// end of class
