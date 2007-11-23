package gralej.gui;

import gralej.GRALEFile;
import gralej.controller.ContentModel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

// FIXME has focus problems, maybe focuses/deletes wrong window

public class WindowsContentObserver extends ContentObserver {
	
	private ArrayList<JFrame> frames;


	/**
	 * @param m
	 */
	public WindowsContentObserver(ContentModel m) {
		super(m);
		frames = new ArrayList<JFrame>();
	}
	

	// methods to add, remove and focus frames
	private void add (GRALEFile file) {
	    // open new JFrame
	    JFrame newframe = new JFrame(file.getName());
	    newframe.add(file.display());
	    newframe.setLocation(100, 100); // better: get location as x+30, y+30 from current active if unmaximized
	    newframe.setSize(300, 250);
		frames.add(newframe);
		// change focus in the view
	    newframe.setVisible(true);
	    newframe.addWindowListener(new Listener());
	    newframe.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
	    
	}
	
	private void remove () {
		// close JFrame
		frames.get(model.getFocused()).dispose();
		frames.remove(model.getFocused());		
	}
	
	private void focus () {
		if (model.getFocused() == -1) return;
		System.err.println("Focus setting to "+model.getFocused());
		frames.get(model.getFocused()).requestFocus();// or .toFront();
		System.err.println("Focus set     to "+model.getFocused());
	}

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
	
	class Listener implements WindowListener {

		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			model.setFocused(frames.indexOf(e.getSource()));
			System.err.println("Focus requested by "+frames.indexOf(e.getSource()));
			
		}

		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			model.close();
			
		}

		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	


}
