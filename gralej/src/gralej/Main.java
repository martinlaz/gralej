/*
 *  $Id$
 *
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej;

import gralej.controller.Controller;
import gralej.util.Log;
import gralej.gui.*;

import java.io.IOException;

import java.util.prefs.Preferences;
import javax.swing.UIManager;

/**
 * @author Armin
 * @version $Id$
 */

public class Main {
    private static void setLookAndFeel(String lookandfeel) {
        // default is system look and feel
        if ("System Default".equals(lookandfeel))
            lookandfeel = UIManager.getSystemLookAndFeelClassName();

        try {
            UIManager.setLookAndFeel(lookandfeel);
        } catch (Exception e) {
            Log.error("Cannot load look and feel:", lookandfeel);
        }
    }

    private static void createAndShowGUI() {

        // initialize look and feel
        String javaLookAndFeelKey = "gui.l+f.java-l+f";
        setLookAndFeel(Config.s(javaLookAndFeelKey));
        
//        new Config.KeyObserver(javaLookAndFeelKey) {
//            protected void keyChanged() {
//                Log.debug("Changining Java's Look & Feel");
//                setLookAndFeel(_val);
//                Log.debug("Done with the Look & Feel");
//            }
//        };

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
