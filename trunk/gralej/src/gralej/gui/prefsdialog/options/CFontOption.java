package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CFontOption extends OptionComponent {
	
	private JComboBox nameCombo;
	private JComboBox styleCombo;
	private JSpinner sizeSpinner;
	private Font theFont;
	private String[] fontNames;
	private String[] fontStyles = { "regular", "bold", "italic", "bold+italic" };
	private static final int fontSizeMin = 6;
	private static final int fontSizeMax = 72;
	

	public CFontOption(GralePreferences prefs, String prefkey, String label) {
		super(prefs, prefkey);
		
		setLayout(new FlowLayout());
		
		// create components
		JLabel l = null;
		if ( label != null ) {
			setLayout(new FlowLayout());
			l = new JLabel(label);
		}
		
		// font name combo box
		fontNames = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		nameCombo = new JComboBox(fontNames); 
		
		// font style combo box, 
		styleCombo = new JComboBox(fontStyles);
		
		// font size spinner
		sizeSpinner = new JSpinner(new SpinnerNumberModel(
				fontSizeMin, fontSizeMin, fontSizeMax,1));

		// initialize
		reloadPref();
		
		if (l != null) {
			add(l);
		}
		add(nameCombo);
		add(styleCombo);
		add(sizeSpinner);
		

		
	}
	
	private void createFont() {
		String name = nameCombo.getModel().getSelectedItem().toString();
		int size = ((SpinnerNumberModel)sizeSpinner.getModel()).getNumber().intValue();
		// happy coincident: index values are the same as font style values
		int style = styleCombo.getSelectedIndex();
		theFont = new Font(name,style, size);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2730817291126385085L;

	/* (non-Javadoc)
	 * @see gralej.gui.prefsdialog.OptionComponent#savePref()
	 */
	@Override
	public void savePref() {
		createFont();
		getPrefs().putFont(getPrefKey(), theFont);
	}

	@Override
	public void reloadPref() {
		theFont = getPrefs().getFont(getPrefKey());

		// select the right font name
		int selIndex;
		for ( selIndex = 0; selIndex < fontNames.length; selIndex++) {
			if ( fontNames[selIndex].equals(theFont.getName()) ) {
				break;
			}
		}
		nameCombo.setSelectedIndex(selIndex);
		
		// select the right font style
		// the selection index coincides
		// with java font style numbers
		styleCombo.setSelectedIndex(theFont.getStyle());
		
		// set size spinner
		((SpinnerNumberModel)sizeSpinner.getModel()).setValue(theFont.getSize());
		
	}

}
