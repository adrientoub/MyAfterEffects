
package view;

// General utilities

import controller.PreviewController;
import de.jaret.util.date.JaretDate;
import de.jaret.util.misc.Pair;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import manager.Media;
import manager.Video;
import model.PreviewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

import static model.TimelineModel.getMediasAtDate;

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

    this.on("media:new", this::handle);
    this.on("marker:changed", this::handleMarker);
    //this.on("timeline:selection", this::handle);
  }

    private void handle(Media m) {
        imagePanel.setImage(m.getImage(0));
        imagePanel.repaint();
    }

    private void handleMarker(JaretDate markerDate) {
      ArrayList<Pair<Long, Media>> pairs = getMediasAtDate(markerDate);
      /* TODO FIXME */
    }

  /**
   * Render the {@link PreviewView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    imagePanel.setContainingPanel(viewPanel);
    viewPanel.add(imagePanel);

    return viewPanel;
  }
}
