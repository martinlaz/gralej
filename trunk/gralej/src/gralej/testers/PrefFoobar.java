package gralej.testers;

import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;
import gralej.prefs.NoDefaultPrefSettingException;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

/**
 * some testing of the GralePreferences class
 * @author Niels OTt
 * @version $Id$
 */
public class PrefFoobar {

	/**
	 * @param args
	 * @throws GralePrefsInitException 
	 * @throws NoDefaultPrefSettingException 
	 * @throws BackingStoreException 
	 * @throws InvalidPreferencesFormatException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws GralePrefsInitException, NoDefaultPrefSettingException, IOException, InvalidPreferencesFormatException, BackingStoreException {
		
		Font f = new Font("Arial", 1 ,16);
		Color c = Color.CYAN;
		
		GralePreferences prefs = GralePreferences.getInstance();
		prefs.restoreDefaults();
		/*
		Font f1 = prefs.getFont("testfont");
		System.err.println(f1);
		Color c1 = prefs.getColor("testcol");
		System.err.println(c1);
		
		
		prefs.putFont("test2_font", f);
		prefs.putColor("test2_col", c);*/

		Color c1 = prefs.getColor("testcol");
		System.err.println(c1);

		
		
		prefs.exportPreferences(System.out);
		

	}

}
