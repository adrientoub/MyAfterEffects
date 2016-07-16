/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package framework;

// Functional utilities
import java.util.function.Consumer;

// Swing utilities
import javax.swing.JComponent;

/**
 * The {@link View} class describes all the basic functionality of a view
 * within the MVC architecture.
 *
 * <p>
 * From Wikipedia:
 *
 * <blockquote>
 * A view requests information from the model that it uses to generate an output
 * representation to the user.
 * </blockquote>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 *      http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller</a>
 *
 * @param <M> The type of model that the view will operate on. This can be
 *            omitted if no model will ever be operated on by the view.
 * @param <C> The type of controller that will operate on the view. This can be
 *            omitted if no controller will ever operate on the view.
 */
public abstract class View<M extends Model, C extends Controller> {
  /**
   * The {@link Application} that the {@link View} is part of.
   */
  private Application application;

  /**
   * The {@link Model} that the {@link View} operates on.
   */
  private M model;

  /**
   * The {@link Controller} operating on the {@link View}.
   */
  private C controller;

  /**
   * Initialize a new {@link View} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link View} is
   *                    associated with.
   */
  public View(final Application application) {
    if (application == null) {
      throw new IllegalArgumentException(
        "An Application must be specified when initialising a View."
      );
    }

    this.application = application;
  }

  /**
   * Access the {@link Application} that the {@link View} is associated with.
   *
   * @return The {@link Application} that the {@link View} is associated with.
   */
  protected final Application application() {
    return this.application;
  }

  /**
   * Access the {@link Model} that the {@link View} renders.
   *
   * @return The {@link Model} that the {@link View} renders.
   */
  protected final M model() {
    return this.model;
  }

  @SuppressWarnings("unchecked")
  protected final void model(final M model) {
    if (model == null) {
      throw new NullPointerException();
    }

    if (this.model != null) {
      throw new IllegalStateException(
        "A Model has already been set on the View."
      );
    }

    this.model = model;

    if (this.controller != null) {
      try {
        this.controller.model(model);
      }
      catch (IllegalStateException ex) {
        ;
      }
    }
  }

  protected final C controller() {
    return this.controller;
  }

  @SuppressWarnings("unchecked")
  protected final void controller(final C controller) {
    if (controller == null) {
      throw new NullPointerException();
    }

    if (this.controller != null) {
      throw new IllegalStateException(
        "A Controller has already been set on the View."
      );
    }

    this.controller = controller;

    try {
      this.controller.view(this);
    }
    catch (IllegalStateException ex) {
      ;
    }

    if (this.model != null) {
      try {
        this.controller.model(this.model);
      }
      catch (IllegalStateException ex) {
        ;
      }
    }
  }

  /**
   * Render the {@link View} as a Swing component.
   *
   * <p>
   * This method must be implemented by subclasses and is where the {@link View}
   * is rendered as a Swing component.
   *
   * @return The rendered Swing component.
   */
  abstract public JComponent render();

  /**
   * Emit an event with the specified name.
   *
   * @param event The name of the event to emit.
   * @return      A boolean indicating whether or not the event was picked up
   *              by a {@link Consumer}.
   */
  protected final boolean emit(final String event) {
    return this.application.radio().emit(event);
  }

  /**
   * Emit an event with the specified name and data.
   *
   * @param <T>       The type of data to utilize in {@link Consumer Consumers}.
   * @param event     The name of the event to emit.
   * @param data      The data to pass on to {@link Consumer Consumers}.
   * @return          A boolean indicating whether or not the event was picked
   *                  up by a {@link Consumer}.
   */
  @SuppressWarnings("unchecked")
  protected final <T extends Object> boolean emit(
    final String event,
    final T data
  ) {
    return this.application.radio().emit(event, data);
  }

  /**
   * Attach a {@link Consumer} to the specified event.
   *
   * @param <T>       The type of data to utilize in {@link Consumer Consumers}.
   * @param event     The name of the event to attach the {@link Consumer} to.
   * @param consumer  The {@link Consumer} to attach to the specified event.
   * @return          A boolean indicating whether or not the operation affected
   *                  the set of {@link Consumer Consumers} attached to the
   *                  specified event.
   */
  @SuppressWarnings("unchecked")
  protected final <T extends Object> boolean on(
    final String event,
    final Consumer<T> consumer
  ) {
    return this.application.radio().on(event, consumer);
  }

  /**
   * Detach a {@link Consumer} from the specified event.
   *
   * @param <T>       The type of data to utilize in {@link Consumer Consumers}.
   * @param event     The name of the event to detach the {@link Consumer} from.
   * @param consumer  The {@link Consumer} to detach from the specified event.
   * @return          A boolean indicating whether or not the operation affected
   *                  the set of {@link Consumer Consumers} attached to the
   *                  specified event.
   */
  @SuppressWarnings("unchecked")
  protected final <T extends Object> boolean off(
    final String event,
    final Consumer<T> consumer
  ) {
    return this.application.radio().off(event, consumer);
  }
}
