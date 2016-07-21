package model;

import de.jaret.util.date.JaretDate;
import framework.Application;
import framework.Model;
import manager.Video;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import process.GenerateFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;


public final class ExportModel extends Model {
    public ExportModel(final Application application) {
        super(application);
    }

    public void process(String path) {
        System.out.println("Process");
        // TODO: handle end of media
        int max = 100;
        int timeByFrame = 30;
        ExecutorService pool = Executors.newFixedThreadPool(8);
        Queue<Future<BufferedImage>> frames = new ArrayDeque<>();
        for (int i = 0; i < max; i++) {
            JaretDate jd = new JaretDate(0, 0, 0, 0, 0, 0);
            jd.advanceMillis(i * timeByFrame);
            Callable<BufferedImage> gf = new GenerateFrame(jd);
            Future<BufferedImage> future = pool.submit(gf);
            frames.add(future);
        }
        // Set correct size
        final int fourCC = VideoWriter.fourcc('M', 'J', 'P', 'G');
        VideoWriter videoWriter = new VideoWriter(path, fourCC, 30, new Size(1280, 720), true);
        if (videoWriter.isOpened()) {
            System.out.println("Opened");
        } else {
            System.out.println("Closed");
        }

        System.out.println(frames.size());
        for (int i = 0; frames.size() != 0; i++) {
            Future<BufferedImage> future = frames.poll();
            try {
                BufferedImage bi = future.get();
                if (bi == null) {
                    System.err.println("Impossible to access " + i);
                    continue;
                }

                videoWriter.write(Video.bufferedImageToMat(bi));
                this.emit("media:exportProgress", i);
                System.out.println("Wrote frame " + i);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        this.emit("media:exportProgress", max);
        videoWriter.release();
        System.out.println("Released");
    }
}
