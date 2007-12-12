/**
 * 
 */
package gralej.prefs;

/**
 * There always should be a default value, if there isn't we go mad
 * 
 * @author Niels Ott
 * @version $Id: NoDefaultPrefSettingException.java 173 2007-12-10 17:10:38Z
 *          niels@drni.de $
 */
public class NoDefaultPrefSettingException extends GralePrefsException {

    public NoDefaultPrefSettingException() {
        super();
    }

    public NoDefaultPrefSettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDefaultPrefSettingException(String message) {
        super(message);
    }

    public NoDefaultPrefSettingException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 7850488071358010538L;

}
