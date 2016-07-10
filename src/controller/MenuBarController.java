package controller;

import view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Adrien on 10/07/2016.
 */
public class MenuBarController implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "Exit":
                System.exit(0);
                break;
            case "New":
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(null);
                File selected = jFileChooser.getSelectedFile();
                if (selected == null)
                    return;
                MainWindow.getMainWindow().getTabbedView().newTab(selected);
                break;
        }
    }
}
