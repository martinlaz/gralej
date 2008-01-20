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
    	
    	try {
			UIManager.setLookAndFeel(GralePreferences.getInstance().get("gui.l+f.java-l+f"));
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				throw new RuntimeException("Cannot set system default L&F!", e1);
			}
			System.err.println("Falling back to default L&F");
			e.printStackTrace();
		}	

        // JFrame frame = new PrefDialogFrame();
        GenDialog frame = new GenDialog(null);

        // don't do this at home, this is just for this tester
        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

    }

}
