
package model;

import framework.Application;
import framework.Model;
import framework.Timeline;
import manager.Video;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.omg.CORBA.UserException;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

// Framework

public final class MenuModel extends Model {

  public MenuModel(final Application application) {
    super(application);
  }

  public void newFile() throws IllegalArgumentException {
    JFileChooser jFileChooser = new JFileChooser();
    jFileChooser.showOpenDialog(null);
    File selected = jFileChooser.getSelectedFile();
    if (selected == null)
      throw new IllegalArgumentException("User didn't select any file.");
    else {
        Video v = new Video(selected);
        this.emit("video:new", v);
    }
  }

  public void exit(int i) {
      System.exit(i);
  }
}
