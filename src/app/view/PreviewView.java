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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// AWT utilities
// Swing utilities
// Swing borders
// Framework
// Models
// Controllers

/**
 * The {@link PreviewView} class takes care of rendering the view for creating,
 * displaying, and completing todo items.
 */
public final class PreviewView extends View<TodoModel, TodoController> {

  BufferedImage image;

  /**
   * Initialize a new {@link PreviewView} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link PreviewView} is
   *                    associated with.
   */
  public PreviewView(final Application application) {
    super(application);

    this.model(new TodoModel(application));
    this.controller(new TodoController(application));
  }

  /**
   * Render the {@link PreviewView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    try {
      image = ImageIO.read(new File("assets/preview.png"));
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }

    JLabel picLabel = new JLabel(new ImageIcon(image));
    viewPanel.add(picLabel);


    /*JList<TodoItem> todosList = new JList<>(this.model().todos());

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
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);*/

    return viewPanel;
  }
}
