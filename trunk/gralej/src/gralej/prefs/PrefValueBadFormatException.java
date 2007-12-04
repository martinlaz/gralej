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
public class PrefValueBadFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8056855465468084405L;

	public PrefValueBadFormatException() {
	}

	public PrefValueBadFormatException(String message) {
		super(message);
	}

	public PrefValueBadFormatException(Throwable cause) {
		super(cause);
	}

	public PrefValueBadFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
