package gralej.gui.prefsdialog;


import gralej.gui.icons.IconThemeFactory;
import gralej.gui.prefsdialog.options.OptionComponentFactory;
import gralej.prefs.GralePreferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * the options pane for look and feel settings.
 *  
 * @author Niels
 * @version $Id$
 */
public class LookAndFeelOptsPane extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -6530235629219328411L;

    public LookAndFeelOptsPane(GralePreferences prefs) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.WEST;
        // cons.weightx = 1.0;

        JLabel l1 = new JLabel("Java Look & Feel: ");
        JComponent o1 = OptionComponentFactory.getComponent(prefs,
                "gui.l+f.java-l+f", "dropdown", getLooksAndFeels());
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l1, cons);
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o1, cons);
        add(l1);
        add(o1);
        
        JLabel l2 = new JLabel("Icon Theme: ");
        JComponent o2 = OptionComponentFactory.getComponent(prefs,
                "gui.l+f.icontheme", "dropdown", getIconThemes());
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l2, cons);
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o2, cons);
        add(l2);
        add(o2);
        


    }
    
    private String[] getLooksAndFeels() {
    	
    	UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
    	String[] res = new String[plaf.length+1];

    	res[0] = "System Default";
    	for (int i = 0, n = plaf.length; i < n; i++) {
    		res[i+1] = plaf[i].getClassName();
    	}

    	return res;
    	
    }
    
    private String[] getIconThemes() {
    	
    	String[] res = new String[IconThemeFactory.getThemeNames().size()];
    	IconThemeFactory.getThemeNames().toArray(res);
    	return res;
    	
    }
    

}
