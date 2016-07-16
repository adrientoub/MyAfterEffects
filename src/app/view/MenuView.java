/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.view;

// AWT utilities
import java.awt.Toolkit;

// AWT events
import java.awt.event.KeyEvent;

// Swing utilities
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

// Framework
import app.framework.Application;
import app.framework.View;

public final class MenuView extends View {
  /**
   * Initialize a new {@link MenuView} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link MenuView} is
   *                    associated with.
   */
  public MenuView(final Application application) {
    super(application);
  }

  /**
   * Render the {@link MenuView}.
   */
  public JMenuBar render() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);

    JMenuItem clearTodos = new JMenuItem("Clear Todos");
    fileMenu.add(clearTodos);

    // Enabled triggering the "Clear Todos" menu item using a keyboard shortcut
    // consisting of the default shortcut key mask (CTRL on Windows, CMD on Mac)
    // and the back space key.
    clearTodos.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_BACK_SPACE,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
    ));

    // Emit an event signalling to controllers and/or views that we wish for the
    // list of todos to be cleared. We're not interested in which component
    // actually takes care of clearing the list of todos; just as long as
    // somebody does it. This ensures that we don't unnecessarily couple
    // components that we otherwise could have avoided coupling.
    clearTodos.addActionListener(e -> this.emit("todos:clear"));

    return menuBar;
  }
}
