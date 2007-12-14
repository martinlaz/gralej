package gralej.prefs;

import java.util.EventListener;

/**
 * @version $Id$
 * @author no
 *
 */
public interface GPrefsChangeListener extends EventListener {
	
	public void preferencesChange();

}
