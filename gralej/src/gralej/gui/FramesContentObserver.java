/**
 * 
 */
package gralej.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;

import gralej.*;
import gralej.controller.*;

/**
 * see ListContentObserver
 * 
 * @author Armin
 *
 */
public class FramesContentObserver extends ContentObserver {
	
	private ArrayList<JInternalFrame> frames;


	/**
	 * @param m
	 */
	public FramesContentObserver(ContentModel m) {
		super(m);
		display = new JDesktopPane();
		frames = new ArrayList<JInternalFrame>();
	}
	
	// methods to add, remove and focus frames
	private void add (GRALEFile file) {
	    // open new JInternalFrame
	    JInternalFrame newframe = new JInternalFrame(file.getName(), true, true, true, true);
	    newframe.add(file.display());
	    newframe.setLocation(100, 100); // better: get location as x+30, y+30 from current active if unmaximized
	    newframe.setSize(300, 250);
		display.add(newframe);
		frames.add(newframe);
		// change focus in the view
	    newframe.setVisible(true);
	    newframe.addInternalFrameListener(new Listener());
	    newframe.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void remove () {
		// close JInternalFrame
		frames.get(model.getFocused()).dispose();
		frames.remove(model.getFocused());		
	}
	
	private void focus () {
		if (model.getFocused() == -1) return;
	    try {
	    	frames.get(model.getFocused()).setSelected(true);
	    } catch (java.beans.PropertyVetoException ignored) {
		
	    }
	}

	/* (non-Javadoc)
	 * @see grale.GRALEContentObserver#update()
	 */
	@Override
	public void update(String message) {
		if (message.equals("open")) {
			// add the file recently added to the model
			this.add(model.getFileAt(model.getFocused()));
			
		} else if (message.equals("close")) {
			this.remove();
		} else if (message.equals("focus")) {
			this.focus();
			
		}
		
		

		
	}
	
	class Listener implements InternalFrameListener {
		
		public void internalFrameClosing(InternalFrameEvent e) {
			// inform model (close selected here, but not done until the model says so)
			model.close();

		}

		public void internalFrameClosed(InternalFrameEvent e) {

		}

		public void internalFrameOpened(InternalFrameEvent e) {

		}

		public void internalFrameIconified(InternalFrameEvent e) {

		}

		public void internalFrameDeiconified(InternalFrameEvent e) {

		}

		public void internalFrameActivated(InternalFrameEvent e) {
			// inform model
			model.setFocused(frames.indexOf(e.getSource()));

		}

		public void internalFrameDeactivated(InternalFrameEvent e) {

		}
		
	}

}
