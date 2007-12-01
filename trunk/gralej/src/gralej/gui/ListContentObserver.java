/**
 * 
 */
package gralej.gui;

//import java.beans.PropertyVetoException;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.event.*;
import javax.swing.DefaultListModel;

import gralej.controller.*;

/**
 * The ListContentObserver is a piece of GRALE's GUI.
 * It displays the file list to the left.
 * 
 * When an item is selected, the observer resets the focus of the model.
 * Then the model notifies its observers (update()). 
 * On update(), the list is rebuilt (selection is automatically done, but delete and add are
 * also changes in the model which would call update. unless the model knows about the
 * specific form of this observer, a complete rebuild is the only choice (?) )
 * 
 * Actually, on open() the change in the model is enough to change the displayed list.
 * it does not have to be rebuilt. might also apply to select and close. then the 
 * listcontentobserver does not need an update() function
 * 
 * @author Armin
 *
 */
public class ListContentObserver extends ContentObserver {
	
	private DefaultListModel listmodel;
	// http://www.exampledepot.com/egs/javax.swing/list_ListAddRem.html
	private JList list;	 


	/**
	 * @param m
	 */
	public ListContentObserver(ContentModel m) {
		super(m);
		m.setLCO(this);
		listmodel = new DefaultListModel();
	    list = new JList(listmodel)
	    {
	    	// the following implementation shows a tooltip with the name of a list element
	    	// useful when the name is too long
            public String getToolTipText(MouseEvent e) {
            	int p = locationToIndex(e.getPoint());
            	if (p != -1) {
            		return getModel().getElementAt(p).toString();
            	} else {
            		return "";
            	}
            }
	    };
        
	    list.addMouseListener(new DoubleClickListener());

	    display = new JScrollPane(list);

	    //  Monitor all list selections
//	    list.addListSelectionListener(new ListUpdater());
	    
	    display.setVisible(true);
	    
	}
	
			
	@Override
	public void add (Object c, String name) {
		listmodel.addElement(name);		
	}
	
	@Override
	public void close () {
		listmodel.remove(list.getSelectedIndex());
	}
	
	public void clear () {
		listmodel.clear();
	}

	
	public int getFocus () {
		return list.getSelectedIndex();
	}

	class DoubleClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2)	{
				// open this data item in a new window
				model.open();
			}
		}
	}

	
/*	 // Inner class that responds to selection
	 class ListUpdater implements ListSelectionListener {
	     public void valueChanged(ListSelectionEvent e) {
	         //  If either of these are true, the event can be ignored.
	         if ((!e.getValueIsAdjusting()) || (e.getFirstIndex() == -1))
	             return;
	         
	         // update action
	         model.setFocus(((JList) e.getSource()).getSelectedIndex());
	         	         
	     }
	 }
*/
}
