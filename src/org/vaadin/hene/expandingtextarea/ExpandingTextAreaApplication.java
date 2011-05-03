package org.vaadin.hene.expandingtextarea;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class ExpandingTextAreaApplication extends Application {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        Window mainWindow = new Window("ExpandingTextArea Application");
        setMainWindow(mainWindow);

        mainWindow.addComponent(new Label(
                "Write text to the field and see how its height expands."));
        ExpandingTextArea expandingTextArea = new ExpandingTextArea();
        expandingTextArea.setMaxRows(10);
        expandingTextArea.setImmediate(true);
        expandingTextArea.setWidth("300px");
        mainWindow.addComponent(expandingTextArea);
    }

}
