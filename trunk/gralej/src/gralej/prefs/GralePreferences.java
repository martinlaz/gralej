package gralej.prefs;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
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
public class GralePreferences  {

    private static final String defaultPrefFile = "DefaultPrefs.xml";

    private static GralePreferences instance = null;

    private Preferences backingprefs;

    private DefaultProperties defaults;
    private DefaultProperties prefs;

    private boolean backingsynced;
    private boolean backingavailable;
    
    private BackingStoreListener backinglistener;
    
    private Vector<Entry<String, GPrefsChangeListener>> observers;

    /**
     * private constructor (singleton)
     * 
     * @throws GralePrefsInitException
     */
    private GralePreferences() throws GralePrefsInitException {
    	
    	observers = new Vector<Entry<String, GPrefsChangeListener>>();

        prefs = new DefaultProperties();
        backinglistener = new BackingStoreListener();

        // load the default values int a special object
        // FIXME: loading from jars doesn't work
        defaults = new DefaultProperties();
        InputStream is;
        try {
            is = GralePreferences.class.getResourceAsStream(defaultPrefFile);
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
            // add listener to the backend
            backingprefs.addPreferenceChangeListener(backinglistener);
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
     * our private listener for Java's {@link Preferences}. 
     * The outer class does not implement this on purpose 
     * in order to avoid wild adding of it to some preferences.
     */
    private class BackingStoreListener implements PreferenceChangeListener {

		private boolean active = true;
   	
		public void preferenceChange(PreferenceChangeEvent evt) {

			if ( active ) {
				
				prefs.put(evt.getKey(), evt.getNewValue());
		        notifyObservers(evt.getKey());
				
			}
		}
		
		public void on() {
			active = true;
		}
		
		public void off() {
			active = false;
		}
		
    }
    
    private class Memento implements ObserverListMemento {
    	
    	private Vector<Entry<String, GPrefsChangeListener>> mem_observers;

    	/**
    	 * create a memento including the current state of the
    	 * GralePreferences' observers
    	 */
    	public Memento() {
    		
    		mem_observers = new Vector<Entry<String, GPrefsChangeListener>>();
    		
    		// clone all but the actuall values
    		for ( Entry<String,GPrefsChangeListener> item : observers ) {
    			mem_observers.add(
    					new SimpleEntry<String, GPrefsChangeListener>(
    							item.getKey(),item.getValue()));
    		}
    		
    	}
    	
    	/**
    	 * Restores the state of GralePreferences' observers
    	 * from this memento.
    	 */
    	public void restoreState() {
    		observers = mem_observers;
    	}
    	
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
            prefs.putNoDeRef(key, (backingprefs.get(key, defaults.getNoDeRef(key))));
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
        	backinglistener.off();
            backingprefs.put(key, prefs.get(key));
        	backinglistener.on();
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
        notifyObservers(key);
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
        notifyObservers(key);
        
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
        notifyObservers(key);
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
        notifyObservers(key);
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
        notifyObservers(key);
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
        notifyObservers(key);
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
        notifyObservers(key);
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
        notifyObservers(key);
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

    	// FIXME: collect changes and notify observers
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
     * Add a  listener to the preferences. The listener
     * will be notified of changes of the given key prefix.
     * Specifying "avm." will lead to notifications of the change
     * of "avm.blah.blubb". To be notified of single events, specify
     * the full name of the configuration key.
     * <code>null</code> listeners (and keyPrefixes) or such already present 
     * are ignored.
     */
    public void addListener(GPrefsChangeListener l, String keyPrefix) {
    	
    	if (l == null || keyPrefix == null ) {
    		return;
    	}
    	
    	// check for already existing mappings and exit if 
    	// one is fount
    	for ( Entry<String,GPrefsChangeListener> item : observers ) {
    		if ( item.getKey().equals(keyPrefix) && 
    				item.getValue().equals(l) ) {
    			return;
    		}
    	}
    	
    	// otherwise store new mapping
    	SimpleEntry<String, GPrefsChangeListener> entry =
    		new SimpleEntry<String, GPrefsChangeListener>(keyPrefix, l);
    	
    	observers.add(entry);
    	
    }
    
    public void removeListener(GPrefsChangeListener l) {
    	
    	for ( int i = 0; i < observers.size(); i++) {
    		Entry<String,GPrefsChangeListener> item = observers.get(i);
    		
    		// if this matches the listener, remove it
    		if ( item.getValue().equals(l) ) {
    			observers.remove(i);
    			// unless the observer list is empty, go back
    			// one element as the remaining elements have moved 
    			// up one position
    			if ( observers.size() > 0 ) {
    				i--;
    			}
    		}    		
    		
    	}
    	
    }
    
    /**
     * This removes all listeners, which is dangerous.
     *
     */
    public void removeAllListeners() {
    	observers.removeAllElements();
    }
    
    /**
     * This returns all observers of the preferences
     * encapsulated in a memento.
     * @return
     */
    public ObserverListMemento getObservers() {
    	return new Memento();
    }
    
    /**
     * restore the obeservers list of this class
     * from the memento
     * @param m
     */
    public void setObservers(ObserverListMemento m)  {
    	if ( m instanceof Memento ) {
    		((Memento) m).restoreState();
    	} else {
    		throw new RuntimeException("Internal error: GralePreferences " +
    				"does not accept foreign mementos");
    	}
    }
    
    private void notifyObservers(String key) {

    	for ( Entry<String,GPrefsChangeListener> item : observers ) {
    		if ( key.startsWith(item.getKey()) ) {
    			item.getValue().preferencesChange();
    		}
    	}
    	
    }
    
    

}
