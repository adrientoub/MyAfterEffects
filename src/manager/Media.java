package manager;

import filters.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 19/07/2016.
 */
public interface Media {
    int getFrameFromMilliseconds(long time);

    /**
     * Returns the media resolution
     * @return The resolution in pixel
     */
    Dimension getResolution();

    /**
     * Returns the image at the selected frame.
     * @param frameNb The frame number
     * @return A filtered image
     */
    BufferedImage getImage(int frameNb);

    /**
     * Add a filter to the Media
     * @param filter the filter to add.
     */
    void addFilter(Filter filter);

    /**
     * Get the duration of the Media on the Timeline
     * @return the duration in milliseconds
     */
    long getDuration();

    /**
     * Get the name of the Media
     * @return the name of the Media
     */
    String getName();
}
