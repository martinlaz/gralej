package gralej;

import gralej.controller.Controller;
import gralej.util.Log;
import gralej.gui.*;
import gralej.prefs.GralePreferences;

import java.io.IOException;

import java.util.prefs.Preferences;
import javax.swing.UIManager;

/**
 * 
 * @author Armin
 * @version $Id$
 */

public class Main {

    private static void createAndShowGUI() {

        // initialize look and feel
        String lookandfeel = GralePreferences.getInstance().get(
                "gui.l+f.java-l+f");

        // default is system look and feel
        if ("System Default".equals(lookandfeel)) {
            lookandfeel = UIManager.getSystemLookAndFeelClassName();
        }

        try {
            UIManager.setLookAndFeel(lookandfeel);
        } catch (Exception e) {
            Log.error(
                    "Cannot load " + lookandfeel
                            + ". Falling back to system default L&F.");
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                throw new RuntimeException(
                        "Cannot set system default L&F! Good bye.", e1);
            }
        }

        // initialize the controller
        Controller c = new Controller();

        // initialize the GUI
        new MainGUI(c);

        c.startServer();

    }

    public static void main(String[] args) throws IOException {
        
        for (String arg : args) {
            if (arg.equals("--reset")) {
                try {
                    Preferences userRoot = Preferences.userRoot();
                    if (userRoot != null && userRoot.nodeExists("gralej"))
                        userRoot.node("gralej").removeNode();
                }
                catch (Exception e) {
                    System.err.println(e);
                }
            }
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
