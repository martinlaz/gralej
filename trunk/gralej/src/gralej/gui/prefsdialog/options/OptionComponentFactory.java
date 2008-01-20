package gralej.gui.prefsdialog.options;

import javax.swing.JComponent;

import gralej.prefs.GralePreferences;

/**
 * @version $Id: OptionComponentFactory.java 172 2007-12-08 14:58:21Z
 *          niels@drni.de $
 * @author no
 * 
 */
public class OptionComponentFactory {

	public static JComponent getComponent(GralePreferences prefs,
			String key, String type, Object label) {

		if (type.equals("boolean")) {
			return new CBooleanOption(prefs, key, (String)label);
		} else if (type.equals("font")) {
			return new CFontOption(prefs, key, (String)label);
		} else if (type.equals("int")) {
			return new CIntOption(prefs, key, (String)label);
		} else if (type.equals("color")) {
			return new CColorOption(prefs, key, (String)label);
		} else if (type.equals("dropdown")) {
			return new DropdownOption(prefs, key, (String[])label);
		}


		return null;
	}

    public static JComponent getComponent(GralePreferences prefs,
            String key, String type) {
        return getComponent(prefs, key, type, null);
    }

}
