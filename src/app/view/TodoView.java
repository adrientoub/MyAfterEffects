/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.view;

// General utilities
import java.util.List;

// AWT utilities
import java.awt.BorderLayout;
import java.awt.FlowLayout;

// Swing utilities
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// Swing borders
import javax.swing.border.EmptyBorder;

// Framework
import app.framework.Application;
import app.framework.View;

// Models
import app.model.TodoModel;
import app.model.TodoModel.TodoItem;

// Controllers
import app.controller.TodoController;

/**
 * The {@link TodoView} class takes care of rendering the view for creating,
 * displaying, and completing todo items.
 */
public final class TodoView extends View<TodoModel, TodoController> {
  /**
   * Initialize a new {@link TodoView} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link TodoView} is
   *                    associated with.
   */
  public TodoView(final Application application) {
    super(application);

    // Initialize the model and controller of the view. The order in which these
    // are initialized does not matter as they will automagically be wired
    // together regardless.
    this.model(new TodoModel(application));
    this.controller(new TodoController(application));
  }

  /**
   * Render the {@link TodoView}.
   */
  public JPanel render() {
    JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
    inputPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
    viewPanel.add(inputPanel, BorderLayout.NORTH);

    // Create a 10-column text field for inputting todo descriptions.
    JTextField todoInput = new JTextField(10);
    inputPanel.add(todoInput, BorderLayout.NORTH);

    todoInput.addActionListener(e -> {
      try {
        // Delegate creation of the Todo item to the controller.
        this.controller().create(todoInput.getText());
      }
      catch (IllegalArgumentException ex) {
        // Simply print out the error message to the error console whenever
        // the user enter invalid input. This should ideally be displayed in a
        // nice looking error dialog of sorts in a real application.
        System.err.println(ex.getMessage());
      }

      // Clear the text input field.
      todoInput.setText(null);
    });

    JButton todoSubmit = new JButton("Create Todo");
    inputPanel.add(todoSubmit, BorderLayout.NORTH);

    // Trigger the action event of the text input field whenever the submit
    // button is pressed. This make pressing the button equivalent to hitting
    // enter when the text input field is focused.
    todoSubmit.addActionListener(e -> todoInput.postActionEvent());

    JList<TodoItem> todosList = new JList<>(this.model().todos());

    this.model().on("todos:changed", (TodoItem todo) -> {
      todosList.setListData(this.model().todos());
    });

    JScrollPane todosPane = new JScrollPane(todosList);
    viewPanel.add(todosPane, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);

    JButton todoComplete = new JButton("Complete Todo");
    todoComplete.setEnabled(!todosList.isSelectionEmpty());
    actionsPanel.add(todoComplete);

    todoComplete.addActionListener(e -> {
      // Get the currently selected Todo items.
      List<TodoItem> selectedTodos = todosList.getSelectedValuesList();

      // Delegate deletion of the Todo item to the controller.
      this.controller().complete(selectedTodos);
    });

    todosList.addListSelectionListener(e -> {
      // Enable/disable the "Complete Todo" button whenever the list of todos
      // goes in and out of focus.
      todoComplete.setEnabled(!todosList.isSelectionEmpty());
    });

    return viewPanel;
  }
}
