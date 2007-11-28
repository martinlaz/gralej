package gralej.gui;

import gralej.GRALEFile;
import gralej.controller.ContentModel;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 * 
 * 
 * 
 * 
 * 
 * @author Armin
 * @version $id$
 */
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
	    newframe.setMinimumSize(new Dimension(250,150));
	    newframe.setSize(file.display().getSize());
//	    System.err.println("size "+file.display().getSize().toString());
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
		frames.get(toFocus).requestFocus();// or .toFront();
	}
	
	/**
	 * method to make the window size fit its content
	 * 
	 */
	private void resize () {
		
	}
	
	/**
	 * distribute open windows over the existing space
	 * all same size
	 * 
	 */
	public void tile () {
		
	}

	
	/**
	 * cascade windows as if newly generated
	 * 
	 */
	public void cascade () {
		
	}

	public void update(String message) {
		if (message.equals("open")) {
			// add the file recently added to the model
			this.add(model.getFileAt(model.getFocused()));
			
		} else if (message.equals("close")) {
			this.remove();
		} else if (message.equals("focus")) {
			this.focus();
		} else if (message.equals("resize")) {
			this.resize();
			
		}
		
		

		
	}
	
	class Listener implements WindowListener {

		public void windowActivated(WindowEvent e) {
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
