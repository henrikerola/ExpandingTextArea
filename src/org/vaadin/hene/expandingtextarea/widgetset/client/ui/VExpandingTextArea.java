package org.vaadin.hene.expandingtextarea.widgetset.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.VTextArea;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VExpandingTextArea extends VTextArea {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-expandingtextarea";

	private static int REPEAT_INTERVAL = 400;

	private HeightObserver heightObserver;

	public VExpandingTextArea() {
		setStyleName(CLASSNAME);
		sinkEvents(Event.ONFOCUS | Event.ONFOCUS);

		heightObserver = new HeightObserver();
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (event.getTypeInt() == Event.ONFOCUS) {
			heightObserver.scheduleRepeating(REPEAT_INTERVAL);
		} else if (event.getTypeInt() == Event.ONBLUR) {
			heightObserver.cancel();
		}
	}

	private void checkHeight() {
		ApplicationConnection.getConsole().log("checkHeight()");
		int origRows = getRows(getElement());
		// Check if we have to increase textarea's height
		int rows = origRows;
		while (getElement().getScrollHeight() > getOffsetHeight()) {
			setRows(++rows);
		}

		// Check if we can reduce textarea's height
		rows = getRows(getElement());
		while (rows > 1) {
			setRows(rows - 1);
			if (!(getElement().getScrollHeight() > getOffsetHeight())) {
				rows -= 1;
				continue;
			} else {
				setRows(rows);
				break;
			}
		}

		setRows(getRows(getElement()) + 1);
		if (origRows != getRows(getElement())) {
			Util.notifyParentOfSizeChange(this, false);
			client.updateVariable(id, "rows", getRows(getElement()), false);
		}
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		addStyleName(VTextArea.CLASSNAME);

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				checkHeight();
			}
		});
	}

	private native int getRows(Element e)
	/*-{
		try {
			return e.rows;
		} catch (e) {
			return -1;
		}
	}-*/;

	private class HeightObserver extends Timer {

		@Override
		public void run() {
			checkHeight();
		}
	}
}
