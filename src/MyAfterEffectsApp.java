

// Swing utilities

import framework.Application;
import view.MenuView;
import view.PreviewView;
import view.TabbedView;
import view.TimelineView;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public final class MyAfterEffectsApp extends Application {
    /**
     * Start the {@link MyAfterEffectsApp}.
     *
     * @param frame The main frame of the {@link MyAfterEffectsApp}.
     */
    protected void start(final JFrame frame) {
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
        jSplitPane.setBottomComponent(new TimelineView(this).render());
        JSplitPane topComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new TabbedView(this).render(), new PreviewView(this).render());
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
}
