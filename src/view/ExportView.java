package view;

// General utilities

import app.MyAfterEffectsApp;
import controller.ExportController;
import de.jaret.util.date.JaretDate;
import framework.Application;
import framework.View;
import model.ExportModel;
import model.TimelineModel;
import process.ProcessThreaded;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The {@link ExportView} class takes care of rendering the view for creating,
 * displaying, and completing todo items.
 */
public final class ExportView extends View<ExportModel, ExportController> {
    private JFrame view;
    private JProgressBar jProgressBar;
    private int max;
    private JLabel progress;
    private JPanel content;

    public ExportView(final Application application) {
        super(application);

        this.model(new ExportModel(application));
        this.controller(new ExportController(application));

        this.on("media:export", this::handle);
        this.on("media:exportProgress", this::setProgress);
    }

    private void handle(String path) {
        System.out.println("Handling " + path);
        view = new JFrame("Export");
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        double fps = ((MyAfterEffectsApp) application()).getOptionView().getFps();
        max = ((MyAfterEffectsApp) application()).getOptionView().getFrames();
        if (max == 0) {
            long ms = TimelineModel.getTimelineEnd().diffMilliSeconds(new JaretDate(0, 0, 0, 0, 0, 0));
            max = (int) ((ms / 1000.0) * fps);
        }
        JPanel progressLine = new JPanel();
        progressLine.setLayout(new BoxLayout(progressLine, BoxLayout.X_AXIS));
        jProgressBar = new JProgressBar(0, max);
        progress = new JLabel("0 %");
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        progressLine.add(progress);
        progressLine.add(jProgressBar);
        content.add(progressLine);
        JButton close = new JButton("Close");
        close.addActionListener(e -> view.setVisible(false));
        content.add(close);
        view.setContentPane(content);
        view.pack();
        view.setVisible(true);
        new ProcessThreaded(model(), path).start();
    }

    private void setProgress(int progress) {
        String percent = String.format("%.2f", ((double) progress / max) * 100);
        this.progress.setText(percent + " %");
        jProgressBar.setValue(progress);
        content.repaint();
    }

    /**
     * Render the {@link ExportView}.
     */
    public JPanel render() {
        return new JPanel();
    }
}

