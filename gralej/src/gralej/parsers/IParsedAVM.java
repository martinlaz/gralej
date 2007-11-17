package gralej.parsers;

import javax.swing.*;

/**
 * (Dummy) interface for any kind of data structure
 * holding a parsed AVM (aka parser output)
 * @author Niels Ott
 * @version $Id$
 */
public interface IParsedAVM {
	
	public String getName();
	
	public JPanel display();


}