package view;

import controller.TabbedController;
import framework.Application;
import framework.ImagePanel;
import framework.View;
import model.TabbedModel;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public final class TabbedView extends View<TabbedModel, TabbedController> {
    private JTabbedPane tab;

    public TabbedView(final Application application) {
        super(application);
        tab = new JTabbedPane();

        this.model(new TabbedModel(application));
        this.controller(new TabbedController(application));

        this.on("menu:new", this::handle);
    }


    public static BufferedImage toBufferedImage(Picture src) {
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

    public static void toBufferedImage(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        int[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) srcData[i];
        }
    }

    private void handle(File f) {
        int frameNumber = 150;
        try {
            Picture picture = FrameGrab.getNativeFrame(f, frameNumber);
            BufferedImage bufferedImage = toBufferedImage(picture);
            tab.addTab("Video", new ImagePanel(bufferedImage));
            tab.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JCodecException e) {
            e.printStackTrace();
        }
    }

    public JPanel render() {
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(tab);

        return viewPanel;
    }
}
