package manager;

import filters.Filter;

import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.List;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Sequence implements Media {
    private Video video;
    private int startFrame;
    private int endFrame;
    private List<Filter> filters;

    public Sequence(Video video, int startFrame, int endFrame) {
        this.video = video;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    @Override
    public BufferedImage getImage(int frameNb) {
        if (frameNb < endFrame - startFrame) {
            return video.getImage(startFrame + endFrame);
        }
        return null;
    }

    @Override
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    @Override
    public long getDuration() {
        return (long) ((endFrame - startFrame) * 1000.0 / video.getFps());
    }

    @Override
    public String getName() {
        return video.getName();
    }
}
