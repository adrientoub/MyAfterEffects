package view;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;

/**
 * Created by Adrien on 10/07/2016.
 */
public class TabbedView extends JTabbedPane {
    String videoFile = "test.mkv";
    public void newTab() {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        addTab("Video", mediaPlayerComponent);
        mediaPlayerComponent.getMediaPlayer().playMedia(videoFile);
    }
}
