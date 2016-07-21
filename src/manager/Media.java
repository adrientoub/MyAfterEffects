package manager;

import filters.Filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 19/07/2016.
 */
public interface Media extends Cloneable {
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
     * Add a filter to the Media
     * @return Filters of this Media
     */
     List<Filter> getFilters();

    /**
     * Remove all filters to the Media
     */
    void cleanFilters();

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

    Object clone();
}
