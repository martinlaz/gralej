package gralej.testers;

import gralej.gui.prefsdialog.GenDialog;
import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;

import javax.swing.UIManager;

/**
 * @version $Id$
 * @author no
 * 
 */
public class PrefsDialogester {

    public static void main(String[] args) throws GralePrefsInitException {

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

        // JFrame frame = new PrefDialogFrame();
        GenDialog frame = new GenDialog(null);

        // don't do this at home, this is just for this tester
        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

    }

}
