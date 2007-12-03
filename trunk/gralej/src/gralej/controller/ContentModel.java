package gralej.controller;

import gralej.gui.*;

import java.util.ArrayList;
import java.util.prefs.*;
import javax.swing.JComponent;


/**
 * The content model stores the semantics. 
 * it keeps track of open files, which one's focused etc.
 * and of the preferences
 * 
 * @author Armin
 *
 */
public class ContentModel { // extends DefaultListModel??
	
	private ListContentObserver list;
	private ContentObserver observer;
	
	// content variables
	private ArrayList<GRALEFile> files = new ArrayList<GRALEFile>(); 
    
    // the content model also handles preferences settings
    Preferences prefs;
    
    public Preferences getPrefs () { return prefs; }
    
    /** code for exporting preferences
     try {
    // Export the node to a file
    prefs.exportNode(new FileOutputStream("output.xml"));
} catch (IOException e) {
} catch (BackingStoreException e) {
}
*/
    
/** code for importing preferences    
    // Create an input stream on a file
    InputStream is = null;
    try {
        is = new BufferedInputStream(new FileInputStream("output.xml"));
    } catch (FileNotFoundException e) {
    }
    
    // Import preference data
    try {
        Preferences.importPreferences(is);
    } catch (InvalidPreferencesFormatException e) {
    } catch (IOException e) {
    }
*/
	

	

    
    // methods for adding/deleting a file. each calls notify
    
	public void open (Object parse, String name) {
		GRALEFile newfile = new GRALEFile(parse, name);
		files.add(newfile);
	    list.add(parse, name);
	    observer.add(parse, name);
	}
	
	/**
	 * Opening a new window for the selected data item.
	 * 
	 */
	public void open () {
		observer.add(files.get(list.getFocus()).display, 
				files.get(list.getFocus()).name);
	}

	public void close () {
		if (files.size() == 0) return; // don't close nothing
		observer.close();
		files.remove(list.getFocus());
		list.close();
	}
	
	public void closeAll () {
		files.clear();
		list.clear();
		observer.clear();
	}
	
	public void tile () {
		// TODO to be changed when other observers are allowed
		((WindowsContentObserver) observer).tile();
	}

	public void cascade () {
		((WindowsContentObserver) observer).cascade();
	}


	/**
	 * 
	 */
	public ContentModel() {
		
		prefs = Preferences.userNodeForPackage(this.getClass());
		resetPreferences();
	}
	
	public void setLCO (ListContentObserver l) {
		if (list != null) return; // instead: finalizing?
		list = l;
	}
	
	public void setObserver (ContentObserver o) {
		if (observer != null) return;
		observer = o;
	}
	
	/**
	 * resetting or initializing the preferences
	 * 
	 * 
	 */
	public void resetPreferences () {
		prefs.put("color", "white");
		prefs.put("font size", "11");
	}

	public int getSize() {
		return files.size();
	}
	
	public ArrayList<GRALEFile> getFiles() {
		return files;
	}
	
	public String getNameAt(int i) {
		return files.get(i).name;
	}

	public JComponent getDisplayAt(int i) {
		return files.get(i).display;
	}

	public int getFocus() {
		return list.getFocus();
	}
	
	/**
	 * GRALEFiles: now only a JComponent-String pair
	 * 
	 * @author Armin
	 *
	 */
	private class GRALEFile {
		
		String name;
		JComponent display;

		GRALEFile (Object parse, String name) {
			this.name = name;
			this.display = (JComponent) parse;
		}
		
//		public String getName () { return name;	}
		
//		public JComponent display() { return content; }

	}

}
