package filters;

import manager.Video;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 20/07/2016.
 */
public class OtsuBinarize implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        Mat img = Video.bufferedImageToMat(image);
        Mat grayImg = new Mat(img.rows(), img.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);
        Mat dst = new Mat();
        Imgproc.threshold(grayImg, dst, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        return Video.Mat2bufferedImage(dst);
    }
}
