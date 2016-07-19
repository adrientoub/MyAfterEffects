package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 19/07/2016.
 */
public class ChromaKey implements Filter {
    private Color color;
    final private int threshold = 3;
    final private int transparentRgba;

    ChromaKey(Color color) {
        this.color = color;
        Color transparent = new Color(255, 255, 255, 255);
        transparentRgba = transparent.getRGB();
    }

    private boolean isInRange(Color original) {
        return Math.abs(original.getGreen() - color.getGreen()) < threshold &&
                Math.abs(original.getRed() - color.getRed()) < threshold &&
                Math.abs(original.getBlue() - color.getBlue()) < threshold;
    }

    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                Color original = new Color(rgb);
                if (isInRange(original)) {
                    bi.setRGB(i, j, transparentRgba);
                } else {
                    bi.setRGB(i, j, rgb);
                }
            }
        }
        return bi;
    }
}
