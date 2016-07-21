package view;

import controller.OptionController;
import framework.Application;
import framework.View;
import model.OptionModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public final class OptionView extends View<OptionModel, OptionController> {
    private JSpinner threads;
    private JSpinner height;
    private JSpinner width;
    private JSpinner fps;
    private JSpinner frames;

    public OptionView(final Application application) {
        super(application);

        height = new JSpinner(new SpinnerNumberModel(720, 0, 2560, 1));
        width = new JSpinner(new SpinnerNumberModel(1280, 0, 2560, 1));
        fps = new JSpinner(new SpinnerNumberModel(30.0, 0.0, 60.0, 1.0));
        frames = new JSpinner(new SpinnerNumberModel(100, 0, 60000, 5));
        threads = new JSpinner(new SpinnerNumberModel(Runtime.getRuntime().availableProcessors() * 2, 0, 256, 1));

        this.model(new OptionModel(application));
        this.controller(new OptionController(application));
    }

    public JPanel render() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new GridLayout(5, 2, 2, 5));
        viewPanel.add(new JLabel("FPS: "));
        viewPanel.add(fps);
        viewPanel.add(new JLabel("Frames to render: "));
        viewPanel.add(frames);
        viewPanel.add(new JLabel("Width: "));
        viewPanel.add(width);
        viewPanel.add(new JLabel("Height: "));
        viewPanel.add(height);
        viewPanel.add(new JLabel("Render Threads: "));
        viewPanel.add(threads);

        return viewPanel;
    }

    public int getThreads() {
        return (int) threads.getValue();
    }


    public int getOutputHeight() {
        return (int) height.getValue();
    }

    public int getOutputWidth() {
        return (int) width.getValue();
    }

    public double getFps() {
        return (double) fps.getValue();
    }

    public int getFrames() {
        return (int) frames.getValue();
    }
}
