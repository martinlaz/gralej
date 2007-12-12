package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

import javax.swing.JComponent;

/**
 * intermediate parent for option jcomponents so that they can be handled all
 * the same
 * 
 * @author Niels Ott
 * @version $Id$
 *          TODO: do we really need this layer of abstraction?
 */
public abstract class OptionComponent extends JComponent {

    private String prefkey;
    private GralePreferences prefs;
    private String prefvalue;

    public OptionComponent(GralePreferences prefs, String prefkey) {
        this.prefs = prefs;
        this.prefkey = prefkey;
        prefvalue = prefs.get(prefkey);
    }

    // TODO: is this really needed?
    protected void setPrefValue(String prefvalue) {
        this.prefvalue = prefvalue;
    }

    protected GralePreferences getPrefs() {
        return prefs;
    }

    protected String getPrefKey() {
        return prefkey;
    }

    // TODO: shouldn't this be abstract?
    public void savePref() {
        prefs.put(prefkey, prefvalue);
    }

    public abstract void reloadPref();

}
