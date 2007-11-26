package gralej.gui.icons;


import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * A generic icon theme that loads icons from 
 * a resource (jar file).
 * @author Niels Ott
 * @version $Id$
 */
public class GenericIconTheme implements IconTheme {
	
	private String iconpackagename;
	private HashMap<String,ImageIcon> map;

	/**
	 * Creates a generic icon theme using the
	 * directory specified. This directory must be
	 * inside the package this very class is in.
	 * @param iconpackagename
	 */
	protected GenericIconTheme(String iconpackagename) {
		this.iconpackagename = iconpackagename;
		map = new HashMap<String,ImageIcon> ();
		preloadAllIcons();
	}
	
	private void preloadAllIcons() {
		
		map.put("closeviews",  loadIcon("closeviews.png"));
		map.put("closewindow",  loadIcon("closewindow.png"));
		map.put("connected",  loadIcon("connected.png"));
		map.put("deletedata",  loadIcon("deletedata.png"));
		map.put("disconnected",  loadIcon("disconnected.png"));
		map.put("expand",  loadIcon("expand.png"));
		map.put("filefloppy",  loadIcon("filefloppy.png"));
		map.put("fileopen",  loadIcon("fileopen.png"));
		map.put("fileprint",  loadIcon("fileprint.png"));
		map.put("gralelogo",  loadIcon("_GRALE.png"));
		map.put("grale",  loadIcon("GRALE.png"));
		map.put("magglass",  loadIcon("magglass.png"));
		map.put("maximize",  loadIcon("maximize.png"));
		map.put("nextstruc",  loadIcon("nextstruc.png"));
		map.put("prevstruc",  loadIcon("prevstruc.png"));
		map.put("raisewindow",  loadIcon("raisewindow.png"));
		map.put("showstruc",  loadIcon("showstruc.png"));
		map.put("showtree",  loadIcon("showtree.png"));
		map.put("unexpand",  loadIcon("unexpand.png"));
		map.put("zoomin",  loadIcon("zoomin.png"));
		map.put("zoomout",  loadIcon("zoomout.png"));

		
		
	}
	
	/**
	 * 
	 * @param name
	 * @return my return null if it doesn't work out.
	 */
	private ImageIcon loadIcon(String filename) {
		
		System.err.println("Loading "
		+	iconpackagename 
		+ "/" +
		filename	
		);
		
		URL imageURL = GenericIconTheme.class.getResource(
				iconpackagename 
				+ "/" +
				filename);
		
		ImageIcon icon = null;
		if (imageURL != null) {
		   icon = new ImageIcon(imageURL);
		} else {
			System.err.println("Loading failed");
		}
		
		return icon;
		
	}

	/**
	 * @see IconTheme#getIcon(String)
	 */
	public ImageIcon getIcon(String name) {
		// return a clone of the corresponding icon
		// or null if it is null
		ImageIcon res = map.get(name);
		if ( res != null ) {
			res = new ImageIcon(res.getImage());
		}
		
		return res;
	}

	/**
	 * @see IconTheme#getIconNames()
	 */
	public List<String> getIconNames() {
		// return a clone of the map's keys
		return new LinkedList<String>(map.keySet());
	}

}
