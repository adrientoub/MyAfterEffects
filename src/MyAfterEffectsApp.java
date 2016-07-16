/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */

// Swing utilities
import framework.Application;
import view.MenuView;
import view.PreviewView;
import view.TabbedView;
import view.TimelineView;

import javax.swing.*;

import java.awt.*;

 public final class MyAfterEffectsApp extends Application {
  /**
   * Start the {@link MyAfterEffectsApp}.
   *
   * @param frame The main frame of the {@link MyAfterEffectsApp}.
   */
  protected void start(final JFrame frame) {
    frame.setTitle("Todos");

    // Set the menu bar of the application frame.
    frame.setJMenuBar(new MenuView(this).render());

    // Render and add the main view to the application frame.
    JButton button;
    frame.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.weightx = 0.5;
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 1;
    frame.add(new TabbedView(this).render(), c);

    c.gridx = 2;
    frame.add(new PreviewView(this).render(), c);

    c.ipady = 40;      //make this component tall
    c.weightx = 0.0;
    c.gridwidth = 3;
    c.gridx = 0;
    c.gridy = 1;
    frame.add(new TimelineView(this).render(), c);
  }

  /**
   * Boot up the {@link MyAfterEffectsApp}.
   *
   * @param args Runtime arguments.
   */
  public static void main(final String[] args) {
    new MyAfterEffectsApp();
  }
}
