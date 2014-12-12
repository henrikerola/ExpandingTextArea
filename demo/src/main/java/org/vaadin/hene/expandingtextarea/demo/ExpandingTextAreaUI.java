package org.vaadin.hene.expandingtextarea.demo;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeEvent;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeListener;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Title("ExpandinTextArea")
public class ExpandingTextAreaUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout content = new VerticalLayout();
		setContent(content);
		
		content.addComponent(new Label(
				"Write text to the field and see how its height expands."));
		final ExpandingTextArea expandingTextArea = new ExpandingTextArea();
		expandingTextArea.setImmediate(true);
		expandingTextArea.setWidth("300px");
		//expandingTextArea.setValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sed imperdiet magna. Sed cursus nunc ac dui hendrerit sed auctor diam pellentesque. Etiam hendrerit lobortis lectus vitae interdum. Nam non elementum sem. Aenean feugiat metus nec libero ullamcorper pharetra. Suspendisse varius dolor quis magna egestas id mollis nisi ullamcorper. Fusce a interdum neque. Pellentesque et nisi nisi. In hendrerit nisl ut odio mattis ut pulvinar ligula porta.");
		content.addComponent(expandingTextArea);

		final Label rowsLabel = new Label("" + expandingTextArea.getRows());
		rowsLabel.setCaption("Rows");
		content.addComponent(rowsLabel);
		expandingTextArea.addRowsChangeListener(new RowsChangeListener() {
			public void rowsChange(RowsChangeEvent event) {
				rowsLabel.setValue("" + event.getRows());
			}
		});

//		final TextField maxRowsTextField = new TextField("Max rows");
//		maxRowsTextField.setInputPrompt("null");
//		maxRowsTextField.setImmediate(true);
//		maxRowsTextField.addListener(new ValueChangeListener() {
//			public void valueChange(ValueChangeEvent event) {
//				try {
//					expandingTextArea.setMaxRows(Integer.parseInt(""
//							+ maxRowsTextField.getValue()));
//					maxRowsTextField.setComponentError(null);
//				} catch (NumberFormatException e) {
//					expandingTextArea.setMaxRows(null);
//				}
//			}
//		});
//		content.addComponent(maxRowsTextField);
	}

}