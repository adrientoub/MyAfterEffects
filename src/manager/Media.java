package manager;

import filters.Filter;
import timeline.model.EventInterval;

import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Adrien on 19/07/2016.
 */
public interface Media {
    BufferedImage getImage(int frameNb);
    void addFilter(Filter filter);
    Time getDuration();
    String getName();
}
