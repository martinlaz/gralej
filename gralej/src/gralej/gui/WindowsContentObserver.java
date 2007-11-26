package gralej.gui;

import gralej.GRALEFile;
import gralej.controller.ContentModel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

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
	

	private void add (GRALEFile file) {
	    JFrame newframe = new JFrame(file.getName());
        newframe.setContentPane(new JScrollPane(file.display()));
	    newframe.setLocationByPlatform(true);
//	    newframe.setMinimumSize(d); // Dimension d
		frames.add(newframe);
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
		int toFocus = model.getFocused();
//		if (frames.get(toFocus).hasFocus()) return;
//		System.err.println("Focus setting to "+model.getFocused());
		frames.get(toFocus).requestFocus();// or .toFront();
//		System.err.println("Focus set     to "+model.getFocused());
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
//			System.err.println("Focus requested by "+
//					frames.indexOf(e.getSource())+ " focus is "+ model.getFocused());
			model.setFocused(frames.indexOf(e.getSource()));
			
		}

		public void windowClosed(WindowEvent arg0) {
			
		}

		public void windowClosing(WindowEvent arg0) {
			model.close();
			
		}

		public void windowDeactivated(WindowEvent arg0) {
			
		}

		public void windowDeiconified(WindowEvent arg0) {
			
		}

		public void windowIconified(WindowEvent arg0) {
			
		}

		public void windowOpened(WindowEvent arg0) {
			
		}
				
	}
	


}
