package org.vaadin.hene.expandingtextarea.demo;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeEvent;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea.RowsChangeListener;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;

@Title("ExpandingTextArea")
@Theme("valo")
@PreserveOnRefresh
public class ExpandingTextAreaUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		setContent(content);

		Label title = new Label("ExpandingTextArea");
		title.addStyleName(ValoTheme.LABEL_HUGE);
		content.addComponent(title);
		
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

		final CheckBox appendExtraRow = new CheckBox("Append extra row", expandingTextArea.isAppendExtraRow());
		appendExtraRow.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				expandingTextArea.setAppendExtraRow(appendExtraRow.getValue());
			}
		});
		content.addComponent(appendExtraRow);

		final NativeSelect maxRows = new NativeSelect("Max rows");
		maxRows.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				expandingTextArea.setMaxRows((Integer) maxRows.getValue());
			}
		});
		maxRows.addItem(2);
		maxRows.addItem(5);
		maxRows.addItem(10);
		content.addComponent(maxRows);
	}

}
