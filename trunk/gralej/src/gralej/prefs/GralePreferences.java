package gralej.prefs;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

/**
 * A class for accessing the preferences of our application, uses the singleton
 * pattern and therefore can be used instead of a traditional global variable
 * <strong>Don't forget to {@link #flush()} in order to save
 * preferences to the registry!</strong>
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
	 * @throws NoDefaultPrefSettingException
	 */
	public boolean getBoolean(String key) {
		boolean res;

		try {
			res = prefs.getBoolean(key, defaults.getBoolean(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putBoolean(String key, boolean value) {
		prefs.putBoolean(key, value);
	}


	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public int getInt(String key)  {
		int res;

		try {
			res = prefs.getInt(key, defaults.getInt(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putInt(String key, int value) {
		prefs.putInt(key, value);
	}


	/**
	 * @throws NoDefaultPrefSettingException 
	 */
	public long getLong(String key)  {
		long res;

		try {
			res = prefs.getLong(key, defaults.getLong(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putLong(String key, long value) {
		prefs.putLong(key, value);
	}
	

	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public double getDouble(String key)  {
		double res;

		try {
			res = prefs.getDouble(key, defaults.getDouble(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putDouble(String key, double value) {
		prefs.putDouble(key, value);
	}
	

	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public float getFloat(String key)  {
		float res;

		try {
			res = prefs.getFloat(key, defaults.getFloat(key));
		} catch (NumberFormatException e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putFloat(String key, float value) {
		prefs.putFloat(key, value);
	}

	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public String get(String key)  {
		String res;

		res = prefs.get(key, defaults.getProperty(key));
		if (res == null) {
			throw new NoDefaultPrefSettingException(key);
		}

		return res;
	}
	
	public void put(String key, String value) {
		prefs.put(key, value);
	}
	

	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public Color getColor(String key)  {

		Color res;
		String rgba = get(key);

		try {
			res = Toolbox.parseRGBA(rgba);
		} catch (Exception e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putColor(String key, Color color) {
		String value = Toolbox.color2RGBA(color);
		prefs.put(key, value);
	}
	
	/**
	 * @throws NoDefaultPrefSettingException
	 */
	public Font getFont(String key)  {

		Font res;
		String fontstr = get(key);

		try {
			res = Toolbox.str2font(fontstr);
		} catch (Exception e) {
			throw new NoDefaultPrefSettingException(key, e);
		}

		return res;
	}
	
	public void putFont(String key, Font font) {
		String value = Toolbox.font2str(font);
		prefs.put(key, value);
	}
	
	
	/**
	 * flushes the preferences to the registry
	 * @throws BackingStoreException
	 */
	public void flush() throws BackingStoreException {
		prefs.flush();
	}
	
	public void importPreferences(InputStream is) throws IOException, InvalidPreferencesFormatException {
		Preferences.importPreferences(is);
	}
	
	public void exportPreferences(OutputStream os) throws IOException, InvalidPreferencesFormatException, BackingStoreException {
		prefs.exportSubtree(os);
	}
	
	/**
	 * this will clear the preferences registry
	 * and load all default values 
	 * @throws BackingStoreException 
	 */
	public void restoreDefaults() throws BackingStoreException {
		prefs.clear();
		
		for ( Object okey : defaults.keySet() ) {
			String key = (String)okey;
			prefs.put(key, defaults.getProperty(key));
		}
		
	}
	


}
