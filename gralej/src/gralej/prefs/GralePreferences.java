package gralej.prefs;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.net.URL;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A class for accessing the preferences of our application, uses the singleton
 * pattern and therefore can be used instead of a traditional global variable
 * <strong>Don't forget to {@link #flush()} in order to save preferences to the
 * registry!</strong>
 * 
 * @author Niels Ott
 * @version $Id$
 */
public class GralePreferences {

    private static final String defaultPrefFile = "DefaultPrefs.xml";

    private static GralePreferences instance = null;

    private Preferences backingprefs;

    private DefaultProperties defaults;
    private DefaultProperties prefs;

    private boolean backingsynced;
    private boolean backingavailable;

    /**
     * private constructor (singleton)
     * 
     * @throws GralePrefsInitException
     */
    private GralePreferences() throws GralePrefsInitException {

        prefs = new DefaultProperties();

        // load the default values int a special object
        defaults = new DefaultProperties();
        URL prefsURL = GralePreferences.class.getResource(defaultPrefFile);
        FileInputStream is;
        try {
            is = new FileInputStream(prefsURL.getFile());
            defaults.loadFromXML(is);
        } catch (Exception e) {
            throw new GralePrefsInitException(e);
        }

        // try to initalize the backend
        try {
            backingprefs = Preferences
                    .userNodeForPackage(GralePreferences.class);
            backingavailable = true;
            // load settings, init empty settings with default
            reloadFromBackend();
        } catch (Exception e) {
            backingavailable = false;
            System.err
                    .println("GralePreferences: Java Preferences not available, will not save anything.");
            e.printStackTrace();
            // use the default settings as settings
            try {
                restoreDefaults();
            } catch (BackingStoreException e1) {
                throw new RuntimeException(
                        "Weird Application Error: Restoring without backing store sync cannot cause this exception!?!?",
                        e1);
            }
        }

        setSync(true);

    }

    /**
     * @return true if we sync and the backing store is available, false
     *         otherwise
     */
    public boolean syncAvailable() {

        return (backingsynced && backingavailable);

    }

    /**
     * called by each getter: the backing store might have changed, so if we're
     * in sync we must copy the value from the backing store before returning
     * anything.
     * 
     * @param key
     */
    private void syncKeyBeforeGet(String key) {

        if (syncAvailable()) {
            prefs.put(key, (backingprefs.get(key, defaults.get(key))));
        }

    }

    /**
     * called by each setter: if we're in sync, this will copy the internal
     * value to the backing store
     * 
     * @param key
     */
    private void syncKeyAfterSet(String key) {

        if (syncAvailable()) {
            backingprefs.put(key, prefs.get(key));
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
            syncKeyBeforeGet(key);
            res = prefs.getBoolean(key);
        } catch (NumberFormatException e) {
            throw new NoDefaultPrefSettingException(key, e);
        }

        return res;
    }

    public void putBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public int getInt(String key) {
        int res;

        try {
            syncKeyBeforeGet(key);
            res = prefs.getInt(key);
        } catch (NumberFormatException e) {
            throw new NoDefaultPrefSettingException(key, e);
        }

        return res;
    }

    public void putInt(String key, int value) {
        prefs.putInt(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public long getLong(String key) {
        long res;

        try {
            syncKeyBeforeGet(key);
            res = prefs.getLong(key);
        } catch (NumberFormatException e) {
            throw new NoDefaultPrefSettingException(key, e);
        }

        return res;
    }

    public void putLong(String key, long value) {
        prefs.putLong(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public double getDouble(String key) {
        double res;

        try {
            syncKeyBeforeGet(key);
            res = prefs.getDouble(key);
        } catch (NumberFormatException e) {
            throw new NoDefaultPrefSettingException(key, e);
        }

        return res;
    }

    public void putDouble(String key, double value) {
        prefs.putDouble(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public float getFloat(String key) {
        float res;

        try {
            syncKeyBeforeGet(key);
            res = prefs.getFloat(key);
        } catch (NumberFormatException e) {
            throw new NoDefaultPrefSettingException(key, e);
        }

        return res;
    }

    public void putFloat(String key, float value) {
        prefs.putFloat(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public String get(String key) {
        String res;

        syncKeyBeforeGet(key);
        res = prefs.get(key);
        if (res == null) {
            throw new NoDefaultPrefSettingException(key);
        }

        return res;
    }

    public void put(String key, String value) {
        prefs.put(key, value);
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public Color getColor(String key) {

        Color res;
        syncKeyBeforeGet(key);
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
        syncKeyAfterSet(key);
    }

    /**
     * @throws NoDefaultPrefSettingException
     */
    public Font getFont(String key) {

        Font res;
        syncKeyBeforeGet(key);
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
        syncKeyAfterSet(key);
    }

    /**
     * Set the sync with the backing store. You might want to call
     * {@link #syncToBackend()} after switching on.
     * 
     * @param s
     */
    public void setSync(boolean s) {
        backingsynced = s;
    }

    /**
     * sync back preferences to the backing store. If sync is switched off,
     * nothing every happens, the same holds of the backing store is not
     * available
     */
    public void syncToBackend() {
        if (syncAvailable()) {
            syncEverythingToBackingStore();
        }
    }

    /**
     * flushes the preferences to the registry
     * 
     * @throws BackingStoreException
     */
    public void flush() throws BackingStoreException {
        backingprefs.flush();
    }

    /**
     * this will load the default values. If sync is on this will clear the
     * backing store and keep it in sync
     * 
     * @throws BackingStoreException
     */
    public void restoreDefaults() throws BackingStoreException {
        prefs = defaults.clone();

        // clean up and save if backing store is available
        if (syncAvailable()) {
            prefs.clear();
            syncEverythingToBackingStore();
        }

    }

    /**
     * this will sync everything back to the backing store
     */
    private void syncEverythingToBackingStore() {

        for (Object okey : prefs.keySet()) {
            String key = (String) okey;
            syncKeyAfterSet(key);
        }

    }

    /**
     * Reloads all values from the backend. The behaviour is undefined if the
     * backend is not available. This is done by looping over the defaults. If
     * the backend does not contain a value for a key, the default is tored in
     * the backend.
     */
    public void reloadFromBackend() {

        // will fail if backingprefs are not available

        // loop over defaults and get values from backing store
        for (Object okey : defaults.keySet()) {
            String key = (String) okey;
            prefs.put(key, backingprefs.get(key, defaults.getProperty(key)));
        }

    }

    /**
     * @see Preferences#addPreferenceChangeListener(PreferenceChangeListener)
     * @param pcl
     */
    /*
     * public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
     * prefs.addPreferenceChangeListener(pcl); }
     */

    /**
     * @see Preferences#removePreferenceChangeListener(PreferenceChangeListener)
     */
    /*
     * public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
     * prefs.removePreferenceChangeListener(pcl); }
     */

}
