/**
 * 
 */
package gralej.parsers;

import javax.swing.JPanel;


/**
 * By now just a dummy (instantiable) class for parse results
 * top node of a parse tree
 * contains meta-information and the root node of the AVM
 * 
 * @author Armin
 * @version $Id$
 */
public class ParsedAVM implements IParsedAVM {
	
	// meta
	private String name;
	
	
	// FIXME add AVM class

	/**
	 * 
	 */
	public ParsedAVM(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JPanel display() {
		// TODO Auto-generated method stub
		return new JPanel();
	}
	
	

}
