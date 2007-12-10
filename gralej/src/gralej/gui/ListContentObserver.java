package gralej.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gralej.controller.*;
import gralej.parsers.IDataPackage;
import gralej.gui.MainGUI;


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
	private JList list;	 
	private MainGUI gui;


	/**
	 * @param m
	 */
	public ListContentObserver(ContentModel m, MainGUI gui) {
		super(m);
		m.setLCO(this);
		this.gui = gui;
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
	    list.addListSelectionListener(new ListUpdater());
	    
	    display.setVisible(true);
	    
	}
	
	
	@Override
	public void add (IDataPackage data) {
		listmodel.addElement(data.getTitle());		
        gui.notifyOfEmptyList(false);

	}
	
	@Override
	public void close () {
		listmodel.remove(list.getSelectedIndex());
		gui.notifyOfSelection(list.getSelectedIndex() != -1);
        gui.notifyOfEmptyList(listmodel.isEmpty());
	}
	
	public void clear () {
		listmodel.clear();
		gui.notifyOfSelection(false);
        gui.notifyOfEmptyList(true);
	}

	
	public int getFocus () {
		return list.getSelectedIndex();
	}

	
	// Inner class that responds to selection
	class ListUpdater implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
	    	if (!e.getValueIsAdjusting()) return;
	        gui.notifyOfSelection(e.getFirstIndex() != -1);
//	        System.err.println("selection changed");
	        return;
		}
	}

}
