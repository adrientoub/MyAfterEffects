package manager;

import de.jaret.util.date.Interval;
import filters.Filter;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import timeline.model.EventInterval;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.videoio.Videoio.*;

/**
 * Created by Damien on 17/07/2016.
 */
public class Video implements Media {
    private List<Filter> filters;
    private double fps;
    private int nbFrames;
    private Time duration;
    private VideoCapture videoCapture;
    private File file;
    private ArrayList<EventInterval> intervals;

    private static BufferedImage Mat2bufferedImage(Mat image) {
        int bufferSize = image.channels() * image.cols() * image.rows();
        byte [] bytes = new byte[bufferSize];
        image.get(0, 0, bytes);
        BufferedImage img = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);
        return img;
    }

    public Video(File f) {
        filters = new ArrayList<>();
        file = f;
        videoCapture = new VideoCapture(f.getAbsolutePath());
        double width = videoCapture.get(CAP_PROP_FRAME_WIDTH);
        double height = videoCapture.get(CAP_PROP_FRAME_HEIGHT);
        fps = videoCapture.get(CAP_PROP_FPS);
        double frameCount = videoCapture.get(CAP_PROP_FRAME_COUNT);
        nbFrames = (int)frameCount;

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("FPS: " + fps);
        System.out.println("Frame count: " + frameCount);
        if (fps > 0)
            duration = new Time((long) (frameCount / fps));
        else
            duration = new Time(nbFrames);

        setIntervals(null);
    }

    public BufferedImage getImage(int frameNb) {
        ImageIO.setUseCache(false);
        Mat frame = new Mat();
        videoCapture.set(CAP_PROP_POS_FRAMES, frameNb);

        if (videoCapture.read(frame)) {
            BufferedImage image = Mat2bufferedImage(frame);
            for (Filter filter: filters) {
                image = filter.applyFilter(image);
            }
            return image;
        } else {
            System.err.println("Impossible to read frame " + frameNb + " of video " + getName());
        }
        return null;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public int getNbFrames() {
        return nbFrames;
    }

    public void setNbFrames(int nbFrames) {
        this.nbFrames = nbFrames;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getName() {
        return file.getName();
    }

    /* A video can be split in several intervals */
    public void setIntervals(ArrayList<EventInterval> intervals) {
        /* No intervals, mean video is new in the software */
        if (intervals == null)
            this.intervals = new ArrayList<>();
        else
            this.intervals = intervals;
    }

    public ArrayList<EventInterval> getIntervals() {
        return intervals;
    }
}
