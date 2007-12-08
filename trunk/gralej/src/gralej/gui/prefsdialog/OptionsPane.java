package gralej.gui.prefsdialog;

import java.awt.Component;

import gralej.gui.prefsdialog.options.OptionComponent;

import javax.swing.JComponent;

/**
 * basic internal functionality of a pane (or panel) holding
 * a buch of options (which should be {@link OptionComponent})
 * @author no
 * @version $Id$
 */
public abstract class OptionsPane extends JComponent {
	
	/**
	 * reloads the preferences of all components of this
	 * OptionsPane that are instances of 
	 * {@link OptionComponent}
	 *
	 */
	void reloadAllPrefs() {
		
		// loop over them and invoke reload
		int count = getComponentCount();
		for ( int i = 0; i< count; i++ ) {
			Component c = getComponent(i);
			if ( c instanceof OptionComponent) {
				((OptionComponent) c).reloadPref();
			}
		}
	}
	
	/**
	 * saves the preferences of all components that
	 * are instances of {@link OptionComponent}
	 *
	 */
	void saveAllPrefs() {

		// loop over them and invoke save
		int count = getComponentCount();
		for ( int i = 0; i< count; i++ ) {
			Component c = getComponent(i);
			if ( c instanceof OptionComponent) {
				((OptionComponent) c).savePref();
			}
		}
	}

}
