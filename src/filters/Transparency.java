package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 18/07/2016.
 */
public class Transparency implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color original = new Color(image.getRGB(i, j));
                Color newColor = new Color(original.getRed(), original.getBlue(), original.getGreen(), 127);
                bi.setRGB(i, j, newColor.getRGB());
            }
        }
        return bi;
    }
}
