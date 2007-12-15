package gralej.gui.prefsdialog.options;


import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CIntOption  extends JComponent implements GPrefsChangeListener  {

    private JSpinner intSpinner;
    private GralePreferences prefs;
    private String prefkey;    
    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected CIntOption(GralePreferences prefs, String prefkey, String label) {
        this(prefs, prefkey, label, prefs.getInt(prefkey + ".min"), prefs
                .getInt(prefkey + ".max"));
    }

    protected CIntOption(GralePreferences prefs, String prefkey, String label,
            int min, int max) {

    	this.prefs = prefs;
    	this.prefkey = prefkey;        

        JLabel l = null;
        if (label != null) {
            setLayout(new FlowLayout());
            l = new JLabel(label);
        }

        // create checkbox for the actual function
        setLayout(new FlowLayout());
        intSpinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
        preferencesChange();
        add(intSpinner);
        if (l != null) {
            add(l);
        }
        
        // observers
        intSpinner.addChangeListener(new changeListener());
        prefs.addListener(this, prefkey);
        

    }

    private class changeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
	        prefs.putInt(
	                prefkey,
	                (Integer) ((SpinnerNumberModel) intSpinner.getModel())
	                        .getNumber());
		}
    	
    }    
    

	public void preferencesChange() {
        ((SpinnerNumberModel) intSpinner.getModel()).setValue(
        		prefs.getInt(prefkey));		
		
	}

}
