package org.vaadin.hene.expandingtextarea;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeEvent;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeListener;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class ExpandingTextAreaApplication extends Application {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		Window mainWindow = new Window("ExpandingTextArea Application");
		setMainWindow(mainWindow);

		mainWindow.addComponent(new Label(
				"Write text to the field and see how its height expands."));
		final ExpandingTextArea expandingTextArea = new ExpandingTextArea();
		expandingTextArea.setImmediate(true);
		expandingTextArea.setWidth("300px");
		mainWindow.addComponent(expandingTextArea);

		final Label rowsLabel = new Label("" + expandingTextArea.getRows());
		rowsLabel.setCaption("Rows");
		mainWindow.addComponent(rowsLabel);
		expandingTextArea.addListener(new RowsChangeListener() {
			public void rowsChange(RowsChangeEvent event) {
				rowsLabel.setValue(event.getRows());
			}
		});

		final TextField maxRowsTextField = new TextField("Max rows");
		maxRowsTextField.setInputPrompt("null");
		maxRowsTextField.setImmediate(true);
		maxRowsTextField.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				try {
					expandingTextArea.setMaxRows(Integer.parseInt(""
							+ maxRowsTextField.getValue()));
					maxRowsTextField.setComponentError(null);
				} catch (NumberFormatException e) {
					expandingTextArea.setMaxRows(null);
				}
			}
		});
		mainWindow.addComponent(maxRowsTextField);
	}

}
