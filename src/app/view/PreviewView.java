/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.view;

// General utilities

import app.controller.PreviewController;
import app.framework.Application;
import app.framework.View;
import app.model.PreviewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// AWT utilities
// Swing utilities
// Swing borders
// Framework
// Models
// Controllers

/**
 * The {@link PreviewView} class takes care of rendering the view for creating,
 * displaying, and completing todo items.
 */
public final class PreviewView extends View<PreviewModel, PreviewController> {

  public PreviewView(final Application application) {
    super(application);

    this.model(new PreviewModel(application));
    this.controller(new PreviewController(application));
  }

  /**
   * Render the {@link PreviewView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JLabel picLabel = new JLabel(new ImageIcon(this.model().image()));
    viewPanel.add(picLabel);

    return viewPanel;
  }
}
