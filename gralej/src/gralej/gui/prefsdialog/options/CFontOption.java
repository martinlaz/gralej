package gralej.gui.prefsdialog.options;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
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
public class CFontOption  extends JComponent implements GPrefsChangeListener  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9149025415938681171L;
	private JComboBox nameCombo;
    private JComboBox styleCombo;
    private JSpinner sizeSpinner;
    private String[] fontNames;
    private String[] fontStyles = { "regular", "bold", "italic", "bold+italic" };
    private static final int fontSizeMin = 6;
    private static final int fontSizeMax = 72;
    private GralePreferences prefs;
    private String prefkey;     

    public CFontOption(GralePreferences prefs, String prefkey, String label) {
    	this.prefs = prefs;
    	this.prefkey = prefkey;         
        

        setLayout(new FlowLayout());

        // create components
        JLabel l = null;
        if (label != null) {
            setLayout(new FlowLayout());
            l = new JLabel(label);
        }

        // font name combo box
        fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        nameCombo = new JComboBox(fontNames);

        // font style combo box,
        styleCombo = new JComboBox(fontStyles);

        // font size spinner
        sizeSpinner = new JSpinner(new SpinnerNumberModel(fontSizeMin,
                fontSizeMin, fontSizeMax, 1));

        // initialize
        preferencesChange();

        if (l != null) {
            add(l);
        }
        add(nameCombo);
        add(styleCombo);
        add(sizeSpinner);
        
        // observers
        changeListener listener = new changeListener();
        nameCombo.addActionListener(listener);
        styleCombo.addActionListener(listener);
        sizeSpinner.addChangeListener(listener);
        prefs.addListener(this, prefkey);

    }

    
    private class changeListener implements ChangeListener, ActionListener {
    	
    	private void doUpdate() {
	        String name = nameCombo.getModel().getSelectedItem().toString();
	        int size = ((SpinnerNumberModel) sizeSpinner.getModel()).getNumber()
	                .intValue();
	        // happy coincident: index values are the same as font style values
	        int style = styleCombo.getSelectedIndex();
	        prefs.putFont(prefkey, new Font(name, style, size));
    	}

		public void stateChanged(ChangeEvent e) {
			doUpdate();
		}

		public void actionPerformed(ActionEvent e) {
			doUpdate();
		}
    	
    }      



	public void preferencesChange() {
        Font theFont = prefs.getFont(prefkey);
        
        // defaul fonts
        String defaults[] = { "Arial", "Helvetica", "SansSerif" }; 

        // select the right font name
        String fontName = theFont.getName();
        boolean fontfound = false;
        int selIndex = -1;
        int alternativeIndex = 0;
        while ( ! fontfound ) {
        	for (selIndex = 0; selIndex < fontNames.length; selIndex++) {
        		if (fontNames[selIndex].equals(fontName)) {
        			fontfound = true;
        			break;
        		}
        	}
        	
        	if (! fontfound ) {
        		try {
        			fontName = defaults[alternativeIndex++];
        		}
        		catch (IndexOutOfBoundsException e) {
        			throw new RuntimeException("Not even the default fonts are available");
        		}
        	}
        }

        // set the font to the combo box
        nameCombo.setSelectedIndex(selIndex);

        // select the right font style
        // the selection index coincides
        // with java font style numbers
        styleCombo.setSelectedIndex(theFont.getStyle());

        // set size spinner
        ((SpinnerNumberModel) sizeSpinner.getModel()).setValue(theFont
                .getSize());
		
	}

}
