package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 05/10/2016.
 */
public class QuantizeColors implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        final int a = 12;
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color original = new Color(image.getRGB(i, j));

                Color newColor = new Color((original.getRed() / a) * a, (original.getGreen() / a) * a, (original.getBlue() / a) * a);
                bi.setRGB(i, j, newColor.getRGB());
            }
        }
        return bi;
    }

}
