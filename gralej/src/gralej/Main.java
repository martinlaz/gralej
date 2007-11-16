package gralej;


//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import javax.swing.*;        
import gralej.controller.*;
import gralej.gui.*;

/**
 * This class instantiates the GUI, controller (with data model), 
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
    	// initialize a model
    	Controller c = new Controller();
    	
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

    

    public static void main(String[] args) {
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

