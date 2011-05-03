package org.vaadin.hene.expandingtextarea.widgetset.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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

    private int maxRows = 100;

    private static int REPEAT_INTERVAL = 400;

    private final HeightObserver heightObserver;

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
        int origRows = getRows(getElement());

        // Check if we have to increase textarea's height
        int rows = origRows;
        while (++rows <= maxRows
                && getElement().getScrollHeight() > getOffsetHeight()) {
            setRows(rows);
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

        int updatedRowCount = getRows(getElement()) + 1;
        // Add stylename if we have reached maximum row number, so we can show a
        // scroll bar
        if (updatedRowCount > maxRows) {
            addStyleName("max");
        } else {
            removeStyleName("max");
        }

        updatedRowCount = updatedRowCount > maxRows ? maxRows : updatedRowCount;

        setRows(updatedRowCount);

        if (origRows != getRows(getElement())) {
            Util.notifyParentOfSizeChange(this, false);
            client.updateVariable(id, "rows", getRows(getElement()), false);
        }
    }

    /**
     * Called whenever an update is received from the server
     */
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasVariable("maxRows")) {
            setMaxRows(uidl.getIntVariable("maxRows"));
        }

        addStyleName(VTextArea.CLASSNAME);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                checkHeight();
            }
        });
    }

    private void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
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
