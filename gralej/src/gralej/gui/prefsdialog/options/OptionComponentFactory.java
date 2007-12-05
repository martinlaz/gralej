package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

/**
 * @version $Id$
 * @author no
 *
 */
public class OptionComponentFactory {
	
	public static OptionComponent getComponent(GralePreferences prefs, String key, String label, String type) {
		
		if ( type.equals("boolean")) {
			return new CBooleanOption(prefs, key, label);
		} else 	if ( type.equals("font")) {
			return new CFontOption(prefs, key, label);
		}

		
		
		return null;
	}

}
