package gralej.gui.prefsdialog;


import gralej.gui.prefsdialog.options.OptionComponentFactory;
import gralej.prefs.GralePreferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * the options pane general application behaviour settings
 *  
 * @author Niels
 * @version $Id$
 */
public class BehaviorOptsPane extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = -6530235629219328411L;

    public BehaviorOptsPane(GralePreferences prefs) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.WEST;
        // cons.weightx = 1.0;

        JComponent o1 = OptionComponentFactory.getComponent(prefs,
                "behavior.internalframes", "boolean",
                "Use internal windows for AVMs");
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o1, cons);
        add(o1);

        JComponent o2 = OptionComponentFactory.getComponent(prefs,
                "behavior.alwaysfitsize", "boolean",
                "Auto-resizing is switched on by default");
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o2, cons);
        add(o2);

        JLabel l3 = new JLabel("Default zoom factor: ");
        JComponent o3 = OptionComponentFactory.getComponent(prefs,
                "behavior.defaultzoom", "int", "%");
        cons.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(l3, cons);
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"
        layout.setConstraints(o3, cons);
        add(l3);
        add(o3);

        /*
         * JLabel l1 = new JLabel("Font 1:"); OptionComponent o1 =
         * OptionComponentFactory.getComponent( prefs, "testfont1", "font");
         * cons.anchor = GridBagConstraints.EAST; layout.setConstraints(l1,
         * cons); add(l1); cons.gridwidth = GridBagConstraints.REMAINDER; //
         * GUI-"\n" layout.setConstraints(o1, cons); add(o1);
         * 
         * JLabel l2 = new JLabel("Font 2 blah:"); OptionComponent o2 =
         * OptionComponentFactory.getComponent( prefs, "testfont2", "font");
         * //layout.setConstraints(l2, cons); add(l2); //cons.gridwidth =
         * GridBagConstraints.REMAINDER; // GUI-"\n" //layout.setConstraints(o2,
         * cons); add(o2);
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
