/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.framework;

// General utilities
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

// Functional utilities
import java.util.function.Consumer;

/**
 * The {@link Radio} class describes an event emitter that can be used for
 * emitting and listening to events.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Event-driven_programming">
 *      https://en.wikipedia.org/wiki/Event-driven_programming</a>
 *
 * @param <T> The type of data to utilize in {@link Consumer Consumers}.
 */
public class Radio<T> {
  /**
   * A {@link Map} of events mapped to listening {@link Consumer Consumers}.
   */
  private Map<String, Set<Consumer<T>>> consumers = new HashMap<>();

  /**
   * Emit an event with the specified name.
   *
   * @param event The name of the event to emit.
   * @return      A boolean indicating whether or not the event was picked up
   *              by a {@link Consumer}.
   */
  public final boolean emit(final String event) {
    if (event == null) {
      throw new NullPointerException();
    }

    return this.emit(event, null);
  }

  /**
   * Emit an event with the specified name and data.
   *
   * @param event     The name of the event to emit.
   * @param data      The data to pass on to {@link Consumer Consumers}.
   * @return          A boolean indicating whether or not the event was picked
   *                  up by a {@link Consumer}.
   */
  public final boolean emit(final String event, final T data) {
    if (event == null) {
      throw new NullPointerException();
    }

    Set<Consumer<T>> consumers = this.consumers.get(event);

    if (consumers == null || consumers.isEmpty()) {
      return false;
    }

    for (Consumer<T> consumer: consumers) {
      consumer.accept(data);
    }

    return true;
  }

  /**
   * Attach a {@link Consumer} to the specified event.
   *
   * @param event     The name of the event to attach the {@link Consumer} to.
   * @param consumer  The {@link Consumer} to attach to the specified event.
   * @return          A boolean indicating whether or not the operation affected
   *                  the set of {@link Consumer Consumers} attached to the
   *                  specified event.
   */
  public final boolean on(final String event, final Consumer<T> consumer) {
    if (event == null || consumer == null) {
      throw new NullPointerException();
    }

    if (!this.consumers.containsKey(event)) {
      this.consumers.put(event, new LinkedHashSet<>());
    }

    return this.consumers.get(event).add(consumer);
  }

  /**
   * Detach a {@link Consumer} from the specified event.
   *
   * @param event     The name of the event to detach the {@link Consumer} from.
   * @param consumer  The {@link Consumer} to detach from the specified event.
   * @return          A boolean indicating whether or not the operation affected
   *                  the set of {@link Consumer Consumers} attached to the
   *                  specified event.
   */
  public final boolean off(final String event, final Consumer<T> consumer) {
    if (event == null || consumer == null) {
      throw new NullPointerException();
    }

    Set<Consumer<T>> consumers = this.consumers.get(event);

    if (consumers == null || consumers.isEmpty()) {
      return false;
    }

    return consumers.remove(consumer);
  }

  /**
   * Detach all attached {@link Consumer Consumers} from the specified event.
   *
   * @param event The name of the event to detach all attached
   *              {@link Consumer Consumers} from.
   * @return      A boolean indicating whether or not the operation affected the
   *              set of {@link Consumer Consumers} attached to the specified
   *              event.
   */
  public final boolean clear(final String event) {
    if (event == null) {
      throw new NullPointerException();
    }

    Set<Consumer<T>> consumers = this.consumers.get(event);

    if (consumers == null || consumers.isEmpty()) {
      return false;
    }

    consumers.clear();

    return true;
  }
}
