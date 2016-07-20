package model;

import framework.Application;
import framework.Model;
import manager.Image;
import manager.Media;
import manager.Video;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public final class MenuModel extends Model {
    public MenuModel(final Application application) {
        super(application);
    }

    public static String getExtension(File file) {
        String[] splited = file.getName().split("\\.");
        return splited[splited.length - 1].toLowerCase();
    }

    public void newFile() throws IllegalArgumentException, IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showOpenDialog(null);
        File selected = jFileChooser.getSelectedFile();
        if (selected == null)
            throw new IllegalArgumentException("User didn't select any file.");
        else {
            String extension = getExtension(selected);
            Media m;
            if (extension.equals("jpg") || extension.equals("png") || extension.equals("bmp") || extension.equals("jpeg") || extension.equals("gif"))
                m = new Image(selected);
            else
                m = new Video(selected);
            this.emit("media:new", m);
        }
    }

    public void exportVideo() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showSaveDialog(null);
        File selected = jFileChooser.getSelectedFile();
        if (selected != null) {
            String path = selected.getAbsolutePath();
            String extension = getExtension(selected);
            if (!extension.equals("mp4"))
                path += ".mp4";

            this.emit("media:export", path);
        }
    }

    public void exit(int i) {
        System.exit(i);
    }
}
