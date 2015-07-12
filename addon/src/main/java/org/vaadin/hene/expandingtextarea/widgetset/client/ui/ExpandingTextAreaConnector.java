package org.vaadin.hene.expandingtextarea.widgetset.client.ui;

import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.vaadin.hene.expandingtextarea.widgetset.client.ui.VExpandingTextArea.HeightChangedListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.VTextArea;
import com.vaadin.client.ui.textarea.TextAreaConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ExpandingTextArea.class)
public class ExpandingTextAreaConnector extends TextAreaConnector implements HeightChangedListener {

	private ExpandingTextAreaServerRpc rpc = RpcProxy.create(ExpandingTextAreaServerRpc.class, this);

    private boolean sendRowsToServerWhenEnabled = false;

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("maxRows")) {
            getWidget().setMaxRows(uidl.getIntAttribute("maxRows"));
        } else {
        	getWidget().setMaxRows(null);
        }

        getWidget().addStyleName(VTextArea.CLASSNAME);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        	public void execute() {
        		getWidget().checkHeight();
        	}
        });

        if (sendRowsToServerWhenEnabled && isEnabled()) {
            rpc.setRows(getWidget().getRows());
            sendRowsToServerWhenEnabled = false;
        }
    }

    @Override
    protected VExpandingTextArea createWidget() {
    	VExpandingTextArea widget = GWT.create(VExpandingTextArea.class);
    	widget.addHeightChangedListener(this);
        return widget;
    }

    @Override
    public VExpandingTextArea getWidget() {
        return (VExpandingTextArea) super.getWidget();
    }

	public void heightChanged(int newHeight) {
        // Vaadin doesn't allow requests from disabled components.
        // If the component is disabled, set a flag to true so that row count
        // is transferred when component is enabled (see updateFromUIDL).
        if (isEnabled()) {
            rpc.setRows(newHeight);
        } else {
            sendRowsToServerWhenEnabled = true;
        }
		getLayoutManager().setNeedsMeasure(this);
	}

}
