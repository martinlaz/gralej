package gralej.gui.prefsdialog.options;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * 
 * @author Niels
 * @version $Id$
 */
public class DropdownOption extends JComponent implements GPrefsChangeListener  {

    private JComboBox combobox;
    private GralePreferences prefs;
    private String prefkey;
    private String[] options;

    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected DropdownOption(GralePreferences prefs, String prefkey,
            String[] opts) {
    	this.prefs = prefs;
    	this.prefkey = prefkey;

    	// something went wrong, display some useless default
    	if ( opts == null ) {
    		options = new String[1];
    		options[0] = "(default)";
    	} else {
    		options = new String[opts.length];
    		System.arraycopy(opts, 0, options, 0, opts.length);
    	}


        // create combobox for the actual function
        setLayout(new FlowLayout());
        combobox = new JComboBox(options);
        preferencesChange();
        add(combobox);
        
        // register observers
        combobox.addActionListener(new changeListener());
        prefs.addListener(this, prefkey);

    }
    
    private class changeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String name = combobox.getModel().getSelectedItem().toString();
			prefs.put(prefkey, name);
		}
    	
    }


	public void preferencesChange() {
		
		String newval = prefs.get(prefkey);

		// try to find new value in model
		boolean found = false;
		int selIndex = -1;

		// search for the given option in the combo box's values
		for (selIndex = 0; selIndex < options.length; selIndex++) {
			if (options[selIndex].equals(newval)) {
				found = true;
				break;
			}
		}

		// if the value can't be found, introduce it and re-initialize the combo box
		if (! found ) {
			System.err.println("Not found: " + newval);
			String[] newoptions = new String[options.length+1];
			System.arraycopy(options, 0, newoptions, 1, options.length);
			newoptions[0] = newval;
			options = newoptions;
			combobox.setModel(new DefaultComboBoxModel(options));
			// TODO: limit recursion?
			preferencesChange();
		} else {
			// set the option selected
			combobox.setSelectedIndex(selIndex);
		}

		
	}

}
