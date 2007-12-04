package gralej.prefs;

import java.awt.Color;

/**
 * Some static helper methods for preferences
 * @author Niels
 * @version $Id$
 */
public class Toolbox {
	
	/**
	 * throws NumberFormatException
	 */
	public static Color parseRGBA(String rgba) {

		Color res;
		
		try {
			String r = "" + rgba.charAt(0) + rgba.charAt(1);
			String g = "" + rgba.charAt(2) + rgba.charAt(3);
			String b = "" + rgba.charAt(4) + rgba.charAt(5);
			String a = "" + rgba.charAt(6) + rgba.charAt(7);
		
			res = new Color(
				Integer.parseInt(r, 16),
				Integer.parseInt(g, 16),
				Integer.parseInt(b, 16),
				Integer.parseInt(a, 16)
			);
		} catch (Exception e) {
			throw new NumberFormatException(rgba);
		}
		
		
		return res;
		
		
	}

}
