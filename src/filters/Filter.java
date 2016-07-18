package filters;

import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 18/07/2016.
 */
public interface Filter {
    BufferedImage applyFilter(final BufferedImage image);
}
