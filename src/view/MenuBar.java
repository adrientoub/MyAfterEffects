package view;

import controller.MenuBarController;

import javax.swing.*;

/**
 * Created by Adrien on 10/07/2016.
 */
public class MenuBar extends JMenuBar {
    private JMenuItem createItem(String name) {
        JMenuItem jMenuItem = new JMenuItem(name);
        jMenuItem.addActionListener(new MenuBarController());
        return jMenuItem;
    }

    private void addFileMenu() {
        JMenu file = new JMenu("File");

        String[] strings = { "New", "Open", "Exit" };
        for (String name: strings) {
            file.add(createItem(name));
        }
        add(file);
    }

    public MenuBar() {
        super();
        addFileMenu();
    }
}
