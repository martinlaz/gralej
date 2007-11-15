/**
 * 
 */
package gralej.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.*;

import gralej.*;
import gralej.gui.*;

/**
 * The content model stores the semantics. 
 * it keeps track of open files, which one's focused etc.
 * and of the preferences
 * 
 * @author Armin
 *
 */
public class ContentModel { // extends DefaultListModel??
	
	// store a list of observers (in our case the list and the desktop
	private ContentObserver[] observers = new ContentObserver[2];
	private int totalObs  = 0;
	public void attach( ContentObserver o ) { observers[totalObs++] = o; }
	
	// content variables
	private ArrayList<GRALEFile> files = new ArrayList<GRALEFile>(); 
    private int focused;
    
    // the content model also handles preferences settings
    Preferences prefs;
    
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
    
	public void open (File file) {
		GRALEFile newfile = new GRALEFile(file);
		files.add(newfile);
	    focused = files.size() - 1;
	    notifyObservers("open"); // could add an event type "open file".
	                       //this would save a complete rebuild from the observers
	}

	public void close () {
		if (files.size() == 0) return; // don't close nothing
		notifyObservers("close");
//		JOptionPane.showMessageDialog(null, "closing " + focused);
		files.remove(focused); // frame close still NEEDS reference, so can only be deleted later
		
		// assign new focus. could be done better
		focused = files.size() - 1;
		
		// and notify of focus change
		notifyObservers("focus");
	}
	
    private void notifyObservers(String message) {     
    	for (int i=0; i < totalObs; i++) {
    		observers[i].update(message);
    	}
    }

	/**
	 * 
	 */
	public ContentModel() {
		focused = -1;
	}

	public int getSize() {
		return files.size();
	}
	
	public ArrayList<GRALEFile> getFiles() {
		return files;
	}
	
	public GRALEFile getFileAt(int i) {
		return files.get(i);
	}

	public int getFocused() {
		return focused;
	}

	public void setFocused(int focused) {
		this.focused = focused;
		notifyObservers("focus");
	}
	
}
