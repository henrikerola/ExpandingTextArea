package org.vaadin.hene.expandingtextarea.widgetset.client.ui;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

public interface ExpandingTextAreaServerRpc extends ServerRpc {
	
	void setRows(int rows);

}
