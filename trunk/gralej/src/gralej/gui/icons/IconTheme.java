package gralej.gui.icons;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * Icon theme interface. Icon themes should
 * implement the singleton pattern.
 * @author Niels Ott
 * @version $Id$
 */
public interface IconTheme {
	
	/**
	 * Retrieves an icon from this theme, may return
	 * <code>null</code> if the icon doesn't exist.
	 * @param name the name if this icon
	 * @return the icon or null if it was not found.
	 */
	public ImageIcon getIcon(String name);
	
	/**
	 * @return a list of names of icons available
	 */
	public List<String> getIconNames();
	

}
