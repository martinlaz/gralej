/**
 * 
 */
package gralej;

import java.io.*;
import javax.swing.*;

/**
 * A GRALEFile stores a reference to the original file and keeps an AVM representation
 * more precisely it needs a complete one and a partial one according to the user input
 *  (hidden nodes)
 * 
 * @author Armin
 *
 */
public class GRALEFile {
	
	
	// the source file
	File file;
	
	// the content to be displayed
	JComponent content;
	
	// internal representation

	/**
	 * 
	 */
	public GRALEFile(File file) {
		this.file = file;
		this.content = new JInternalFrame(file.getName(), true, true, true, true);
	}
	
	public String getName () {
		return file.getName();		
	}
	
	/**
	 * rendering the file means to compute a Swing representation of the AVM
	 * 
	 */
	public void render () {
		
		
	}
	
	public JComponent display() {
//		if (content == null ) render(); // when called for the first time
		return content;		
	}

}
