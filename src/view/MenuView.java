
package view;

// AWT utilities
import java.awt.Toolkit;

// AWT events
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// Swing utilities
import javax.swing.*;

// Framework
import controller.MenuController;
import framework.Application;
import framework.View;
import model.MenuModel;

public final class MenuView extends View<MenuModel, MenuController> {

  public MenuView(final Application application) {
    super(application);

    this.model(new MenuModel(application));
    this.controller(new MenuController(application));
  }
  private JMenuItem createItem(String name) {
    JMenuItem jMenuItem = new JMenuItem(name);
    jMenuItem.addActionListener(e -> this.controller().handle(name));

    char c;
    if (name.equals("Exit"))
      c = 'X';
    else
      c = name.charAt(0);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(c, ActionEvent.CTRL_MASK));
    return jMenuItem;
  }

  private void addFileMenu(JMenuBar menuBar) {
    JMenu file = new JMenu("File");

    String[] strings = { "New", "Export", "Save", "Exit" };
    for (String name: strings) {
      file.add(createItem(name));
    }
    menuBar.add(file);
  }

  public JMenuBar render() {
    JMenuBar menuBar = new JMenuBar();
    addFileMenu(menuBar);
    return menuBar;
  }
}
