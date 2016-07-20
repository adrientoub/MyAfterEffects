
package app;
// Swing utilities

import framework.Application;
import view.*;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public final class MyAfterEffectsApp extends Application {
    private ExportView exportView;
    private TimelineView timelineView;
    private TabbedView tabbedView;
    private PreviewView previewView;

    /**
     * Start the {@link MyAfterEffectsApp}.
     *
     * @param frame The main frame of the {@link MyAfterEffectsApp}.
     */
    protected void start(final JFrame frame) {
        timelineView = new TimelineView(this);
        tabbedView = new TabbedView(this);
        previewView = new PreviewView(this);
        exportView = new ExportView(this);

        frame.setTitle("MyAfterEffects");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        loadLibraries();

        // Set the menu bar of the application frame.
        frame.setJMenuBar(new MenuView(this).render());
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.setBottomComponent(timelineView.render());
        JSplitPane topComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedView.render(), previewView.render());
        jSplitPane.setTopComponent(topComponent);
        frame.setContentPane(jSplitPane);
    }

    private static ArrayList<File> listEndsWith(String path, String ext) {
        File dir = new File(path);
        File[] files = dir.listFiles((d, name) -> name.endsWith('.' + ext));
        return new ArrayList<>(Arrays.asList(files));
    }

    private static void loadLibraries() {
        String lib_ext;
        String separator;
        if (System.getProperty("os.name").contains("Windows")) {
            separator = "\\";
            lib_ext = "dll";
        } else {
            separator = "/";
            lib_ext = "so";
        }

        ArrayList<File> files = new ArrayList<>();
        files.addAll(listEndsWith("lib" + separator + "opencv", lib_ext));
        files.addAll(listEndsWith("lib" + separator + "ffmpeg", lib_ext));
        files.addAll(listEndsWith("lib" + separator + "h264", lib_ext));
        for (File f : files) {
            System.out.println("Loading external library: " + f.getAbsolutePath());
            System.load(f.getAbsolutePath());
        }
    }

    /**
     * Boot up the {@link MyAfterEffectsApp}.
     *
     * @param args Runtime arguments.
     */
    public static void main(final String[] args) {
        new MyAfterEffectsApp();
    }

    public TimelineView getTimelineView() {
        return timelineView;
    }

    public TabbedView getTabbedView() {
        return tabbedView;
    }

    public PreviewView getPreviewView() {
        return previewView;
    }
}
