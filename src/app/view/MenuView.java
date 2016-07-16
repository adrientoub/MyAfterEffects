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


    clearTodos.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_BACK_SPACE,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
    ));

    clearTodos.addActionListener(e -> this.emit("todos:clear"));

    return menuBar;
  }
}
