package gralej.gui.prefsdialog;

import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/**
 * useless testing class for the preferences dialog
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
		
		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = new AvmDisplayOptsPane(prefs);
		JComponent panel2 = new JLabel("Empty right now");
		
		tabbedPane.addTab("AVM Display",  panel1);
		tabbedPane.addTab("Empty",  panel2);
		

		cp.add(tabbedPane);

		pack();
		
	}

}
