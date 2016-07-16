/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.framework;

// General utilities
import java.awt.*;

// Swing utilities
import javax.swing.JFrame;

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

  final Radio radio() {
    return this.radio;
  }

  protected abstract void start(final JFrame frame);
}
