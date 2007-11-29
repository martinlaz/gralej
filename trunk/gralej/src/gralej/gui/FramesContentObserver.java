/**
 * 
 */
package gralej.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.awt.*;
import gralej.*;
import gralej.controller.*;

/**
 * see ListContentObserver
 * 
 * @author Armin
 *
 */
public class FramesContentObserver extends ContentObserver {
	
    Point x; // where to display a new window

	
	private ArrayList<JInternalFrame> frames;


	/**
	 * @param m
	 */
	public FramesContentObserver(ContentModel m) {
		super(m);
		display = new JDesktopPane();
		frames = new ArrayList<JInternalFrame>();
    	x = new Point(0,0);
	}
	
	// methods to add, remove and focus frames
	private void add (GRALEFile file) {
	    // open new JInternalFrame
	    JInternalFrame newframe = new JInternalFrame(file.getName(), true, true, true, true);
	    file.display().setOpaque(true);
        newframe.setContentPane(file.display());
        
        
       	x.translate(30, 30);
       	if (x.y + 100 > display.getSize().height ) {
       		x.move(x.x - x.y + 75, 30);
       	}
       	if (x.x + 100 > display.getSize().width ) {
       		x.move(30, 30);
       	}

        
	    newframe.setLocation(x);
	    newframe.setSize(300, 250);
		display.add(newframe);
		frames.add(newframe);
		// change focus in the view
	    newframe.addInternalFrameListener(new Listener());
	    newframe.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
	    newframe.setVisible(true);
	    newframe.pack();
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
	    } catch (java.beans.PropertyVetoException ignored) {}
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
		Dimension size = display.getSize();
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
		x = new Point(0,0);
		for (int i = 0; i < frames.size(); i++) {
	       	x.translate(30, 30);
	       	if (x.y + 100 > display.getSize().height ) {
	       		x.move(x.x - x.y + 75, 30);
	       	}
	       	if (x.x + 100 > display.getSize().width ) {
	       		x.move(30, 30);
	       	}
	        
		    frames.get(i).setLocation(x);
			model.setFocused(i);
			
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
		} else if (message.equals("cascade")) {
			this.cascade();
		} else if (message.equals("tile")) {
			this.tile();
		} else if (message.equals("resize")) {
			this.resize();
			
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
