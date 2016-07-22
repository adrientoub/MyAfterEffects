package model;

import app.MyAfterEffectsApp;
import de.jaret.util.date.JaretDate;
import framework.Application;
import framework.Model;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoWriter;
import process.GenerateFrameAsMat;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;

public final class ExportModel extends Model {
    public ExportModel(final Application application) {
        super(application);
    }

    public void process(String path) {
        System.out.println("Process");
        long start = System.nanoTime();

        // Options:
        int max = ((MyAfterEffectsApp) application()).getOptionView().getFrames();
        double fps = ((MyAfterEffectsApp) application()).getOptionView().getFps();
        int timeByFrame = (int) (1000 / fps);
        int threads = ((MyAfterEffectsApp) application()).getOptionView().getThreads();
        int width = ((MyAfterEffectsApp) application()).getOptionView().getOutputWidth();
        int height = ((MyAfterEffectsApp) application()).getOptionView().getOutputHeight();

        System.out.println("Exporting " + max + " frames using " + threads + " threads.");
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        Queue<Future<Mat>> frames = new ArrayDeque<>();
        for (int i = 0; i < max; i++) {
            JaretDate jd = new JaretDate(0, 0, 0, 0, 0, 0);
            jd.advanceMillis(i * timeByFrame);
            Callable<Mat> gf = new GenerateFrameAsMat(jd, width, height);
            Future<Mat> future = pool.submit(gf);
            frames.add(future);
        }
        System.out.println("Queued in " + (System.nanoTime() - start) / 1000000 + "ms");
        // Set correct size
        // Use MJPEG because it works
        final int fourCC = VideoWriter.fourcc('M', 'J', 'P', 'G');
        VideoWriter videoWriter = new VideoWriter(path, fourCC, fps, new Size(width, height), true);
        if (videoWriter.isOpened()) {
            System.out.println("Opened");
        } else {
            System.err.println("Closed");
        }
        System.out.println("Created video in " + (System.nanoTime() - start) / 1000000 + "ms");

        System.out.println(frames.size());
        long frameTime = System.nanoTime();
        long transcodeStart = System.nanoTime();
        for (int i = 0; frames.size() != 0; i++) {
            Future<Mat> future = frames.poll();
            try {
                Mat mat = future.get();
                if (mat == null) {
                    System.err.println("Impossible to access " + i);
                    continue;
                }

                videoWriter.write(mat);
                System.out.println("Wrote frame " + i + " in " + (System.nanoTime() - frameTime) / 1000000 + "ms");
                this.emit("media:exportProgress", i);
                frameTime = System.nanoTime();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        this.emit("media:exportProgress", max);
        videoWriter.release();
        System.out.println("Exported " + max + " frames using " + threads + " threads.");
        System.out.println("Exported in " + (System.nanoTime() - start) / 1000000 + "ms (" + ((System.nanoTime() - transcodeStart) / 1000000) / max + "ms per frame).");
        pool.shutdown();
    }
}
