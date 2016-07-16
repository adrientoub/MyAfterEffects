/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.model;

import app.framework.Application;
import app.framework.Model;

import java.util.Collection;
import java.util.LinkedHashSet;

// Framework

/**
 * Example {@link Model} using the MVC micro-framework presented in
 * {@link app.framework}.
 */
public final class PreviewModel extends Model {
  private Collection<TodoItem> todos = new LinkedHashSet<>();

  public PreviewModel(final Application application) {
    super(application);
  }

  public TodoItem[] todos() {
    return this.todos.toArray(new TodoItem[this.todos.size()]);
  }

  public void add(final TodoItem todo) {
    if (todo == null) {
      throw new NullPointerException();
    }

    this.todos.add(todo);
    this.emit("todos:changed", todo);
  }

  public void remove(final TodoItem todo) {
    if (todo == null) {
      throw new NullPointerException();
    }

    this.todos.remove(todo);
    this.emit("todos:changed", todo);
  }

  public void clear() {
    if (this.todos.isEmpty()) {
      return;
    }

    this.todos.clear();
    this.emit("todos:changed");
  }

  public static final class TodoItem {
    private final String description;

    public TodoItem(final String description) {
      if (description == null) {
        throw new NullPointerException();
      }

      this.description = description;
    }

    public String description() {
      return this.description;
    }

    @Override
    public String toString() {
      return this.description;
    }
  }
}
