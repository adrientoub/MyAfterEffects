
package framework;

// Functional utilities
import de.jaret.util.ui.timebars.TimeBarMarker;

import java.util.function.Consumer;

/**
 * The {@link Model} class describes all the basic functionality of a model
 * within the MVC architecture.
 *
 * <p>
 * From Wikipedia:
 *
 * <blockquote>
 * A model notifies its associated views and controllers when there has been a
 * change in its state. This notification allows the views to produce updated
 * output, and the controllers to change the available set of commands.
 * </blockquote>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 *      http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller</a>
 */
public abstract class Model {
  /**
   * The {@link Application} that the {@link Model} is part of.
   */
  private Application application;

  /**
   * Initialize a new {@link Model} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link Model} is
   *                    associated with.
   */
  public Model(final Application application) {
    if (application == null) {
      throw new IllegalArgumentException(
        "An Application must be specified when initialising a Model."
      );
    }

    this.application = application;
  }

  /**
   * Access the {@link Application} that the {@link Model} is associated with.
   *
   * @return The {@link Application} that the {@link Model} is associated with.
   */
  protected final Application application() {
    return this.application;
  }

  /**
   * Emit an event with the specified name.
   *
   * @param event The name of the event to emit.
   * @return      A boolean indicating whether or not the event was picked up
   *              by a {@link Consumer}.
   */
  protected final boolean emit(final String event) {
    return this.application().radio().emit(event);
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
    return this.application().radio().emit(event, data);
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
  public final <T extends Object> boolean on(
    final String event,
    final Consumer<T> consumer
  ) {
    return this.application().radio().on(event, consumer);
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
  public final <T extends Object> boolean off(
    final String event,
    final Consumer<T> consumer
  ) {
    return this.application().radio().off(event, consumer);
  }
}
