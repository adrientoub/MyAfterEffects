package controller;

import view.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                MainWindow.getMainWindow().getTabbedView().newTab();
                break;
        }
    }
}
