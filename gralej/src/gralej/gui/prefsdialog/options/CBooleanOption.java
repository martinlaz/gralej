package gralej.gui.prefsdialog.options;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CBooleanOption extends JComponent implements GPrefsChangeListener  {

    private JCheckBox checkbox;
    private GralePreferences prefs;
    private String prefkey;

    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected CBooleanOption(GralePreferences prefs, String prefkey,
            String label) {
    	this.prefs = prefs;
    	this.prefkey = prefkey;


        String l = label;
        if (l == null) {
            l = "";
        }

        // create checkbox for the actual function
        setLayout(new FlowLayout());
        checkbox = new JCheckBox(l);
        preferencesChange();
        add(checkbox);
        
        // register observers
        checkbox.addChangeListener(new changeListener());
        prefs.addListener(this, prefkey);

    }
    
    private class changeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			prefs.putBoolean(prefkey, checkbox.isSelected());
		}
    	
    }


	public void preferencesChange() {
		checkbox.setSelected(prefs.getBoolean(prefkey));
	}

}
