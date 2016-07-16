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

// AWT utilities
// Swing utilities
// Swing borders
// Framework
// Models
// Controllers

/**
 * The {@link TimelineView} class takes care of rendering the view for creating,
 * displaying, and completing todo items.
 */
public final class TimelineView extends View<TimelineModel, TimelineController> {
  /**
   * Initialize a new {@link TimelineView} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link TimelineView} is
   *                    associated with.
   */
  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));
  }

  /**
   * Render the {@link TimelineView}.
   */
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
