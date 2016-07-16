/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.view;

// General utilities

import app.controller.TimelineController;
import app.framework.Application;
import app.framework.Timeline;
import app.framework.View;
import app.model.TimelineModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));
  }

  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JList<Timeline> todosList = new JList<Timeline>(this.model().timelines());

    this.model().on("timelines:changed", (Timeline timeline) -> {
      todosList.setListData(this.model().timelines());
    });

    this.controller().create(new Timeline("Layer 1"));
    this.controller().create(new Timeline("Layer 2"));
    this.controller().create(new Timeline("Layer 3"));

    JScrollPane todosPane = new JScrollPane(todosList);
    viewPanel.add(todosPane, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);

    return viewPanel;
  }
}
