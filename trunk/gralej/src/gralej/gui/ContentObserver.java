/**
 * 
 */
package gralej.gui;

import javax.swing.*;
import gralej.controller.*;

/**
 * The observer class needs subclasses for lists and frame desktop (and probably more)
 * their instantiations register with a subject (an instance of GRALEContentWindow)
 * 
 * @author Armin
 *
 */
public abstract class ContentObserver {
	
	protected ContentModel model;  
	
	protected JComponent display;
	
	public JComponent getDisplay() {
		return display;
	}
	
	public abstract void add (Object c, String name);
	
	public abstract void close ();
	
	public abstract void clear ();
	

	/**
	 * 
	 */
	public ContentObserver(ContentModel m) {
		model = m;
//		model.attach(this); // registering with the subject
	}

}
