/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */

// Swing utilities
import framework.Application;
import view.MenuView;
import view.PreviewView;
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
    boolean shouldFill = true, shouldWeightX = true;
    if (shouldFill) {
      //natural height, maximum width
      c.fill = GridBagConstraints.HORIZONTAL;
    }

    button = new JButton("Button 1");
    if (shouldWeightX) {
      c.weightx = 0.5;
    }
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    frame.add(button, c);

    button = new JButton("Button 2");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.gridx = 1;
    frame.add(button, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.gridx = 2;
    frame.add(new PreviewView(this).render(), c);

    button = new JButton("Long-Named Button 4");
    c.fill = GridBagConstraints.HORIZONTAL;
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
