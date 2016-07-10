package view;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.io.File;

/**
 * Created by Adrien on 10/07/2016.
 */
public class TabbedView extends JTabbedPane {
    public void newTab(File selected) {
        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        addTab("Video", mediaPlayerComponent);
        mediaPlayerComponent.getMediaPlayer().playMedia(selected.getAbsolutePath());
    }
}
