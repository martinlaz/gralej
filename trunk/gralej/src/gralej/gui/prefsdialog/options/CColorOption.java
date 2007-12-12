package gralej.gui.prefsdialog.options;

import gralej.prefs.GralePreferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;

/**
 * 
 * @author no
 * @version $Id$
 */
public class CColorOption extends OptionComponent {

    private JButton colorButton;
    private static final int iconWidth = 30;
    private static final int iconHeight = 15;
    /**
     * 
     */
    private static final long serialVersionUID = 2511191620764297054L;

    protected CColorOption(GralePreferences prefs, String prefkey, String label) {

        super(prefs, prefkey);

        JLabel l = null;
        if (label != null) {
            setLayout(new FlowLayout());
            l = new JLabel(label);
        }

        // create checkbox for the actual function
        setLayout(new FlowLayout());
        colorButton = new JButton();
        reloadPref();
        if (l != null) {
            add(l);
        }
        add(colorButton);

        // add listener
        colorButton.addActionListener(new ButtonClickListener());

    }

    private class ButtonClickListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(CColorOption.this,
                    "Color Chooser", getColor());

            if (newColor != null) {
                setColor(newColor);
            }

        }

    }

    private void setColor(Color c) {
        colorButton.setIcon(new ColorImageIcon(c, new Dimension(iconWidth,
                iconHeight)));
    }

    private Color getColor() {
        return ((ColorImageIcon) colorButton.getIcon()).getColor();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gralej.gui.prefsdialog.OptionComponent#savePref()
     */
    @Override
    public void savePref() {
        getPrefs().putColor(getPrefKey(), getColor());
    }

    @Override
    public void reloadPref() {
        setColor(getPrefs().getColor(getPrefKey()));
    }

}
