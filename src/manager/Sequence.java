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
    private Media media;
    private int startFrame;
    private int endFrame;

    public Sequence(Media media, int startFrame, int endFrame) {
        this.media = media;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    @Override
    public int getFrameFromMilliseconds(long time) {
        return media.getFrameFromMilliseconds(time);
    }

    @Override
    public Dimension getResolution() {
        return media.getResolution();
    }

    @Override
    public BufferedImage getImage(int frameNb) {
        if (frameNb < endFrame - startFrame) {
            return media.getImage(startFrame + frameNb);
        }
        return null;
    }

    @Override
    public void addFilter(Filter filter) {
        media.addFilter(filter);
    }

    @Override
    public long getDuration() {
        return (long) ((endFrame - startFrame) * 1000.0 / media.getFps());
    }


    @Override
    public String getName() {
        return media.getName();
    }

    @Override
    public List<Filter> getFilters() {
        return media.getFilters();
    }

    @Override
    public double getFps() {
        return media.getFps();
    }

    @Override
    public void cleanFilters() {
        this.getFilters().clear();
    }

    @Override
    public Object clone() {
        Sequence s = null;
        try {
            s = (Sequence) super.clone();
            s.media = (Media) s.media.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return s;
    }
}
