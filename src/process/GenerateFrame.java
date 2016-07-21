package process;

import de.jaret.util.date.JaretDate;
import de.jaret.util.misc.Pair;
import manager.Media;
import model.TimelineModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Adrien on 20/07/2016.
 */
public class GenerateFrame implements Callable<BufferedImage> {
    public JaretDate date;

    public GenerateFrame(JaretDate date) {
        this.date = date;
    }

    @Override
    public BufferedImage call() throws Exception {
        ArrayList<Pair<Long, Media>> pairs = TimelineModel.getMediasAtDate(date);
        if (pairs.size() == 0) {
            System.err.println(":-(");
            return null;
        }
        Dimension resolution = new Dimension(0, 0);
        for (Pair<Long, Media> pair: pairs) {
            Media m = pair.getRight();
            Dimension d = m.getResolution();
            resolution.width = Math.max(resolution.width, d.width);
            resolution.height = Math.max(resolution.height, d.height);
        }

        BufferedImage bi = new BufferedImage((int) resolution.getWidth(), (int) resolution.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = bi.getGraphics();
        for (Pair<Long, Media> pair: pairs) {
            Media m = pair.getRight();
            long time = pair.getLeft();
            BufferedImage image = m.getImage(m.getFrameFromMilliseconds(time));
            graphics.drawImage(image, 0, 0, null);
        }
        return bi;
    }
}
