package manager;

import filters.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 19/07/2016.
 */
public class Image implements Media {
    private BufferedImage image;
    private List<Filter> filters;
    private Time duration;
    private String name;

    public Image(File file) throws IOException {
        image = ImageIO.read(file);
        name = file.getName();
        filters = new ArrayList<>();
        duration = new Time(5);
    }

    public Image(BufferedImage image, String name) {
        this.name = name;
        this.image = image;
        filters = new ArrayList<>();
        duration = new Time(5);
    }

    public static BufferedImage deepCopyBufferedImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int[] buffer = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), buffer, 0, image.getWidth());
        copy.setRGB(0, 0, image.getWidth(), image.getHeight(), buffer, 0, image.getWidth());
        return copy;
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
    public Time getDuration() {
        return duration;
    }

    @Override
    public String getName() {
        return null;
    }
}
