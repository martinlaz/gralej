/**
 * 
 */
package gralej.prefs;

/**
 * There always should be a default value, if there
 * isn't we go mad
 * @author Niels Ott
 * @version $Id$
 */
public class NoDefaultPrefSetting extends RuntimeException {

	private static final long serialVersionUID = 7850488071358010538L;

	public NoDefaultPrefSetting() {
		super();
	}

	public NoDefaultPrefSetting(String message) {
		super(message);
	}

	public NoDefaultPrefSetting(Throwable cause) {
		super(cause);
	}

	public NoDefaultPrefSetting(String message, Throwable cause) {
		super(message, cause);
	}

}
