/**
 * 
 */
package gralej.prefs;

import java.util.Properties;

/**
 * A class managing default properties that are
 * loaded from a Properties file
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
		if ( getProperty(key).equals("1") ) {
			return true;
		} else 	if ( getProperty(key).equals("0") ) {
			return true;
		}
		
		// otherwise, "true" is true and "false" is false
		return Boolean.parseBoolean(getProperty(key));

	}

	/**
	 * @throws NumberFormatException
	 */
	public int getInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	/**
	 * @throws NumberFormatException
	 */
	public double getDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	/**
	 * @throws NumberFormatException
	 */
	public float getFloat(String key) {
		return Float.parseFloat(getProperty(key));
	}
	
	/**
	 * @throws NumberFormatException
	 */
	public long getLong(String key) {
		return Long.parseLong(getProperty(key));
	}
	
	
	

}
