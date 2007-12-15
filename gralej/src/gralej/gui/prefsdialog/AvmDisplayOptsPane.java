package gralej.gui.prefsdialog;


import gralej.gui.prefsdialog.options.OptionComponentFactory;
import gralej.prefs.GralePreferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * the options pane for AVM display options
 * 
 * @author Niels
 * @version $Id$
 */
public class AvmDisplayOptsPane extends JComponent {
	
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private GralePreferences prefs;

    /**
     * 
     */
    private static final long serialVersionUID = -6530235629219328411L;

    private void addFontOption(String label, String fontKey, String colorKey) {
    	
        JLabel l1 = new JLabel(label);
        JComponent f1 = OptionComponentFactory.getComponent(prefs,
                fontKey, "font");
        JComponent c1 = OptionComponentFactory.getComponent(prefs,
                colorKey, "color");
        cons.anchor = GridBagConstraints.EAST;
        cons.gridwidth = GridBagConstraints.BASELINE;
        layout.setConstraints(l1, cons);
        add(l1);
        cons.anchor = GridBagConstraints.WEST;
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(f1, cons);
        add(f1);
        cons.anchor = GridBagConstraints.WEST;
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(c1, cons);
        add(c1);    	
    	
    	
    }
    
    
    public AvmDisplayOptsPane(GralePreferences prefs) {

    	this.prefs = prefs;
    	
        layout = new GridBagLayout();
        setLayout(layout);

        cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        //cons.weightx = 2.0;
        
        addFontOption("Sort names:", "avm-display.sortnames.font",
        		"avm-display.sortnames.color");

        addFontOption("Tree node labels:", "avm-display.tree.node.font",
			"avm-display.tree.node.color");

        addFontOption("Tree leaf labels:", "avm-display.tree.leaf.font",
			"avm-display.tree.leaf.color");
        
        
        
        
/*

        JLabel l2 = new JLabel("Font 2 blah:");
        OptionComponent o2 = OptionComponentFactory.getComponent(prefs,
                "testfont2", "font");
        cons.anchor = GridBagConstraints.EAST;
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l2, cons);
        add(l2);
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o2, cons);
        add(o2);

        JLabel l3 = new JLabel("Color 1:");
        OptionComponent o3 = OptionComponentFactory.getComponent(prefs,
                "testcolor1", "color");
        cons.anchor = GridBagConstraints.EAST;
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l3, cons);
        add(l3);
        cons.anchor = GridBagConstraints.WEST;
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o3, cons);
        add(o3);

        JLabel l4 = new JLabel("Color 2:");
        OptionComponent o4 = OptionComponentFactory.getComponent(prefs,
                "testcolor2", "color");
        cons.anchor = GridBagConstraints.EAST;
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l4, cons);
        add(l4);
        cons.anchor = GridBagConstraints.WEST;
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o4, cons);
        add(o4);
*/
        /*
         * // put label to (space,x) of the container
         * layout.putConstraint(SpringLayout.WEST, l1, paddingSpace,
         * SpringLayout.WEST,this); // put option component 5px right to the
         * label layout.putConstraint(SpringLayout.WEST, o1, spaceAfterLabel,
         * SpringLayout.EAST, l1); // put option component (x,space) from the
         * top layout.putConstraint(SpringLayout.NORTH, o1, paddingSpace,
         * SpringLayout.NORTH,this);
         *  // put the label on the vertical middle of the option component
         * layout.putConstraint(SpringLayout.VERTICAL_CENTER, l1, 0,
         * SpringLayout.VERTICAL_CENTER, o1);
         *  // adjust size of the whole thing plus padding
         * layout.putConstraint(SpringLayout.EAST, this, paddingSpace,
         * SpringLayout.EAST, o1); layout.putConstraint(SpringLayout.SOUTH,
         * this, paddingSpace, SpringLayout.SOUTH, o1);
         */
    }

}
