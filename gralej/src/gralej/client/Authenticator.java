/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
