
package framework;

// Functional utilities
import java.util.function.Consumer;

/**
 * The {@link Controller} class describes all the basic functionality of a
 * controller within the MVC architecture.
 * @param <M> The type of model that the controller will operate on.
 * @param <V> The type of view that the controller will operate on.
 */
public abstract class Controller<M extends Model, V extends View> {

  private Application application;

  private M model;

  private V view;

  /**
   * Initialize a new {@link Controller} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link Controller} is
   *                    associated with.
   */
  public Controller(final Application application) {
    if (application == null) {
      throw new IllegalArgumentException(
        "An Application must be specified when initialising a Controller."
      );
    }

    this.application = application;
  }

  protected final Application application() {
    return this.application;
  }

  protected final M model() {
    return this.model;
  }

  /**
   * Set the {@link Model} that the {@link Controller} operates on.
   *
   * @param model The {@link Model} that the {@link Controller} operates on.
   */
  @SuppressWarnings("unchecked")
  final void model(final M model) {
    if (model == null) {
      throw new NullPointerException();
    }

    if (this.model != null) {
      throw new IllegalStateException(
        "A Model has already been set on the Controller."
      );
    }

    this.model = model;

    if (this.view != null) {
      try {
        this.view.model(this.model);
      }
      catch (IllegalStateException ex) {
        ;
      }
    }
  }

  protected final V view() {
    return this.view;
  }

  @SuppressWarnings("unchecked")
  final void view(final V view) {
    if (view == null) {
      throw new NullPointerException();
    }

    if (this.view != null) {
      throw new IllegalStateException(
        "A View has already been set on the Controller."
      );
    }

    this.view = view;

    try {
      this.view.controller(this);
    }
    catch (IllegalStateException ex) {
      ;
    }

    if (this.model != null) {
      try {
        this.view.model(this.model);
      }
      catch (IllegalStateException ex) {
        ;
      }
    }
  }

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
  protected final <T extends Object> boolean emit(final String event, final T data) {
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
