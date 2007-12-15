package gralej.gui.prefsdialog.options;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CColorOption  extends JComponent implements GPrefsChangeListener  {

    private JButton colorButton;
    private static final int iconWidth = 30;
    private static final int iconHeight = 15;
    
    private GralePreferences prefs;
    private String prefkey;    
    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected CColorOption(GralePreferences prefs, String prefkey, String label) {

    	this.prefs = prefs;
    	this.prefkey = prefkey;        

        JLabel l = null;
        if (label != null) {
            setLayout(new FlowLayout());
            l = new JLabel(label);
        }

        // create checkbox for the actual function
        setLayout(new FlowLayout());
        colorButton = new JButton();
        preferencesChange();
        if (l != null) {
            add(l);
        }
        add(colorButton);

        // observers
        colorButton.addActionListener(new ButtonClickListener());
        prefs.addListener(this, prefkey);

    }

    private class ButtonClickListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(CColorOption.this,
                    "Color Chooser", prefs.getColor(prefkey));

            if (newColor != null) {
            	// store color to model
                prefs.putColor(prefkey, newColor);
            }

        }

    }

	public void preferencesChange() {
		// get newly set color from model
		Color c = prefs.getColor(prefkey);
		colorButton.setIcon(new ColorImageIcon(c, new Dimension(iconWidth,
                iconHeight)));
	}

}
