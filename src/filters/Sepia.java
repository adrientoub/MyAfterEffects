package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Sepia implements Filter {
    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j), image.getType() == BufferedImage.TYPE_INT_ARGB);
                int red = (int) (c.getRed() * .393 + c.getGreen() * .769 + c.getBlue() * .189);
                int green = (int) (c.getRed() * .349 + c.getGreen() * .686 + c.getBlue() * .168);
                int blue = (int) (c.getRed() * .272 + c.getGreen() * .534 + c.getBlue() * .131);
                red = Math.max(Math.min(red, 255), 0);
                green = Math.max(Math.min(green, 255), 0);
                blue = Math.max(Math.min(blue, 255), 0);
                res.setRGB(i, j, new Color(red, green, blue, c.getAlpha()).getRGB());
            }
        }
        return res;
    }
}
