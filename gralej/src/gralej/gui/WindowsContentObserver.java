package gralej.gui;

import gralej.GRALEFile;
import gralej.controller.ContentModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

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
	    file.display().setOpaque(true);
        newframe.setContentPane(file.display());
	    newframe.setLocationByPlatform(true);
	    newframe.setMinimumSize(new Dimension(250,150));
	    newframe.setSize(file.display().getSize());
//	    System.err.println("size "+file.display().getSize().toString());
		frames.add(newframe);
	    newframe.addWindowListener(new Listener());


	    newframe.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
	    newframe.setVisible(true);
	    newframe.pack();	    
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
		frames.get(model.getFocused()).pack();
	}
	
	/**
	 * distribute open frames over the existing space
	 * all same size
	 * 
	 * The code is almost directly taken from
	 * http://www.javalobby.org/forums/thread.jspa?threadID=15696&tstart=30
	 * (cannot find out their copyright policy)
	 * 
	 */
	public void tile () {
		Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getMaximumWindowBounds();
//		Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		int cols = (int)Math.sqrt(frames.size());
	    int rows = (int)(Math.ceil( ((double)frames.size()) / cols));
	    int lastRow = frames.size() - cols*(rows-1);
	    int width, height;
	 
	    if ( lastRow == 0 ) {
	        rows--;
	        height = size.height / rows;
	    }
	    else {
	        height = size.height / rows;
	        if ( lastRow < cols ) {
	            rows--;
	            width = size.width / lastRow;
	            for (int i = 0; i < lastRow; i++ ) {
	                frames.get(cols*rows+i).setBounds( i*width, rows*height,
	                                               width, height );
	            }
	        }
	    }
	            
	    width = size.width/cols;
	    for (int j = 0; j < rows; j++ ) {
	        for (int i = 0; i < cols; i++ ) {
	            frames.get(i+j*cols).setBounds( i*width, j*height,
	                                        width, height );
	        }
	    }

	}

	
	/**
	 * cascade windows as if newly generated
	 * 
	 */
	public void cascade () {
		Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getMaximumWindowBounds();
//	Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		Point x = new Point(0,0);
		for (int i = 0; i < frames.size(); i++) {
	       	x.translate(30, 30);
	       	if (x.y + 100 > size.height ) {
	       		x.move(x.x - x.y + 75, 30);
	       	}
	       	if (x.x + 100 > size.width ) {
	       		x.move(30, 30);
	       	}
	        
		    frames.get(i).setLocation(x);
//			model.setFocused(i); // threading problems: infinite re-focus loop
			
		}
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
		} else if (message.equals("cascade")) {
			this.cascade();
		} else if (message.equals("tile")) {
			this.tile();
		} else if (message.equals("resize")) {
			this.resize();
			
		}
		
		

		
	}
	
	class Listener implements WindowListener {

		public void windowActivated(WindowEvent e) {
			model.setFocused(frames.indexOf(e.getSource()));
			resize();
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
