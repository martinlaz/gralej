package gralej.gui.prefsdialog;

import gralej.gui.prefsdialog.options.OptionComponent;
import gralej.gui.prefsdialog.options.OptionComponentFactory;
import gralej.prefs.GralePreferences;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

/**
 * the options pane for AVM display options
 * @author Niels
 * @version $Id$
 */
public class AvmDisplayOptsPane extends JComponent {
	
	static final int spaceAfterLabel = 5;
	static final int paddingSpace = 15;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6530235629219328411L;
	
	
	public AvmDisplayOptsPane(GralePreferences prefs) {
		
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        
        JLabel l1 = new JLabel("Font 1:");
        OptionComponent o1 = OptionComponentFactory.getComponent(
        		prefs, "testfont1", "font");
        add(l1);
        add(o1);
        
        // put label to (space,x) of the container 
        layout.putConstraint(SpringLayout.WEST, l1,
        		paddingSpace,
        		SpringLayout.WEST,this);
        // put option component 5px right to the label
        layout.putConstraint(SpringLayout.WEST, o1,
        		spaceAfterLabel,
        		SpringLayout.EAST, l1);
        // put option component (x,space) from the top
        layout.putConstraint(SpringLayout.NORTH, o1,
        		paddingSpace,
        		SpringLayout.NORTH,this);
        
        // put the label on the vertical middle of the option component
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, l1,
        		0,
        		SpringLayout.VERTICAL_CENTER, o1);        

        // adjust size of the whole thing plus padding
        layout.putConstraint(SpringLayout.EAST, this,
        		paddingSpace,
        		SpringLayout.EAST, o1);
        layout.putConstraint(SpringLayout.SOUTH, this,
        		paddingSpace,
        		SpringLayout.SOUTH, o1);
		
	}

	

}
