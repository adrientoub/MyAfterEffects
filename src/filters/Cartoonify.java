package filters;

import manager.Video;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 30/09/2016.
 */
public class Cartoonify implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        Mat src = Video.bufferedImageToMat(image);
        // Edges contains the edges found by Canny
        Mat edges = new Mat();
        double threshold = 75;
        Imgproc.Canny(src, edges, threshold, 3 * threshold);

        Mat bileteraled = new Mat();
        Imgproc.bilateralFilter(src, bileteraled, 15, 80, 80);

        Mat mask = new Mat();
        src.copyTo(mask, edges);
        Mat dilated = new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3));
        Imgproc.dilate(mask, dilated, element);

        Mat black = new Mat(bileteraled.rows(), bileteraled.cols(), bileteraled.type());

        Mat out = new Mat();
        bileteraled.copyTo(out);
        black.copyTo(out, dilated);

        return Video.Mat2bufferedImage(out);
    }
}
