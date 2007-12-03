/**
 * 
 */
package gralej.server;

/**
 * @author Niels Ott
 * @version $Id$
 *
 */
public class NotImplementedInServerException extends Exception {

	private static final long serialVersionUID = 7907971897659822722L;

	public NotImplementedInServerException() {
	}

	public NotImplementedInServerException(String message) {
		super(message);
	}

	public NotImplementedInServerException(Throwable cause) {
		super(cause);
	}

	public NotImplementedInServerException(String message, Throwable cause) {
		super(message, cause);
	}

}
