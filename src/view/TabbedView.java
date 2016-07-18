package view;

import controller.TabbedController;
import framework.Application;
import framework.View;
import model.TabbedModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public final class TabbedView extends View<TabbedModel, TabbedController> {
    private JTabbedPane tab;

    public TabbedView(final Application application) {
        super(application);
        tab = new JTabbedPane();

        this.model(new TabbedModel(application));
        this.controller(new TabbedController(application));

        this.on("menu:new", this::handle);
    }

    private void handle(File f) {
    }

    public JPanel render() {
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(tab);

        return viewPanel;
    }
}
