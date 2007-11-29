/**
 * 
 */
package gralej;

import javax.swing.JComponent;

/**
 * A GRALEFile contains name and graphical representation of a parse
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
