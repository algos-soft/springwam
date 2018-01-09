package it.algos.springvaadin.toolbar;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class ADialogToolbar extends HorizontalLayout {

	public ADialogToolbar() {
		super();

		addStyleName("toolbar");
		setMargin(true);
		setSpacing(true);

		addComponents();

	}

	/**
	 * Add the components to this layout
	 */
	protected void addComponents() {
		Label spacer = new Label();
		addComponent(spacer);
		this.setExpandRatio(spacer, 1f);
	}

}
