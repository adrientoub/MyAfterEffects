/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.view;

// General utilities

import app.controller.TodoController;
import app.framework.Application;
import app.framework.View;
import app.model.TodoModel;
import app.model.TodoModel.TodoItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

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
public final class TimelineView extends View<TodoModel, TodoController> {
  /**
   * Initialize a new {@link TimelineView} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link TimelineView} is
   *                    associated with.
   */
  public TimelineView(final Application application) {
    super(application);

    this.model(new TodoModel(application));
    this.controller(new TodoController(application));
  }

  /**
   * Render the {@link TimelineView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JList<TodoItem> todosList = new JList<>(this.model().todos());

    this.model().on("todos:changed", (TodoItem todo) -> {
      todosList.setListData(this.model().todos());
    });

    this.controller().create("Layer 1");
    this.controller().create("Layer 2");
    this.controller().create("Layer 3");

    JScrollPane todosPane = new JScrollPane(todosList);
    viewPanel.add(todosPane, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);

    return viewPanel;
  }
}
