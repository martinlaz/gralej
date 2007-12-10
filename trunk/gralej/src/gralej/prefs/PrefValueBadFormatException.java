/**
 * 
 */
package gralej.prefs;

/**
 * Exception to be thrown if a configuration
 * value cannot be parsed into the requested format.
 * @author Niels Ott
 * @version $Id$
 */
public class PrefValueBadFormatException extends GralePrefsException {

	private static final long serialVersionUID = 478852184144492112L;

	public PrefValueBadFormatException() {
		super();
	}

	public PrefValueBadFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrefValueBadFormatException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PrefValueBadFormatException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
