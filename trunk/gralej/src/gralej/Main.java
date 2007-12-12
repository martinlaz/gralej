package gralej;

import gralej.controller.Controller;
import gralej.gui.*;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

import java.io.IOException;

/**
 * This class instantiates the GUI, controller (with data model), the server
 * (copied from Niels' ServerTestApp)
 * 
 * 
 * after this it's done.
 * 
 * @author Armin
 * @version
 */

public class Main {

    private static void createAndShowGUI() {

        // initialize the server
        IGraleServer server = new SocketServer(1080);
        try {
            server.startListening();
            System.err.println("-- Server up and listening");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // initialize the controller
        Controller c = new Controller(server);

        // initialize the GUI
        new MainGUI(c);

    }

    public static void main(String[] args) throws IOException {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
