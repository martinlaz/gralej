package gralej.gui.icons;

import java.util.LinkedList;
import java.util.List;

/**
 * Factory for retrieving icon themes
 * @author Niels Ott
 * @version $Id$
 */
public class IconThemeFactory {
	
	/**
	 * Returns the icon theme identified by the name
	 * or the default icon theme if the requested
	 * one cannot be found.
	 * @param name the name of the icon theme
	 * @return the icon theme or the default.
	 */
	public static IconTheme getIconTheme(String name) {
		
		if ( name.equals("traditional" )) {
			return new GenericIconTheme("traditional");
		} else if ( name.equals("crystal" )) {
			return new GenericIconTheme("crystal");
		}

		
		
		// "traditional" is the default for now
		return new GenericIconTheme("traditional");
		
	}
	
	/**
	 * 
	 * @return a list names of the themes available
	 */
	public static List<String> getThemeNames() {
		
		LinkedList<String> res = new LinkedList<String>();
		
		res.add("traditional");
		res.add("crystal");
		
		return res;
		
	}

}
