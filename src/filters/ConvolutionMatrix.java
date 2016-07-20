package filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Adrien on 20/07/2016.
 */
public class ConvolutionMatrix implements Filter {
    float[] matrix;
    int length;
    int halfLen;
    int bias;

    public ConvolutionMatrix(float[] matrix) {
        this(matrix, 0);
    }

    public ConvolutionMatrix(float[] matrix, int bias) {
        this.matrix = matrix;
        this.bias = bias;
        length = (int) Math.sqrt(matrix.length);
        halfLen = length / 2;
    }

    @Override
    public BufferedImage applyFilter(BufferedImage image) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (j == 0 || i == 0 || j == image.getHeight() - 1 || i == image.getWidth() - 1)
                    res.setRGB(i, j, image.getRGB(i, j));
                else {
                    float r = 0;
                    float g = 0;
                    float b = 0;
                    float a = 0;
                    for (int k = 0; k < length; k++) {
                        for (int l = 0; l < length; l++) {
                            Color c = new Color(image.getRGB(i + k - halfLen, j + l - halfLen), image.getType() == BufferedImage.TYPE_INT_ARGB);
                            r += c.getRed() * matrix[k * length + l];
                            b += c.getBlue() * matrix[k * length + l];
                            g += c.getGreen() * matrix[k * length + l];
                            a += c.getAlpha() * matrix[k * length + l];
                        }
                    }
                    r = Math.max(Math.min(r + bias, 255), 0);
                    g = Math.max(Math.min(g + bias, 255), 0);
                    b = Math.max(Math.min(b + bias, 255), 0);
                    a = Math.max(Math.min(a + bias, 255), 0);
                    res.setRGB(i, j, new Color((int) r, (int) g, (int) b, (int) a).getRGB());
                }
            }
        }
        return res;
    }
}
