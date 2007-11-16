package gralej;


//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import javax.swing.*;        
import gralej.controller.*;
import gralej.fileIO.FileLoader;
import gralej.gui.*;
import gralej.server.*;
import gralej.testers.DummyStreamHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class instantiates the GUI, controller (with data model), 
 * the server (copied from Niels' ServerTestApp)
 * TODO connect server and controller
 * TODO pass server information to main GUI to be displayed in bottom line
 * 
 * and probably the Readers, Writers etc. 
 * and registers some listeners
 * 
 * after this it's done.
 * 
 * @author Armin
 *
 */

public class Main {
	
	
    /**
     * Create the GUI and show it. 
     */
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
    	MainGUI gui = new MainGUI(c);
    	
    	// initialize the observers
    	// list observer (registers with model in its constructor)
    	ContentObserver list = new ListContentObserver(c.getModel());
    	// add list to GUI
    	gui.addToSplit(list);
    	// frame observer (registers with model in its constructor)
    	ContentObserver frames = new FramesContentObserver(c.getModel());
    	// add list to GUI
    	gui.addToSplit(frames);
    }

    

    public static void main(String[] args) throws IOException {
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

