package view;

import controller.TabbedController;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import model.TabbedModel;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

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
