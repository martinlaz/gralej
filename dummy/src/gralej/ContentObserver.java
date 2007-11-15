/**
 * 
 */
package gralej;

import javax.swing.*;

/**
 * The observer class needs subclasses for lists and frame desktop (and probably more)
 * their instantiations register with a subject (an instance of GRALEContentWindow)
 * 
 * @author Armin
 *
 */
abstract class ContentObserver {
	
	protected ContentModel model;  
	
	protected JComponent display;
	
	public JComponent getDisplay() {
		return display;
	}
	
	public abstract void update(String message);

	/**
	 * 
	 */
	public ContentObserver(ContentModel m) {
		model = m;
		model.attach(this); // registering with the subject
	}

}
