
package view;

// General utilities

import controller.PreviewController;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import model.PreviewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

  ImagePanel imagePanel;

  public PreviewView(final Application application) {
    super(application);

    this.model(new PreviewModel(application));
    this.controller(new PreviewController(application));

    imagePanel = new ImagePanel(this.model().image());

    this.on("menu:new", this::handle);
  }

  private void handle(File f) {
    try {
      imagePanel.setImage(ImageIO.read(f));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
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
