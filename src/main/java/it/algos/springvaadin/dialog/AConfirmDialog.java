package it.algos.springvaadin.dialog;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class AConfirmDialog extends ADialog {

	private Listener listener;
	//private ConfirmListener confirmListener;
	private ArrayList<ConfirmListener> confirmListeners=new ArrayList<>();

	private Button confirmButton;
	private Button cancelButton;

	public AConfirmDialog(Listener closeListener) {
		this(null, null, closeListener);
	}// end of constructor

	public AConfirmDialog(String title, String message, Listener closeListener) {
		super(title, message);
		setCloseListener(closeListener);
		init();
	}// end of constructor

	private void init() {
		cancelButton = new Button("Annulla");
		cancelButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				onCancel();
			}
		});
		getToolbar().addComponent(cancelButton);
		getToolbar().setComponentAlignment(cancelButton, com.vaadin.ui.Alignment.MIDDLE_CENTER);

		confirmButton = new Button("Conferma");
		confirmButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
//		confirmButton.addStyleName(Reindeer.BUTTON_DEFAULT);//@todo controllare
		confirmButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				onConfirm();
			}
		});
		getToolbar().addComponent(confirmButton);
		getToolbar().setComponentAlignment(confirmButton, com.vaadin.ui.Alignment.MIDDLE_CENTER);
	}// end of method

	public void setConfirmButtonText(String text) {
		getConfirmButton().setCaption(text);
	}// end of method

	public void setCancelButtonText(String text) {
		getCancelButton().setCaption(text);
	}// end of method

	public Button getConfirmButton() {
		return confirmButton;
	}// end of method

	public Button getCancelButton() {
		return cancelButton;
	}// end of method

	protected void onCancel() {
		if (listener != null) {
			listener.onClose(this, false);
		}
		close();
	}// end of method

	protected void onConfirm() {
		if (listener != null) {
			listener.onClose(this, true);
		}

        for(ConfirmListener l : confirmListeners){
            l.confirmed(this);
        }

		close();
	}// end of method

	public void setCloseListener(Listener listener) {
		this.listener = listener;
	}// end of method

	public interface Listener {
		public void onClose(AConfirmDialog dialog, boolean confirmed);
	}// end of method

	public void setConfirmListener(ConfirmListener l) {
        confirmListeners.clear();
        addConfirmListener(l);
	}// end of method


    public void addConfirmListener(ConfirmListener l) {
        confirmListeners.add(l);
    }// end of method


    public interface ConfirmListener {
		public void confirmed(AConfirmDialog dialog);
	}// end of method

}// end of class
