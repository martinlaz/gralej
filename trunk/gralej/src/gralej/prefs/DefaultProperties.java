/**
 * 
 */
package gralej.prefs;

import java.util.Properties;

/**
 * A class managing default properties that are loaded from a Properties file
 * 
 * @author Niels Ott
 * @version $Id$
 */
public class DefaultProperties extends Properties {

    /**
     * 
     */
    private static final long serialVersionUID = -5540840369041252549L;

    public DefaultProperties() {
        super();
    }

    public DefaultProperties(Properties defaults) {
        super(defaults);
    }

    /**
     * @throws NumberFormatException
     */
    public boolean getBoolean(String key) {
        // "1" is true, "0" is false
        if (getProperty(key).equals("1")) {
            return true;
        } else if (getProperty(key).equals("0")) {
            return true;
        }

        // otherwise, "true" is true and "false" is false
        return Boolean.parseBoolean(getProperty(key));

    }

    public void putBoolean(String key, boolean value) {
        put(key, Boolean.toString(value));
    }

    /**
     * @throws NumberFormatException
     */
    public int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public void putInt(String key, int value) {
        put(key, Integer.toString(value));
    }

    /**
     * @throws NumberFormatException
     */
    public double getDouble(String key) {
        return Double.parseDouble(getProperty(key));
    }

    public void putDouble(String key, double value) {
        put(key, Double.toString(value));
    }

    /**
     * @throws NumberFormatException
     */
    public float getFloat(String key) {
        return Float.parseFloat(getProperty(key));
    }

    public void putFloat(String key, float value) {
        put(key, Float.toString(value));
    }

    /**
     * @throws NumberFormatException
     */
    public long getLong(String key) {
        return Long.parseLong(getProperty(key));
    }

    public void putLong(String key, long value) {
        put(key, Long.toString(value));
    }

    public String get(String key) {
    	
    	String res = new String((String) super.get(key));
    	if ( res.startsWith("$")) {
    		String newkey = res.substring(1);
    		res = new String((String) super.get(newkey));
    	}
    	
        return Toolbox.unEscape(res); 
    }
    
    public String getNoDeRef(String key) {
    	try {
    		return new String((String) super.get(key));
    	} catch (NullPointerException e) {
    		throw new NoDefaultPrefSettingException(key, e);
    	}
    }

    public void put(String key, String value) {
    	// refuse to overwrite existing references
    	String current_value = (String)super.get(key);
    	if ( current_value != null &&
    			current_value.startsWith("$")) {
    		throw new CannotOverwriteReferenceException(key + ":" + current_value);
    	}
    	
        super.put(key, Toolbox.doEscape(new String(value)));
    }
    
    public void putNoDeRef(String key, String value) {
    	super.put(key, new String(value));
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#clone()
     */
    @Override
    public synchronized DefaultProperties clone() {
        DefaultProperties newProp = new DefaultProperties();
        for (Object okey : this.keySet()) {
            String key = (String) okey;
            newProp.put(key, new String(this.get(key)));
        }

        return newProp;
    }

}
