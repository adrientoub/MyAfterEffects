package model;

import framework.Application;
import framework.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public final class ExportModel extends Model {
    BufferedImage image;

    public ExportModel(final Application application) {
        super(application);
    }

    public void process() {
        System.out.println("Process");
    }
}
