package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 18/07/2016.
 */
public class Grayscale implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color original = new Color(image.getRGB(i, j));
                int gray = (int) (original.getRed() * 0.2126 + original.getGreen() * 0.7152 + original.getBlue() * 0.0722);
                Color newColor = new Color(gray, gray, gray);
                bi.setRGB(i, j, newColor.getRGB());
            }
        }
        return null;
    }
}
