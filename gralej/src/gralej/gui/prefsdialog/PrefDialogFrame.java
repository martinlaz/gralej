package gralej.gui.prefsdialog;

import gralej.gui.prefsdialog.options.OptionComponent;
import gralej.gui.prefsdialog.options.OptionComponentFactory;
import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * 
 * @author no
 * @version $Id$
 */
public class PrefDialogFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9033956108346319208L;

	public PrefDialogFrame() throws GralePrefsInitException {
		
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		
		GralePreferences prefs = GralePreferences.getInstance();
		
		OptionComponent c1 = OptionComponentFactory.getComponent(
				prefs, "testbool", "Boolean Test", "boolean");
		cp.add(c1);

		OptionComponent c2 = OptionComponentFactory.getComponent(
				prefs, "testfont3", "Font Test", "font");
		cp.add(c2);
		
		c2.savePref();
		
		
		pack();
 
		

		pack();
		
	}

}
