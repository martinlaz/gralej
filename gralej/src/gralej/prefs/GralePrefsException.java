package gralej.prefs;

/**
 * Everything that goes wrong in the {@link GralePreferences}
 * is a subclass of this exception
 * @version $Id$
 * @author niels
 */
public class GralePrefsException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8774481724882714357L;

	public GralePrefsException() {
	}

	public GralePrefsException(String message) {
		super(message);
	}

	public GralePrefsException(Throwable cause) {
		super(cause);
	}

	public GralePrefsException(String message, Throwable cause) {
		super(message, cause);
	}

}
