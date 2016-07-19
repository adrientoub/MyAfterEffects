package manager;

import filters.Filter;

import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 19/07/2016.
 */
public interface Media {
    BufferedImage getImage(int frameNb);
    void addFilter(Filter filter);
    long getDuration();
    String getName();
}
