/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.controller;

// General utilities
import java.util.List;

// Framework
import app.framework.Application;
import app.framework.Controller;

// Models
import app.model.TodoModel;
import app.model.TodoModel.TodoItem;

// Views
import app.view.TodoView;

/**
 * Example controller using the MVC micro-framework presented in
 * {@link app.framework}.
 */
public final class TodoController extends Controller<TodoModel, TodoView> {
  /**
   * Initialize a new {@link TodoController} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link TodoController}
   *                    is associated with.
   */
  public TodoController(final Application application) {
    super(application);

    // Whenever another component requests that the list of todos be cleared,
    // clear it. This event can be emitted by both views and/or controllers; we
    // don't really care.
    this.on("todos:clear", (data) -> this.clear());
  }

  public void create(final String description) throws NullPointerException, IllegalArgumentException {
    if (description == null) {
      throw new NullPointerException();
    }

    // Trim the description for trailing whitespace.
    String trimmedDescription = description.trim();

    // Check that the trimmed description isn't empty. If the description is
    // indeed empty, throw an exception for the callers to act upon. If the
    // caller is a view, then a suitable error message could be displayed.
    if (trimmedDescription.isEmpty()) {
      throw new IllegalArgumentException("Todo descriptions cannot be empty.");
    }

    // Create a new todo item using the now sanitized description.
    TodoItem todo = new TodoItem(trimmedDescription);

    // Update the model with the newly created todo item.
    this.model().add(todo);
  }

  public void complete(final TodoItem todo) {
    if (todo == null) {
      throw new NullPointerException();
    }

    // Remove the todo item from the model.
    this.model().remove(todo);
  }

  public void complete(final List<TodoItem> todos) {
    if (todos == null) {
      throw new NullPointerException();
    }

    for (TodoItem todo: todos) {
      this.complete(todo);
    }
  }

  public void clear() {
    this.model().clear();
  }
}
