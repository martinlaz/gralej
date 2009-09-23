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
import gralej.controller.StreamInfo;
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
        if ("System Default".equals(lookandfeel)) {
            lookandfeel = UIManager.getSystemLookAndFeelClassName();
        }
        else {
            try {
                Class.forName(lookandfeel);
            }
            catch (ClassNotFoundException ex) {
                Log.warning("Non-existent look and feel:", lookandfeel);
                lookandfeel = UIManager.getSystemLookAndFeelClassName();
            }
        }

        try {
            UIManager.setLookAndFeel(lookandfeel);
        }
        catch (Exception e) {
            Log.warning("Cannot set look and feel:", lookandfeel);
        }
    }

    private static void createAndShowGUI(boolean startServer) {

        // initialize look and feel
        String javaLookAndFeelKey = "gui.l+f.java-l+f";
        setLookAndFeel(Config.s(javaLookAndFeelKey));
        
        // initialize the controller
        Controller c = new Controller();

        // initialize the GUI
        MainGUI gui = new MainGUI(c, startServer);

        if (startServer)
            c.startServer();
        else {
            c.newStream(System.in, StreamInfo.GRISU);
            gui.getStatusBar().setConnectionInfo("StdIn");
        }
    }

    public static void main(String[] args) throws IOException {
        boolean useStdIn = false;
        
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
            else if (arg.equals("--stdin")) {
                useStdIn = true;
            }
            else {
                System.err.println("-- unknown arg: " + arg);
            }
        }

        final boolean startServer = !useStdIn;

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(startServer);
            }
        });
    }
}
