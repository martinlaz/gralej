package gralej.testers;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import gralej.gui.prefsdialog.PrefDialogFrame;
import gralej.prefs.GralePrefsInitException;

/**
 * @version $Id$
 * @author no
 *
 */
public class PrefsDialogester {


	public static void main(String[] args) throws GralePrefsInitException {

		JFrame frame = new PrefDialogFrame();
		
		// don't do this at home, this is just for this tester
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		

	}

}
