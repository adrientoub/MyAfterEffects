
package model;

import framework.Application;
import framework.Model;
import manager.Media;
import manager.Video;

import javax.swing.*;
import java.io.File;

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
        Media m = new Video(selected);
        this.emit("media:new", m);
    }
  }

  public void exit(int i) {
      System.exit(i);
  }
}
