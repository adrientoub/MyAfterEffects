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
    public JaretDate date;

    public GenerateFrameAsMat(JaretDate date, int width, int height) {
        this.date = date;
    }

    @Override
    public Mat call() throws Exception {
        BufferedImage bi = new GenerateFrame(date).call();
        if (bi == null)
            return null;
        Mat mat = Video.bufferedImageToMat(bi);
        return mat;
    }
}
