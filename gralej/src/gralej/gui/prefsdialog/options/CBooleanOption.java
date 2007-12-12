package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CBooleanOption extends OptionComponent {

    private JCheckBox checkbox;

    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected CBooleanOption(GralePreferences prefs, String prefkey,
            String label) {

        super(prefs, prefkey);

        String l = label;
        if (l == null) {
            l = "";
        }

        // create checkbox for the actual function
        setLayout(new FlowLayout());
        checkbox = new JCheckBox(l);
        reloadPref();
        add(checkbox);

    }

    /*
     * (non-Javadoc)
     * 
     * @see gralej.gui.prefsdialog.OptionComponent#savePref()
     */
    @Override
    public void savePref() {
        // System.err.println("Saving " + getPrefKey());
        getPrefs().putBoolean(getPrefKey(), checkbox.isSelected());
    }

    @Override
    public void reloadPref() {
        checkbox.setSelected(getPrefs().getBoolean(getPrefKey()));
    }

}
