package gralej.gui;

//import java.beans.PropertyVetoException;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.DefaultListModel;

import gralej.controller.*;

/**
 * The ListContentObserver is a piece of GRALE's GUI.
 * It displays the file list to the left.
 * 
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
        
	    // open new window instance on double-click
	    list.addMouseListener(new MouseAdapter () {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)	{
					model.open();
				}
			}
		});
	    
	    // open new window instance on ENTER
	    list.addKeyListener(new KeyListener () {

	    	//@Override
			public void keyPressed(KeyEvent arg0) {
	    		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
	    			model.open();
	    		}
			}
			//@Override
			public void keyReleased(KeyEvent arg0) {}
			//@Override
			public void keyTyped(KeyEvent arg0) {}
	    });

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
