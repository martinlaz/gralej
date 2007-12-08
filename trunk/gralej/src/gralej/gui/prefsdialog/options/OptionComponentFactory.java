package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

/**
 * @version $Id$
 * @author no
 *
 */
public class OptionComponentFactory {
	
	public static OptionComponent getComponent(GralePreferences prefs, String key, String type, String label) {
		
		if ( type.equals("boolean")) {
			return new CBooleanOption(prefs, key, label);
		} else 	if ( type.equals("font")) {
			return new CFontOption(prefs, key, label);
		} else 	if ( type.equals("int")) {
			return new CIntOption(prefs, key, label);
		} else 	if ( type.equals("color")) {
			return new CColorOption(prefs, key, label);
		}
		
		
		return null;
	}
	
	public static OptionComponent getComponent(GralePreferences prefs, String key, String type) {
		return getComponent(prefs, key, type, null);
	}

}
