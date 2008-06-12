package gralej.gui.prefsdialog;


import gralej.prefs.GralePreferences;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * the options pane for advanced settings
 *  
 * @author Niels
 * @version $Id$
 */
public class AdvancedOptsPane extends JComponent {

	private static final long serialVersionUID = 948685334475298016L;

        JTable table;
        
    public AdvancedOptsPane(GralePreferences prefs) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints cons = new GridBagConstraints();
        
        
        // create components
        AdvancedOptsTableModel model = new AdvancedOptsTableModel(prefs);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setMinimumSize(new Dimension(500,300));
        
        JLabel l1 = new JLabel("Here you can adjust every little screw of the gear. Be careful!");


        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.WEST;
        cons.gridwidth = GridBagConstraints.REMAINDER; // GUI-"\n"        
        layout.setConstraints(l1, cons);
        add(l1);

        //cons.fill = GridBagConstraints.BOTH;
        //cons.anchor = GridBagConstraints.WEST;
        layout.setConstraints(scrollPane, cons);
        add(scrollPane);

    
    }

}
