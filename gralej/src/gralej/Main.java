package gralej;

import gralej.controller.Controller;
import gralej.gui.*;

import java.io.IOException;

/**
 * 
 * @author Armin
 * @version
 */

public class Main {

    private static void createAndShowGUI() {

        // initialize the controller
        Controller c = new Controller();

        // initialize the GUI
        new MainGUI(c);
        
        c.startServer();

    }

    public static void main(String[] args) throws IOException {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
