/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package view;

// General utilities

import controller.TimelineController;
import framework.Application;
import framework.Timeline;
import framework.View;
import model.TimelineModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  JList<Timeline> todosList;

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));

    todosList = new JList<>(this.model().timelines());
    this.on("timelines:changed", (Timeline T) -> todosList.setListData(this.model().timelines()));
  }

  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JScrollPane todosPane = new JScrollPane(todosList);
    viewPanel.add(todosPane, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);

    return viewPanel;
  }
}
