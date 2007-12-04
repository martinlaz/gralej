package gralej.prefs;

import java.awt.Color;
import java.io.FileInputStream;
import java.net.URL;
import java.util.prefs.Preferences;

/**
 * A class for accessing the preferences of our application, uses the singleton
 * pattern and therefore can be used instead of a traditional global variable
 * 
 * @author Niels Ott
 * @version $Id$
 */
public class GralePreferences {

	private static final String defaultPrefFile = "DefaultPrefs.xml";

	private static GralePreferences instance = null;

	private Preferences prefs;

	private DefaultProperties defaults;

	/**
	 * private constructor (singleton)
	 * 
	 * @throws GralePrefsInitException
	 */
	private GralePreferences() throws GralePrefsInitException {
		prefs = Preferences.userNodeForPackage(GralePreferences.class);

		defaults = new DefaultProperties();
		URL prefsURL = GralePreferences.class.getResource(defaultPrefFile);
		FileInputStream is;
		try {
			is = new FileInputStream(prefsURL.getFile());
			defaults.loadFromXML(is);
		} catch (Exception e) {
			throw new GralePrefsInitException(e);
		}

	}

	/**
	 * @return the instance if our preferences
	 * @throws GralePrefsInitException
	 */
	public static GralePreferences getInstance() throws GralePrefsInitException {
		if (instance == null) {
			instance = new GralePreferences();
		}
		return instance;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public boolean getBoolean(String key) {
		boolean res;

		try {
			res = prefs.getBoolean(key, defaults.getBoolean(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public int getInt(String key) {
		int res;

		try {
			res = prefs.getInt(key, defaults.getInt(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public long getLong(String key) {
		long res;

		try {
			res = prefs.getLong(key, defaults.getLong(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public double getDouble(String key) {
		double res;

		try {
			res = prefs.getDouble(key, defaults.getDouble(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public float getFloat(String key) {
		float res;

		try {
			res = prefs.getFloat(key, defaults.getFloat(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public String get(String key) {
		String res;

		res = prefs.get(key, defaults.getProperty(key));
		if (res == null) {
			throw new NoDefaultPrefSetting(key);
		}

		return res;
	}

	/**
	 * @throws NoDefaultPrefSetting
	 */
	public Color getColor(String key) {

		Color res;
		String rgba = get(key);

		try {
			res = Toolbox.parseRGBA(rgba);
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSetting(key, e);
		}

		return res;
	}

	// TODO: ad putters here

}
