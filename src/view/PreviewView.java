
package view;

// General utilities

import controller.PreviewController;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import manager.Video;
import model.PreviewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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

  ImagePanel imagePanel;

  public PreviewView(final Application application) {
    super(application);

    this.model(new PreviewModel(application));
    this.controller(new PreviewController(application));

    imagePanel = new ImagePanel(this.model().image());

    this.on("video:new", this::handle);
  }

  private void handle(Video v) {
    imagePanel.setImage(v.getImage(250));
    imagePanel.repaint();
  }

  /**
   * Render the {@link PreviewView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    viewPanel.add(imagePanel);

    return viewPanel;
  }
}
