package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CIntOption extends OptionComponent {

	private JSpinner intSpinner;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2511191620764297054L;

	protected CIntOption(GralePreferences prefs, String prefkey, String label) {
		this(prefs,prefkey,label,
				prefs.getInt(prefkey + ".min"),
				prefs.getInt(prefkey + ".max"));
	}
	
	
	protected CIntOption(GralePreferences prefs, String prefkey, String label, int min, int max) {

		super(prefs,prefkey);
		
		JLabel l = null;
		if ( label != null ) {
			setLayout(new FlowLayout());
			l = new JLabel(label);
		}
		
		// create checkbox for the actual function
		setLayout(new FlowLayout());
		intSpinner = new JSpinner(new SpinnerNumberModel(
				min, min, max,1));		
		reloadPref();
		add(intSpinner);
		if (l != null) {
			add(l);
		}		
		
	}

	/* (non-Javadoc)
	 * @see gralej.gui.prefsdialog.OptionComponent#savePref()
	 */
	@Override
	public void savePref() {
		//System.err.println("Saving " + getPrefKey());
		getPrefs().putInt(getPrefKey(), 
				(Integer)((SpinnerNumberModel) intSpinner.getModel()).getNumber());
	}

	@Override
	public void reloadPref() {
		((SpinnerNumberModel) intSpinner.getModel()).setValue(
				getPrefs().getInt(getPrefKey()));
	}
	
	
	
	

}
