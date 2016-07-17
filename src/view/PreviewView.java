
package view;

// General utilities

import controller.PreviewController;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import model.PreviewModel;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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



  public static BufferedImage toBufferedImage(Picture src) {
    if (src.getColor() != ColorSpace.RGB) {
      Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
      Picture rgb = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB, src.getCrop());
      transform.transform(src, rgb);
      src = rgb;
    }

    BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
            BufferedImage.TYPE_3BYTE_BGR);

    toBufferedImage(src, dst);

    return dst;
  }

  public static void toBufferedImage(Picture src, BufferedImage dst) {
    byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
    int[] srcData = src.getPlaneData(0);
    for (int i = 0; i < data.length; i++) {
      data[i] = (byte) srcData[i];
    }
  }

  private void handle(File f) {
    int frameNumber = 150;
    try {
      Picture picture = FrameGrab.getNativeFrame(f, frameNumber);
      BufferedImage bufferedImage = toBufferedImage(picture);
      imagePanel.setImage(bufferedImage);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JCodecException e) {
      e.printStackTrace();
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
