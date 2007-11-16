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
	
	
	String name;
	
	// the content to be displayed
	JComponent content;
	
	/**
	 * 
	 */
	public GRALEFile(Object parse, String name) {
		this.name = name;
		this.content = (JComponent) parse;
	}
	
	public String getName () {
		return name;		
	}
	
	
	public JComponent display() {
		return content;		
	}

}
