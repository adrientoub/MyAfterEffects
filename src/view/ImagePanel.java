package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private final int width;
    private final int height;
    private BufferedImage image;

    /**
     * Create the ImagePanel
     *
     * @param width  the width of the image
     * @param height the height of the image
     */
    public ImagePanel(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    /**
     * Create the ImagePanel
     *
     * @param image: image to display
     */
    public ImagePanel(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width, height));
    }

    /**
     * Create the ImagePanel
     *
     * @param file: image to display
     */
    public ImagePanel(File file) {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
