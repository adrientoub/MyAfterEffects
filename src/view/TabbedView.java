/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package view;

// General utilities

import controller.TabbedController;
import controller.TimelineController;
import framework.Application;
import framework.Timeline;
import framework.View;
import model.TabbedModel;
import model.TimelineModel;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public final class TabbedView extends View<TabbedModel, TabbedController> {

  private JTabbedPane tab;

  public TabbedView(final Application application) {
    super(application);
    tab = new JTabbedPane();

    this.model(new TabbedModel(application));
    this.controller(new TabbedController(application));

    this.on("menu:new", this::handle);
  }

  private void handle(File f) {

    /* TODO Adrien, Still use libvlc */
    EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    tab.addTab("Video", mediaPlayerComponent);
    mediaPlayerComponent.getMediaPlayer().playMedia(f.getAbsolutePath());

  }

  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    tab.addTab("Test", new JLabel("Hello, I am a tab"));
    viewPanel.add(tab);

    return viewPanel;
  }
}
