package gralej.prefs;

/**
 * @version $Id$
 * @author no
 *
 */
public class CannotOverwriteReferenceException extends GralePrefsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9124803281707247011L;

	public CannotOverwriteReferenceException() {
	}

	public CannotOverwriteReferenceException(String message) {
		super(message);
	}

	public CannotOverwriteReferenceException(Throwable cause) {
		super(cause);
	}

	public CannotOverwriteReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

}
