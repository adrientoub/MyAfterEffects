/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.framework;

// General utilities
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

// Swing utilities
import javax.swing.JFrame;

/**
 * The {@link Application} class describes a complete MVC application.
 *
 * <p>
 * The class takes care of constructing the main application frame and passing
 * it on to subclasses. To create a new MVC application one must therefore
 * extend the {@link Application} class and implement the
 * {@link #start(JFrame)} method:
 *
 * <pre>
 * public final class MyApp extends Application {
 *   protected void start(final JFrame frame) {
 *     frame.getContentPane().add(...);
 *   }
 *
 *   public static void main(final String[] args) {
 *     new MyApp();
 *   }
 * }
 * </pre>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 *      http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller</a>
 */
public abstract class Application {
  /**
   * A {@link Radio} instance used for {@link Application}-wide communication.
   */
  private Radio radio = new Radio();

  /**
   * Initialize a new {@link Application} instance.
   */
  public Application() {
    // Construct the main frame of the application.
    JFrame frame = new JFrame();
    frame.setLayout(new BorderLayout());

    // Exit the application when the main frame is closed.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Start the application.
    this.start(frame);

    // Pack the application frame.
    frame.pack();

    // Make the application frame visible.
    frame.setVisible(true);
  }

  /**
   * Access the {@link Radio} used for {@link Application}-wide communication.
   *
   * <p>
   * This method can only be used within framework classes.
   *
   * @return The {@link Radio} used for {@link Application}-wide communication.
   */
  final Radio radio() {
    return this.radio;
  }

  /**
   * Start the {@link Application}.
   *
   * <p>
   * This method is called as part of the {@link Application} initialization.

   * @param frame The main frame of the {@link Application}.
   */
  protected abstract void start(final JFrame frame);
}
