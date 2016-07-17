

// Swing utilities

import framework.Application;
import view.MenuView;
import view.PreviewView;
import view.TabbedView;
import view.TimelineView;

import javax.swing.*;

import java.awt.*;

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

        // Set the menu bar of the application frame.
        frame.setJMenuBar(new MenuView(this).render());
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.setBottomComponent(new TimelineView(this).render());
        JSplitPane topComponent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new TabbedView(this).render(), new PreviewView(this).render());
        jSplitPane.setTopComponent(topComponent);
        frame.setContentPane(jSplitPane);
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
