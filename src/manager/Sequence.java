package manager;

import filters.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Sequence implements Media {
    private Video video;
    private int startFrame;
    private int endFrame;

    public Sequence(Video video, int startFrame, int endFrame) {
        this.video = video;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    @Override
    public int getFrameFromMilliseconds(long time) {
        return video.getFrameFromMilliseconds(time);
    }

    @Override
    public Dimension getResolution() {
        return video.getResolution();
    }

    @Override
    public BufferedImage getImage(int frameNb) {
        /* Sequence is from 69 to 305, ask for 374, WTF */
        /*if (frameNb < endFrame - startFrame) {
            return video.getImage(startFrame + endFrame);
        }*/
        if (frameNb < endFrame - startFrame) {
            return video.getImage(startFrame + frameNb);
        }
        return null;
    }

    @Override
    public void addFilter(Filter filter) {
        video.addFilter(filter);
    }

    @Override
    public long getDuration() {
        return (long) ((endFrame - startFrame) * 1000.0 / video.getFps());
    }

    @Override
    public String getName() {
        return video.getName();
    }

    @Override
    public List<Filter> getFilters() {
        return video.getFilters();
    }

    @Override
    public void cleanFilters() {
        this.getFilters().clear();
    }

    @Override
    public Object clone() {
        Sequence s = null;
        try {
            s = (Sequence)super.clone();
            /* TODO Dangerous cast when we will change to media */
            s.video = (Video)s.video.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return s;
    }
}
