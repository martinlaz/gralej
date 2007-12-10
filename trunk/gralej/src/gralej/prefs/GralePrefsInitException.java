/**
 * 
 */
package gralej.prefs;

/**
 * Thrown if the GralePreferences cannot be initialized
 * either because the system croaks or the default
 * values are absent...
 * @author Niels Ott
 * @version $Id$
 */
public class GralePrefsInitException extends GralePrefsException {

	private static final long serialVersionUID = -8041431375691755974L;

	public GralePrefsInitException() {
		super();
	}

	public GralePrefsInitException(String message) {
		super(message);
	}

	public GralePrefsInitException(Throwable cause) {
		super(cause);
	}

	public GralePrefsInitException(String message, Throwable cause) {
		super(message, cause);
	}

}
