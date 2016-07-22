package view;

import controller.OptionController;
import framework.Application;
import framework.View;
import model.OptionModel;

import javax.swing.*;
import java.awt.*;

public final class OptionView extends View<OptionModel, OptionController> {
    private JSpinner threadsSpinner;
    private int threads;
    private JSpinner heightSpinner;
    private int height = 720;
    private JSpinner widthSpinner;
    private int width = 1280;
    private JSpinner fpsSpinner;
    private double fps = 30.0;
    private JSpinner framesSpinner;
    private int frames = 100;

    public OptionView(final Application application) {
        super(application);

        // Set threads to 2 times the number of available core
        threads = Runtime.getRuntime().availableProcessors() * 2;

        heightSpinner = new JSpinner(new SpinnerNumberModel(height, 0, 2560, 1));
        heightSpinner.addChangeListener(changeEvent ->
                height = ((SpinnerNumberModel)((JSpinner)changeEvent.getSource()).getModel()).getNumber().intValue());

        widthSpinner = new JSpinner(new SpinnerNumberModel(width, 0, 2560, 1));
        widthSpinner.addChangeListener(changeEvent ->
                width = ((SpinnerNumberModel)((JSpinner)changeEvent.getSource()).getModel()).getNumber().intValue());

        fpsSpinner = new JSpinner(new SpinnerNumberModel(fps, 0.0, 60.0, 1.0));
        fpsSpinner.addChangeListener(changeEvent ->
                fps = ((SpinnerNumberModel)((JSpinner)changeEvent.getSource()).getModel()).getNumber().doubleValue());

        framesSpinner = new JSpinner(new SpinnerNumberModel(frames, 0, 60000, 5));
        framesSpinner.addChangeListener(changeEvent ->
                frames = ((SpinnerNumberModel)((JSpinner)changeEvent.getSource()).getModel()).getNumber().intValue());

        threadsSpinner = new JSpinner(new SpinnerNumberModel(threads, 0, 256, 1));
        threadsSpinner.addChangeListener(changeEvent ->
                threads = ((SpinnerNumberModel)((JSpinner)changeEvent.getSource()).getModel()).getNumber().intValue());

        this.model(new OptionModel(application));
        this.controller(new OptionController(application));
    }

    public JPanel render() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new GridLayout(5, 2, 2, 5));
        viewPanel.add(new JLabel("FPS: "));
        viewPanel.add(fpsSpinner);
        viewPanel.add(new JLabel("Frames to render: "));
        viewPanel.add(framesSpinner);
        viewPanel.add(new JLabel("Width: "));
        viewPanel.add(widthSpinner);
        viewPanel.add(new JLabel("Height: "));
        viewPanel.add(heightSpinner);
        viewPanel.add(new JLabel("Render Threads: "));
        viewPanel.add(threadsSpinner);

        return viewPanel;
    }

    public int getThreads() {
        return threads;
    }

    public int getOutputHeight() {
        return height;
    }

    public int getOutputWidth() {
        return width;
    }

    public double getFps() {
        return fps;
    }

    public int getFrames() {
        return frames;
    }
}
