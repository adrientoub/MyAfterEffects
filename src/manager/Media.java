package manager;

import filters.Filter;

import java.awt.image.BufferedImage;
import java.sql.Time;

/**
 * Created by Adrien on 19/07/2016.
 */
public interface Media {
    BufferedImage getImage(int frameNb);
    void addFilter(Filter filter);
    Time getDuration();
    String getName();
}
