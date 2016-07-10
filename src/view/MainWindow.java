package view;

import javax.swing.*;

/**
 * Created by Adrien on 10/07/2016.
 */
public class MainWindow extends JFrame {
    private TabbedView tabbedView = new TabbedView();

    public MainWindow() {
        setContentPane(tabbedView);
        setTitle("MyAfterEffects");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    public void display() {
        setVisible(true);
    }

    public TabbedView getTabbedView() {
        return tabbedView;
    }
}
