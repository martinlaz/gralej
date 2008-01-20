package gralej;

import gralej.controller.Controller;
import gralej.gui.*;
import gralej.prefs.GralePreferences;

import java.io.IOException;

import javax.swing.UIManager;

/**
 * 
 * @author Armin
 * @version
 */

public class Main {

    private static void createAndShowGUI() {
    	
    	// initialize look and feel
    	String lookandfeel = GralePreferences.getInstance().get("gui.l+f.java-l+f");
    	try {
			UIManager.setLookAndFeel(lookandfeel);
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				throw new RuntimeException("Cannot set system default L&F!", e1);
			}
			System.err.println("Cannot load " + lookandfeel + ". Falling back to system default L&F.");
			e.printStackTrace();
		}	    	

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
