
package view;

// AWT utilities
import java.awt.Toolkit;

// AWT events
import java.awt.event.KeyEvent;

// Swing utilities
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
    return jMenuItem;
  }

  private void addFileMenu(JMenuBar menuBar) {
    JMenu file = new JMenu("File");

    String[] strings = { "New", "Open", "Exit" };
    for (String name: strings) {
      file.add(createItem(name));
    }
    menuBar.add(file);
  }

  public JMenuBar render() {

    JMenuBar menuBar = new JMenuBar();
    addFileMenu(menuBar);

    /*JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);

    JMenuItem clearTodos = new JMenuItem("Clear Todos");
    fileMenu.add(clearTodos);


    clearTodos.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_BACK_SPACE,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
    ));

    clearTodos.addActionListener(e -> this.emit("todos:clear"));*/

    return menuBar;
  }
}
