package framework;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private int width;
    private int height;
    private BufferedImage image;
    private JPanel containingPanel;

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

    public void setContainingPanel(JPanel containingPanel) {
        this.containingPanel = containingPanel;
    }

    private Dimension getScaledSize() {
        double ws = containingPanel.getWidth();
        double hs = containingPanel.getHeight();
        double rs = ws / hs;
        double ri = width / (double) height;
        if (rs > ri) {
            return new Dimension((int) (width * hs) / height, (int) hs);
        }
        else {
            return new Dimension((int) ws, (int) (height * ws) / width);
        }
    }

    @Override
    public int getWidth() {
        if (containingPanel == null) {
            return width;
        }

        return (int) getScaledSize().getWidth();
    }

    @Override
    public int getHeight() {
        if (containingPanel == null) {
            return height;
        }

        return (int) getScaledSize().getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        setPreferredSize(new Dimension(width, height));
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
