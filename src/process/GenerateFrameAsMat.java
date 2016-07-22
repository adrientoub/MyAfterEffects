package process;

import de.jaret.util.date.JaretDate;
import de.jaret.util.misc.Pair;
import manager.Media;
import manager.Video;
import model.TimelineModel;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Adrien on 20/07/2016.
 */
public class GenerateFrameAsMat implements Callable<Mat> {
    private JaretDate date;
    private int width;
    private int height;

    public GenerateFrameAsMat(JaretDate date, int width, int height) {
        this.date = date;
        this.width = width;
        this.height = height;
    }

    @Override
    public Mat call() throws Exception {
        BufferedImage bi = new GenerateFrame(date).call();
        if (bi == null)
            return null;
        if (bi.getWidth() != width || bi.getHeight() != height) {
            Image tmp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage scaledBi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics graphics = scaledBi.createGraphics();
            graphics.drawImage(tmp, 0, 0, null);
            graphics.dispose();

            bi = scaledBi;
        }
        return Video.bufferedImageToMat(bi);
    }
}
