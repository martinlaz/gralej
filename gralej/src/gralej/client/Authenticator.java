/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
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

package gralej.client;

import java.awt.Dialog;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.net.PasswordAuthentication;
import java.net.URL;
import javax.swing.JDialog;

/**
 * 
 * @author Martin
 */
public class Authenticator extends java.net.Authenticator {

    private static Authenticator instance;

    public static void install() {
        if (instance != null)
            return;
        instance = new Authenticator();
        java.net.Authenticator.setDefault(instance);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        URL url = getRequestingURL();
        return showAuthenticationDialog(url);
    }

    private PasswordAuthentication showAuthenticationDialog(URL url) {
        Window focusedWindow = null;
        KeyboardFocusManager kfm = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        if (kfm != null)
            focusedWindow = kfm.getFocusedWindow();

        JDialog dlg = new JDialog(focusedWindow, "Authentication required",
                Dialog.ModalityType.APPLICATION_MODAL);
        AuthenticatorPanel ap = new AuthenticatorPanel(dlg, url);
        dlg.add(ap);
        dlg.pack();
        dlg.setVisible(true);
        dlg.dispose();

        return ap.getPasswordAuthentication();
    }
}
