package manager;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.sql.Time;

/**
 * Created by Damien on 17/07/2016.
 */
public class Video {

    private float _fps;
    private int _nbframes;
    private Time duration;
    private File file;
    private BufferedImage image;

    public Video(File f) {
        this.file = f;
        /* Open the file with jcodec */
        try {
            // TODO
            int frameNumber = 150;
            Picture picture = FrameGrab.getNativeFrame(f, frameNumber);
            image = toBufferedImage(picture);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JCodecException e) {
            e.printStackTrace();
        }
        duration = new Time(0, 2, 30);
        // TODO
    }

    private BufferedImage toBufferedImage(Picture src) {
        if (src.getColor() != ColorSpace.RGB) {
            Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
            Picture rgb = Picture.create(src.getWidth(), src.getHeight(), ColorSpace.RGB, src.getCrop());
            transform.transform(src, rgb);
            src = rgb;
        }

        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(),
                BufferedImage.TYPE_3BYTE_BGR);

        toBufferedImage(src, dst);

        return dst;
    }

    public void toBufferedImage(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        int[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) srcData[i];
        }
    }

    public BufferedImage getImage(int frame) {
        // TODO, for now only return 150th frame
        return image;
    }

    public float get_fps() {
        return _fps;
    }

    public void set_fps(float _fps) {
        this._fps = _fps;
    }

    public int get_nbframes() {
        return _nbframes;
    }

    public void set_nbframes(int _nbframes) {
        this._nbframes = _nbframes;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getName() {
        return file.getName();
    }
}
