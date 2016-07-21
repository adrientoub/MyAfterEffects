package manager;

import filters.Filter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 19/07/2016.
 */
public class Image implements Media {
    private BufferedImage image;
    private List<Filter> filters;
    private long duration = 5000;
    private String name;
    private File file;

    public Image(File file) throws IOException {
        image = ImageIO.read(file);
        name = file.getName();
        filters = new ArrayList<>();
    }

    public Image(BufferedImage image, String name) {
        this.name = name;
        this.image = image;
        filters = new ArrayList<>();
    }

    public static BufferedImage deepCopyBufferedImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int[] buffer = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), buffer, 0, image.getWidth());
        copy.setRGB(0, 0, image.getWidth(), image.getHeight(), buffer, 0, image.getWidth());
        return copy;
    }

    @Override
    public int getFrameFromMilliseconds(long time) {
        return 0;
    }

    @Override
    public Dimension getResolution() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public BufferedImage getImage(int frameNb) {
        BufferedImage copy = deepCopyBufferedImage(image);
        for (Filter filter: filters) {
            copy = filter.applyFilter(copy);
        }
        return copy;
    }

    @Override
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    @Override
    public double getFps() {
        return 30;
    }

    @Override
    public void cleanFilters() {
        getFilters().clear();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
